package com.wumboos.app.moneyapp.eventpublication;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.swing.JApplet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.core.EventSerializer;
import org.springframework.modulith.events.core.PublicationTargetIdentifier;
import org.springframework.modulith.events.core.TargetEventPublication;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReactiveEventPublicationRepositoryImpl implements ReactiveEventPublicationRepository {
	private static String BY_EVENT_AND_LISTENER_ID = """
			select p.*
			from event_publication p
			where
				p.serialized_event = :event
				and p.listener_id = :listenerId
				and p.completion_date is null
			""";

	private static String INCOMPLETE = """
			select p
			from event_publication p
			where
				p.completionDate is null
			order by
				p.publicationDate asc
			""";

	private static String INCOMPLETE_BEFORE = """
			select p
			from event_publication p
			where
				p.completionDate is null
				and p.publicationDate < ?1
			order by
				p.publicationDate asc
			""";

	private static final String MARK_COMPLETED_BY_EVENT_AND_LISTENER_ID = """
			update event_publication p
			   set p.completionDate = ?3
			 where p.serializedEvent = ?1
			   and p.listenerId = ?2
			""";

	private static final String DELETE = """
			delete
			from event_publication p
			where
				p.id in ?1
			""";

	private static final String DELETE_COMPLETED = """
			delete
			from event_publication p
			where
				p.completionDate is not null
			""";

	private static final String DELETE_COMPLETED_BEFORE = """
			delete
			from event_publication p
			where
				p.completionDate < ?1
			""";

	private static final String INSERT = """
			insert into event_publication (id,listener_id,event_type,serialized_event,publication_date,completion_date)
			 values(
			:id ,  :listenerId ,  :eventType , :serializedEvent , :publicationDate , :completionDate)
			""";

	private static final int DELETE_BATCH_SIZE = 100;

	private final EventSerializer serializer;
	public static final Calendar tzUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));  

	@Autowired
	private DatabaseClient databaseClient;
	
	public ReactiveEventPublicationRepositoryImpl(DatabaseClient databaseClient, EventSerializer serializer) {

		Assert.notNull(databaseClient, "DatabaseClient must not be null!");
		Assert.notNull(serializer, "EventSerializer must not be null!");

		this.databaseClient = databaseClient;
		this.serializer = serializer;
	}

	@Override
	public Mono<TargetEventPublication> create(TargetEventPublication publication) {
		return databaseClient.sql(INSERT)
				.bind("id", publication.getIdentifier())
				.bind("publicationDate", publication.getPublicationDate())
				.bind("listenerId", publication.getTargetIdentifier().getValue())
				.bind("serializedEvent", serializer.serialize(publication.getEvent()))
				.bind("eventType", publication.getEvent().getClass().toString())
				.bindNull("completionDate", Instant.class)
				.then().log().map(e -> publication);
	}

	@Override
	public Mono<Void> markCompleted(Object event, PublicationTargetIdentifier identifier, Instant completionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<TargetEventPublication> findIncompletePublications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<TargetEventPublication> findIncompletePublicationsPublishedBefore(Instant instant) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<TargetEventPublication> findIncompletePublicationsByEventAndTargetIdentifier(Object event,
			PublicationTargetIdentifier targetIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> deletePublications(List<UUID> identifiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> deleteCompletedPublications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> deleteCompletedPublicationsBefore(Instant instant) {
		// TODO Auto-generated method stub
		return null;
	}
	private EventPublication domainToEntity(TargetEventPublication domain) {
		return new EventPublication(domain.getIdentifier(), domain.getPublicationDate(),
				domain.getTargetIdentifier().getValue(),
				serializeEvent(domain.getEvent()), domain.getEvent().getClass());
	}
	
	private Mono<EventPublication> findEntityBySerializedEventAndListenerIdAndCompletionDateNull(
			Object event, PublicationTargetIdentifier listenerId) {

		var serializedEvent = serializeEvent(event);
		return databaseClient.sql(BY_EVENT_AND_LISTENER_ID).bind(0, event).bind(1, listenerId).fetch().all()
				.bufferUntilChanged(r -> r.get("id")).flatMap(EventPublication::fromRows).singleOrEmpty();

	}

	private String serializeEvent(Object event) {
		return serializer.serialize(event).toString();
	}

	private TargetEventPublication entityToDomain(EventPublication entity) {
		return new EventPublicationAdapter(entity, serializer);
	}

	private static <T> List<List<T>> batch(List<T> input, int batchSize) {

		var inputSize = input.size();

		return IntStream.range(0, (inputSize + batchSize - 1) / batchSize)
				.mapToObj(i -> input.subList(i * batchSize, Math.min((i + 1) * batchSize, inputSize)))
				.toList();
	}

	private static class EventPublicationAdapter implements TargetEventPublication {

		private final EventPublication publication;
		private final EventSerializer serializer;

		/**
		 * Creates a new {@link EventPublicationAdapter} for the given {@link JpaEventPublication} and
		 * {@link EventSerializer}.
		 *
		 * @param publication must not be {@literal null}.
		 * @param serializer must not be {@literal null}.
		 */
		public EventPublicationAdapter(EventPublication publication, EventSerializer serializer) {

			Assert.notNull(publication, "JpaEventPublication must not be null!");
			Assert.notNull(serializer, "EventSerializer must not be null!");

			this.publication = publication;
			this.serializer = serializer;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.modulith.events.EventPublication#getPublicationIdentifier()
		 */
		@Override
		public UUID getIdentifier() {
			return publication.id;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.modulith.events.EventPublication#getEvent()
		 */
		@Override
		public Object getEvent() {
			return serializer.deserialize(publication.serializedEvent, publication.eventType);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.modulith.events.EventPublication#getTargetIdentifier()
		 */
		@Override
		public PublicationTargetIdentifier getTargetIdentifier() {
			return PublicationTargetIdentifier.of(publication.listenerId);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.modulith.events.EventPublication#getPublicationDate()
		 */
		@Override
		public Instant getPublicationDate() {
			return publication.publicationDate;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.modulith.events.CompletableEventPublication#getCompletionDate()
		 */
		@Override
		public Optional<Instant> getCompletionDate() {
			return Optional.ofNullable(publication.completionDate);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.modulith.events.CompletableEventPublication#isPublicationCompleted()
		 */
		@Override
		public boolean isPublicationCompleted() {
			return publication.completionDate != null;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.modulith.events.Completable#markCompleted(java.time.Instant)
		 */
		@Override
		public void markCompleted(Instant instant) {
			this.publication.completionDate = instant;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {

			if (this == obj) {
				return true;
			}

			if (!(obj instanceof EventPublicationAdapter that)) {
				return false;
			}

			return Objects.equals(publication, that.publication)
					&& Objects.equals(serializer, that.serializer);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return Objects.hash(publication, serializer);
		}
	}

}

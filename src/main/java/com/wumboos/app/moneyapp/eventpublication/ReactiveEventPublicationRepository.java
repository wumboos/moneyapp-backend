package com.wumboos.app.moneyapp.eventpublication;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.modulith.events.core.PublicationTargetIdentifier;
import org.springframework.modulith.events.core.TargetEventPublication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface ReactiveEventPublicationRepository {
	/**
	 * Persists the given {@link TargetEventPublication}.
	 *
	 * @param publication must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	Mono<TargetEventPublication> create(TargetEventPublication publication);

	/**
	 * Marks the given {@link TargetEventPublication} as completed.
	 *
	 * @param publication    must not be {@literal null}.
	 * @param completionDate must not be {@literal null}.
	 */
	default Mono<Void> markCompleted(TargetEventPublication publication, Instant completionDate) {

		Assert.notNull(publication, "EventPublication must not be null!");
		Assert.notNull(completionDate, "Instant must not be null!");

	    return Mono.fromCallable(() -> {
	        publication.markCompleted(completionDate);
	        return publication;
	    }).flatMap(p -> markCompleted(p.getEvent(), p.getTargetIdentifier(), completionDate));
	}

	/**
	 * Marks the publication for the given event and
	 * {@link PublicationTargetIdentifier} to be completed at the given
	 * {@link Instant}.
	 *
	 * @param event          must not be {@literal null}.
	 * @param identifier     must not be {@literal null}.
	 * @param completionDate must not be {@literal null}.
	 */
	Mono<Void> markCompleted(Object event, PublicationTargetIdentifier identifier, Instant completionDate);

	/**
	 * Returns all {@link TargetEventPublication}s that have not been completed yet.
	 *
	 * @return will never be {@literal null}.
	 */
	Flux<TargetEventPublication> findIncompletePublications();

	/**
	 * Returns all {@link TargetEventPublication}s that have not been completed and
	 * were published before the given {@link Instant}.
	 *
	 * @param instant must not be {@literal null}.
	 * @return will never be {@literal null}.
	 * @since 1.1
	 */
	Flux<TargetEventPublication> findIncompletePublicationsPublishedBefore(Instant instant);

	/**
	 * Return the incomplete {@link TargetEventPublication} for the given serialized
	 * event and listener identifier.
	 *
	 * @param event            must not be {@literal null}.
	 * @param targetIdentifier must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	Mono<TargetEventPublication> findIncompletePublicationsByEventAndTargetIdentifier( //
			Object event, PublicationTargetIdentifier targetIdentifier);

	/**
	 * Deletes all publications with the given identifiers.
	 *
	 * @param identifiers must not be {@literal null}.
	 * @since 1.1
	 */
	Mono<Void> deletePublications(List<UUID> identifiers);

	/**
	 * Deletes all publications that were already marked as completed.
	 */
	Mono<Void> deleteCompletedPublications();

	/**
	 * Deletes all publication that were already marked as completed with a
	 * completion date before the given one.
	 *
	 * @param instant must not be {@literal null}.
	 */
	Mono<Void> deleteCompletedPublicationsBefore(Instant instant);
}

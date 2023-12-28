package com.wumboos.app.moneyapp.eventpublication;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.Assert;

import reactor.core.publisher.Mono;

@Table(name = "EVENT_PUBLICATION")
class EventPublication {

	final @Id UUID id;
	final Instant publicationDate;
	final String listenerId;
	final String serializedEvent;
	final Class<?> eventType;

	Instant completionDate;

	EventPublication(UUID id, Instant publicationDate, String listenerId, String serializedEvent, Class<?> eventType) {

		Assert.notNull(id, "Identifier must not be null!");
		Assert.notNull(publicationDate, "Publication date must not be null!");
		Assert.notNull(listenerId, "Listener id must not be null or empty!");
		Assert.notNull(serializedEvent, "Serialized event must not be null or empty!");
		Assert.notNull(eventType, "Event type must not be null!");

		this.id = id;
		this.publicationDate = publicationDate;
		this.listenerId = listenerId;
		this.serializedEvent = serializedEvent;
		this.eventType = eventType;
	}

	EventPublication() {

		this.id = null;
		this.publicationDate = null;
		this.listenerId = null;
		this.serializedEvent = null;
		this.eventType = null;
	}

	EventPublication markCompleted() {

		this.completionDate = Instant.now();
		return this;
	}
	
    public static Mono<EventPublication> fromRows(List<Map<String, Object>> rows){
        try {
			return Mono.just(new EventPublication(UUID.fromString((String) rows.get(0).get("id")), null, (String)rows.get(0).get("listener_id"),(String) rows.get(0).get("serialized_event"),Class.forName( (String) rows.get(0).get("event_type"))));
		} catch (ClassNotFoundException e) {
			return Mono.just(new EventPublication());
		}
    }

}

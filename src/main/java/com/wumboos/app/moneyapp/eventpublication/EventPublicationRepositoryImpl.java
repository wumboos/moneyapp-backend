package com.wumboos.app.moneyapp.eventpublication;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.core.EventPublicationRepository;
import org.springframework.modulith.events.core.PublicationTargetIdentifier;
import org.springframework.modulith.events.core.TargetEventPublication;

import reactor.core.scheduler.Schedulers;

public class EventPublicationRepositoryImpl implements EventPublicationRepository {
	
	@Autowired
	private ReactiveEventPublicationRepository eventPublicationRepository;
	
	public EventPublicationRepositoryImpl(ReactiveEventPublicationRepository eventPublicationRepository) {
		this.eventPublicationRepository = eventPublicationRepository;
	}
	
	@Override
	public TargetEventPublication create(TargetEventPublication publication) {
		eventPublicationRepository.create(publication).subscribe();
		return null;
	}

	@Override
	public void markCompleted(Object event, PublicationTargetIdentifier identifier, Instant completionDate) {
		eventPublicationRepository.markCompleted(event, identifier, completionDate).block();
	}

	@Override
	public List<TargetEventPublication> findIncompletePublications() {
		return eventPublicationRepository.findIncompletePublications().collectList().block();
	}

	@Override
	public List<TargetEventPublication> findIncompletePublicationsPublishedBefore(Instant instant) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<TargetEventPublication> findIncompletePublicationsByEventAndTargetIdentifier(Object event,
			PublicationTargetIdentifier targetIdentifier) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public void deletePublications(List<UUID> identifiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCompletedPublications() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCompletedPublicationsBefore(Instant instant) {
		// TODO Auto-generated method stub
		
	}

}

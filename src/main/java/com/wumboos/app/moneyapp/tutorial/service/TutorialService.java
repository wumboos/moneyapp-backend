package com.wumboos.app.moneyapp.tutorial.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.wumboos.app.moneyapp.TransactionCreatedEvent;
import com.wumboos.app.moneyapp.tutorial.model.Tutorial;
import com.wumboos.app.moneyapp.tutorial.repository.TutorialRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TutorialService {
	Logger log = LoggerFactory.getLogger(TutorialService.class);

	@Autowired
	TutorialRepository tutorialRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Transactional
	public Flux<Tutorial> findAll() {
		return new TransactionalEventPublisher(this.eventPublisher)
				.publishEvent(new TransactionCreatedEvent(UUID.randomUUID())).log()
	    .as(Flux::from).log().flatMap(s -> tutorialRepository.findAll());
	}
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener
	Mono<Void> on(TransactionCreatedEvent event) {
		return Mono.just(event).log().then();
	}

	public Flux<Tutorial> findByTitleContaining(String title) {
		return tutorialRepository.findByTitleContaining(title);
	}

	public Mono<Tutorial> findById(int id) {
		return tutorialRepository.findById(id);
	}

	public Mono<Tutorial> save(Tutorial tutorial) {
		return tutorialRepository.save(tutorial);
	}

	public Mono<Tutorial> update(int id, Tutorial tutorial) {
		return tutorialRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
				.flatMap(optionalTutorial -> {
					if (optionalTutorial.isPresent()) {
						tutorial.setId(id);
						return tutorialRepository.save(tutorial);
					}

					return Mono.empty();
				});
	}

	public Mono<Void> deleteById(int id) {
		return tutorialRepository.deleteById(id);
	}

	public Mono<Void> deleteAll() {
		return tutorialRepository.deleteAll();
	}

	public Flux<Tutorial> findByPublished(boolean isPublished) {
		return tutorialRepository.findByPublished(isPublished);
	}
}

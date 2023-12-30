package com.wumboos.app.moneyapp.tutorial.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.wumboos.app.moneyapp.tutorial.model.Tutorial;
import com.wumboos.app.moneyapp.tutorial.repository.TutorialRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class TutorialService {
	Logger log = LoggerFactory.getLogger(TutorialService.class);

	TutorialRepository tutorialRepository;

	private final Scheduler scheduler;

	private ApplicationEventPublisher eventPublisher;

	public TutorialService(Scheduler scheduler, TutorialRepository tutorialRepository,
			ApplicationEventPublisher eventPublisher) {
		this.scheduler = scheduler;
		this.tutorialRepository = tutorialRepository;
		this.eventPublisher = eventPublisher;
	}

	public Flux<Tutorial> findAll() {
		return Flux.defer(() -> Flux.fromIterable(this.tutorialRepository.findAll())).subscribeOn(scheduler);
	}

//	@Transactional(value = TxType.REQUIRES_NEW)
//	@TransactionalEventListener
//	Mono<Void> on(TransactionCreatedEvent event) {
//		return Mono
//				.fromCallable(() -> this.tutorialRepository
//						.save(new Tutorial(event.id(), event.title(), event.description(), event.published())))
//				.publishOn(scheduler).then();
//	}

	public Flux<Tutorial> findByTitleContaining(String title) {
		return Flux.defer(() -> Flux.fromIterable(this.tutorialRepository.findByTitleContaining(title)))
				.subscribeOn(scheduler);
	}

	public Mono<Tutorial> findById(UUID id) {
		return Mono.fromCallable(() -> this.tutorialRepository.findById(id).orElseGet(null)).subscribeOn(scheduler);
	}

//	@Transactional(value = TxType.REQUIRES_NEW)
//	public Mono<Void> save(Tutorial tutorial) {
//		this.eventPublisher.publishEvent(new TransactionCreatedEvent(UUID.randomUUID(), tutorial.getTitle(),
//				tutorial.getDescription(), tutorial.isPublished()));
//		log.info("masukkkkk");
//		return Mono.empty();
//	}
//
//	public Mono<Tutorial> update(int id, Tutorial tutorial) {
//		return tutorialRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
//				.flatMap(optionalTutorial -> {
//					if (optionalTutorial.isPresent()) {
//						tutorial.setId(id);
//						return tutorialRepository.save(tutorial);
//					}
//
//					return Mono.empty();
//				});
//	}
//
//	public Mono<Void> deleteById(int id) {
//		return tutorialRepository.deleteById(id);
//	}
//
//	public Mono<Void> deleteAll() {
//		return tutorialRepository.deleteAll();
//	}
//
//	public Flux<Tutorial> findByPublished(boolean isPublished) {
//		return Flux.fromStream(tutorialRepository.findByPublished(isPublished));
//	}
}

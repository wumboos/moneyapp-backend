package com.wumboos.app.moneyapp.category.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.wumboos.app.moneyapp.category.model.Category;
import com.wumboos.app.moneyapp.category.repository.CategoryRepository;
import com.wumboos.app.moneyapp.transaction.TransactionCreatedEvent;
import com.wumboos.app.moneyapp.transaction.TransactionDTO;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class CategoryService {
	Logger log = LoggerFactory.getLogger(CategoryService.class);

	CategoryRepository categoryRepository;

	private final Scheduler scheduler;

	private ApplicationEventPublisher eventPublisher;

	public CategoryService(Scheduler scheduler, CategoryRepository categoryRepository,
			ApplicationEventPublisher eventPublisher) {
		this.scheduler = scheduler;
		this.categoryRepository = categoryRepository;
		this.eventPublisher = eventPublisher;
	}

	public Flux<Category> findAll() {
		return Flux.defer(() -> Flux.fromIterable(this.categoryRepository.findAll())).subscribeOn(scheduler);
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	@TransactionalEventListener
	Mono<Void> on(TransactionCreatedEvent event) {
		return Mono
				.fromCallable(() -> this.categoryRepository
						.save(new Category()))
				.publishOn(scheduler).then();
	}


	public Mono<Category> findById(UUID id) {
		return Mono.fromCallable(() -> this.categoryRepository.findById(id).orElseGet(null)).subscribeOn(scheduler);
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public Mono<Void> save(TransactionDTO dto) {
		this.eventPublisher.publishEvent(TransactionCreatedEvent.builder()
				.id(UUID.randomUUID())
				.amount(dto.getAmount())
				.category(dto.getCategory())
				.description(dto.getDescription())
				.owner(dto.getOwner())
				.type(dto.getType())
				.build());
		log.info("masukkkkk");
		return Mono.empty();
	}

}

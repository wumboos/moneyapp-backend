package com.wumboos.app.moneyapp.transaction.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.wumboos.app.moneyapp.transaction.TransactionCreatedEvent;
import com.wumboos.app.moneyapp.transaction.TransactionDTO;
import com.wumboos.app.moneyapp.transaction.model.Transaction;
import com.wumboos.app.moneyapp.transaction.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class TransactionService {
	Logger log = LoggerFactory.getLogger(TransactionService.class);

	TransactionRepository transactionRepository;

	private final Scheduler scheduler;

	private ApplicationEventPublisher eventPublisher;

	public TransactionService(Scheduler scheduler, TransactionRepository transactionRepository,
			ApplicationEventPublisher eventPublisher) {
		this.scheduler = scheduler;
		this.transactionRepository = transactionRepository;
		this.eventPublisher = eventPublisher;
	}

	public Flux<Transaction> findAll() {
		return Flux.defer(() -> Flux.fromIterable(this.transactionRepository.findAll())).subscribeOn(scheduler);
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	@TransactionalEventListener
	Mono<Void> on(TransactionCreatedEvent event) {
		return Mono
				.fromCallable(() -> this.transactionRepository
						.save(new Transaction()))
				.publishOn(scheduler).then();
	}


	public Mono<Transaction> findById(UUID id) {
		return Mono.fromCallable(() -> this.transactionRepository.findById(id).orElseGet(null)).subscribeOn(scheduler);
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

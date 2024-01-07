package com.wumboos.app.moneyapp.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wumboos.app.moneyapp.transaction.model.Transaction;
import com.wumboos.app.moneyapp.transaction.service.TransactionService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("${api.v1}/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@GetMapping
	public Flux<Transaction> transactions(){
		return transactionService.findAll();
	}
}

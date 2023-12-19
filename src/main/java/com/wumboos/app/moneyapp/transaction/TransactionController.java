package com.wumboos.app.moneyapp.transaction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	@GetMapping("/helloWorld")
	public Mono<String> helloWorld(@RequestParam(required = false) String hore){
		return Mono.just("helloworld");
	}
}

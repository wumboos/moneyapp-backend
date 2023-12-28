package com.wumboos.app.moneyapp.transaction.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;


public class Transaction {
	@Id
	private int id;

	private String desc;

	private BigDecimal amount;

	private String type;
	
	private String owner;
}

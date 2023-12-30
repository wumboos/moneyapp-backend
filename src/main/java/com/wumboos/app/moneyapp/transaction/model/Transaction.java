package com.wumboos.app.moneyapp.transaction.model;

import java.math.BigDecimal;
import java.util.UUID;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
	@Id
	private UUID id;

	private String description;

	private BigDecimal amount;

	private String type;
	
	private String owner;
	
	private String category;
}

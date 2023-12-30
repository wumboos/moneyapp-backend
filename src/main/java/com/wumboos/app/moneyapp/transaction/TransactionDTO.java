package com.wumboos.app.moneyapp.transaction;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Table;

public class TransactionDTO {

	private String description;

	private BigDecimal amount;

	private String type;

	private String owner;

	private String category;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "TransactionDTO [description=" + description + ", amount=" + amount + ", type=" + type + ", owner="
				+ owner + ", category=" + category + "]";
	}

}

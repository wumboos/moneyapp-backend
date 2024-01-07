package com.wumboos.app.moneyapp.category;

import java.math.BigDecimal;
import java.util.UUID;

public record CategoryCreatedEvent(UUID id, String description, BigDecimal amount, String type, String owner,
		String category) {

	public CategoryCreatedEvent() {
		this(null, null, null, null, null, null);
	}
	public static Builder builder() {
		return new Builder();
	}
	public static class Builder {
		UUID id;
		String description;
		BigDecimal amount;
		String type;
		String owner;
		String category;

		public Builder() {

		}

		public Builder id(UUID id) {
			this.id = id;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder type(String type) {
			this.type = type;
			return this;
		}

		public Builder owner(String owner) {
			this.owner = owner;
			return this;
		}

		public Builder category(String category) {
			this.category = category;
			return this;
		}

		public Builder amount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public CategoryCreatedEvent build() {
			return new CategoryCreatedEvent(this.id, this.description, this.amount, this.type, this.owner, this.category);
		}

	}
}

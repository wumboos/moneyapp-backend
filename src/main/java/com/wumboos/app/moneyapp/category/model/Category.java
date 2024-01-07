package com.wumboos.app.moneyapp.category.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {
	@Id
	private UUID id;

	private String name;

	private String owner;
}

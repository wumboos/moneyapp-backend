package com.wumboos.app.moneyapp.category.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wumboos.app.moneyapp.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>{
	
}

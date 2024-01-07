package com.wumboos.app.moneyapp.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wumboos.app.moneyapp.category.model.Category;
import com.wumboos.app.moneyapp.category.service.CategoryService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("${api.v1}/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public Flux<Category> categories(){
		return categoryService.findAll();
	}
}

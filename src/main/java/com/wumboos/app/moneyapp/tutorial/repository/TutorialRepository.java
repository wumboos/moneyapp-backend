package com.wumboos.app.moneyapp.tutorial.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wumboos.app.moneyapp.tutorial.model.Tutorial;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, UUID> {
	List<Tutorial> findByTitleContaining(String title);

	List<Tutorial> findByPublished(boolean isPublished);
}

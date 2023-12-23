package com.wumboos.app.moneyapp.tutorial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wumboos.app.moneyapp.tutorial.model.Tutorial;
import com.wumboos.app.moneyapp.tutorial.service.TutorialService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class TutorialController {
	@Autowired
	TutorialService tutorialService;
	private static final Logger LOG = LoggerFactory.getLogger(TutorialController.class);

	@GetMapping("/tutorials")
	@ResponseStatus(HttpStatus.OK)
	public Flux<Tutorial> getAllTutorials(@RequestParam(required = false) String title) {
		LOG.info("getAllTutorials");
		if (title == null)
			return tutorialService.findAll();
		else
			return tutorialService.findByTitleContaining(title);
	}

	@GetMapping("/tutorials/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<Tutorial> getTutorialById(@PathVariable("id") int id) {
		return tutorialService.findById(id);
	}

	@PostMapping("/tutorials")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		return tutorialService.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
	}

	@PutMapping("/tutorials/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<Tutorial> updateTutorial(@PathVariable("id") int id, @RequestBody Tutorial tutorial) {
		return tutorialService.update(id, tutorial);
	}

	@DeleteMapping("/tutorials/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteTutorial(@PathVariable("id") int id) {
		return tutorialService.deleteById(id);
	}

	@DeleteMapping("/tutorials")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteAllTutorials() {
		return tutorialService.deleteAll();
	}

	@GetMapping("/tutorials/published")
	@ResponseStatus(HttpStatus.OK)
	public Flux<Tutorial> findByPublished() {
		return tutorialService.findByPublished(true);
	}
}

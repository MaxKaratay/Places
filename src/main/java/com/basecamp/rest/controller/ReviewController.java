package com.basecamp.rest.controller;

import com.basecamp.rest.domain.Person;
import com.basecamp.rest.domain.Review;
import com.basecamp.rest.domain.Views;
import com.basecamp.rest.exception.NotFoundException;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.model.ReviewModel;
import com.basecamp.rest.service.PersonService;
import com.basecamp.rest.service.PlaceService;
import com.basecamp.rest.service.ReviewService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final PlaceService placeService;
    private final PersonService personService;

    @Autowired
    public ReviewController(ReviewService reviewService, PlaceService placeService, PersonService personService) {
        this.reviewService = reviewService;
        this.placeService = placeService;
        this.personService = personService;
    }

    @JsonView(Views.Public.class)
    @GetMapping
    public ResponseEntity<CollectionWrapper<Review>> getAll() {
        List<Review> all = reviewService.getAll();
        return new ResponseEntity<>(new CollectionWrapper<>(all), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @JsonView(Views.Public.class)
    @PostMapping
    public ResponseEntity<Review> add(@RequestBody @Valid ReviewModel reviewModel, Authentication person) throws NotFoundException {
        Review review = reviewModel.toReview(placeService);
        Optional<Person> personByName = personService.findPersonByName((String) person.getPrincipal());
        review.setPerson(personByName.orElseThrow(() -> new NotFoundException("Person with name " + person.getPrincipal())));
        return new ResponseEntity<>(reviewService.addReview(review), HttpStatus.OK);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/pages/{number}")
    public ResponseEntity<Page<Review>> getPageContent(@PathVariable int number,
                                                       @RequestParam(required = false) String filter,
                                                       @RequestParam(required = false) String cityName) {
        if (filter != null) {
            filter = filter.trim();
        }
        if (cityName != null) {
            cityName = cityName.trim();
        }
        PageRequest pageRequest = new PageRequest(number - 1, 4);
        return new ResponseEntity<>(reviewService.getAllForPage(pageRequest, filter, cityName), HttpStatus.OK);
    }

}

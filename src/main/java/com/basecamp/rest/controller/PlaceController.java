package com.basecamp.rest.controller;

import com.basecamp.rest.domain.Place;
import com.basecamp.rest.domain.Views;
import com.basecamp.rest.exception.NotFoundException;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.model.PlaceModel;
import com.basecamp.rest.service.CityService;
import com.basecamp.rest.service.PlaceService;
import com.basecamp.rest.service.TypeService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/places")
public class PlaceController {
    private final PlaceService placeService;
    private final CityService cityService;
    private final TypeService typeService;

    @Autowired
    public PlaceController(PlaceService placeService, CityService cityService, TypeService typeService) {
        this.placeService = placeService;
        this.cityService = cityService;
        this.typeService = typeService;
    }

    @JsonView(Views.Public.class)
    @GetMapping
    public ResponseEntity<CollectionWrapper<Place>> getAll() {
        List<Place> all = placeService.getAll();
        return new ResponseEntity<>(new CollectionWrapper<>(all), HttpStatus.OK);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/{id}")
    public ResponseEntity<Place> getById(@PathVariable int id) throws NotFoundException {
        Place place = placeService.getById(id).orElseThrow(() -> new NotFoundException("Place with id " + id + " not found"));
        return new ResponseEntity<>(place, HttpStatus.OK);
    }


    @PostMapping
    @JsonView(Views.Attribute.class)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Place> create(@RequestBody @Valid PlaceModel placeModel) {
        Place place = placeModel.toPlace(cityService, typeService);
        return new ResponseEntity<>(placeService.addPlace(place), HttpStatus.OK);
    }

}

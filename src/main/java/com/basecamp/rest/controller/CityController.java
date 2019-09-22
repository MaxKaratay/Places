package com.basecamp.rest.controller;

import com.basecamp.rest.domain.City;
import com.basecamp.rest.domain.Views;
import com.basecamp.rest.exception.NotFoundException;
import com.basecamp.rest.model.CityModel;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.service.CityService;
import com.basecamp.rest.service.CountryService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;
    private final CountryService countryService;

    @Autowired
    public CityController(CityService cityService, CountryService countryService) {
        this.cityService = cityService;
        this.countryService = countryService;
    }

    @GetMapping
    @JsonView(Views.Attribute.class)
    public ResponseEntity<CollectionWrapper<City>> getAll() {
        List<City> all = cityService.getAll();
        return new ResponseEntity<>(new CollectionWrapper<>(all), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<City> getById(@PathVariable int id) throws NotFoundException {
        City city = cityService.getById(id).orElseThrow(() -> new NotFoundException("City with id " + id + " not found"));
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    @PostMapping
    @JsonView(Views.Public.class)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<City> create(@RequestBody @Valid CityModel cityModel) {
        City city = cityModel.toCity(countryService);
        return new ResponseEntity<>(cityService.add(city), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        cityService.removeById(id);
        log.info("City with id {} was deleted", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package com.basecamp.rest.controller;

import com.basecamp.rest.domain.Country;
import com.basecamp.rest.domain.Views;
import com.basecamp.rest.exception.NotFoundException;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.model.CountryModel;
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
@RequestMapping("/countries")
public class CountryController {
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @JsonView(Views.Model.class)
    @GetMapping
    public ResponseEntity<CollectionWrapper<Country>> getAll() {
        List<Country> all = countryService.getAll();
        return new ResponseEntity<>(new CollectionWrapper<>(all), HttpStatus.OK);
    }

    @JsonView(Views.Model.class)
    @GetMapping("/{id}")
    public ResponseEntity<Country> getById(@PathVariable int id) throws NotFoundException {
        Country country = countryService.getById(id).orElseThrow(() -> new NotFoundException("Country with id " + id + " not found"));
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @JsonView(Views.Model.class)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Country> create(@RequestBody @Valid CountryModel country) {
        Country add = countryService.add(country.toCountry());
        return new ResponseEntity<>(add, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        countryService.removeById(id);
        log.info("City with id {} was deleted", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

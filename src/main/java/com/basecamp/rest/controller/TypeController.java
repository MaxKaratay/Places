package com.basecamp.rest.controller;

import com.basecamp.rest.domain.Type;
import com.basecamp.rest.exception.NotFoundException;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.service.TypeService;
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
@RequestMapping("/types")
public class TypeController {
    private final TypeService typeService;

    @Autowired
    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping
    public ResponseEntity<CollectionWrapper<Type>> getAll() {
        List<Type> all = typeService.getAll();
        return new ResponseEntity<>(new CollectionWrapper<>(all), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Type> getById(@PathVariable int id) throws NotFoundException {
        Type type = typeService.getById(id).orElseThrow(() -> new NotFoundException("Type with id " + id + " not found"));
        return new ResponseEntity<>(type, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Type> create(@RequestBody @Valid Type type) {
        return new ResponseEntity<>(typeService.addType(type), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        typeService.removeById(id);
        log.info("Type with id {} was deleted", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.basecamp.rest.controller;

import com.basecamp.rest.domain.Person;
import com.basecamp.rest.exception.ObjectExistsException;
import com.basecamp.rest.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final PasswordEncoder encoder;

    public PersonController(PersonService personService, PasswordEncoder encoder) {
        this.personService = personService;
        this.encoder = encoder;
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) throws ObjectExistsException {
        Optional<Person> personByName = personService.findPersonByName(person.getName());
        if (personByName.isPresent()) {
            throw new ObjectExistsException("Person with name " + person.getName() + " already exists");
        }
        String encode = encoder.encode(person.getPassword());
        person.setPassword(encode);
        return new ResponseEntity<>(personService.add(person), HttpStatus.OK);
    }
}

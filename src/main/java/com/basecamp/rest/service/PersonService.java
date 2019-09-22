package com.basecamp.rest.service;

import com.basecamp.rest.domain.Person;
import com.basecamp.rest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> findPersonByName(String name) {
        return personRepository.findByName(name);
    }

    public Person getById(Integer id) {
        return personRepository.findOne(id);
    }

    public void removeById(Integer id) {
        personRepository.delete(id);
    }

    public Person add(Person person) {
        return personRepository.save(person);
    }

}

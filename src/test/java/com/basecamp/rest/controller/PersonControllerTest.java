package com.basecamp.rest.controller;


import com.basecamp.rest.domain.Person;
import com.basecamp.rest.service.PersonService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class PersonControllerTest extends ControllerTest {
    @MockBean
    private PersonService personService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void create_ShouldReturnObjectOnSuccess() {
        Person person = new Person();
        person.setName("Name");
        person.setPassword("Pass");

        given(personService.findPersonByName(person.getName())).willReturn(Optional.empty());
        given(passwordEncoder.encode(person.getPassword())).willReturn(person.getPassword());
        given(personService.add(person)).willReturn(person);

        ResponseEntity<Person> entity = restTemplate.postForEntity("/person", person, Person.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(person);
        verify(personService, times(1)).findPersonByName(person.getName());
        verify(personService, times(1)).add(person);
        verifyNoMoreInteractions(personService);
    }
}
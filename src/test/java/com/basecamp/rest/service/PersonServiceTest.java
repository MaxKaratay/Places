package com.basecamp.rest.service;

import com.basecamp.rest.domain.Person;
import com.basecamp.rest.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest {
    @MockBean
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;
    private Person person;

    @Before
    public void init() {
        person = new Person();
        person.setName("person");
        person.setPassword("password");
        person.setId(1);
    }

    @Test
    public void findPersonByName_ShouldReturnPerson() {
        when(personRepository.findByName(person.getName())).thenReturn(Optional.of(person));

        Optional<Person> personByName = personService.findPersonByName(person.getName());

        assertThat(personByName.isPresent()).isTrue();
        assertThat(personByName.get()).isEqualTo(person);
        verify(personRepository, times(1)).findByName(person.getName());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void getById_ShouldReturnCorrectObject() {
        when(personRepository.findOne(person.getId())).thenReturn(person);

        Person byId = personService.getById(person.getId());
        assertThat(person).isEqualTo(byId);
        verify(personRepository, times(1)).findOne(person.getId());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void verifyNumberOfMethodCalls_removeById() {
        doNothing().when(personRepository).delete(person.getId());

        personService.removeById(person.getId());

        verify(personRepository, times(1)).delete(person.getId());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void verifyAddMethod() {
        when(personRepository.save(person)).thenReturn(person);

        personService.add(person);

        verify(personRepository, times(1)).save(person);
        verifyNoMoreInteractions(personRepository);
    }
}
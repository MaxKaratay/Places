package com.basecamp.rest.repository;

import com.basecamp.rest.domain.Person;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class PersonRepositoryTest extends RepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PersonRepository repository;
    private Person person;

    @Before
    public void init() {
        person = new Person();
        person.setName("person");
        person.setPassword("password");
    }

    @Test
    public void savePerson_ShouldSavePersonObject() {
        Person save = repository.save(person);

        assertThat(save.getId()).isNotNull();
        assertThat(save.getName()).isEqualTo(person.getName());
    }

    @Test
    public void findOne_ShouldFindCorrectObject() {
        Integer id = entityManager.persistAndGetId(person, Integer.class);

        Person found = repository.findOne(id);
        assertThat(found).isEqualTo(person);
    }

    @Test
    public void findAll_ShouldReturnAllCitiesObjects() {
        Person person1 = new Person();
        Person person2 = new Person();
        Person person3 = new Person();
        person1.setName("person1");
        person1.setPassword("password1");
        person2.setName("person2");
        person2.setPassword("password2");
        person3.setName("person3");
        person3.setPassword("password3");
        List<Person> people = Arrays.asList(person1, person2, person3);

        people.forEach(entityManager::persistAndFlush);
        List<Person> all = repository.findAll();

        all.forEach(country -> assertThat(country.getId()).isNotNull());
        assertThat(all).isEqualTo(people);
    }

    @Test
    public void removeCity_ShouldRemoveCity() {
        Integer id = entityManager.persistAndGetId(person, Integer.class);
        repository.delete(id);
        Person one = repository.findOne(id);

        assertThat(one).isNull();
    }


    @Test
    public void findByName_ShouldReturnPerson_IfIPersonExists() {
        entityManager.persistAndFlush(person);
        Optional<Person> person = repository.findByName("person");

        assertThat(person.isPresent()).isTrue();
    }

    @Test
    public void findByName_ShouldNotReturnPerson_IfPersonDoesntExist() {
        Optional<Person> person = repository.findByName("person");

        assertThat(person.isPresent()).isFalse();
    }

}
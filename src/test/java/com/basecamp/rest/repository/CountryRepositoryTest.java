package com.basecamp.rest.repository;

import com.basecamp.rest.domain.Country;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryRepositoryTest extends RepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CountryRepository repository;
    private Country country;

    @Before
    public void init() {
        country = new Country();
        country.setName("Country");
        country.setCode("CO");
    }

    @Test
    public void saveCountry_ShouldSaveCountryObject() {
        Country save = repository.save(country);

        assertThat(save.getId()).isNotNull();
        assertThat(save.getName()).isEqualTo(country.getName());
    }

    @Test
    public void findOne_ShouldFindCorrectObject() {
        Integer id = entityManager.persistAndGetId(this.country, Integer.class);

        Country found = repository.findOne(id);
        assertThat(found).isEqualTo(country);
    }

    @Test
    public void findAll_ShouldReturnAllCitiesObjects() {
        Country country1 = new Country();
        Country country2 = new Country();
        Country country3 = new Country();
        country1.setName("Country A");
        country1.setCode("CA");
        country2.setName("Country B");
        country2.setCode("CB");
        country3.setName("Country C");
        country3.setCode("CC");
        List<Country> countries = Arrays.asList(country1, country2, country3);

        countries.forEach(entityManager::persistAndFlush);
        List<Country> all = repository.findAll();

        all.forEach(country -> assertThat(country.getId()).isNotNull());
        assertThat(all).isEqualTo(countries);
    }

    @Test
    public void removeCity_ShouldRemoveCity() {
        Integer id = entityManager.persistAndGetId(country, Integer.class);
        repository.delete(id);
        Country one = repository.findOne(id);

        assertThat(one).isNull();
    }

    @Test
    public void findByName_ShouldFindCountry_WhenCountryExists() {
        entityManager.persistAndFlush(country);
        Optional<Country> byName = repository.findByName(country.getName());

        assertThat(byName.isPresent()).isTrue();
    }

    @Test
    public void findByName_ShouldNotFindCountry_WhenCountryDoesntExist() {
        Optional<Country> byName = repository.findByName(country.getName());

        assertThat(byName.isPresent()).isFalse();
    }

    @Test
    public void existsByName_ShouldReturnTrue_IfObjectExists() {
        entityManager.persistAndFlush(country);
        boolean exists = repository.existsByName(country.getName());

        assertThat(exists).isTrue();
    }

    @Test
    public void existsByName_ShouldReturnFalse_IfObjectDoesntExist() {
        boolean exists = repository.existsByName(country.getName());

        assertThat(exists).isFalse();
    }
}
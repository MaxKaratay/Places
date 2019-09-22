package com.basecamp.rest.repository;

import com.basecamp.rest.domain.City;
import com.basecamp.rest.domain.Country;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CityRepositoryTest extends RepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CityRepository cityRepository;
    private City city;

    @Before
    public void init() {
        city = new City();
        city.setName("City");
    }

    @Test
    public void saveCity_ShouldSaveCityObject() {
        City save = cityRepository.save(city);

        assertThat(save.getId()).isNotNull();
        assertThat(save.getName()).isEqualTo(city.getName());
    }

    @Test
    public void findOne_ShouldFindCorrectObject() {
        Integer id = entityManager.persistAndGetId(this.city, Integer.class);

        City found = cityRepository.findOne(id);
        assertThat(found).isEqualTo(city);
    }

    @Test
    public void findAll_ShouldReturnAllCitiesObjects() {
        City city1 = new City();
        City city2 = new City();
        City city3 = new City();
        city1.setName("CityA");
        city2.setName("CityB");
        city3.setName("CityC");
        List<City> cities = Arrays.asList(city1, city2, city3);

        cities.forEach(entityManager::persistAndFlush);
        List<City> all = cityRepository.findAll();

        all.forEach(city -> assertThat(city.getId()).isNotNull());
        assertThat(all).isEqualTo(cities);
    }

    @Test
    public void removeCity_ShouldRemoveCity() {
        Integer id = entityManager.persistAndGetId(city, Integer.class);
        cityRepository.delete(id);
        City one = cityRepository.findOne(id);

        assertThat(one).isNull();
    }

    @Test
    public void existsByNameAndCountryName_ShouldReturnTrue_ForExistingCity() {
        Country country = new Country();
        country.setName("Country");
        country.setCode("CO");
        city.setCountry(country);
        entityManager.persistAndFlush(country);
        entityManager.persistAndFlush(city);

        boolean exists = cityRepository.existsByNameAndCountryName("City", "Country");

        assertThat(exists).isTrue();
    }

    @Test
    public void existsByNameAndCountryName_ShouldReturnFalse_ForNonExistingCity() {
        boolean exists = cityRepository.existsByNameAndCountryName("City", "Country");
        assertThat(exists).isFalse();
    }
}
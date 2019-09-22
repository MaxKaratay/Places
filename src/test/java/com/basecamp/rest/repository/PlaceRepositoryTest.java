package com.basecamp.rest.repository;

import com.basecamp.rest.domain.Place;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaceRepositoryTest extends RepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PlaceRepository repository;
    private Place place;

    @Before
    public void init() {
        place = new Place();
        place.setName("place");
    }

    @Test
    public void savePlace_ShouldSavePlaceObject() {
        Place save = repository.save(place);

        assertThat(save.getId()).isNotNull();
        assertThat(save.getName()).isEqualTo(place.getName());
    }

    @Test
    public void findOne_ShouldFindCorrectObject() {
        Integer id = entityManager.persistAndGetId(place, Integer.class);

        Place found = repository.findOne(id);
        assertThat(found).isEqualTo(place);
    }

    @Test
    public void findAll_ShouldReturnAllCitiesObjects() {
        Place place1 = new Place();
        Place place2 = new Place();
        Place place3 = new Place();
        place1.setName("Place1");
        place2.setName("Place2");
        place3.setName("Place3");
        List<Place> places = Arrays.asList(place1, place2, place3);

        places.forEach(entityManager::persistAndFlush);
        List<Place> all = repository.findAll();

        all.forEach(city -> assertThat(city.getId()).isNotNull());
        assertThat(all).isEqualTo(places);
    }

    @Test
    public void removeCity_ShouldRemoveCity() {
        Integer id = entityManager.persistAndGetId(place, Integer.class);
        repository.delete(id);
        Place one = repository.findOne(id);

        assertThat(one).isNull();
    }
}
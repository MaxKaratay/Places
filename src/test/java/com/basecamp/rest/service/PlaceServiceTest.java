package com.basecamp.rest.service;

import com.basecamp.rest.domain.Place;
import com.basecamp.rest.repository.PlaceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlaceServiceTest {
    @MockBean
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceService placeService;
    private Place place;

    @Before
    public void setUp() throws Exception {
        place = new Place();
        place.setName("Place");
        place.setId(1);
    }

    @Test
    public void getAll_ShouldReturnAllObjects() {
        List<Place> places = Collections.singletonList(place);
        when(placeRepository.findAll()).thenReturn(places);

        List<Place> all = placeService.getAll();

        assertThat(all).isEqualTo(places);
        verify(placeRepository, times(1)).findAll();
        verifyNoMoreInteractions(placeRepository);
    }

    @Test
    public void addPlace_ShouldAddPlace() {
        when(placeRepository.save(place)).thenReturn(place);

        placeService.addPlace(place);

        verify(placeRepository, times(1)).save(place);
        verifyNoMoreInteractions(placeRepository);
    }

    @Test
    public void getById_ShouldReturnCorrectObject() {
        when(placeRepository.findOne(place.getId())).thenReturn(place);

        Place byId = placeService.getById(place.getId()).get();

        assertThat(byId).isEqualTo(place);
        verify(placeRepository, times(1)).findOne(place.getId());
        verifyNoMoreInteractions(placeRepository);
    }

    @Test
    public void verifyRemoveById() {
        doNothing().when(placeRepository).delete(place.getId());

        placeService.removeById(place.getId());

        verify(placeRepository, times(1)).delete(place.getId());
        verifyNoMoreInteractions(placeRepository);
    }
}
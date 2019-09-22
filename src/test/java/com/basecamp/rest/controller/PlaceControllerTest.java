package com.basecamp.rest.controller;

import com.basecamp.rest.config.WebSecurityConfig;
import com.basecamp.rest.domain.City;
import com.basecamp.rest.domain.Place;
import com.basecamp.rest.domain.Role;
import com.basecamp.rest.domain.Type;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.model.PlaceModel;
import com.basecamp.rest.service.CityService;
import com.basecamp.rest.service.PlaceService;
import com.basecamp.rest.service.TypeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class PlaceControllerTest extends ControllerTest {
    @MockBean
    private PlaceService placeService;
    @MockBean
    private TypeService typeService;
    @MockBean
    private CityService cityService;
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${jwt.key}")
    private String key;

    @Test
    public void getById_ShouldReturnCorrectObject() {
        Place place = new Place();
        place.setName("Place");
        place.setId(1);

        given(placeService.getById(1))
                .willReturn(Optional.of(place));
        ResponseEntity<Place> entity = restTemplate.getForEntity("/places/1", Place.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(place);
        verify(placeService, times(1)).getById(1);
        verifyNoMoreInteractions(placeService);
    }

    @Test
    public void getAll_ShouldReturnAllObjects() {
        List<Place> places = Arrays.asList(new Place(), new Place());

        given(placeService.getAll()).willReturn(places);
        ResponseEntity<CollectionWrapper<Place>> entity = restTemplate
                .exchange("/places", HttpMethod.GET, null, new ParameterizedTypeReference<CollectionWrapper<Place>>() {
                });

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(new CollectionWrapper<>(places));
        verify(placeService, times(1)).getAll();
        verifyNoMoreInteractions(placeService);
    }

    @Test
    public void create_ShouldReturnObjectOnSuccess() {
        PlaceModel model = new PlaceModel();
        model.setName("Place");
        model.setCityId(1);
        model.setCountryId(1);
        model.setTypeId(1);

        Place place = new Place();
        place.setName("Place");

        String token = getToken(key, Collections.singleton(Role.USER));
        HttpHeaders headers = new HttpHeaders();
        headers.add(WebSecurityConfig.TOKEN_HEADER, WebSecurityConfig.TOKEN_PREFIX + token);
        HttpEntity<PlaceModel> httpEntity = new HttpEntity<>(model, headers);

        given(cityService.getById(1)).willReturn(Optional.of(new City()));
        given(typeService.getById(1)).willReturn(Optional.of(new Type()));
        given(placeService.addPlace(any())).willReturn(place);

        ResponseEntity<Place> entity = restTemplate.postForEntity("/places", httpEntity, Place.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(place);
        verify(placeService, times(1)).addPlace(any());
        verifyNoMoreInteractions(placeService);
    }

}
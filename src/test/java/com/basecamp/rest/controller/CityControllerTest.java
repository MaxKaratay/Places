package com.basecamp.rest.controller;

import com.basecamp.rest.config.WebSecurityConfig;
import com.basecamp.rest.domain.City;
import com.basecamp.rest.domain.Country;
import com.basecamp.rest.domain.Role;
import com.basecamp.rest.model.CityModel;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.service.CityService;
import com.basecamp.rest.service.CountryService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


public class CityControllerTest extends ControllerTest {
    @MockBean
    private CityService cityService;
    @MockBean
    private CountryService countryService;
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${jwt.key}")
    private String key;

    @Test
    public void getById_ShouldReturnCorrectObject() {
        City city = new City();
        city.setName("City");
        city.setId(1);

        given(cityService.getById(1))
                .willReturn(Optional.of(city));
        ResponseEntity<City> entity = restTemplate.getForEntity("/cities/1", City.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(city);
        verify(cityService, times(1)).getById(1);
        verifyNoMoreInteractions(cityService);
    }

    @Test
    public void getAll_ShouldReturnAllObjects() {
        List<City> cities = Arrays.asList(new City(), new City());

        given(cityService.getAll()).willReturn(cities);
        ResponseEntity<CollectionWrapper<City>> entity = restTemplate.exchange("/cities", HttpMethod.GET, null, new ParameterizedTypeReference<CollectionWrapper<City>>() {
        });

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(new CollectionWrapper<>(cities));
        verify(cityService, times(1)).getAll();
        verifyNoMoreInteractions(cityService);
    }

    @Test
    public void create_ShouldReturnObjectOnSuccess() {
        City city = new City();
        city.setName("City");
        CityModel model = new CityModel();
        model.setCountryId(1);
        model.setName(city.getName());

        String token = getToken(key, Collections.singleton(Role.ADMIN));
        HttpHeaders headers = new HttpHeaders();
        headers.add(WebSecurityConfig.TOKEN_HEADER, WebSecurityConfig.TOKEN_PREFIX + token);
        HttpEntity<CityModel> httpEntity = new HttpEntity<>(model, headers);

        given(countryService.getById(1)).willReturn(Optional.of(new Country()));
        given(cityService.add(any())).willReturn(city);

        ResponseEntity<City> entity = restTemplate.postForEntity("/cities", httpEntity, City.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(city);
        verify(cityService, times(1)).add(any());
        verifyNoMoreInteractions(cityService);
    }

    @Test
    public void delete_ShouldReturnStatusOk() {
        String token = getToken(key, Collections.singleton(Role.ADMIN));
        HttpHeaders headers = new HttpHeaders();
        headers.add(WebSecurityConfig.TOKEN_HEADER, WebSecurityConfig.TOKEN_PREFIX + token);
        HttpEntity<City> httpEntity = new HttpEntity<>(null, headers);


        doNothing().when(cityService).removeById(1);

        ResponseEntity<Object> entity = restTemplate.exchange("/cities/1", HttpMethod.DELETE, httpEntity, Object.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cityService, times(1)).removeById(1);
        verifyNoMoreInteractions(cityService);
    }
}
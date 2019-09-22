package com.basecamp.rest.controller;

import com.basecamp.rest.config.WebSecurityConfig;
import com.basecamp.rest.domain.Country;
import com.basecamp.rest.domain.Role;
import com.basecamp.rest.model.CollectionWrapper;
import com.basecamp.rest.model.CountryModel;
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

public class CountryControllerTest extends ControllerTest {
    @MockBean
    private CountryService countryService;
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${jwt.key}")
    private String key;

    @Test
    public void getById_ShouldReturnCorrectObject() {
        Country country = new Country();
        country.setName("Country");
        country.setCode("CO");

        given(countryService.getById(1))
                .willReturn(Optional.of(country));
        ResponseEntity<Country> entity = restTemplate.getForEntity("/countries/1", Country.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(country);
        verify(countryService, times(1)).getById(1);
        verifyNoMoreInteractions(countryService);
    }

    @Test
    public void getAll_ShouldReturnAllObjects() {
        List<Country> countries = Arrays.asList(new Country(), new Country());

        given(countryService.getAll()).willReturn(countries);
        ResponseEntity<CollectionWrapper<Country>> entity = restTemplate
                .exchange("/countries", HttpMethod.GET, null, new ParameterizedTypeReference<CollectionWrapper<Country>>() {
                });

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(new CollectionWrapper<>(countries));
        verify(countryService, times(1)).getAll();
        verifyNoMoreInteractions(countryService);
    }

    @Test
    public void create_ShouldReturnObjectOnSuccess() {
        CountryModel model = new CountryModel();
        model.setCode("CO");
        model.setName("Countries");
        Country country = model.toCountry();

        String token = getToken(key, Collections.singleton(Role.ADMIN));
        HttpHeaders headers = new HttpHeaders();
        headers.add(WebSecurityConfig.TOKEN_HEADER, WebSecurityConfig.TOKEN_PREFIX + token);
        HttpEntity<CountryModel> httpEntity = new HttpEntity<>(model, headers);

        given(countryService.getById(1)).willReturn(Optional.of(new Country()));
        given(countryService.add(country)).willReturn(country);

        ResponseEntity<Country> entity = restTemplate.postForEntity("/countries", httpEntity, Country.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(country);
        verify(countryService, times(1)).add(any());
        verifyNoMoreInteractions(countryService);
    }

    @Test
    public void delete_ShouldReturnStatusOk() {
        String token = getToken(key, Collections.singleton(Role.ADMIN));
        HttpHeaders headers = new HttpHeaders();
        headers.add(WebSecurityConfig.TOKEN_HEADER, WebSecurityConfig.TOKEN_PREFIX + token);
        HttpEntity<Country> httpEntity = new HttpEntity<>(null, headers);


        doNothing().when(countryService).removeById(1);

        ResponseEntity<Object> entity = restTemplate.exchange("/countries/1", HttpMethod.DELETE, httpEntity, Object.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(countryService, times(1)).removeById(1);
        verifyNoMoreInteractions(countryService);
    }
}
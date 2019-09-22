package com.basecamp.rest.service;

import com.basecamp.rest.domain.City;
import com.basecamp.rest.domain.Country;
import com.basecamp.rest.repository.CityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

public class CityServiceTest {
    @Mock
    private CityRepository cityRepository;
    @InjectMocks
    private CityService cityService;
    private Country country;
    private City city;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        country = new Country();
        country.setName("country");
        city = new City();
        city.setName("city");
        city.setCountry(country);
    }

    @Test
    public void isExist_ShouldReturnTrue_ForExistingCity() {
        when(cityRepository.existsByNameAndCountryName(city.getName(), country.getName())).thenReturn(true);
        boolean actual = cityService.isExists(city);

        assertTrue("Should return true for existing object", actual);
    }

    @Test
    public void isExist_ShouldReturnFalse_ForNonExistingObject() {
        when(cityRepository.existsByNameAndCountryName(city.getName(), country.getName())).thenReturn(false);
        boolean exists = cityService.isExists(city);

        assertFalse("Should returnFalse for non existing object", exists);
    }

    @Test
    public void getAll_ShouldReturnAllCityRecords() {
        List<City> cities = Arrays.asList(new City(), new City(), new City());
        when(cityRepository.findAll()).thenReturn(cities);
        List<City> all = cityService.getAll();
        assertEquals("Lists should be the same",cities, all);
    }
}
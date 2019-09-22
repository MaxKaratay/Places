package com.basecamp.rest.service;

import com.basecamp.rest.domain.Country;
import com.basecamp.rest.repository.CountryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryServiceTest {
    @MockBean
    private CountryRepository countryRepository;
    @Autowired
    private CountryService countryService;
    private Country country;

    @Before
    public void setUp() {
        country = new Country();
        country.setName("Country");
        country.setCode("CO");
        country.setId(1);
    }

    @Test
    public void getAll_ShouldReturnAllObjects() {
        Country country1 = new Country();
        Country country2 = new Country();
        List<Country> countries = Arrays.asList(country1, country2);

        when(countryRepository.findAll()).thenReturn(countries);
        List<Country> all = countryService.getAll();

        assertThat(all).isEqualTo(countries);
        verify(countryRepository, times(1)).findAll();
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void getByName_ShouldReturnObject() {
        when(countryRepository.findByName(country.getName())).thenReturn(Optional.of(country));
        Optional<Country> byName = countryService.getByName(country.getName());

        assertThat(byName.isPresent()).isTrue();
        assertThat(byName.get()).isEqualTo(country);
        verify(countryRepository, times(1)).findByName(country.getName());
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void getById_ShouldReturnCorrectObject() {
        when(countryRepository.findOne(country.getId())).thenReturn(country);
        Country byId = countryService.getById(country.getId()).get();

        assertThat(byId).isEqualTo(country);
        verify(countryRepository, times(1)).findOne(country.getId());
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void verifySingleCall_ToSaveMethod() {
        when(countryRepository.save(country)).thenReturn(country);

        countryService.add(country);

        verify(countryRepository, times(1)).save(country);
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void isExist_ShouldReturnTrueForExistsObject() {
        when(countryRepository.existsByName(country.getName())).thenReturn(true);

        boolean exist = countryService.isExist(country.getName());

        assertThat(exist).isTrue();
        verify(countryRepository, times(1)).existsByName(country.getName());
        verifyNoMoreInteractions(countryRepository);
    }
}
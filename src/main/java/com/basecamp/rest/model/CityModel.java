package com.basecamp.rest.model;

import com.basecamp.rest.domain.City;
import com.basecamp.rest.service.CountryService;
import com.basecamp.rest.validation.CityName;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CityModel {
    private int countryId;
    @NotBlank(message = "Please fill city name")
    @CityName
    private String name;

    public City toCity(CountryService countryService) {
        City city = new City();
        city.setCountry(countryService.getById(countryId).get());
        city.setName(name);
        return city;
    }
}

package com.basecamp.rest.service;


import com.basecamp.rest.domain.City;
import com.basecamp.rest.repository.CityRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAll() {
        return cityRepository.findAll();
    }

    public boolean isExists(City city) {
        return cityRepository.existsByNameAndCountryName(city.getName(), city.getCountry().getName());
    }

    public Optional<City> getById(Integer id) {
        return Optional.ofNullable(cityRepository.findOne(id));
    }

    public void removeById(Integer id) {
        cityRepository.delete(id);
    }

    public City add(City city) {
        return cityRepository.save(city);
    }
}

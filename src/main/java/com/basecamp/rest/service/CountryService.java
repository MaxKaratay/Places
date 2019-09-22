package com.basecamp.rest.service;

import com.basecamp.rest.domain.Country;
import com.basecamp.rest.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    public Optional<Country> getByName(String name) {
        return countryRepository.findByName(name);
    }

    public Optional<Country> getById(Integer id) {
        return Optional.ofNullable(countryRepository.findOne(id));
    }

    public void removeById(Integer id) {
        countryRepository.delete(id);
    }

    public Country add(Country country) {
        return countryRepository.save(country);
    }

    public boolean isExist(String name) {
        return countryRepository.existsByName(name);
    }
}

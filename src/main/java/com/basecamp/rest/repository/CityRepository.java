package com.basecamp.rest.repository;

import com.basecamp.rest.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
    boolean existsByNameAndCountryName(String name, String countryName);
}

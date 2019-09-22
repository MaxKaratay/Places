package com.basecamp.rest.service;

import com.basecamp.rest.domain.Place;
import com.basecamp.rest.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }


    public List<Place> getAll() {
        return placeRepository.findAll();
    }

    public Place addPlace(Place place) {
        return placeRepository.save(place);
    }

    public Optional<Place> getById(int id) {
        return Optional.ofNullable(placeRepository.findOne(id));
    }

    public void removeById(int id) {
        placeRepository.delete(id);
    }
}

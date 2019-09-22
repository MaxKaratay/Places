package com.basecamp.rest.model;

import com.basecamp.rest.domain.Place;
import com.basecamp.rest.service.CityService;
import com.basecamp.rest.service.TypeService;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class PlaceModel {
    private int cityId;
    private int countryId;
    private int typeId;
    @Size(min = 2, max = 20, message = "Valid place name from 2 to 20 characters")
    private String name;

    public Place toPlace(CityService cityService, TypeService typeService) {
        Place place = new Place();
        place.setName(name);
        place.setCity(cityService.getById(cityId).get());
        place.setType(typeService.getById(typeId).get());
        return place;
    }
}

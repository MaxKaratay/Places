package com.basecamp.rest.model;

import com.basecamp.rest.domain.Country;
import com.basecamp.rest.validation.CountryCode;
import com.basecamp.rest.validation.CountryName;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CountryModel {
    @NotBlank(message = "Fill country name")
    @CountryName
    private String name;
    @NotBlank(message = "Fill country code")
    @CountryCode
    private String code;

    public Country toCountry() {
        Country country = new Country();
        country.setName(name);
        country.setCode(code);
        return country;
    }
}

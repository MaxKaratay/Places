package com.basecamp.rest.validation;

import com.basecamp.rest.validation.CityName;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CityNameValidator implements ConstraintValidator<CityName, String> {
    /**
     * City name can be presented by several words that delimited by ' ' or '-' sign
     */
    public static final String CITY_NAME_REG_EXP = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";

    public void initialize(CityName constraint) {
    }

    public boolean isValid(String name, ConstraintValidatorContext context) {
        return StringUtils.isNotEmpty(name) && name.matches(CITY_NAME_REG_EXP);
    }
}

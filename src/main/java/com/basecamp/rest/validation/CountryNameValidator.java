package com.basecamp.rest.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryNameValidator implements ConstraintValidator<CountryName, String> {
    /**
     * Regular expression for country name, not allows anagrams, firs word of country name should start from uppercase
     * letter, all other words in country name may avoid it`s rule.
     */
    public static final String COUNTRY_NAME_REG_EX = "^[A-Z][a-z]*(?: [A-Z]?[a-z]*)*$";

    public void initialize(CountryName constraint) {
    }

    public boolean isValid(String name, ConstraintValidatorContext context) {
        return StringUtils.isNotEmpty(name) && name.matches(COUNTRY_NAME_REG_EX);
    }
}

package com.basecamp.rest.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryCodeValidator implements ConstraintValidator<CountryCode, String> {
    /**
     * String should contain only characters, and its length should be from 2 to 4 characters inclusive
     */
    public static final String COUNTRY_CODE_REG_EXP = "^[a-zA-Z]{2,4}$";

    public void initialize(CountryCode constraint) {
    }

    public boolean isValid(String code, ConstraintValidatorContext context) {
        return StringUtils.isNotEmpty(code) && code.matches(COUNTRY_CODE_REG_EXP);
    }
}

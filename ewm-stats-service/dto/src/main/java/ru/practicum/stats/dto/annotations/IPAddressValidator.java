package ru.practicum.stats.dto.annotations;

import inet.ipaddr.IPAddressString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IPAddressValidator implements ConstraintValidator<IPAddress, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return new IPAddressString(s).isValid();
    }
}

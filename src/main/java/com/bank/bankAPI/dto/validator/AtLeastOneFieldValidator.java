package com.bank.bankAPI.dto.validator;

import com.bank.bankAPI.dto.UserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneFieldValidator implements ConstraintValidator<AtLeastOneField, UserDto> {

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
        return userDto.getEmail() != null && !userDto.getEmail().isEmpty()
                || userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().isEmpty();
    }
}

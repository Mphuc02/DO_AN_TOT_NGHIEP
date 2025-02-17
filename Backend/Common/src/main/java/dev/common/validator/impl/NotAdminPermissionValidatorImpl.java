package dev.common.validator.impl;

import dev.common.model.Role;
import dev.common.validator.NotAdminPermissionValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class NotAdminPermissionValidatorImpl implements ConstraintValidator<NotAdminPermissionValidator, Role> {
    @Override
    public boolean isValid(Role role, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(role))
            return true;
        return !role.equals(Role.ADMIN);
    }
}
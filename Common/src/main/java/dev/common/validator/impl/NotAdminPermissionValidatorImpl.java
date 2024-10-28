package dev.common.validator.impl;

import dev.common.model.Permission;
import dev.common.validator.NotAdminPermissionValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class NotAdminPermissionValidatorImpl implements ConstraintValidator<NotAdminPermissionValidator, Permission> {
    @Override
    public boolean isValid(Permission permission, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(permission))
            return true;
        return !permission.equals(Permission.ADMIN);
    }
}
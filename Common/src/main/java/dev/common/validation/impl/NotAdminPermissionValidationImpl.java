package dev.common.validation.impl;

import dev.common.model.Permission;
import dev.common.validation.NotAdminPermissionValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class NotAdminPermissionValidationImpl implements ConstraintValidator<NotAdminPermissionValidation, Permission> {
    @Override
    public boolean isValid(Permission permission, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(permission))
            return true;
        return  !permission.equals(Permission.ADMIN_CREATE) &&
                !permission.equals(Permission.ADMIN_UPDATE) &&
                !permission.equals(Permission.ADMIN_READ) &&
                !permission.equals(Permission.ADMIN_DELETE);
    }
}
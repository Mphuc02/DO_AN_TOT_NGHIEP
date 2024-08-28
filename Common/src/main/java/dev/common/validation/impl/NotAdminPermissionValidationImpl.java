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
        //Todo: Chưa thể xác thực các role khác ADMIN
        return  !permission.getPermission().equals(Permission.ADMIN_CREATE) &&
                !permission.getPermission().equals(Permission.ADMIN_UPDATE) &&
                !permission.getPermission().equals(Permission.ADMIN_READ) &&
                !permission.getPermission().equals(Permission.ADMIN_DELETE);
    }
}
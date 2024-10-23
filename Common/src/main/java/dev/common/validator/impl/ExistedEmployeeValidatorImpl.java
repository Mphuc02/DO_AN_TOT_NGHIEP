package dev.common.validator.impl;

import dev.common.client.RoleClient;
import dev.common.model.Permission;
import dev.common.validator.ExistedEmployeeValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ExistedEmployeeValidatorImpl implements ConstraintValidator<ExistedEmployeeValidator, UUID> {
    private final RoleClient roleClient;

    private Permission permission;

    @Override
    public void initialize(ExistedEmployeeValidator constraintAnnotation) {
        permission = constraintAnnotation.permissions();
    }

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null){
            return true;
        }

        List<Permission> permissionsOfEmployee = roleClient.getAllRolesOfEmployee(id);
        for (Permission temp : permissionsOfEmployee) {
            if(temp.equals(permission)){
                return true;
            }
        }
        return false;
    }
}
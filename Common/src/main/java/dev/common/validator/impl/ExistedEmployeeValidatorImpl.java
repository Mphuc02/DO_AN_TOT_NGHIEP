package dev.common.validator.impl;

import dev.common.client.RoleClient;
import dev.common.model.Role;
import dev.common.validator.ExistedEmployeeValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ExistedEmployeeValidatorImpl implements ConstraintValidator<ExistedEmployeeValidator, UUID> {
    private final RoleClient roleClient;

    private Role role;

    @Override
    public void initialize(ExistedEmployeeValidator constraintAnnotation) {
        role = constraintAnnotation.permissions();
    }

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null){
            return true;
        }

        List<Role> permissionsOfEmployee = roleClient.getAllRolesOfEmployee(id);
        for (Role temp : permissionsOfEmployee) {
            if(temp.equals(role)){
                return true;
            }
        }
        return false;
    }
}
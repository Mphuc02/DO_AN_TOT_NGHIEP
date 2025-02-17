package dev.common.util;

import dev.common.model.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuditingUtil {
    public AuthenticatedUser getUserLogged(){
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
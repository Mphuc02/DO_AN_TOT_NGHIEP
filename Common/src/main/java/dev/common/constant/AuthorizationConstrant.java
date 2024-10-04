package dev.common.constant;

public class AuthorizationConstrant {
    public static final String ADMIN = "hasAnyAuthority('ADMIN_READ', 'ADMIN_UPDATE', 'ADMIN_CREATE', 'ADMIN_DELETE')";
    public static final String GREETING_EMPLOYEE = "hasAuthority('RECEPTION_STAFF')";
    public static final String DOCTOR = "hasAuthority('DOCTOR')";
    public static final String USER = "hasAuthority('USER')";
}
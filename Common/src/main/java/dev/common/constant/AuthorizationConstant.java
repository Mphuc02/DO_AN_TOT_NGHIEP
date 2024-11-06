package dev.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationConstant {
    public static final String ADMIN = "hasAnyAuthority('ADMIN')";
    public static final String RECEIPT = "hasAuthority('RECEPTION_STAFF')";
    public static final String DOCTOR = "hasAuthority('DOCTOR')";
    public static final String USER = "hasAuthority('USER')";
    public static final String RECEIPT_ADMIN = "hasAnyAuthority('RECEPTION_STAFF', 'ADMIN')";
    public static final String RECEIPT_ADMIN_DOCTOR = "hasAnyAuthority('RECEPTION_STAFF', 'ADMIN', 'DOCTOR')";
}

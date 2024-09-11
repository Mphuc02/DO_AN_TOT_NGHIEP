package dev.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission {
    ADMIN_READ("Quản trị viên"),
    ADMIN_UPDATE( "Quản trị viên"),
    ADMIN_CREATE("Quản trị viên"),
    ADMIN_DELETE("Quản trị viên"),
    RECEPTION_STAFF("Nhân viên tiếp đón"),
    DOCTOR("Bác sĩ"),
    MEDICINE_DISPENSER("Nhân viên phát thuốc");

    private final String position;
}
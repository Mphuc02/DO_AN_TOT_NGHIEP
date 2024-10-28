package dev.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission {
    ADMIN("Quản trị viên"),
    RECEPTION_STAFF("Nhân viên tiếp đón"),
    DOCTOR("Bác sĩ"),
    MEDICINE_DISPENSER("Nhân viên kho thuốc"),
    USER("Người dùng");

    private final String position;
}
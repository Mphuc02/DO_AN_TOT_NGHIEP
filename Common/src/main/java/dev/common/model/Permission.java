package dev.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission {
    ADMIN_READ("admin:read", "Quản trị viên"),
    ADMIN_UPDATE("admin:update", "Quản trị viên"),
    ADMIN_CREATE("admin:create", "Quản trị viên"),
    ADMIN_DELETE("admin:delete", "Quản trị viên"),
    TEST("ASD", "112312");

    private final String permission;
    private final String position;
}
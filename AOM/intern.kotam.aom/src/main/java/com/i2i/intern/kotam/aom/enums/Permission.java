package com.i2i.intern.kotam.aom.enums;



public enum Permission {
    READ("read"),
    WRITE("write"),
    DELETE("delete"),
    UPDATE("update");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
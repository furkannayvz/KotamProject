package com.i2i.intern.kotam.aom.enums;

public enum TokenType {
    BEARER("Bearer"),
    BASIC("Basic"),
    REFRESH("Refresh");

    private final String type;

    TokenType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
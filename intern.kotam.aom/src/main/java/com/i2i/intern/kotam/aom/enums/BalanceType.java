package com.i2i.intern.kotam.aom.enums;

public enum BalanceType {
    MAIN_BALANCE("Main Balance"),
    SMS_BALANCE("SMS Balance"),
    VOICE_BALANCE("Voice Balance"),
    DATA_BALANCE("Data Balance");

    private final String type;

    BalanceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
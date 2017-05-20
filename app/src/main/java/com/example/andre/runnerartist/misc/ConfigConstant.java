package com.example.andre.runnerartist.misc;

public final class ConfigConstant {
    private String value;
    private ConfigConstant(String value) {
        this.value = value;
    }
    public String val() {
        return this.value;
    }
    public static final ConfigConstant DEFAULT_PROFILE_ID = new ConfigConstant("DEFAULT_PROFILE_ID");
    public static final ConfigConstant DEFAULT_CONTINUOUS = new ConfigConstant("DEFAULT_CONTINUOUS");
}

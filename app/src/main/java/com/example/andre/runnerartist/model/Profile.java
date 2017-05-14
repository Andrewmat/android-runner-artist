package com.example.andre.runnerartist.model;

public class Profile {
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Profile withName(String name) {
        this.name = name;
        return this;
    }
}

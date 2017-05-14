package com.example.andre.runnerartist.model;

public class Drawing {
    private Path path;
    private Profile profile;
    private Long startCreationTime;
    private Long finishCreationTime;
    private String description;
    private Boolean cycle;

    public Path getPath() {
        return path;
    }
    public Profile getProfile() {
        return profile;
    }
    public Long getStartCreationTime() {
        return startCreationTime;
    }
    public Long getFinishCreationTime() {
        return finishCreationTime;
    }
    public String getDescription() {
        return description;
    }
    public Boolean getCycle() {
        return cycle;
    }
    public void setPath(Path path) {
        this.path = path;
    }
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    public void setStartCreationTime(Long startCreationTime) {
        this.startCreationTime = startCreationTime;
    }
    public void setFinishCreationTime(Long finishCreationTime) {
        this.finishCreationTime = finishCreationTime;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCycle(Boolean cycle) {
        this.cycle = cycle;
    }
    public Drawing withPath(Path path) {
        this.path = path;
        return this;
    }
    public Drawing withProfile(Profile profile) {
        this.profile = profile;
        return this;
    }
    public Drawing withStartCreationTime(Long startCreationTime) {
        this.startCreationTime = startCreationTime;
        return this;
    }
    public Drawing withFinishCreationTime(Long finishCreationTime) {
        this.finishCreationTime = finishCreationTime;
        return this;
    }
    public Drawing withDescription(String description) {
        this.description = description;
        return this;
    }
    public Drawing withCycle(Boolean cycle) {
        this.cycle = cycle;
        return this;
    }
}

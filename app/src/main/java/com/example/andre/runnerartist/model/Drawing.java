package com.example.andre.runnerartist.model;

import android.content.ContentValues;

import java.io.Serializable;

public class Drawing implements ContentValueAble, Serializable {

    private static final long serialVersionUID = 1894757065349691269L;
    private Long id;
    private DrawingPath drawingPath;
    private Profile profile;
    private Long startCreationTime;
    private Long finishCreationTime;
    private String description;
    private Boolean cycle;

    public Long getId() {
        return id;
    }
    public DrawingPath getDrawingPath() {
        return drawingPath;
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
    public void setId(Long id) {
        this.id = id;
    }
    public void setDrawingPath(DrawingPath drawingPath) {
        this.drawingPath = drawingPath;
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
    public Drawing withId(Long id) {
        setId(id);
        return this;
    }
    public Drawing withPath(DrawingPath drawingPath) {
        setDrawingPath(drawingPath);
        return this;
    }
    public Drawing withProfile(Profile profile) {
        setProfile(profile);
        return this;
    }
    public Drawing withStartCreationTime(Long startCreationTime) {
        setStartCreationTime(startCreationTime);
        return this;
    }
    public Drawing withFinishCreationTime(Long finishCreationTime) {
        setFinishCreationTime(finishCreationTime);
        return this;
    }
    public Drawing withDescription(String description) {
        setDescription(description);
        return this;
    }
    public Drawing withCycle(Boolean cycle) {
        setCycle(cycle);
        return this;
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues cval = new ContentValues();
        cval.put("description", getDescription());
        cval.put("cycle", getCycle());
        cval.put("start", getStartCreationTime());
        cval.put("finish", getFinishCreationTime());
        cval.put("profile_id", getProfile().getId());
        return cval;
    }
}

package com.example.andre.runnerartist.model;

import android.content.ContentValues;

import java.io.Serializable;

public class Profile implements ContentValueAble, Serializable {

    private static final long serialVersionUID = 448655919120546380L;
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Profile withId(Long id) {
        setId(id);
        return this;
    }
    public Profile withName(String name) {
        setName(name);
        return this;
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues cval = new ContentValues();
        cval.put("name", getName());
        return cval;
    }
}

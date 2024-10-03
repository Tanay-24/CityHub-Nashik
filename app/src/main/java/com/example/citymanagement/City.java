package com.example.citymanagement;

public class City {
    private String name;
    private int imageResourceId;

    public City(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}

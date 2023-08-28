package com.example.event_assignment_lyp;

public class YourDataModel {
    private String imageResource;
    private String description;

    public YourDataModel(String imageResource, String description) {

        this.imageResource = imageResource;
        this.description = description;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getDescription() {
        return description;
    }
}

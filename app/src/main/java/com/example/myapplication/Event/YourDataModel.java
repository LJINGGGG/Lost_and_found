package com.example.myapplication.Event;

public class YourDataModel {
    private String imageResource;
    private String postlink;
    private String description;

    public YourDataModel(String imageResource, String description, String postlink) {

        this.imageResource = imageResource;
        this.postlink = postlink;
        this.description = description;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getDescription() {
        return description;
    }

    public String getLink(){ return postlink;}
}

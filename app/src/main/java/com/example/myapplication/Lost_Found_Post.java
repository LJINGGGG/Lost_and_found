package com.example.myapplication;
public class Lost_Found_Post {
    private String information;
    private String category;
    private String location;
    private String post_catrgory;
    private String imageurl;
    private String name;

    public Lost_Found_Post(String category, String imageurl, String information, String location, String name, String post_catrgory) {
        this.information = information;
        this.category = category;
        this.location = location;
        this.post_catrgory = post_catrgory;
        this.imageurl = imageurl;
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPost_catrgory() {
        return post_catrgory;
    }

    public void setPost_catrgory(String post_catrgory) {
        this.post_catrgory = post_catrgory;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

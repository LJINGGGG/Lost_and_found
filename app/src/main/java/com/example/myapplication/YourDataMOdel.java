package com.example.myapplication;

public class YourDataMOdel {
    private int Image_Post;
    private String description_post;

    private String name;

    public YourDataMOdel(int Image_Post, String description_post, String name) {
        this.Image_Post = Image_Post;
        this.description_post = description_post;
        this.name = name;
    }

    public int getImage_Post() {
        return Image_Post;
    }

    public void setImage_Post(int image_Post) {
        Image_Post = image_Post;
    }

    public String getDescription_post() {
        return description_post;
    }

    public void setDescription_post(String description_post) {
        this.description_post = description_post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemName() {
return String.valueOf(true);
    }
}

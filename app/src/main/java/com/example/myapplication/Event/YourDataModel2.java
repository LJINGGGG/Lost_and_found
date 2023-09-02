package com.example.myapplication.Event;

public class YourDataModel2 {
    //private String imageResource;
    private String Name;

    public YourDataModel2(String Name) {
       // this.imageResource = imageResource;
        this.Name = Name;
    }

    //public int getImageResource() {return imageResource;}

    public String getDescription() {
        return Name;
    }
}

package com.example.myapplication.user_features;

public class User {

    private String name;
    private String email;
    private String date;
    private String phone_number ;

    private String country;

    private String state;

    private String userType;

    private String imageUrI;


    public User(){

    }

    public User(String name, String email, String date, String phone_number, String country, String state, String userType, String imageUrI) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.phone_number = phone_number;
        this.country = country;
        this.state = state;
        this.userType = userType;
        this.imageUrI = imageUrI;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImageUrI() {
        return imageUrI;
    }

    public void setImageUrI(String imageUrI) {
        this.imageUrI = imageUrI;
    }

}

package com.example.myapplication;

public class User {

    private String name;
    private String email;
    private String date;
    private String phone_number ;
    private String gender;


    public User(){

    }

    public User(String name, String date, String email , String phone_number , String gender ) {
        this.name = name;
        this.date = date;
        this.email = email;
        this.phone_number = phone_number;
        this.gender = gender;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

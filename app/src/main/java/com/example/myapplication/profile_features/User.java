package com.example.myapplication.profile_features;

public class User {
    private String username;
    private String email;
    private String country;
    private String dateOfBirth;
    private String phoneNumber;
    private String state;
    private String userType;
    private String profileImageUrl;

    public User() {
    }

    public User(String username, String email, String country, String dateOfBirth,
                String phoneNumber, String state, String userType, String profileImageUrl) {
        this.username = username;
        this.email = email;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.userType = userType;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
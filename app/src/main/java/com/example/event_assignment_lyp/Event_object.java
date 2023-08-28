package com.example.event_assignment_lyp;

public class Event_object {
    private String event_name;
    private String date;
    private String information;
    private String category;
    private String location;
    private String State;
    private String image_video;
    private int number_helper = 0;
    private String auth_user;


    public Event_object() {
        // Default constructor required for Firebase
    }

    public Event_object(String event_name, String date,String info, String ctgory, String loc, String State, String imvi, String user) {
        this.event_name = event_name;
        this.date = date;
        this.information = info;
        this.category = ctgory;
        this.location = loc;
        this.State = State;
        this.image_video = imvi;
        this.auth_user = user;
    }
}

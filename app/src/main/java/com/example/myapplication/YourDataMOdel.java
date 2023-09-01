package com.example.myapplication;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class YourDataMOdel {
    private String Post;
    private String img_url;
    private String category;
    private String Location;
    private String info;
    private String name;
    private DatabaseReference databsereference = FirebaseDatabase.getInstance().getReference("post_found_lost");

    public void YourDataModel(String Image_Post) {
        this.Post = Image_Post;

        databsereference.child(Image_Post).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                category = snapshot.child("category").getValue(String.class);
                Location = snapshot.child("location").getValue(String.class);
                info = snapshot.child("information").getValue(String.class);
                img_url = snapshot.child("imageurl").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public String getPost() {
        return Post;
    }

    public String getImg_post() {
        return  img_url;
    }

    public String getCategory() {
        return category;
    }

    public String get_loc() {
        return Location;
    }

    public String getpost_Name() {
        return name;
    }

    public String getInfo() {
        return info;
    }
}
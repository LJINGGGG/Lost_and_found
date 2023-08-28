package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton filterButton = findViewById(R.id.FilterButton);
        ImageButton eventButton = findViewById(R.id.EventButton);
        ImageButton homeButton = findViewById(R.id.HomeButton);
        FloatingActionButton addButton = findViewById(R.id.addButton);
        ImageButton nearbyButton = findViewById(R.id.NearByButton);
        ImageButton meButton = findViewById(R.id.MeButton);

        RecyclerView view_ = findViewById(R.id.recyclerView);

        List<YourDataMOdel> data = new ArrayList<>();

        data.add(new YourDataMOdel(R.drawable.pic, "Description 1" , "DF"));
        data.add(new YourDataMOdel(R.drawable.pic, "Description 2","FFF"));
        data.add(new YourDataMOdel(R.drawable.pic, "Description 2","FFF"));
        data.add(new YourDataMOdel(R.drawable.pic, "Description 2","FFF"));
        data.add(new YourDataMOdel(R.drawable.pic, "Description 2","FFF"));
        data.add(new YourDataMOdel(R.drawable.pic, "Description 2","FFF"));

        filterButton.setOnClickListener(v -> {

        });

        eventButton.setOnClickListener(v -> {

        });

        homeButton.setOnClickListener(v -> {

        });

        addButton.setOnClickListener(v -> {

        });

        nearbyButton.setOnClickListener(v -> {

        });

        meButton.setOnClickListener(v -> {

        });


        view_.setLayoutManager(new LinearLayoutManager(this));
        view_.setAdapter(new dataModel_adapter(getApplicationContext(),data));
    }





}
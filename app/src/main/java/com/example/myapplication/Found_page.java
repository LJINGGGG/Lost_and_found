package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.android.car.ui.toolbar.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Found_page extends AppCompatActivity {
    private View swipeRefreshLayout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_page);



        ImageButton filterButton = findViewById(R.id.FilterButton);
        ImageButton eventButton = findViewById(R.id.EventButton);
        ImageButton homeButton = findViewById(R.id.HomeButton);
        FloatingActionButton addButton = findViewById(R.id.addButton);
        ImageButton nearbyButton = findViewById(R.id.NearByButton);
        ImageButton meButton = findViewById(R.id.MeButton);
        RecyclerView view_ = findViewById(R.id.recyclerView);
        TabLayout LostPage = findViewById(R.id.Lost_tab);

       LostPage.setOnClickListener(v->{
            Intent intent = new Intent(Found_page.this,MainActivity.class);
            startActivity(intent);
        });

        List<YourDataMOdel> data = new ArrayList<>();

        filterButton.setOnClickListener(v -> {
            Intent intent = new Intent(Found_page.this,filter.class);
            startActivity(intent);
        });

        eventButton.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this,filter.class);
//            startActivity(intent);
        });

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Found_page.this,MainActivity.class);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(Found_page.this,Create_post.class);
            startActivity(intent);
        });

        nearbyButton.setOnClickListener(v -> {

        });

        meButton.setOnClickListener(v -> {

        });


        view_.setLayoutManager(new LinearLayoutManager(this));
        view_.setAdapter(new dataModel_adapter(getApplicationContext(),data));
    }


}
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.android.car.ui.toolbar.TabLayout;
import android.widget.Button;

import com.example.myapplication.nearBy_features.nearBy;
import com.example.myapplication.user_features.LogIn_;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private View swipeRefreshLayout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

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
        TabLayout FoundPage = findViewById(R.id.Found_tab);

        FoundPage.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this,Found_page.class);
            startActivity(intent);
        });

        List<YourDataMOdel> data = new ArrayList<>();

        filterButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,filter.class);
            startActivity(intent);
        });

        eventButton.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this,filter.class);
//            startActivity(intent);
        });

        homeButton.setOnClickListener(v -> {
//
//            swipeRefreshLayout.setOnKeyListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    refreshContent();
//                }
//            });
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,Create_post.class);
            startActivity(intent);
        });

        nearbyButton.setOnClickListener(v -> {

        });

        meButton.setOnClickListener(v -> {

        });


        view_.setLayoutManager(new LinearLayoutManager(this));
        view_.setAdapter(new dataModel_adapter(getApplicationContext(),data));
    }

//    private void refreshContent() {
//        Intent intent =
//    }


}



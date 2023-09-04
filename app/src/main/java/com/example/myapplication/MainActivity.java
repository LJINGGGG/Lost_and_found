package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.example.myapplication.Event.Create_event;
import com.example.myapplication.Event.MainActivity_event;
import com.example.myapplication.nearBy_features.nearBy;
import com.example.myapplication.user_features.SignIn_;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private View swipeRefreshLayout;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private List<Lost_Found_Post>  Lost_postList = new ArrayList<>();;

    private List<Lost_Found_Post> Found_postList = new ArrayList<>();;

    private TextView lost_view , found_view;

    private String name_get,userId,ImageUrl , filter_country = "Select State" ,state, filter_category = "Select Category";

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

        found_view = findViewById(R.id.textView2);
        lost_view = findViewById(R.id.textView3);

        Intent intent_get = getIntent();
        if (intent_get.hasExtra("info_country")) {
            String info = intent_get.getStringExtra("info_country");
            String[] info1Array = info.split(",");
            filter_category = info1Array[0];
            filter_country = info1Array[1];
        } else if  (intent_get.hasExtra("name")){
            name_get = intent_get.getStringExtra("name");
        }

        getdata(view_);
        load_userInfo();


        // Insert the info
        SharedPreferences prefQ1 = getSharedPreferences("MySharedPreferences" ,MODE_PRIVATE );
        SharedPreferences.Editor prefEditor = prefQ1.edit();
        prefEditor.putString("Name" ,name_get);
        prefEditor.putString("Key", userId);
        prefEditor.putString("Image", ImageUrl);
        prefEditor.putString("state", state);
        prefEditor.commit();


        found_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                found_view.setBackgroundColor(Color.parseColor("#ffffff"));
                found_view.setTextColor(Color.parseColor("#000000"));

                lost_view.setBackgroundColor(Color.parseColor("#032068"));
                lost_view.setTextColor(Color.parseColor("#ffffff"));

                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                view_.setLayoutManager(layoutManager);
                view_.setAdapter(new dataModel_adapter(MainActivity.this,Found_postList));

            }
        });

        lost_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lost_view.setBackgroundColor(Color.parseColor("#ffffff"));
                lost_view.setTextColor(Color.parseColor("#000000"));

                found_view.setBackgroundColor(Color.parseColor("#032068"));
                found_view.setTextColor(Color.parseColor("#ffffff"));

                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                view_.setLayoutManager(layoutManager);
                view_.setAdapter(new dataModel_adapter(MainActivity.this,Lost_postList));
            }
        });


        filterButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, filter.class);
            startActivity(intent);
        });

        eventButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity_event.class);
            startActivity(intent);
        });

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            dialogBuilder.setTitle("👾 Add Post / Event 👾");
            dialogBuilder.setPositiveButton("Lost / Found post", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, Create_post.class);
                    startActivity(intent);
                }
            });
            dialogBuilder.setNegativeButton("Event", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, Create_event.class);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });

        nearbyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, nearBy.class);
            startActivity(intent);
        });

        meButton.setOnClickListener(v -> {
        });
    }


    public void getdata(RecyclerView view) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post_found_lost");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String selectedCategory = postSnapshot.child("category").getValue(String.class);
                    String imageUrl = postSnapshot.child("imageurl").getValue(String.class);
                    String information = postSnapshot.child("information").getValue(String.class);
                    String selectedCountry = postSnapshot.child("location").getValue(String.class);
                    String name = postSnapshot.child("name").getValue(String.class);
                    String post_category = postSnapshot.child("post_catrgory").getValue(String.class);
                    Lost_Found_Post post = new Lost_Found_Post(selectedCategory, imageUrl, information, selectedCountry, name, post_category);

                    if ("Lost".equals(post.getPost_catrgory())) {
                        if (filter_country.equals("Select State") && filter_category.equals("Select Category")) {
                            Lost_postList.add(post);
                        }else {
                            if (!(filter_country.isEmpty()) && (filter_category.equals("Select Category"))) {
                                if (post.getLocation().equals(filter_country)) {
                                    Lost_postList.add(post);
                                }
                            } else if ((filter_country.equals("Select State")) && !(filter_category.isEmpty())) {
                                if (post.getCategory().equals(filter_category)) {
                                    Lost_postList.add(post);
                                }
                            }else{
                                if (post.getLocation().equals(filter_country) && post.getCategory().equals(filter_category)) {
                                    Lost_postList.add(post);
                                }
                            }
                        }
                    } else if ("Found".equals(post.getPost_catrgory())) {

                        if (filter_country.equals("Select State") && filter_category.equals("Select Category")) {
                            Found_postList.add(post);
                        }else {
                            if (!(filter_country.isEmpty()) && (filter_category.equals("Select Category"))) {
                                if (post.getLocation().equals(filter_country)) {
                                    Found_postList.add(post);
                                }
                            } else if ((filter_country.equals("Select State")) && !(filter_category.isEmpty())) {
                                if (post.getCategory().equals(filter_category)) {
                                    Found_postList.add(post);
                                }
                            }else{
                                if (post.getLocation().equals(filter_country) && post.getCategory().equals(filter_category)) {
                                    Found_postList.add(post);
                                }
                            }
                        }
                    }
                }
                lost_view.setBackgroundColor(Color.parseColor("#ffffff"));
                lost_view.setTextColor(Color.parseColor("#000000"));

                found_view.setBackgroundColor(Color.parseColor("#032068"));
                found_view.setTextColor(Color.parseColor("#ffffff"));

                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                view.setLayoutManager(layoutManager);
                view.setAdapter(new dataModel_adapter(MainActivity.this,Lost_postList));



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval
            }
        });
    }

    public void load_userInfo() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot friendSnapshot : snapshot.getChildren()) {
                    String name = friendSnapshot.child("name").getValue(String.class);

                    if (name != null && name.equals(name_get)) {
                        userId = friendSnapshot.getKey();
                        ImageUrl = friendSnapshot.child("imageUrI").getValue(String.class);
                        state = friendSnapshot.child("state").getValue(String.class);
                        state = friendSnapshot.child("state").getValue(String.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}



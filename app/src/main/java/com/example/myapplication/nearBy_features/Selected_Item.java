package com.example.myapplication.nearBy_features;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;


import com.example.myapplication.Create_post;
import com.example.myapplication.Event.YourDataModel;
import com.example.myapplication.Lost_Found_Post;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Selected_Item extends AppCompatActivity {

    private String nearby_name = "YM";
    private List<Lost_Found_Post> lost_post_list = new ArrayList<>();
    private List<Lost_Found_Post> found_post_list = new ArrayList<>();
    private List<YourDataModel> event_List = new ArrayList<>();
    private ImageButton back_button;


    private Selected_Item_Adapter selectedItemAdapter;
    private Selected_Item_Adapter selectedItemAdapter2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        Intent intent_get = getIntent();
        if  (intent_get.hasExtra("nearBy_name")){
            nearby_name = intent_get.getStringExtra("nearBy_name");
        }

        RecyclerView recyclerView = findViewById(R.id.lost_view);
        RecyclerView recyclerView1 = findViewById(R.id.found_view);


        load_Post_List_(recyclerView , recyclerView1);


        back_button = findViewById(R.id.back_btn);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Selected_Item.this, nearBy.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void load_Post_List_(RecyclerView recyclerView , RecyclerView recyclerView1) {

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

                    if ("Lost".equals(post.getPost_catrgory()) && nearby_name.equals(name)) {
                        lost_post_list.add(post);
                    } else if ("Found".equals(post.getPost_catrgory()) && nearby_name.equals(name)) {
                        found_post_list.add(post);
                    }
                }

                // Lost List
                selectedItemAdapter = new Selected_Item_Adapter(Selected_Item.this ,lost_post_list); // Create your adapter with the list
                recyclerView.setAdapter(selectedItemAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(Selected_Item.this, LinearLayoutManager.HORIZONTAL, false); // Use horizontal orientation
                recyclerView.setLayoutManager(layoutManager);

                //2
                selectedItemAdapter2 = new Selected_Item_Adapter(Selected_Item.this ,found_post_list); // Create your adapter with the list
                recyclerView1.setAdapter(selectedItemAdapter2);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(Selected_Item.this, LinearLayoutManager.HORIZONTAL, false); // Use horizontal orientation
                recyclerView1.setLayoutManager(layoutManager2);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval
            }
        });
    }



}
package com.example.myapplication.Event;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Create_post;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.nearBy_features.nearBy;
import com.example.myapplication.user_features.Profile_Picture;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_event extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<YourDataModel> data = new ArrayList<>();

    private String userId , name , profile_imageUrl , state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);

        Intent intent_get = getIntent();
        if  (intent_get.hasExtra("user_info")){
            String user_info = intent_get.getStringExtra("user_info");
            String[] user_info_Array = user_info.split(",");
            userId = user_info_Array[0];
            name = user_info_Array[1];
            profile_imageUrl = user_info_Array[2];
            state = user_info_Array[3];
        }
        String user_info = userId + "," + name + "," + profile_imageUrl +"," + state;


        ImageButton eventButton = findViewById(R.id.EventButton);
        ImageButton homeButton = findViewById(R.id.HomeButton);
        FloatingActionButton addButton = findViewById(R.id.addButton);
        ImageButton nearbyButton = findViewById(R.id.NearByButton);
        ImageButton meButton = findViewById(R.id.MeButton);

        recyclerView = findViewById(R.id.myrecycleview);
        adapter = new MyAdapter(data, new MyAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                YourDataModel clickedItem = data.get(position);
                String imageUrl = clickedItem.getImageResource();
                String eventName = clickedItem.getDescription();
                String post_link1 = clickedItem.getLink();

                Intent intent = new Intent(MainActivity_event.this, Show_event.class);
                intent.putExtra("image_url", imageUrl);
                intent.putExtra("event_name", eventName);
                intent.putExtra("postlink", post_link1);
                intent.putExtra("show_userId",userId);
                intent.putExtra("show_name",name);
                intent.putExtra("show_ImageUrl",userId);
                intent.putExtra("show_state",state);

                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Event");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear(); // Clear existing data
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String imageUrl = eventSnapshot.child("image_video").getValue(String.class);
                    String eventName = eventSnapshot.child("event_name").getValue(String.class);
                    String post_link = eventSnapshot.child("post_link").getValue(String.class);
                    data.add(new YourDataModel(imageUrl, eventName, post_link));
                }
                adapter.notifyDataSetChanged(); // Update RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database errors
            }
        });


        eventButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_event.this, MainActivity_event.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_event.this,MainActivity.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity_event.this);
            dialogBuilder.setTitle("👾 Add Post / Event 👾");
            dialogBuilder.setPositiveButton("Lost / Found post", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity_event.this, Create_post.class);
                    intent.putExtra("user_info", user_info);
                    startActivity(intent);
                }
            });
            dialogBuilder.setNegativeButton("Event", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity_event.this, Create_event.class);
                    intent.putExtra("user_info", user_info);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });

        nearbyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_event.this, nearBy.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });

        meButton.setOnClickListener(v -> {
        });



    }
}

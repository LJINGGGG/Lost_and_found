package com.example.myapplication.Event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);


        Button create_event = findViewById(R.id.create_event);
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

                Log.d("Postlink","get post link:"+ post_link1);
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

                    Log.d("image uri:","afunaifnaifiafn" + imageUrl);
                    data.add(new YourDataModel(imageUrl, eventName, post_link));
                }
                adapter.notifyDataSetChanged(); // Update RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database errors
            }
        });

        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_event.this, Create_event.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.myapplication.Event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
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

public class Member_list extends AppCompatActivity {

    private String img_url;
    private String postId;
    private String Uri_desiredPart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);

        RecyclerView recyclerView = findViewById(R.id.Myrecycleview2);
        List<YourDataModel2> data = new ArrayList<>();

        Intent intent = getIntent();
        postId = intent.getStringExtra("postlink");
        img_url = intent.getStringExtra("image_url");


        Log.d("lookup", "ongetlink" + postId);

        Uri uri_temporary = Uri.parse(postId);
        Uri_desiredPart = uri_temporary.getLastPathSegment();

        Button return_page = findViewById(R.id.return_page);

        MyAdapter2 adapter = new MyAdapter2(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference postRef2 = databaseReference.child("Event").child(Uri_desiredPart);

// Create a new "participants" node under the specific post
        DatabaseReference participantsRef = postRef2.child("participants");


        participantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear(); // Clear existing data
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String participant = eventSnapshot.getValue(String.class);

                    data.add(new YourDataModel2(participant));
                }
                adapter.notifyDataSetChanged(); // Update RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database errors
            }
        });

        return_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Member_list.this, Show_event.class);
                intent.putExtra("image_url", img_url);
                intent.putExtra("postlink", postId);
                startActivity(intent);
            }
        });
    }

}

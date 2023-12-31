package com.example.myapplication.nearBy_features;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.myapplication.Create_post;
import com.example.myapplication.Event.Create_event;
import com.example.myapplication.Event.MainActivity_event;
import com.example.myapplication.Event.MyAdapter;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.profile_features.DisplayProfile;
import com.example.myapplication.user_features.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class nearBy extends AppCompatActivity {

    private List<User> userList = new ArrayList<>();
    private nearBy_Adapter nearByAdapter;

    private String name , profile_imageUrl , state , user_info , userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);

        Intent intent_get = getIntent();
        if  (intent_get.hasExtra("user_info")){
            String user_info = intent_get.getStringExtra("user_info");
            String[] user_info_Array = user_info.split(",");
            userId = user_info_Array[0];
            name = user_info_Array[1];
            profile_imageUrl = user_info_Array[2];
            state = user_info_Array[3];
        }
        user_info = userId + "," + name + "," + profile_imageUrl +"," + state;

        RecyclerView recyclerView = findViewById(R.id.nearBy_RecycleView);
        ImageButton eventButton = findViewById(R.id.EventButton);
        ImageButton homeButton = findViewById(R.id.HomeButton);
        FloatingActionButton addButton = findViewById(R.id.addButton);
        ImageButton nearbyButton = findViewById(R.id.NearByButton);
        ImageButton meButton = findViewById(R.id.MeButton);

        load_User_List(recyclerView);

        eventButton.setOnClickListener(v -> {
            Intent intent = new Intent(nearBy.this, MainActivity_event.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(nearBy.this,MainActivity.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(nearBy.this);
            dialogBuilder.setTitle("👾 Add Post / Event 👾");
            dialogBuilder.setPositiveButton("Lost / Found post", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(nearBy.this, Create_post.class);
                    intent.putExtra("user_info", user_info);
                    startActivity(intent);
                }
            });
            dialogBuilder.setNegativeButton("Event", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(nearBy.this, Create_event.class);
                    intent.putExtra("user_info", user_info);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });

        nearbyButton.setOnClickListener(v -> {
            Intent intent = new Intent(nearBy.this, nearBy.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });
        meButton.setOnClickListener(v -> {
            Intent intent = new Intent(nearBy.this, DisplayProfile.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });

    }

    public void load_User_List(RecyclerView recyclerView) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot friendSnapshot : snapshot.getChildren()) {
                    User user_ = friendSnapshot.getValue(User.class);
                    if (("Other".equals(user_.getUserType()) && (state.equals(user_.getState())))) {
                        userList.add(user_);
                    }
                }

                // Direct to profile page
                nearByAdapter = new nearBy_Adapter(nearBy.this ,userList ,new MyAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (userList != null && position >= 0 && position < userList.size()) {
                            User user_obj = userList.get(position);
                            name = user_obj.getName();

                            Intent intent = new Intent(nearBy.this, Selected_Item.class);
                            intent.putExtra("nearBy_name", name);
                            startActivity(intent);
                        }
                    }
                });


                recyclerView.setAdapter(nearByAdapter);
                // Set up RecyclerView and adapter here
                LinearLayoutManager layoutManager = new LinearLayoutManager(nearBy.this);
                recyclerView.setLayoutManager(layoutManager);


                SearchView usersearchView = findViewById(R.id.nearBy_SearchView);
                usersearchView.clearFocus();

                usersearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        filterList(s);
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error here
                // For example, you can show a toast or log the error
            }
        });
    }

    private void filterList(String text) {
        List<User> filteredList = new ArrayList<>();
        for (User item : userList){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
        }else{
            nearByAdapter.setFilteredList(this,filteredList);
        }
    }



}
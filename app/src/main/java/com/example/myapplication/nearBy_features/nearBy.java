package com.example.myapplication.nearBy_features;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import com.example.myapplication.R;
import com.example.myapplication.user_features.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class nearBy extends AppCompatActivity {

    private List<User> userList = new ArrayList<>();
    private nearBy_Adapter nearByAdapter = new nearBy_Adapter(this  ,userList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);

        RecyclerView recyclerView = findViewById(R.id.nearBy_RecycleView);

        load_User_List(recyclerView);

    }

    public void load_User_List(RecyclerView recyclerView) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot friendSnapshot : snapshot.getChildren()) {
                    User user_ = friendSnapshot.getValue(User.class);

                    if ("Other".equals(user_.getUserType())) {
                        userList.add(user_);
                    }
                }

                // Set up RecyclerView and adapter here
                LinearLayoutManager layoutManager = new LinearLayoutManager(nearBy.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(nearByAdapter);

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

                // Show alert dialog with user count
                showUserCountDialog(userList.size());
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


    private void showUserCountDialog(int count) {
        AlertDialog.Builder builder = new AlertDialog.Builder(nearBy.this);
        builder.setTitle("User Count");
        builder.setMessage("Total Users: " + count);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Optionally, add code here to handle the "Ok" button click
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
package com.example.myapplication.profile_features;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.myapplication.Create_post;
import com.example.myapplication.Event.Create_event;
import com.example.myapplication.Event.MainActivity_event;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.nearBy_features.nearBy;
import com.example.myapplication.user_features.LogIn_;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DisplayProfile extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView countryTextView;
    private TextView dateTextView;
    private TextView phoneNumberTextView;
    private TextView stateTextView;

    private TextView usertypeTextView;

    Button editProfileButton;
    Button filter_button , log_out_btn;

    private String name , profile_imageUrl , state  , userId , user_info;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

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


        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        countryTextView = findViewById(R.id.countryTextView);
        dateTextView = findViewById(R.id.dateTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        stateTextView = findViewById(R.id.stateTextView);
        usertypeTextView = findViewById(R.id.userTypeTextView);
        editProfileButton = findViewById(R.id.editProfileButton);
        log_out_btn = findViewById(R.id.log_out_button);

        ImageButton eventButton = findViewById(R.id.EventButton);
        ImageButton homeButton = findViewById(R.id.HomeButton);
        FloatingActionButton addButton = findViewById(R.id.addButton);
        ImageButton nearbyButton = findViewById(R.id.NearByButton);
        ImageButton meButton = findViewById(R.id.MeButton);


        load_userInfo(name);

        eventButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayProfile.this, MainActivity_event.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayProfile.this,MainActivity.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(DisplayProfile.this);
            dialogBuilder.setTitle("👾 Add Post / Event 👾");
            dialogBuilder.setPositiveButton("Lost / Found post", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DisplayProfile.this, Create_post.class);
                    intent.putExtra("user_info", user_info);
                    startActivity(intent);
                }
            });
            dialogBuilder.setNegativeButton("Event", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DisplayProfile.this, Create_event.class);
                    intent.putExtra("user_info", user_info);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });

        nearbyButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayProfile.this, nearBy.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });
        meButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayProfile.this, DisplayProfile.class);
            intent.putExtra("user_info", user_info);
            startActivity(intent);
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayProfile.this, EditProfile.class);
                intent.putExtra("user_info", user_info);
                startActivity(intent);
            }
        });

        filter_button = findViewById(R.id.filter_button);

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayProfile.this, FilterP.class);
                startActivity(intent);
            }
        });

        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayProfile.this, LogIn_.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void load_userInfo(String name_get) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean userFound = false;
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    String country = userSnapshot.child("country").getValue(String.class);
                    String date = userSnapshot.child("date").getValue(String.class);
                    String phoneNumber = userSnapshot.child("phone_number").getValue(String.class);
                    String state = userSnapshot.child("state").getValue(String.class);
                    String userType = userSnapshot.child("userType").getValue(String.class);
                    String profileImageUrl = userSnapshot.child("imageUrI").getValue(String.class);

                    Log.d("FirebaseData", "Name: " + name);

                    ImageView profileImageView = findViewById(R.id.profileImageView);

                    if (name != null && name.equals(name_get)) {
                        userFound = true;
                        usernameTextView.setText(name);
                        emailTextView.setText(email);
                        countryTextView.setText(country);
                        dateTextView.setText(date);
                        phoneNumberTextView.setText(phoneNumber);
                        stateTextView.setText(state);
                        usertypeTextView.setText(userType);

                        if (profileImageUrl != null) {
                            Glide.with(DisplayProfile.this)
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.img_3)
                                    .error(R.drawable.img_2)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(profileImageView);
                        }
                        break;
                    }
                }

                if (!userFound) {
                    Toast.makeText(DisplayProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DisplayProfile.this, "Database error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}



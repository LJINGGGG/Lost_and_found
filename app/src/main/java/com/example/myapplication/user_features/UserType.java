package com.example.myapplication.user_features;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.user_features.SignIn_;

public class UserType extends AppCompatActivity {

    private String usertype;
    private Button normal_user_button , other_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        normal_user_button = findViewById(R.id.normal_user_btn);
        other_button = findViewById(R.id.other_btn);

        normal_user_button.setOnClickListener(v -> {
            usertype = "Normal";
            Intent intent = new Intent(this, SignIn_.class);
            intent.putExtra("userType", usertype); // Replace userType with your actual variable
            startActivity(intent);
        });

        other_button.setOnClickListener(v -> {
            usertype = "Other";
            Intent intent = new Intent(this, SignIn_.class);
            intent.putExtra("userType", usertype); // Replace userType with your actual variable
            startActivity(intent);
        });



    }
}
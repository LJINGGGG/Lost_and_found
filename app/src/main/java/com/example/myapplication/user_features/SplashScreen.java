package com.example.myapplication.user_features;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.nearBy_features.nearBy;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(5000);
                    startActivity(new Intent(SplashScreen.this , LogIn_.class ));
                    finish();
                }catch(Exception e){

                }
            }
        };thread.start();
    }
}
package com.example.myapplication.profile_features;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FilterP extends AppCompatActivity {
    private EditText searchUserEditText;
    private Button searchButton;
    private TextView nameTextView;
    private TextView categoryTextView;
    private TextView informationTextView;
    private TextView locationTextView;
    private TextView postCategoryTextView;
    private ImageView postImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_p);

        searchUserEditText = findViewById(R.id.searchUserEditText);
        searchButton = findViewById(R.id.searchButton);
        nameTextView = findViewById(R.id.nameTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        informationTextView = findViewById(R.id.informationTextView);
        locationTextView = findViewById(R.id.locationTextView);
        postCategoryTextView = findViewById(R.id.postCategoryTextView);
        postImageView = findViewById(R.id.postImageView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser();
            }
        });
    }

    private void searchUser() {
        String userName = searchUserEditText.getText().toString().trim();

        DatabaseReference postReference = FirebaseDatabase.getInstance().getReference().child("post_found_lost");
        Query query = postReference.orderByChild("name").equalTo(userName);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("DataCount", "Number of matching records: " + dataSnapshot.getChildrenCount());

                clearUI();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String category = postSnapshot.child("category").getValue(String.class);
                    String information = postSnapshot.child("information").getValue(String.class);
                    String location = postSnapshot.child("location").getValue(String.class);
                    String postCategory = postSnapshot.child("post_catrgory").getValue(String.class);
                    String imageUrl = postSnapshot.child("imageurl").getValue(String.class);

                    nameTextView.setText("Name: " + userName);
                    categoryTextView.setText("Category: " + category);
                    informationTextView.setText("Information: " + information);
                    locationTextView.setText("Location: " + location);
                    postCategoryTextView.setText("Post Category: " + postCategory);

                    if (imageUrl != null) {
                        // Load and display the image using Glide
                        loadImage(imageUrl);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FilterP.this, "Database error occurred: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                clearUI();
            }
        });
    }

    private void loadImage(String imageUrl) {
        Glide.with(FilterP.this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Image load failed: " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        // Image successfully loaded
                        return false;
                    }
                })
                .into(postImageView);
    }

    private void clearUI() {
        nameTextView.setText("Name: ");
        categoryTextView.setText("Category: ");
        informationTextView.setText("Information: ");
        locationTextView.setText("Location: ");
        postCategoryTextView.setText("Post Category: ");
        postImageView.setImageResource(R.drawable.img_1);
    }
}
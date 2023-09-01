package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class share extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view);

        ImageButton shareButton = findViewById(R.id.OptionButton);

        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                // Replace these with the actual values you want to share
                String postTitle = "Post Title";
                String postText = "Check out this post!";
                String postImageUrl = "https://example.com/image.jpg"; // URL of the image you want to share (optional)

                sharePost(postTitle, postText, postImageUrl);
            }

        });
}
    private void sharePost(String title, String text, String imageUrl) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        if (imageUrl != null) {
            // If you have an image to share, you can add it here
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUrl));
            shareIntent.setType("image/*");
        }

        // Create a chooser to allow the user to select an app for sharing
        Intent chooserIntent = Intent.createChooser(shareIntent, "Share via");

        // Start the chooser activity
        startActivity(chooserIntent);


    }

}
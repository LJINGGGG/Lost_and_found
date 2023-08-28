package com.example.event_assignment_lyp;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Map;

public class Create_event extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button selectImageBtn, CreateEventBtn;
    private ImageView imageView;
    private String imageId;
    private Uri imageUri;

    private String event_name;
    private String date;
    private String information;
    private String category;
    private String location;
    private String State;
    private String image_video;
    private String auth_user = "liewYikPUI";

    private DatabaseReference databaseReference;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        EditText event_name1 = findViewById(R.id.event_name);
        EditText date1 = findViewById(R.id.Date);
        EditText information1 = findViewById(R.id.info);
        EditText category1 = findViewById(R.id.category);
        EditText location1 = findViewById(R.id.loc);
        EditText State1 = findViewById(R.id.state);

        selectImageBtn = findViewById(R.id.button9);
        CreateEventBtn = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView2);

        databaseReference = FirebaseDatabase.getInstance().getReference("images");

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            imageUri = data.getData();

                            // Method - to display the Image
                            displayImageFromUri(imageUri);



                        }
                    }
                }
        );

        selectImageBtn.setOnClickListener(v -> openGallery());

        CreateEventBtn.setOnClickListener(v ->{
            // Method - Upload the image URI to Firebase Realtime Database
            uploadImageToDatabase();
            StoreEventData();

        });
    }

    private void displayImageFromUri(Uri imageUri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);
            Glide.with(this).load(imageUri).into(imageView);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        imagePickerLauncher.launch(Intent.createChooser(galleryIntent, "Select Picture"));
    }

    private void uploadImageToDatabase() {
        if (imageUri != null) {
            imageId = databaseReference.push().getKey();
            if (imageId != null) {
                databaseReference.child(imageId).setValue(imageUri.toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Image URI uploaded to Firebase Realtime Database successfully
                            } else {
                                // Error uploading image URI
                            }
                        });
            }
        }
    }

    private void StoreEventData(){


        EditText event_name1 = findViewById(R.id.event_name);
        EditText date1 = findViewById(R.id.Date);
        EditText information1 = findViewById(R.id.info);
        EditText category1 = findViewById(R.id.category);
        EditText location1 = findViewById(R.id.loc);
        EditText State1 = findViewById(R.id.state);

        event_name = event_name1.getText().toString();
        date = date1.getText().toString();
        information = information1.getText().toString();
        category = category1.getText().toString();
        location = location1.getText().toString();
        State = State1.getText().toString();
        if (imageUri == null) {
            image_video = " ";
        } else {
            image_video = imageUri.toString();
        }


        if (event_name.isEmpty() || date.isEmpty() || information.isEmpty() ||
                category.isEmpty() || location.isEmpty() || State.isEmpty()) {
            Toast.makeText(this, "Missing columns: Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;

        } else {
            Map<String, Object> eventMap = new HashMap<>();
            eventMap.put("event_name", event_name);
            eventMap.put("date", date);
            eventMap.put("information", information);
            eventMap.put("category", category);
            eventMap.put("location", location);
            eventMap.put("State", State);
            eventMap.put("image_video", imageId);
            eventMap.put("number of participants", 0);
            eventMap.put("auth_user", auth_user);

            Log.d("ImageUri", "Image URI: " + imageUri);
            Log.d("ImageUri", "Image ID: " + imageId);
            // Get a reference to the Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("Post");

            // Push the data to the database
            DatabaseReference newUserRef = usersRef.push();
            newUserRef.setValue(eventMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(this, "Successful update and create", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle error
                        }
                    });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}

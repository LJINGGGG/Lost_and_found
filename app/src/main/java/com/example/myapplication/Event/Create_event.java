package com.example.myapplication.Event;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Create_event extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button selectImageBtn, CreateEventBtn;

    private String imageUrl;
    private ImageView imageView;
    private String imageId;
    private Uri imageUri;

    private Intent intent;

    private UploadTask uploadTask;
    private String event_name;
    private String date_start;
    private String date_end;
    private String information;
    private String category;
    private String location;
    private String State;
    private String image_video;
    private String auth_user = " ";

    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private Calendar calendarStartDate;
    private Calendar calendarEndDate;

    private DatabaseReference databaseReference;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        SharedPreferences prefget = getSharedPreferences("MySharedPreferences",0);
        auth_user = prefget.getString("Name","NA");

        Intent intent_get = getIntent();
        if  (intent_get.hasExtra("user_info")){
            String user_info = intent_get.getStringExtra("user_info");
            String[] user_info_Array = user_info.split(",");
            auth_user = user_info_Array[1];
        }

        EditText event_name1 = findViewById(R.id.event_name);
        EditText information1 = findViewById(R.id.info);
        Spinner category1 = findViewById(R.id.category);
        EditText location1 = findViewById(R.id.loc);
        AutoCompleteTextView State1 = findViewById(R.id.state);
        ImageButton return_main = findViewById(R.id.return_page);

        textViewStartDate = findViewById(R.id.Date);
        textViewEndDate = findViewById(R.id.Date2);

        calendarStartDate = Calendar.getInstance();
        calendarEndDate = Calendar.getInstance();
        selectImageBtn = findViewById(R.id.button9);
        CreateEventBtn = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView2);

        String[] values = {"Electronics", "Clothing and Accessories", "Personal Items", "Bags and Luggage", "Others"};

        // Initialize the Spinner

        // Create an ArrayAdapter to populate the Spinner
        ArrayAdapter<String> adapter12 = new ArrayAdapter<>(this, R.layout.custom_spinner_dropdown_event, values);

        // Set the custom dropdown view resource (layout for the dropdown items)
        adapter12.setDropDownViewResource(R.layout.custom_spinner_dropdown_event);

        // Set the adapter for the Spinner
        category1.setAdapter(adapter12);

        // Adjust the Spinner's height to match the dropdown items' height
        category1.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.dropdown_item_height);



        String[] states = {
                "Johor",
                "Kedah",
                "Kelantan",
                "Melaka (Malacca)",
                "Negeri Sembilan",
                "Pahang",
                "Penang (Pulau Pinang)",
                "Perak",
                "Perlis",
                "Sabah",
                "Sarawak",
                "Selangor",
                "Terengganu"
        };


        // Create an ArrayAdapter to provide the suggestions
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, states);

        // Set the adapter for the AutoCompleteTextView
        State1.setAdapter(adapter);


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
            StoreImagetoStore();

        });
        textViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickerDialog(calendarEndDate, textViewEndDate);
            }
        });
        textViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(calendarStartDate, textViewStartDate);
            }
        });
        return_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Create_event.this,MainActivity_event.class));
            }
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
                databaseReference.child(imageId).setValue(imageUrl)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                StoreEventData();
                            } else {
                            }
                        });
            }
        }
    }


    private void StoreImagetoStore(){
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("Images");

        imageId = UUID.randomUUID().toString();

        // Upload the image using putFile()

        if (imageUri != null){
            uploadTask = imagesRef.child(imageId).putFile(imageUri);

            // You can also listen for upload success or failure
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully
                // Now you can retrieve the download URL
                imagesRef.child(imageId).getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    imageUrl = downloadUri.toString();
                    uploadImageToDatabase();
                });
            }).addOnFailureListener(exception -> {
            });
        }
        else{
            imageId = null;
            StoreEventData();
        }


    }

    private void StoreEventData(){


        EditText event_name1 = findViewById(R.id.event_name);
        EditText information1 = findViewById(R.id.info);
        Spinner category1 = findViewById(R.id.category);
        EditText location1 = findViewById(R.id.loc);
        AutoCompleteTextView State1 = findViewById(R.id.state);

        event_name = event_name1.getText().toString();
        information = information1.getText().toString();
        category = category1.getSelectedItem().toString();
        location = location1.getText().toString();
        State = State1.getText().toString();
        date_start = getStartDateString();
        date_end = getEndDateString();



        if (event_name.isEmpty() || date_start.isEmpty() ||date_end.isEmpty() || information.isEmpty() ||
                category.isEmpty() || location.isEmpty() || State.isEmpty()) {
            general_dialog("🚫Warning💖" ,"Missing columns: Please fill in all fields.");
            return;

        } else {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("Event");

            // Push the data to the database
            DatabaseReference newUserRef = usersRef.push();


            Map<String, Object> eventMap = new HashMap<>();
            eventMap.put("event_name", event_name);
            eventMap.put("date_start", date_start);
            eventMap.put("date_end", date_end);
            eventMap.put("information", information);
            eventMap.put("category", category);
            eventMap.put("location", location);
            eventMap.put("State", State);
            eventMap.put("image_video", imageId);
            eventMap.put("number of participants", 0);
            eventMap.put("auth_user", auth_user);
            eventMap.put("post_link",newUserRef.toString());

            // Get a reference to the Firebase Realtime Database

            newUserRef.setValue(eventMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            general_dialog("💖Information💖" ,"Successful update and create");
                            intent = new Intent(Create_event.this,MainActivity_event.class);
                            startActivity(intent);
                            finish();
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


    private void showDatePickerDialog(final Calendar calendar, final TextView textView) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String dateString = dateFormat.format(calendar.getTime());
                textView.setText(dateString);
            }
        };

        new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private String getStartDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateFormat.format(calendarStartDate.getTime());
    }

    // Method to get the selected end date as a string
    private String getEndDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateFormat.format(calendarEndDate.getTime());
    }

    public void general_dialog(String title , String content){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(content);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

}

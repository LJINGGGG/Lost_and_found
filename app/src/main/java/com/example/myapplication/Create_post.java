package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Create_post extends AppCompatActivity {
    private EditText information_edit;
    private EditText Category_edit;
    private EditText Location_edit;
    private Button lostButton;
    private Button foundButton;
    private Button insertImageButton;
    private Button openCameraButton;
    private Button postButton;

    private ProgressBar progressBar;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private ActivityResultLauncher<Intent> cameraLauncher;

    private String post_category, imageUrl, imageId;
    private Uri imageUri;

    //////////// Combine (YM)
    private String name = "YM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        information_edit = findViewById(R.id.info);
        Category_edit = findViewById(R.id.category);
        Location_edit = findViewById(R.id.location);
        lostButton = findViewById(R.id.lostButton);
        foundButton = findViewById(R.id.foundButton);
        insertImageButton = findViewById(R.id.insert_imageButton);
        openCameraButton = findViewById(R.id.open_cameraButton);
        postButton = findViewById(R.id.postButton);
        progressBar = findViewById(R.id.progressBar);

        String information = information_edit.getText().toString();
        String location = Location_edit.getText().toString();

        // Better using selection (for them to select)
        String category = Category_edit.getText().toString();

//
//
        // 0. Button click
        lostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_category = "Lost";
            }
        });

        foundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_category = "Found";
            }
        });


        // 1. Using Gallery insert images
        insertImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the progress bar while uploading
                openImagePicker();
                progressBar.setVisibility(View.VISIBLE);

                // Simulate uploading by using a thread with delay
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Simulate uploading time
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Hide the progress bar when upload is complete
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).start();
            }
        });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            imageUri = data.getData();
                            uploadImageToFirebase();
                        }
                    }
                }
        );

//
        // 2. Open camera
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        imageUri = saveImageToGallery(photo);
                        uploadImageToFirebase();
                    }
                }
        );

        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(cameraIntent);
                progressBar.setVisibility(View.VISIBLE);

                // Simulate uploading by using a thread with delay
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Simulate uploading time
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Hide the progress bar when upload is complete
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).start();
            }
        });


        // 3. Post Button
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /// Beter have validation for input
                if (post_category == null) {
                    general_dialog("Error" , "Please select whether this is lost post or found post.");
                }else{
                    postData post_data = new postData(information ,category ,location, post_category, imageUrl , name );
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("post_found_lost");
                    String uniqueKey = myRef.push().getKey();
                    myRef.child(uniqueKey).setValue(post_data);


                    Toast.makeText(Create_post.this , "Success" , Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void openImagePicker() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(Intent.createChooser(galleryIntent, "Select Picture"));
    }

    private void uploadImageToFirebase() {
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("images");
        imageId = UUID.randomUUID().toString();

        UploadTask uploadTask = imagesRef.child(imageId).putFile(imageUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imagesRef.child(imageId).getDownloadUrl().addOnSuccessListener(downloadUri -> {
                imageUrl = downloadUri.toString();
                Log.e("Successful", "Image upload success: " + imageUrl);
                // Display a toast or any other action to notify the user
                Toast.makeText(Create_post.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(exception -> {
            Log.e("Firebase", "Image upload failed: " + exception.getMessage());
        });
    }

    private Uri saveImageToGallery(Bitmap bitmap) {
        try {
            File imagesFolder = new File(getFilesDir(), "images");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdir();
            }
            File imageFile = new File(imagesFolder, "captured_image.jpg");
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void general_dialog(String title , String content){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(content);

        // Set up the dialog buttons
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If ok
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the Cancel button click (optional)
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
    }

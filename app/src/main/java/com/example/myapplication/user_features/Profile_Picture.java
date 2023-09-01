package com.example.myapplication.user_features;

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
import android.widget.ImageView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Profile_Picture extends AppCompatActivity {
    private Uri imageUri;
    private ImageView imageView;
    private Button no_need_button ;
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference databaseReference;

    private String imageId , imageUrl , info_;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private  String[] info1Array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        imageView = findViewById(R.id.profile_view);
        no_need_button = findViewById(R.id.no_need_btn);

        Intent intent = getIntent();
        if (intent != null) {
            info_ = intent.getStringExtra("info");
        }

        info1Array = info_.split(",");

        databaseReference = FirebaseDatabase.getInstance().getReference("User");


        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            imageUri = data.getData();
                            no_need_button.setText("Continue");
                            StoreImagetoStore();
                        }
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        imageUri = saveImageToGallery(photo);
                        no_need_button.setText("Continue");
                        StoreImagetoStore();
                    }
                }
        );

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Profile_Picture.this);
                dialogBuilder.setTitle("👾 Upload Profile Picture 👾");
                dialogBuilder.setPositiveButton("From Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        imagePickerLauncher.launch(Intent.createChooser(galleryIntent, "Select Picture"));
                    }
                });
                dialogBuilder.setNegativeButton("Open Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraLauncher.launch(cameraIntent);
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        no_need_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String buttonText = no_need_button.getText().toString();
                if(buttonText.equals("No need")){

                    // ***************************************************** Validation for image *****************************************************

                    User userinfo = new User(info1Array[0], info1Array[1], info1Array[2], info1Array[3], info1Array[4],info1Array[5],info1Array[6], " ");

                    String uniqueKey = databaseReference.push().getKey();
                    databaseReference.child(uniqueKey).setValue(userinfo);

                    Log.d("success", "User data and image uploaded successfully");
                    Intent intent1 = new Intent(Profile_Picture.this , MainActivity.class);
                    intent1.putExtra("name", info1Array[0]);
                    startActivity(intent1);
                }else{
                    uploadUserDataAndImage();
                    Intent intent1 = new Intent(Profile_Picture.this , MainActivity.class);
                    intent1.putExtra("name", info1Array[0]);
                    startActivity(intent1);
                }
            }
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


    private void StoreImagetoStore() {

        StorageReference imagesRef = FirebaseStorage.getInstance().getReference("images");

        imageId = UUID.randomUUID().toString();

        UploadTask uploadTask = imagesRef.child(imageId).putFile(imageUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imagesRef.child(imageId).getDownloadUrl().addOnSuccessListener(downloadUri -> {
                imageUrl = downloadUri.toString();
                uploadUserDataAndImage();

                Log.e("Successful", "Image upload success. ");
            });
        }).addOnFailureListener(exception -> {
            Log.e("Firebase", "Image upload failed: " + exception.getMessage());
        });
    }

    private void uploadUserDataAndImage() {
        if (imageUri != null) {
            User userinfo = new User(info1Array[0], info1Array[1], info1Array[2], info1Array[3], info1Array[4],info1Array[5],info1Array[6], imageUrl);
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
            String uniqueKey = myRef.push().getKey();
            myRef.child(uniqueKey).setValue(userinfo);

            Log.d("success", "User data and image uploaded successfully");
        }
    }

}
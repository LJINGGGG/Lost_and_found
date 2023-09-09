package com.example.myapplication.profile_features;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfile extends AppCompatActivity {

    private EditText editNameEditText;
    private EditText editEmailEditText;
    private EditText editCountryEditText;
    private EditText editDateEditText;
    private EditText editPhoneEditText;
    private EditText editStateEditText;
    private Button saveChangesButton;

    private ImageView editProfileImageView;

    private DatabaseReference databaseReference;

    private StorageReference storageReference;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private Uri imageUri;

    private String name , profile_imageUrl , state  , userId , user_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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

        editNameEditText = findViewById(R.id.editNameEditText);
        editEmailEditText = findViewById(R.id.editEmailEditText);
        editCountryEditText = findViewById(R.id.editCountryEditText);
        editDateEditText = findViewById(R.id.editDateEditText);
        editPhoneEditText = findViewById(R.id.editPhoneEditText);
        editStateEditText = findViewById(R.id.editStateEditText);
        editProfileImageView = findViewById(R.id.editProfileImageView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageReference = FirebaseStorage.getInstance().getReference().child("imageUrI");

        saveChangesButton = findViewById(R.id.saveChangesButton);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri selectedImageUri = result.getData().getData();

                            Glide.with(EditProfile.this)
                                    .load(selectedImageUri)
                                    .apply(new RequestOptions().centerCrop())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(editProfileImageView);

                            imageUri = selectedImageUri;
                        }
                    }
                });

        load_userInfo(name);

        editProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = editNameEditText.getText().toString();
                String updatedEmail = editEmailEditText.getText().toString();
                String updatedCountry = editCountryEditText.getText().toString();
                String updatedDate = editDateEditText.getText().toString();
                String updatedPhoneNumber = editPhoneEditText.getText().toString();
                String updatedState = editStateEditText.getText().toString();

                saveUpdatedUserInfo(name, updatedName, updatedEmail, updatedCountry, updatedDate, updatedPhoneNumber, updatedState);
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
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

                    if (name != null && name.equals(name_get)) {
                        userFound = true;
                        editNameEditText.setText(name);
                        editEmailEditText.setText(email);
                        editCountryEditText.setText(country);
                        editDateEditText.setText(date);
                        editPhoneEditText.setText(phoneNumber);
                        editStateEditText.setText(state);

                        String imageUrl = userSnapshot.child("profile_image_url").getValue(String.class);
                        if (imageUrl != null) {
                            Glide.with(EditProfile.this)
                                    .load(imageUrl)
                                    .apply(new RequestOptions().centerCrop())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(editProfileImageView);
                        }
                        break;
                    }
                }

                if (!userFound) {
                    Toast.makeText(EditProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfile.this, "Database error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveUpdatedUserInfo(String name_get, String updatedName, String updatedEmail, String updatedCountry, String updatedDate, String updatedPhoneNumber, String updatedState) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean userFound = false;
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);

                    if (name != null && name.equals(name_get)) {
                        userFound = true;
                        userSnapshot.getRef().child("name").setValue(updatedName);
                        userSnapshot.getRef().child("email").setValue(updatedEmail);
                        userSnapshot.getRef().child("country").setValue(updatedCountry);
                        userSnapshot.getRef().child("date").setValue(updatedDate);
                        userSnapshot.getRef().child("phone_number").setValue(updatedPhoneNumber);
                        userSnapshot.getRef().child("state").setValue(updatedState);

                        if (imageUri != null) {
                            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(name_get + ".jpg");

                            UploadTask uploadTask = imageRef.putFile(imageUri);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUri) {
                                            String imageUrl = downloadUri.toString();
                                            userSnapshot.getRef().child("imageUrI").setValue(imageUrl);
                                            Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfile.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }

                if (userFound) {
                    Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedName", updatedName);
                resultIntent.putExtra("updatedEmail", updatedEmail);
                resultIntent.putExtra("updatedCountry", updatedCountry);
                resultIntent.putExtra("updatedDate", updatedDate);
                resultIntent.putExtra("updatedPhoneNumber", updatedPhoneNumber);
                resultIntent.putExtra("updatedState", updatedState);

                setResult(RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfile.this, "Database error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}







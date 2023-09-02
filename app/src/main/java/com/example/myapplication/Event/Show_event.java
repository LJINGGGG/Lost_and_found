package com.example.myapplication.Event;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


public class Show_event extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    private Intent intent2;


    //event data
    private String event_name;
    private String category;
    private String info;
    private String loc;
    private String state;
    private Integer participant;
    private String date_start;
    private String date_end;

    private String username1;
    private String prof_url;

    ///
    private String url_get;
    private String img_url;
    private String userkey = "-Nd3aL11ai2JasjmKdIw";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_event);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button join_member = findViewById(R.id.button_join);
        Button return_page = findViewById(R.id.back);
        Button member_list = findViewById(R.id.member);

        Intent intent = getIntent();
        url_get = intent.getStringExtra("postlink");
        img_url = intent.getStringExtra("image_url");

        Log.d("lookup", "ongetlink" + url_get);

        Uri uri_temporary = Uri.parse(url_get);
        String Uri_desiredPart = uri_temporary.getLastPathSegment();


        ImageView img_view = findViewById(R.id.imageView_event);
        ImageView prof_view = findViewById(R.id.user_profile);
        TextView username = findViewById(R.id.User_name);
        TextView title = findViewById(R.id.Event_name2);
        TextView event_n = findViewById(R.id.Event_name1);
        TextView event_c = findViewById(R.id.Category);
        TextView event_i = findViewById(R.id.information);
        TextView event_l = findViewById(R.id.location);
        TextView event_s = findViewById(R.id.State);
        TextView event_p = findViewById(R.id.participants);

        DatabaseReference user_get = databaseReference.child("User").child(userkey);

        user_get.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username1 = snapshot.child("name").getValue(String.class);
                username.setText(username1);
                prof_url =  snapshot.child("imageUrI").getValue(String.class);

                Log.d("prof_get","porfile link:" + prof_url);

                Glide.with(prof_view.getContext())
                        .load(prof_url) // Assuming childValue is the URL or resource for the image
                        .placeholder(R.drawable.place_holder_image) // Add a placeholder image resource
                        .error(R.drawable.error_image) // Add an error image resource
                        .into(prof_view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference userref = databaseReference.child("Event").child(Uri_desiredPart);

        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    date_start =snapshot.child("date_start").getValue(String.class);
                    date_end = snapshot.child("date_end").getValue(String.class);
                    event_name = snapshot.child("event_name").getValue(String.class);
                    category = snapshot.child("category").getValue(String.class);
                    info = snapshot.child("information").getValue(String.class);
                    loc = snapshot.child("location").getValue(String.class);
                    state = snapshot.child("State").getValue(String.class);
                    participant = snapshot.child("number of participants").getValue(Integer.class);

                    event_p.setText(participant.toString());
                    event_s.setText(state);
                    event_l.setText(loc);
                    event_i.setText(info);
                    event_c.setText(category);
                    event_n.setText(date_start + " - " + date_end);
                    title.setText(event_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        DatabaseReference imagesRef = databaseReference.child("images");

        if (img_url != null){
            imagesRef.child(img_url).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String childValue = dataSnapshot.getValue(String.class);

                        Log.d("ImageUri", "Image URI2: " + childValue);




                        // Load the image using Glide
                        Glide.with(img_view.getContext())
                                .load(childValue) // Assuming childValue is the URL or resource for the image
                                .placeholder(R.drawable.place_holder_image) // Add a placeholder image resource
                                .error(R.drawable.error_image) // Add an error image resource
                                .into(img_view);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
                }
            });
        }
       else{
            Glide.with(img_view.getContext())
                    .load(R.drawable.place_holder_image) // Assuming childValue is the URL or resource for the image
                    .placeholder(R.drawable.place_holder_image) // Add a placeholder image resource
                    .error(R.drawable.error_image) // Add an error image resource
                    .into(img_view);
        }




        join_member.setOnClickListener(view -> {

            DatabaseReference postRef = databaseReference.child("Event").child(Uri_desiredPart);

            postRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                    // Get the current participants count
                    Integer currentCount = currentData.child("number of participants").getValue(Integer.class);

                    Log.d("value","value"+ currentCount);
                    // Increment the count by 1
                    if (currentCount != null) {
                        currentData.child("number of participants").setValue(currentCount + 1);
                    }

                    return Transaction.success(currentData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                    if (error != null) {
                        // Handle the error
                    } else {
                        // Update UI or perform other actions
                    }
                }
            });

            // authorise user here
            DatabaseReference postRef2 = databaseReference.child("Event").child(Uri_desiredPart);

// Create a new "participants" node under the specific post
            DatabaseReference participantsRef = postRef2.child("participants");

// Push the user key to the "participants" node
            participantsRef.push().setValue(userkey)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(this, "Participant added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle error
                            Toast.makeText(this, "Failed to add participant", Toast.LENGTH_SHORT).show();
                        }
                    });
            //String currentUserKey = auth.getCurrentUser().getUid();

            intent2 = new Intent(Show_event.this, Member_list.class);
            intent2.putExtra("image_url", img_url);
            intent2.putExtra("postlink", url_get);



            startActivity(intent2);

        });

        return_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(Show_event.this, MainActivity_event.class);

                startActivity(intent3);
            }
        });

        member_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent2 = new Intent(Show_event.this, Member_list.class);
                intent2.putExtra("image_url", img_url);
                intent2.putExtra("postlink", url_get);



                startActivity(intent2);
            }
        });

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

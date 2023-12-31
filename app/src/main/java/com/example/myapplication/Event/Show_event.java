package com.example.myapplication.Event;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.user_features.SignIn_;
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
    private String post_user = "";
    private String url_get;
    private String img_url , state_show;
    private String userkey = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_event);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button join_member = findViewById(R.id.button_join);
        ImageButton return_page = findViewById(R.id.back);
        ImageButton member_list = findViewById(R.id.member);

        Intent intent = getIntent();
        url_get = intent.getStringExtra("postlink");
        img_url = intent.getStringExtra("image_url");
        userkey = intent.getStringExtra("show_userId");
        username1= intent.getStringExtra("show_name");
        prof_url = intent.getStringExtra("show_ImageUrl");
        state_show = intent.getStringExtra("show_state");


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

        DatabaseReference post_user_ref = databaseReference.child("Event").child(Uri_desiredPart);

        post_user_ref.child("auth_user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post_user = snapshot.getValue(String.class);
                Log.d("post-user", "post-user: " + post_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(post_user == null){
            post_user ="";
        }

        username.setText(username1);
        Glide.with(prof_view.getContext())
                .load(img_url) // Assuming childValue is the URL or resource for the image
                .placeholder(R.drawable.place_holder_image) // Add a placeholder image resource
                .error(R.drawable.error_image) // Add an error image resource
                .into(prof_view);




        DatabaseReference userref1 = databaseReference.child("Event").child(Uri_desiredPart);

        userref1.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            general_dialog("💖Information💖" ,"Participant added successfully");
                        } else {
                            // Handle error
                            general_dialog("🚫 Warning 🚫" ,"A friend with the same name already exists.");
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
                String user_info = userkey + "," + username1 + "," + prof_url +"," + state_show;
                intent3.putExtra("user_info", user_info);
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

package com.example.myapplication.user_features;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.filter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class SignIn_ extends AppCompatActivity {

    private EditText emailTextView, passwordTextView, date, usernameTextView, phoneTextView, stateTextView;

    private DatePickerDialog datePickerDialog;

    private GoogleSignInClient mGoogleSignInClient;

    private String userType_, info, country, state;

    private Button Btn, Btn1;
    private StorageReference storageReference;

    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    private ArrayList<User> userList = new ArrayList<>();

    private String user , pass , phone_number , name ,date_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        load_UserList();

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, configureGoogleSignIn());

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            try {
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                firebaseAuthWithGoogle(account);
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        usernameTextView = findViewById(R.id.username);
        phoneTextView = findViewById(R.id.phone);
        Spinner countrySpinner = findViewById(R.id.county_spinner);

        Btn = findViewById(R.id.btnregister);
        Btn1 = findViewById(R.id.btngoogle);

        Intent intent2 = getIntent();
        if (intent2 != null) {
            userType_ = intent2.getStringExtra("userType");
        }

        // 2. Date
        date = findViewById(R.id.detail_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(SignIn_.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        String[] countries ={
                "Select State",
                "Johor",
                "Kedah",
                "Kelantan",
                "Melaka",
                "Negeri Sembilan",
                "Pahang",
                "Perak",
                "Perlis",
                "Pulau Pinang",
                "Sabah",
                "Sarawak",
                "Selangor",
                "Terengganu",
                "Wilayah Persekutuan (KL)",
                "Wilayah Persekutuan (Putrajaya)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                state = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = emailTextView.getText().toString().trim();
                pass = passwordTextView.getText().toString().trim();
                phone_number = phoneTextView.getText().toString().trim();
                name = usernameTextView.getText().toString().trim();
                date_ = date.getText().toString().trim();
                country = "Malaysia";

                // Check if any of the fields are empty
                if (user.isEmpty() || pass.isEmpty() || phone_number.isEmpty() || name.isEmpty() || date_.isEmpty() || country.isEmpty() || state.isEmpty()) {
                    general_analog(SignIn_.this, "🚫 Missing Information 🚫", "Please fill in all fields.");
                    return;
                }

                for (User user1 : userList) {
                    if (user1.getName().equalsIgnoreCase(name.trim())) {
                        general_analog(SignIn_.this, "🚫 Duplicate Name 🚫", "Please fill in again.");
                        return;
                    }
                }
            }
        });

        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_google();
            }
        });
    }

    private void register_google() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        for (User user1 : userList) {
                            if (user1.getEmail().equalsIgnoreCase(acct.getEmail())) {
                                general_analog(SignIn_.this, "🚫 Warning 🚫", "Already register before");
                                return;
                            }
                        }

                        info = acct.getDisplayName() + "," + acct.getEmail() + "," + " " + "," + " " + "," + " " + "," + " " + "," + userType_;
                        Toast.makeText(SignIn_.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(SignIn_.this, Profile_Picture.class);
                        intent2.putExtra("info", info);
                        startActivity(intent2);
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignIn_.this, "This Google account is already linked to another account.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignIn_.this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private GoogleSignInOptions configureGoogleSignIn() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }


    public void load_UserList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot friendSnapshot : snapshot.getChildren()) {
                    User user = friendSnapshot.getValue(User.class);
                    userList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    public void general_analog(Context context, String message_1, String message_2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message_1);
        builder.setMessage(message_2);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Optionally, add code here to handle the "Ok" button click
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

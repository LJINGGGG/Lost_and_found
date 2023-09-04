package com.example.myapplication;//package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class filter extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<Lost_Found_Post> itemList;
    private ImageButton done_button , clear_button , back_button;
    private String selectedCategory, selectedCountry ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Spinner countrySpinner = findViewById(R.id.stateSpinner);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        EditText customCategoryEditText = findViewById(R.id.customCategoryEditText);
        done_button = findViewById(R.id.done_btn);
        clear_button = findViewById(R.id.clear_btn);
        back_button = findViewById(R.id.back_btn);

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

        String[] categories = {
                "Select Category",
                "Bottle",
                "Bag",
                "Key",
                "Student Card",
                "Phone",
                "iPad",
                "Others" // Add "Others" as an option
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCountry = (String) parentView.getItemAtPosition(position);
                Toast.makeText(filter.this, "Selected State: " + selectedCountry, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapter2);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = (String) parentView.getItemAtPosition(position);
                Toast.makeText(filter.this, "Selected State: " + selectedCategory, Toast.LENGTH_SHORT).show();

                // Show or hide the EditText field based on the selection
                if (TextUtils.equals(selectedCategory, "Others")) {
                    customCategoryEditText.setVisibility(View.VISIBLE);
                } else {
                    customCategoryEditText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( (! selectedCategory.isEmpty()) || (! selectedCountry.isEmpty())){
                    general_dialog("💖Information💖" ,"Successfully filter");
                    Intent intent = new Intent(filter.this,MainActivity.class);
                    intent.putExtra("info_country", selectedCategory + "," +selectedCountry);
                    startActivity(intent);
                    finish();
                }


            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                selectedCategory = " ";
                selectedCountry = " ";
                general_dialog("💖Information💖" ,"Successfully reset");
                Intent intent = new Intent(filter.this,MainActivity.class);
                intent.putExtra("Category", selectedCategory);
                intent.putExtra("Country", selectedCountry);
                startActivity(intent);
                finish();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = " ";
                selectedCountry = " ";
                Intent intent = new Intent(filter.this,MainActivity.class);
                intent.putExtra("Category", selectedCategory);
                intent.putExtra("Country", selectedCountry);
                startActivity(intent);
                finish();
            }
        });



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


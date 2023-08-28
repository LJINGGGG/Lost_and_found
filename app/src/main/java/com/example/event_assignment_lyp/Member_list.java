package com.example.event_assignment_lyp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Member_list extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);

        RecyclerView recyclerView = findViewById(R.id.Myrecycleview2);
        List<YourDataModel2> data = new ArrayList<>();
        data.add(new YourDataModel2(R.drawable.customer, "Description 1"));
        data.add(new YourDataModel2(R.drawable.customer, "Description 2"));
        data.add(new YourDataModel2(R.drawable.customer, "Description 3"));
        data.add(new YourDataModel2(R.drawable.customer, "Description 1"));
        data.add(new YourDataModel2(R.drawable.customer, "Description 2"));
        data.add(new YourDataModel2(R.drawable.customer, "Description 31"));
        data.add(new YourDataModel2(R.drawable.customer, "Description 1"));
        data.add(new YourDataModel2(R.drawable.customer, "Description 2"));
        data.add(new YourDataModel2(R.drawable.customer, "Description 3"));
        // Add more items...

        MyAdapter2 adapter = new MyAdapter2(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

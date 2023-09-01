package com.example.myapplication.nearBy_features;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class nearBy_Viewholder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nametextView , phonetextView , addresstextView;


    public nearBy_Viewholder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView);

        nametextView = itemView.findViewById(R.id.name_textview);
        phonetextView = itemView.findViewById(R.id.phoneNumber_textview);
        addresstextView = itemView.findViewById(R.id.address_textview);

    }
}

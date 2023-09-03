package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class dataModel_viewholder extends RecyclerView.ViewHolder {

    ImageView imageView , userProfile;
    TextView username , descripton ;

    public dataModel_viewholder(@NonNull View itemView) {
        super(itemView);

        userProfile = itemView.findViewById(R.id.user_profile);
        imageView = itemView.findViewById(R.id.Image_Post);
        username = itemView.findViewById(R.id.user_name);
        descripton = itemView.findViewById(R.id.description);

    }
}

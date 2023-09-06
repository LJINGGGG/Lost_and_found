package com.example.myapplication.nearBy_features;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class Selected_Item_Viewholder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView descripton;

    public Selected_Item_Viewholder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.Image_Post);
        descripton = itemView.findViewById(R.id.description);
    }
}

package com.example.myapplication.nearBy_features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Lost_Found_Post;
import com.example.myapplication.R;

import java.util.List;

public class Selected_Item_Adapter extends RecyclerView.Adapter<Selected_Item_Viewholder> {

    Context context;
    List<Lost_Found_Post> items;


    public Selected_Item_Adapter(Context context, List<Lost_Found_Post> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Selected_Item_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Selected_Item_Viewholder(LayoutInflater.from(context).inflate(R.layout.selected_item_nearby,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Selected_Item_Viewholder holder, int position) {
        StringBuilder information = new StringBuilder();
        information.append("     Category : " + items.get(position).getCategory()+"\n");
        information.append("     Location : " + items.get(position).getLocation()+"\n");
        information.append("     Information : " + items.get(position).getInformation()+"\n");

        holder.descripton.setText(information);
        Lost_Found_Post currentItem = items.get(position);
        String imageUri = currentItem.getImageurl();

        if (imageUri != null) {
            // Load the image using Glide
            Glide.with(holder.imageView.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.place_holder_image)
                    .error(R.drawable.error_image)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

package com.example.myapplication;//package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.user_features.User;

import java.util.List;

public class dataModel_adapter extends RecyclerView.Adapter<dataModel_viewholder>
{
    Context context;
    List<Lost_Found_Post> items;

    public dataModel_adapter(Context context , List<Lost_Found_Post>items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public dataModel_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new dataModel_viewholder(LayoutInflater.from(context).inflate(R.layout.recycle_view_itrm,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull dataModel_viewholder holder, int position) {

        holder.username.setText(items.get(position).getName());

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
                    .load(imageUri) // Assuming imageUri is the URL or resource for the image
//                    .placeholder(R.drawable.place_holder_image) // Add a placeholder image resource
//                    .error(R.drawable.error_image) // Add an error image resource
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

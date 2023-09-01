package com.example.myapplication.nearBy_features;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.user_features.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class nearBy_Adapter extends RecyclerView.Adapter<nearBy_Viewholder> {

    Context context;
    List<User> userList;

    public nearBy_Adapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void setFilteredList(nearBy context, List<User> filteredList) {
        this.context = context;
        this.userList = filteredList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public nearBy_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new nearBy_Viewholder(LayoutInflater.from(context).inflate(R.layout.nearby_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull nearBy_Viewholder holder, int position) {
        holder.nametextView.setText(userList.get(position).getName());
        holder.phonetextView.setText(userList.get(position).getEmail());
        holder.addresstextView.setText(userList.get(position).getState() + " , "+ userList.get(position).getCountry());


        User currentItem = userList.get(position);
        String imageUri = currentItem.getImageUrI();

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
        return userList.size();
    }




}

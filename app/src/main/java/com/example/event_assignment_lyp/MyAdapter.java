package com.example.event_assignment_lyp;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<YourDataModel> data;

    public MyAdapter(List<YourDataModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YourDataModel currentItem = data.get(position);

        // Set image using Glide or your preferred image loading library
        String imageUri = currentItem.getImageResource();



        Log.d("ImageUri", "Image URsdasdadadadI: " + imageUri);

        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference().child("images");
        if(imageUri != null){
            imagesRef.child(imageUri).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String childValue = dataSnapshot.getValue(String.class);

                        // Load the image using Glide
                        Glide.with(holder.imageView.getContext())
                                .load(childValue) // Assuming childValue is the URL or resource for the image
                                .placeholder(R.drawable.place_holder_image) // Add a placeholder image resource
                                .error(R.drawable.error_image) // Add an error image resource
                                .into(holder.imageView);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
                }
            });
        }




        holder.textView.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}

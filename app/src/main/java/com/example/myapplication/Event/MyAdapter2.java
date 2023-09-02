package com.example.myapplication.Event;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
    private List<YourDataModel2> data;
    private String name;
    private String img_url;

    public MyAdapter2(List<YourDataModel2> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YourDataModel2 currentItem = data.get(position);

        String participant = currentItem.getDescription();

        DatabaseReference paticipant_info =  FirebaseDatabase.getInstance().getReference("User");
        paticipant_info.child(participant).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("name").getValue(String.class);
                    img_url =  dataSnapshot.child("imageUrI").getValue(String.class);

                    holder.descriptionTextView.setText(name);

                    Glide.with(holder.imageView.getContext())
                            .load(img_url) // Assuming childValue is the URL or resource for the image
                            .placeholder(R.drawable.place_holder_image) // Add a placeholder image resource
                            .error(R.drawable.error_image) // Add an error image resource
                            .into(holder.imageView);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<YourDataModel> data;
    private ItemClickListener itemClickListener;

    public MyAdapter(List<YourDataModel> data, ItemClickListener itemClickListener) {
        this.data = data;
        this.itemClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(int position);
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
        /////////////////////////////////////////////////////////////////////////////////////
        // Set image using Glide or your preferred image loading library
        String imageUri = currentItem.getImageResource();

        Log.d("ImageUri", "Image URI: " + imageUri);


        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("images");
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
        else{
            Glide.with(holder.imageView.getContext())
                    .load(R.drawable.place_holder_image) // Assuming childValue is the URL or resource for the image
                    .placeholder(R.drawable.place_holder_image) // Add a placeholder image resource
                    .error(R.drawable.error_image) // Add an error image resource
                    .into(holder.imageView);
        }

///////////////////////////////////////////////////////////////////////////////////////////////


        holder.event_Name.setText(currentItem.getDescription());

        DatabaseReference Eventref = FirebaseDatabase.getInstance().getReference("Event");


        String url = currentItem.getLink();
        String[] parts = url.split("/");
        String eventId = parts[parts.length - 1];
        Eventref.child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String category = snapshot.child("category").getValue(String.class);
                String date_end = snapshot.child("date_end").getValue(String.class);
                String date_start = snapshot.child("date_start").getValue(String.class);
                Integer participants_n = snapshot.child("number of participants").getValue(Integer.class);

                String combine =
                        "\nDate              :   " + date_start +" - " + date_end +
                        "\nCategory      :   " + category +
                        "\nParticipants :   " + participants_n;



                holder.textView.setText(combine);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition(); // Get the current adapter position
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(adapterPosition);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView event_Name;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            event_Name = itemView.findViewById(R.id.event_textView);
            textView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}

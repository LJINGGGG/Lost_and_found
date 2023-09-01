package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class dataModel_adapter extends RecyclerView.Adapter<dataModel_viewholder>
{
    private List<YourDataMOdel> data;
    private String name;
    private String img_url;
    private String img_url2 = null;

    public dataModel_adapter(Context applicationContext, List<YourDataMOdel> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView profile;
        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.Image_Post);
            profile = itemView.findViewById(R.id.user_profile);
            username =itemView.findViewById(R.id.user_name);
        }
    }

    @NonNull
    @Override
    public dataModel_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_item, parent, false);
        return new dataModel_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull dataModel_viewholder holder, int position) {
        YourDataMOdel currentItem = data.get(position);

        img_url = currentItem.getImg_post();

        holder.username.setText(currentItem.getpost_Name());

        Glide.with(holder.imageView.getContext())
                .load(img_url) // Assuming childValue is the URL or resource for the image
                .placeholder(R.drawable.ic_customer) // Add a placeholder image resource
                .error(R.drawable.ic_error) // Add an error image resource
                .into(holder.imageView);

        Glide.with(holder.imageView.getContext())
                .load(img_url) // Assuming childValue is the URL or resource for the image
                .placeholder(R.drawable.ic_customer) // Add a placeholder image resource
                .error(R.drawable.ic_error) // Add an error image resource
                .into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}

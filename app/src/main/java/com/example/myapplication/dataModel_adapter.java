package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class dataModel_adapter extends RecyclerView.Adapter<dataModel_viewholder>
{
    Context context;
    List<YourDataMOdel> items ;

    public dataModel_adapter(Context context , List<YourDataMOdel>items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public dataModel_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new dataModel_viewholder(LayoutInflater.from(context).inflate(R.layout.recycle_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull dataModel_viewholder holder, int position) {

        holder.username.setText(items.get(position).getName());
        holder.descripton.setText(items.get(position).getDescription_post());
        holder.imageView.setImageResource(items.get(position).getImage_Post());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

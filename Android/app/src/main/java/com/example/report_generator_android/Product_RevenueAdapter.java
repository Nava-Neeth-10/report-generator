package com.example.report_generator_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Product_RevenueAdapter extends RecyclerView.Adapter<Product_RevenueAdapter.ViewHolder> {
    public ArrayList<String> year,q1,q2,q3,q4;
    public Product_RevenueAdapter(ArrayList<String> year,ArrayList<String> q1,ArrayList<String> q2,ArrayList<String> q3,ArrayList<String> q4)
    {
        this.year=year;
        this.q1=q1;
        this.q2=q2;
        this.q3=q3;
        this.q4=q4;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_revenue,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.year.setText(year.get(position));
        holder.q1.setText(q1.get(position));
        holder.q2.setText(q2.get(position));
        holder.q3.setText(q3.get(position));
        holder.q4.setText(q4.get(position));
    }

    @Override
    public int getItemCount() {
        return year.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView year,q1,q2,q3,q4;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            year=itemView.findViewById(R.id.year);
            q1=itemView.findViewById(R.id.q1);
            q2=itemView.findViewById(R.id.q2);
            q3=itemView.findViewById(R.id.q3);
            q4=itemView.findViewById(R.id.q4);
        }
    }
}

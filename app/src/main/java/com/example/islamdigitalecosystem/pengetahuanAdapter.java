package com.example.islamdigitalecosystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class pengetahuanAdapter extends RecyclerView.Adapter<pengetahuanAdapter.MyViewHolder> {
    ArrayList<pengetahuanData> pengetahuanData;
    Context context;
    private static final String TAG = "pengetahuan adapter";

    public pengetahuanAdapter(ArrayList<pengetahuanData> pengetahuanData) {
        this.pengetahuanData = pengetahuanData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pengetahuan_list,parent,false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       final String judulMateri = pengetahuanData.get(position).getMateriName();
       holder.setBabList(judulMateri);

        holder.goMateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, materiBab1.class);
                intent.putExtra("babMateriRef", judulMateri);
                context.startActivity(intent);
            }
        });

        holder.materiImage.setImageResource(R.drawable.aljabar);
    }

    @Override
    public int getItemCount() {
        return pengetahuanData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView materiImage;
        LinearLayout goMateri;
        TextView textViewName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            materiImage = itemView.findViewById(R.id.ivBabListMateri);
            goMateri = itemView.findViewById(R.id.bablistMateri);
        }

        public void setBabList(String babList){
            textViewName = itemView.findViewById(R.id.textName);
            textViewName.setText(babList);
        }
    }

}

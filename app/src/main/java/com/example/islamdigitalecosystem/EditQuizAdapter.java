package com.example.islamdigitalecosystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class EditQuizAdapter extends RecyclerView.Adapter<EditQuizAdapter.MyViewHolder>{
    ArrayList <bablistmodel> bablistmodels;
    Context context;

    public  EditQuizAdapter(ArrayList<bablistmodel> bablistmodels){
        this.bablistmodels = bablistmodels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_kuis_single_bablist_item_view, parent, false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  String babListData = bablistmodels.get(position).getBabList();
        holder.setBabList(babListData);

        holder.startEditQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent quizIntent = new Intent(context, EditKuis.class);
                quizIntent.putExtra("BabReference", babListData);
                context.startActivity(quizIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bablistmodels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout startEditQuiz;
        TextView babView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            startEditQuiz = itemView.findViewById(R.id.startEditQuizButton);
        }
        public void setBabList(String babList){
            babView = itemView.findViewById(R.id.tvBabView);
            babView.setText(babList);
        }
    }
}
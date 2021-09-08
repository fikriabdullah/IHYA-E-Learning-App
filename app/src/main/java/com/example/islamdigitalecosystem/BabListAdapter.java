package com.example.islamdigitalecosystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BabListAdapter extends RecyclerView.Adapter<BabListAdapter.MyViewHolder> {
    ArrayList<bablistmodel>Mlist;
    Context context;

    public BabListAdapter(ArrayList<bablistmodel> Mlist){
        this.Mlist = Mlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitembablistview, parent, false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String babListData = Mlist.get(position).getBabList();
        holder.setBabList(babListData);

        holder.playQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent quizIntent = new Intent(context, quiz.class);
                quizIntent.putExtra("BabReference", babListData);
                context.startActivity(quizIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  Mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView BabList;
        ImageView playQuizButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playQuizButton = itemView.findViewById(R.id.playButttonQuiz);
        }

        public void setBabList(String babList) {
            BabList = itemView.findViewById(R.id.BabView);
            BabList.setText(babList);
        }
    }

}

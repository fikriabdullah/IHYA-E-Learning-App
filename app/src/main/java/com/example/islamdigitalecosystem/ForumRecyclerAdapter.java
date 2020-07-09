package com.example.islamdigitalecosystem;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class ForumRecyclerAdapter extends RecyclerView.Adapter<ForumRecyclerAdapter.ViewHolder> {

    public List<QuestionPost> forum_list;

    public ForumRecyclerAdapter(List<QuestionPost> forum_list){
        this.forum_list = forum_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String question_data = forum_list.get(position).getContent();
        holder.setQuestionView(question_data);

        String userIDdata = forum_list.get(position).getUserID();
        holder.setUserID(userIDdata);

        long milisecond = forum_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(milisecond)).toString();
        holder.setTime(dateString);

    }

    @Override
    public int getItemCount() {
        return forum_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView questionView;
        private TextView userIDtv;
        private TextView blogDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setQuestionView(String questionText){
            questionView = mView.findViewById(R.id.blogQuestion);
            questionView.setText(questionText);

        }

        public void setTime(String DateText){
            blogDate = mView.findViewById(R.id.blogDate);
            blogDate.setText(DateText);
        }

        public void setUserID(String UserID){
            userIDtv = mView.findViewById(R.id.blogUsername);
            userIDtv.setText(UserID);
        }

    }
}
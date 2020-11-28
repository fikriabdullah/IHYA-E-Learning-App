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
    public Context context;

    public ForumRecyclerAdapter(List<QuestionPost> forum_list){
        this.forum_list = forum_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        final String forumPostId = forum_list.get(position).ForumPostId;

        String question_data = forum_list.get(position).getContent();
        holder.setQuestionView(question_data);

        String userIDdata = forum_list.get(position).getUserID();
        holder.setUserID(userIDdata);

        long milisecond = forum_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(milisecond)).toString();
        holder.setTime(dateString);

        holder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, commentActivity.class);
                commentIntent.putExtra("forum_post_id", forumPostId);
                context.startActivity(commentIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return forum_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private ImageView blogCommentBtn;
        private TextView questionView;
        private TextView userIDtv;
        private TextView blogDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            blogCommentBtn = mView.findViewById(R.id.forumComents);
        }
        public void setQuestionView(String questionText){
            questionView = mView.findViewById(R.id.blogQuestion);
            questionView.setText(questionText);

        }

        public void setTime(String DateText){
            blogDate = mView.findViewById(R.id.commentDate);
            blogDate.setText(DateText);
        }

        public void setUserID(String UserID){
            userIDtv = mView.findViewById(R.id.blogUsername);
            userIDtv.setText(UserID);
        }

    }
}

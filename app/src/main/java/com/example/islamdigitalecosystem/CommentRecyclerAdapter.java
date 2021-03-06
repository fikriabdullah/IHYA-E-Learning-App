package com.example.islamdigitalecosystem;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.Date;
import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CommentRecyclerAdapter";
    public List<Comments> comments_list;
    private Context context;

    public CommentRecyclerAdapter(List<Comments> comments_list){
        this.comments_list = comments_list;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_single_list_item, parent, false);
        context = parent.getContext();
        return new CommentRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        String commentMessage = comments_list.get(position).getMessage();
        holder.setCommentMessage(commentMessage);

        String userID = comments_list.get(position).getUserID();
        holder.setUserId(userID);

        try {
            long dateText = comments_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("dd/MM/yyyy  hh:mm", new Date(dateText)).toString();
            holder.setCommentDate(dateString);
        }catch (Exception e){
            Log.d(TAG, "Failed to load date : " +e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (comments_list != null){
            return comments_list.size();
        }else {
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView commentMessage, cmnUsername, commentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCommentMessage(String message){
            commentMessage = mView.findViewById(R.id.commentPH);
            commentMessage.setText(message);
        }

        public void setUserId(String userId){
            cmnUsername = mView.findViewById(R.id.cmnUsername);
            cmnUsername.setText(userId);
        }

        public void setCommentDate(String dateText){
            commentDate = mView.findViewById(R.id.commentDate);
            commentDate.setText(dateText);
        }
    }
}

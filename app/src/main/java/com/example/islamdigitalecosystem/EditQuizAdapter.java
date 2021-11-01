package com.example.islamdigitalecosystem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditQuizAdapter extends RecyclerView.Adapter<EditQuizAdapter.MyViewHolder>{
    ArrayList <bablistmodel> bablistmodels;
    Context context;
    FirebaseFirestore db;
    private static final String TAG = "EditQuizAdapter:";
    CollectionReference collectionReference;

    public  EditQuizAdapter(ArrayList<bablistmodel> bablistmodels){
        this.bablistmodels = bablistmodels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_kuis_single_item_view, parent, false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  String babListData = bablistmodels.get(position).getBabList();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("quiz");
        holder.setBabList(babListData);

        holder.startEditQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent quizIntent = new Intent(context, EditKuis.class);
                quizIntent.putExtra("BabReference", babListData);
                context.startActivity(quizIntent);
            }
        });
        holder.deleteBab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectionReference.document(babListData).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document Deleted");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Delete Document Failed : " + e.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return bablistmodels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout startEditQuiz;
        ImageView deleteBab;
        TextView babView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            startEditQuiz = itemView.findViewById(R.id.startEditQuizButton);
            deleteBab = itemView.findViewById(R.id.deleteBab);
        }
        public void setBabList(String babList){
            babView = itemView.findViewById(R.id.tvBabView);
            babView.setText(babList);
        }
    }
}
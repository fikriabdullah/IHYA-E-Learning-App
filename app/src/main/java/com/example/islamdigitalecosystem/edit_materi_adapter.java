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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class edit_materi_adapter extends RecyclerView.Adapter<edit_materi_adapter.ViewHolder> {
        ArrayList<pengetahuanData> pengetahuanData;
        Context context;
        private static final String TAG = "editMateriAdapter";
        FirebaseFirestore db;
        CollectionReference collectionReference;

        public edit_materi_adapter(ArrayList<pengetahuanData> pengetahuanData) {
            this.pengetahuanData = pengetahuanData;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_materi_list, parent, false);
            context = parent.getContext();
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           final String jdulMateri = pengetahuanData.get(position).getMateriName();
           holder.setBabList(jdulMateri);
           db = FirebaseFirestore.getInstance();
           collectionReference = db.collection("Materi");

           holder.goEditMtr.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(context, materiBab1.class);
                   intent.putExtra("babMateriRef", jdulMateri);
                   context.startActivity(intent);
               }
           });

           holder.deleteBab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   try {
                       collectionReference.document(jdulMateri).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Log.d(TAG, "Document Deleted");
                               Toast.makeText(context, "Document Deleted, Pull Down To Refresh Page", Toast.LENGTH_LONG).show();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Log.d(TAG, "Delete Document Failed : " + e.getMessage());
                               Toast.makeText(context, "Document Delete Failed ", Toast.LENGTH_LONG).show();
                           }
                       });
                   }catch (Exception e){
                       Log.d(TAG, "Delete Document Failed : " + e.getMessage());
                   }
               }
           });

           holder.babImage.setImageResource(R.drawable.aljabar);
        }

        @Override
        public int getItemCount() {
            return pengetahuanData.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            ImageView deleteBab, babImage;
            TextView textViewName;
            LinearLayout goEditMtr;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                deleteBab = itemView.findViewById(R.id.deleteMtrBab);
                babImage = itemView.findViewById(R.id.imageviewMtrEdt);
                goEditMtr = itemView.findViewById(R.id.goMateriEdit);
            }
            public  void setBabList(String babData){
                textViewName = itemView.findViewById(R.id.textName);
                textViewName.setText(babData);
            }
        }

    }


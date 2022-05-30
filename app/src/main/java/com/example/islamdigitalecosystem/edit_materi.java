package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class edit_materi extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFirestore db;
    Task<QuerySnapshot> collectionReference;
    edit_materi_adapter edit_materi_adapter;
    private static final String TAG = "editMateriBablist";
    private ArrayList<pengetahuanData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_materi);
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.editMateriBabListrv);
        edit_materi_adapter = new edit_materi_adapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        recyclerView.setAdapter(edit_materi_adapter);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutEditMateri);
        collectionReference = db.collection("Materi").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    Log.d(TAG, documentSnapshot.getId());
                    String babList = documentSnapshot.getId();
                    pengetahuanData pengetahuanData = new pengetahuanData(babList);
                    list.add(pengetahuanData);
                }
                edit_materi_adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Get Document Id Failed :" + e.getMessage());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                list.clear();
                Log.d(TAG, " List Cleared, reloading data");
                collectionReference = db.collection("Materi").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            Log.d(TAG, documentSnapshot.getId());
                            String babList = documentSnapshot.getId();
                            pengetahuanData pengetahuanData = new pengetahuanData(babList);
                            list.add(pengetahuanData);
                        }
                        edit_materi_adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Get Document Id Failed :" + e.getMessage());
                    }
                });
            }
        });
    }
}


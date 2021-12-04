package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class pengetahuan extends AppCompatActivity {
    private static final String TAG = "pengetahuan";
    FirebaseFirestore db;
    private RecyclerView recyclerView;
    pengetahuanAdapter pengetahuanAdapter;
    Task<QuerySnapshot> collectionReference;
    private ArrayList<pengetahuanData> data;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengetahuan);
        data = new ArrayList<>();
        recyclerView = findViewById(R.id.pengetahuanRv);
        pengetahuanAdapter = new pengetahuanAdapter(data);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutPengetahuan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        recyclerView.setAdapter(pengetahuanAdapter);

        collectionReference = db.collection("Materi").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                    Log.d(TAG, documentSnapshot.getId());
                    String jdulList = documentSnapshot.getId();
                    pengetahuanData pengetahuanData = new pengetahuanData(jdulList);
                    data.add(pengetahuanData);
                }
                pengetahuanAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Load Materi List Dataset Failed" + e.getMessage());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                data.clear();
                Log.d(TAG, "on refresh, list cleared");
                collectionReference = db.collection("Materi").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                            Log.d(TAG, documentSnapshot.getId());
                            String jdulList = documentSnapshot.getId();
                            pengetahuanData pengetahuanData = new pengetahuanData(jdulList);
                            data.add(pengetahuanData);
                        }
                        pengetahuanAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Load Materi List Dataset Failed" + e.getMessage());
                    }
                });
            }
        });
    }

}

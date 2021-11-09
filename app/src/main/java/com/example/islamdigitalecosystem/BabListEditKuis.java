package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BabListEditKuis extends AppCompatActivity {
    private RecyclerView recyclerView;
    EditQuizAdapter editQuizAdapter;
    private final static String TAG = "BabListEditQUiz";
    FirebaseFirestore db;
    SwipeRefreshLayout swipeRefreshLayout;
    Task<QuerySnapshot> collectionReference;
    private ArrayList<bablistmodel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bab_list_edit_kuis);
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.bablistEditQuizRV);
        editQuizAdapter = new EditQuizAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        recyclerView.setAdapter(editQuizAdapter);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        collectionReference = db.collection("quiz").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshots : queryDocumentSnapshots.getDocuments()){
                    Log.d(TAG, documentSnapshots.getId());
                    String babList = documentSnapshots.getId();
                    bablistmodel bablistmodel = new bablistmodel(babList);
                    list.add(bablistmodel);
                }
                editQuizAdapter.notifyDataSetChanged();
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
                collectionReference = db.collection("quiz").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshots : queryDocumentSnapshots.getDocuments()){
                            Log.d(TAG, documentSnapshots.getId());
                            String babList = documentSnapshots.getId();
                            bablistmodel bablistmodel = new bablistmodel(babList);
                            list.add(bablistmodel);
                        }
                        editQuizAdapter.notifyDataSetChanged();
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
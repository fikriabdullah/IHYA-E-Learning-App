package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SelectBabQuiz extends AppCompatActivity {
    private RecyclerView recyclerView;
    BabListAdapter babListAdapter;
    private static final String TAG = "SelectBabQuiz";
    FirebaseFirestore db;
    Task<QuerySnapshot> collectionReference;

    private ArrayList<bablistmodel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bab_quiz);
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.babselectquizRecyclerView);
        babListAdapter = new BabListAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();

        recyclerView.setAdapter(babListAdapter);


        collectionReference = db.collection("quiz").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshots : queryDocumentSnapshots.getDocuments()){
                    Log.d(TAG, documentSnapshots.getId());
                    String babList = documentSnapshots.getId();
                    bablistmodel bablistmodel = new bablistmodel(babList);
                    list.add(bablistmodel);
                }
                babListAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Get Document Id Failed :" + e.getMessage());
            }
        });
    }
}
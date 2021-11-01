package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BabListEditKuis extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    private RecyclerView recyclerView;
    EditQuizAdapter editQuizAdapter;
    private final static String TAG = "BabListEditQUiz";
    FirebaseFirestore db;
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();
        recyclerView.setAdapter(editQuizAdapter);

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
/**
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String bablist = dataSnapshot1.child("babList").getValue(String.class);
                    bablistmodel bablistmodel = new bablistmodel(bablist);
                    list.add(bablistmodel);
                }
                editQuizAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
 **/

    }
}
package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
<<<<<<< HEAD
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
=======
>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
<<<<<<< HEAD
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
=======

>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditKuis extends AppCompatActivity {
    FirebaseFirestore db;
    Task<QuerySnapshot> documentReference;
    private static final String TAG = "EditQuizMainAct:";
    private RecyclerView recyclerView;
<<<<<<< HEAD
    SwipeRefreshLayout swipeRefreshLayout;
=======
>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b
    EditKuisMainAdapter editKuisMainAdapter;
    private String documentEditQuiz;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_kuis);
        recyclerView = findViewById(R.id.rvEditQuizMain);
        questions = new ArrayList<>();
<<<<<<< HEAD
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
=======
>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b
        editKuisMainAdapter = new EditKuisMainAdapter(questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        recyclerView.setAdapter(editKuisMainAdapter);
        documentEditQuiz = getIntent().getStringExtra("BabReference");
        Log.d(TAG, "BabReference : " + documentEditQuiz);

        documentReference = db.collection("quiz").document(documentEditQuiz).collection(documentEditQuiz)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot :queryDocumentSnapshots){
                            Question question = documentSnapshot.toObject(Question.class);
                            questions.add(question);

                            editKuisMainAdapter.notifyDataSetChanged();
                        }
                    }
                });

<<<<<<< HEAD
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                questions.clear();
                Log.d(TAG, " List Cleared, reloading data");
                documentReference = db.collection("quiz").document(documentEditQuiz).collection(documentEditQuiz)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot :queryDocumentSnapshots){
                                    Question question = documentSnapshot.toObject(Question.class);
                                    questions.add(question);

                                    editKuisMainAdapter.notifyDataSetChanged();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "refresh failed : " + e.getMessage());
                                Toast.makeText(swipeRefreshLayout.getContext(), "Refresh Failed!!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
=======
>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b
        Singleton babref = Singleton.getInstance();
        babref.setBabReference(documentEditQuiz);
    }
}
package com.example.islamdigitalecosystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditKuis extends AppCompatActivity {
    FirebaseFirestore db;
    Task<QuerySnapshot> documentReference;
    private static final String TAG = "EditQuizMainAct:";
    private RecyclerView recyclerView;
    EditKuisMainAdapter editKuisMainAdapter;
    private String documentEditQuiz;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_kuis);
        recyclerView = findViewById(R.id.rvEditQuizMain);
        questions = new ArrayList<>();
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

    }
}
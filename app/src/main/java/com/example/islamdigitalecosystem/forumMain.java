package com.example.islamdigitalecosystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class forumMain extends AppCompatActivity {

    private RecyclerView forumListView;
    private List<QuestionPost> forumList;
    private FirebaseFirestore firebaseFirestore;
    private ForumRecyclerAdapter forumRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_main);

        forumList = new ArrayList<>();
        forumListView = findViewById(R.id.blogListView);

        forumRecyclerAdapter = new ForumRecyclerAdapter(forumList);
        forumListView.setLayoutManager(new LinearLayoutManager(getApplication()));
        forumListView.setAdapter(forumRecyclerAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Post").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        QuestionPost questionPost = doc.getDocument().toObject(QuestionPost.class);
                        forumList.add(questionPost);

                        forumRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void addNewPost(View view) {
        startActivity(new Intent(forumMain.this, addNewPost.class));
    }
}

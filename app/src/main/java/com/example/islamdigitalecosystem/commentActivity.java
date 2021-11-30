package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class commentActivity extends AppCompatActivity {
    private EditText commentfield;
    private ImageView commentPostBtn;
    private String forumPostId;

    private RecyclerView commentListView;
    private List<Comments> comment_list;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private static final String TAG = "Comment Activity";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentfield = findViewById(R.id.commentField);
        commentPostBtn = findViewById(R.id.commentPostBtn);
        commentListView = findViewById(R.id.commentList);

        comment_list = new ArrayList<>();
        commentRecyclerAdapter = new CommentRecyclerAdapter(comment_list);
        commentListView.setHasFixedSize(true);
        commentListView.setLayoutManager(new LinearLayoutManager(this));
        commentListView.setAdapter(commentRecyclerAdapter);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        forumPostId =  getIntent().getStringExtra("forum_post_id");

        firebaseFirestore.collection("Post/" + forumPostId + "/Comments")
                .addSnapshotListener(commentActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED){
                            String commentId = doc.getDocument().getId();
                            Comments comments = doc.getDocument().toObject(Comments.class);
                            comment_list.add(comments);
                            commentRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }
        });
    }

    public void post(View view) {
        String commentMessage = commentfield.getText().toString();

        if (!commentMessage.isEmpty()){
            Map<String, Object> CommentsMap = new HashMap<>();
            CommentsMap.put("Message", commentMessage);
            CommentsMap.put("userID", firebaseUser.getDisplayName());
            CommentsMap.put("Timestamp", FieldValue.serverTimestamp());

            firebaseFirestore.collection("Post/" + forumPostId + "/Comments").add(CommentsMap)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(commentActivity.this, "Adding Comment Complete", Toast.LENGTH_LONG).show();
                        commentfield.setText("");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(commentActivity.this, "Adding Comment Failed : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Adding Comment Failed : " + e.getMessage());
                }
            });
        }
    }
}

package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addNewPost extends AppCompatActivity {
    EditText etPertanyaan;
    private static final String TAG = "AddNewPost : ";
    Button btAddPost;
    ProgressDialog postProgress;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        etPertanyaan = findViewById(R.id.etPertanyaan);
        btAddPost = findViewById(R.id.btPostQuestion);
        postProgress = new ProgressDialog(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    public void post(View view) {
        String question = etPertanyaan.getText().toString();
        if (!TextUtils.isEmpty(question)){
            postProgress.setMessage("Posting Question");
            postProgress.show();

            final Map<String, Object> postMap = new HashMap<>();
            postMap.put("Question", question);
            postMap.put("UserID", firebaseUser.getDisplayName());
            Log.d(TAG, "Display Name : " + firebaseUser.getDisplayName());
            postMap.put("TimeStamp", FieldValue.serverTimestamp());

            firebaseFirestore.collection("Post").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(addNewPost.this, "Pertanyaan Ditambahkan", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(addNewPost.this, forumMain.class));
                        finish();
                    }

                    postProgress.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addNewPost.this, "Menambahkan Pertanyaan Gagal " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Adding Question Failed" + e.getMessage());
                }
            });
        }else {
            postProgress.dismiss();
        }


    }
}

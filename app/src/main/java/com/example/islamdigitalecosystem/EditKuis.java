package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EditKuis extends AppCompatActivity {
    private static final String TAG = "Edit Kuis";
    FirebaseFirestore firebaseFirestore;
    CollectionReference question;
    DocumentReference documentReference;

    String babReference;

    private RecyclerView recyclerView;
    editKuisAdapter editKuisAdapter;
    private ArrayList<Question> editKuisList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_kuis);

        editKuisList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewEditKuis);

        editKuisAdapter = new editKuisAdapter(editKuisList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(editKuisAdapter);

        babReference = getIntent().getStringExtra("BabReference");

        editKuisAdapter.notifyDataSetChanged();
        }
    }

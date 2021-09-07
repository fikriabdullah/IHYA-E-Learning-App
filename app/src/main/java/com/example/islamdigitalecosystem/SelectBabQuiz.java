package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;

public class SelectBabQuiz extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    private RecyclerView recyclerView;
    BabListAdapter babListAdapter;
    private static final String TAG = "MyActivity";
    private ArrayList<bablistmodel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bab_quiz);
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.babselectquizRecyclerView);
        babListAdapter = new BabListAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("BabList");

        recyclerView.setAdapter(babListAdapter);

        AndroidNetworking.get("https://ihya-api.herokuapp.com/Quiz/fetchBabList")
                .setTag(this)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "response array length : " + response.length());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "fetch bablist error: " + anError.getCause());
                    }
                });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String bablist = dataSnapshot1.child("babList").getValue(String.class);
                    bablistmodel bablistmodel = new bablistmodel(bablist);
                    list.add(bablistmodel);
                }
                babListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
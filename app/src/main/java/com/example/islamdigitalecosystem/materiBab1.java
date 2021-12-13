package com.example.islamdigitalecosystem;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class materiBab1 extends AppCompatActivity {
 TextView textview;
 FirebaseFirestore db;
 Button dwnldPDF;
 TextView materiContent;
 String pdfDwnldUrl, contentMateri, DocRefMateri;
 private static final String TAG = "materiBab1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materi_bab1);
        textview =findViewById(R.id.text1);
        db = FirebaseFirestore.getInstance();
        dwnldPDF = findViewById(R.id.downloadPDF);

        DocRefMateri = getIntent().getStringExtra("babMateriRef");

        db.collection("Materi").document(DocRefMateri).collection(DocRefMateri)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    materiModelClass modelClass = documentSnapshot.toObject(materiModelClass.class);
                    contentMateri = modelClass.getMateriContent();
                    pdfDwnldUrl = modelClass.getFileDwnldUrl();
                    Log.d(TAG, "cntn1 : " + contentMateri + "\n" + "url1 : " + pdfDwnldUrl);
                        if (contentMateri.length() == 0 ){
                            //get pdf only, show default screen
                            Log.d(TAG, "pdf only available " + pdfDwnldUrl);
                            textview.setText(R.string.defaultScreen);
                            dwnldPDF.setVisibility(View.VISIBLE);
                            dwnldPDF.setClickable(true);
                        }else {
                            //check if pdf is null, => get pdf and text
                            if (pdfDwnldUrl != null ){
                                Log.d(TAG, "both available : " + pdfDwnldUrl);
                                textview.setText(contentMateri);
                                textview.setMovementMethod(new ScrollingMovementMethod());
                                dwnldPDF.setVisibility(View.VISIBLE);
                                dwnldPDF.setClickable(true);
                            }else {
                                Log.d(TAG, "pdf not available" + pdfDwnldUrl);
                                dwnldPDF.setVisibility(View.INVISIBLE);
                                dwnldPDF.setClickable(false);
                                textview.setText(contentMateri);
                                textview.setMovementMethod(new ScrollingMovementMethod());
                            }
                        }
                }
            }
        });

    }

    public void downloadPDF(View view) {

    }
}
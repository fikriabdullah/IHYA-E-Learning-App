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
        final Context context = super.getBaseContext();

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
                    try {
                        if (!contentMateri.equals(null) || !contentMateri.equals(" ")){
                            Log.d(TAG, "cntn : " + contentMateri + "\n" + "url : " + pdfDwnldUrl);
                            textview.setText(contentMateri);
                            textview.setMovementMethod(new ScrollingMovementMethod());
                            dwnldPDF.setVisibility(View.INVISIBLE);
                            dwnldPDF.setClickable(false);
                        }else if ((contentMateri.equals(null)||contentMateri.equals(" "))&&!pdfDwnldUrl.equals(null)){
                            Log.d(TAG, "cntn : " + contentMateri + "\n" + "url : " + pdfDwnldUrl);
                            dwnldPDF.setVisibility(View.VISIBLE);
                            dwnldPDF.setClickable(true);
                            textview.setText(R.string.please_dwnld);
                        }
                        else {
                            throw new Exception("Something Went Wrong..");
                        }
                    }catch (Exception e){
                        Toast.makeText(context, "Something Went Wrong ", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "cntn : " + contentMateri + "\n" + "url : " + pdfDwnldUrl);
                        Log.d(TAG, "error fetch data" + e.getMessage());
                    }
                }
            }
        });

    }

    public void downloadPDF(View view) {

    }
}
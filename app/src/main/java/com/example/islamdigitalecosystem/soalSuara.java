package com.example.islamdigitalecosystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class soalSuara extends AppCompatActivity {
    EditText pert, opt1, opt2, opt3, opt4, crAn, babQuiz;
    ImageView ivPick;
    Uri audioUri;
    ProgressBar progressUpload;
    private Button eRecordBtn;
    private MediaRecorder mRecorder;
    private String mFileName;
    private static final String LOG_TAG = "Record_log";
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private static final String TAG = "MyActivity";
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseFirestore firebaseFirestore;
    CollectionReference babRef;
    public static String babrefImp;
    int questionReady;
    int docRef;
    int aref;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soal_suara);
        pert = findViewById(R.id.etPert);
        opt1 = findViewById(R.id.etOpt1);
        opt2 = findViewById(R.id.etOpt2);
        opt3 = findViewById(R.id.etOpt3);
        opt4 = findViewById(R.id.etOpt4);
        crAn = findViewById(R.id.etCrOpt);
        babQuiz = findViewById(R.id.etBabQuiz);
        progressUpload = findViewById(R.id.progressBarUpQ);
        ivPick = findViewById(R.id.imgSelection);
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        eRecordBtn = findViewById(R.id.suara);
        mProgress = new ProgressDialog(this);
        aref = 1;
        eRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (ActivityCompat.checkSelfPermission(soalSuara.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(soalSuara.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(soalSuara.this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
                        ActivityCompat.requestPermissions(soalSuara.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                    } else {
                        startRecording();
                        v.performClick();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                    v.performClick();
                }
                return false;
            }
        });
    }

    public void getQuestionReady() { //ambil jumlah pertanyaan kalo ada babnya, buat di increment tiap nambahin ke bab yg sama
        babRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    questionReady = task.getResult().size() - 1;
                    questionReady++;
                    Log.d(TAG, "success question" + "bab : " + babQuiz.getText().toString() + " count : " + questionReady + "incrementing");
                    docRef = questionReady;
                    Log.d(TAG, "docref : " + docRef);// ini docref bisa 0
                } else {
                    Toast.makeText(com.example.islamdigitalecosystem.soalSuara.this, task.getException() + "", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Question Ready : " + questionReady + " ,making new collection " + task.getException());
                }
            }
        });
    }

    public void addQuestion2(View view) {
        int aRef = aref++;
        audioUri = Uri.fromFile(new File(file.getAbsolutePath()));
        if (audioUri != null) {
            mProgress.setMessage("Uploading Audio.....");
            mProgress.show();
            StorageReference filepath = mStorage.child("Audio").child(aRef + ".3gp");
            Log.d(TAG, "Dir : " + file.getAbsolutePath());
            filepath.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Upload Voice Error : " + e.getMessage());
                    Toast.makeText(soalSuara.this, "Uploading Voice Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            Question question = new Question();
            question.setOpt1(opt1.getText().toString().trim());
            question.setOpt2(opt2.getText().toString().trim());
            question.setOpt3(opt3.getText().toString().trim());
            question.setOpt4(opt4.getText().toString().trim());
            question.setCrAnswer(crAn.getText().toString().trim());

            babrefImp = babQuiz.getText().toString();
            babRef = firebaseFirestore.collection(babrefImp);//taruh pertanyaan sesuai bab
            getQuestionReady();
            babRef.document("Question" + docRef)//ini harusnya kalo si user upload di babRef yg sama dia increment. tapi kalo gak, dia reset dari 0
                    .set(question).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(com.example.islamdigitalecosystem.soalSuara.this, "Uploading Question Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Silahkan Rekam Audio", Toast.LENGTH_LONG).show();
        }

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        try {
            file = File.createTempFile("audio", "wav");
            mRecorder.setOutputFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
            Log.d(TAG, "Recording Started ");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Recording Failed : " + e.getMessage());
        }


    }

    private void stopRecording() {
        mRecorder.stop();
        Log.d(TAG, "Recording Stopped");
        mRecorder.release();

    }


}


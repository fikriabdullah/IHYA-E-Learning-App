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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class soalSuara extends AppCompatActivity {
    EditText pert, opt1, opt2, opt3, opt4, crAn, babQuiz;
    ImageView ivPick;
    Uri audioUri;
    Uri audioDwnldUrl;
    int docRef;
    ProgressBar progressUpload;
    private Button eRecordBtn;
    private MediaRecorder mRecorder;
    private static final String LOG_TAG = "Record_log";
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private static final String TAG = "MyActivity";
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CollectionReference babRef;
    public static String babrefImp;
    int questionReady;
    public int DOCREF;
    int aRef;
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        eRecordBtn = findViewById(R.id.suara);
        mProgress = new ProgressDialog(this);

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
                        questionReady();
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


    public void questionReady() { //ambil jumlah pertanyaan kalo ada babnya, buat di increment tiap nambahin ke bab yg sama
        AndroidNetworking.get("https://ihya-api.herokuapp.com/Quiz/readQuestion/{bab}")
                .setPriority(Priority.MEDIUM)
                .setTag("test")
                .addPathParameter("bab", babQuiz.getText().toString())
                .build()
                .getAsObjectList(Question.class, new ParsedRequestListener<List<Question>>() {
                    @Override
                    public void onResponse(List<Question> questions) {
                        Log.d(TAG, "Question Ready :"+ questions.size());
                        questionReady = questions.size();
                        questionReady++;
                        docRef = questionReady;
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "API Request Errror : " +anError.getCause());
                    }
                });
    }

    public void addQuestion2(View view) {
        audioUri = Uri.fromFile(new File(file.getAbsolutePath()));
        if (audioUri != null) {
            mProgress.setMessage("Uploading Audio.....");
            mProgress.show();
            Log.d(TAG, "Audio File Name : " + file.getName());
            final StorageReference filepath = mStorage.child("Audio").child(file.getName());
            Log.d(TAG, "aRef : " + aRef);
            Log.d(TAG, "Dir : " + file.getAbsolutePath());
            filepath.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Question question = new Question();
                            audioDwnldUrl = uri;
                            Log.d(TAG, "Audio Dwnld URL : " + audioDwnldUrl);
                            question.setAudioDwnldUrl(audioDwnldUrl.toString());
                            question.setOpt1(opt1.getText().toString().trim());
                            question.setOpt2(opt2.getText().toString().trim());
                            question.setOpt3(opt3.getText().toString().trim());
                            question.setOpt4(opt4.getText().toString().trim());
                            question.setCrAnswer(crAn.getText().toString().trim());
                            babrefImp = babQuiz.getText().toString().toLowerCase().trim();
                            Log.d(TAG, "Doc ref : " + questionReady);
                            babRef = firebaseFirestore.collection("quiz");//taruh pertanyaan sesuai bab
                            DocumentReference babRefDummy = babRef.document(babrefImp);
                            Map<String, Object> dummyMapping = new HashMap<>();
                            dummyMapping.put("dummy", "dummy");
                            babRefDummy.set(dummyMapping);
                            babRef.document(babrefImp).collection(babrefImp).document("Question"+docRef)
                                    .set(question).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(com.example.islamdigitalecosystem.soalSuara.this, "Uploading Question Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Audio Dwnld URL eror : " + Arrays.toString(e.getStackTrace()));
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Upload Voice Error : " + e.getMessage());
                    Toast.makeText(soalSuara.this, "Uploading Voice Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
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


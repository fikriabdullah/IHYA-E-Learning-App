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
        Uri imageSelectUri;
        ProgressBar progressUpload;
        private Button eRecordBtn;
        private MediaRecorder mRecorder;
        private String mFileName = null;
        private static final String LOG_TAG ="Record_log";
        private StorageReference mStorage;
        private ProgressDialog mProgress;
        private static final String TAG = "MyActivity";
        private static final int PICK_IMAGE_REQUEST = 1;
        FirebaseFirestore firebaseFirestore;
        CollectionReference babRef;
        public static String babrefImp;
        int questionReady;
        int docRef;
        int iref;

        @SuppressLint("ClickableViewAccessibility")
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
            mFileName +="/recorded_audio_3gp";
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();

            iref = 1;
            eRecordBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        if(ActivityCompat.checkSelfPermission(soalSuara.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(soalSuara.this, new String[]{Manifest.permission.RECORD_AUDIO},10);
                        }else {
                            startRecording();
                        }


                    } else if (event.getAction() == MotionEvent.ACTION_UP){
                        stopRecording();
                    }
                    return false;
                }
            });
        }

        public void pickImage2(View view) {
            openImageExplorer();
        }

        private void openImageExplorer() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                imageSelectUri = data.getData();
                Picasso.with(this).load(imageSelectUri).fit().into(ivPick);
            }
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
            int iRef = iref++;
            if (imageSelectUri != null) {
                final StorageReference imageReference = FirebaseStorage.getInstance().getReference().child(iRef++ + "");//probably fixed, need testingpik
                Log.d(TAG, "Image Ref : " + iRef);
                imageReference.putFile(imageSelectUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressUpload.setProgress(0);
                            }
                        }, 500);
                        Question question = new Question();
                        question.setImgDwnldUrl(imageReference.getDownloadUrl().toString());//Error, need testing getDownloadUrl tu hasilnya uri bukan url
                        //gakepake, kita pake get image (disimpen file temp terus di decode see getquestionimage
                        question.setQuestion(pert.getText().toString().trim());
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
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(com.example.islamdigitalecosystem.soalSuara.this, "Uploading Image Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Uploading Image Error : " + e.getMessage());
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressUpload.setProgress((int) progress);
                    }
                });
            } else {
                Toast.makeText(this, "Silahkan Pilih Gambar", Toast.LENGTH_SHORT).show();
            }

        }
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }


    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();

        uploadAudio();
    }

    private void uploadAudio() {
            mProgress.setMessage("Uploading Audio.....");
            mProgress.show();
            StorageReference filepath = mStorage.child("Audio").child("new_audio.3gp");
            Uri uri = Uri.fromFile(new File(mFileName));
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();
                }
            });
    }


}


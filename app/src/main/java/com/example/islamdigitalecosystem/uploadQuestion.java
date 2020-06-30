package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class uploadQuestion extends AppCompatActivity {
    EditText pert, opt1, opt2, opt3, opt4, crAn, babQuiz;
    ImageView ivPick;
    Uri imageSelectUri;
    ProgressBar progressUpload;
    private static final String TAG = "MyActivity";
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CollectionReference babRef;
    public static String babrefImp;
    int questionReady;
    int docRef;
    int iRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_question);
        pert = findViewById(R.id.etPert);
        opt1 = findViewById(R.id.etOpt1);
        opt2 = findViewById(R.id.etOpt2);
        opt3 = findViewById(R.id.etOpt3);
        opt4 = findViewById(R.id.etOpt4);
        crAn = findViewById(R.id.etCrOpt);
        babQuiz = findViewById(R.id.etBabQuiz);
        progressUpload = findViewById(R.id.progressBarUpQ);
        ivPick = findViewById(R.id.imgSelection);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //iref = 1;
    }

    public void pickImage(View view) {
        openImageExplorer();
        saveImageCount();
        getQuestionReady();
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

    public void getQuestionReady(){ //ambil jumlah pertanyaan kalo ada babnya, buat di increment tiap nambahin ke bab yg sama
        babRef = firebaseFirestore.collection(babQuiz.getText().toString());
        babRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    questionReady = task.getResult().size();
                    Log.d(TAG, "success question" +"bab : " + babQuiz.getText().toString() + " count : " + questionReady + " incrementing");
                    questionReady++;
                    docRef = questionReady;
                    Log.d(TAG, "docref : " + docRef);
                }else{
                    Toast.makeText(uploadQuestion.this, task.getException() + "", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Question Ready : " + questionReady + " ,making new collection " + task.getException());
                }
            }
        });
    }

    public void saveImageCount() {
        databaseReference = firebaseDatabase.getReference("Asset Count ").child("image");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Log.d(TAG, "image count : " + dataSnapshot.getValue());
                    iRef = 1;
                    databaseReference.setValue(iRef);
                    Log.d(TAG, "image count after add : " + dataSnapshot.getValue());
                } else {
                    Log.d(TAG, "image count not null : " + dataSnapshot.getValue());
                    int imageCount = Integer.parseInt(dataSnapshot.getValue().toString());
                    Log.d(TAG, "imageCount :  " + imageCount);
                    imageCount++;
                    iRef = imageCount;
                    Log.d(TAG, "iref new : " + iRef);
                    databaseReference.setValue(iRef);
                    Log.d(TAG, "image count not null after add : " + dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addQuestion(View view) {
        if (imageSelectUri != null){
            final StorageReference imageReference = FirebaseStorage.getInstance().getReference().child(iRef + "");//probably fixed, need testingpik
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
                    Log.d(TAG, "Doc Ref : " + docRef);
                    babRef.document("Question" + docRef)//ini harusnya kalo si user upload di babRef yg sama dia increment. tapi kalo gak, dia reset dari 0
                            .set(question).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(uploadQuestion.this, "Uploading Question Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(uploadQuestion.this, "Uploading Image Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Uploading Image Error : " + e.getMessage() );
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressUpload.setProgress((int) progress);
                }
            });
        }else {
            Toast.makeText(this, "Silahkan Pilih Gambar", Toast.LENGTH_SHORT).show();
        }

    }
}

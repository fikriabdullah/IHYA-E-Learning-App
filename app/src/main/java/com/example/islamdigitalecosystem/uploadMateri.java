package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class uploadMateri extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST_CODE =1;
    private Uri pdfSelectUri, pdfUriTemp;
    private static final String TAG = "uploadMateri";
    private
    File file;
    CollectionReference collectionReference;
    EditText materiField, jdulField;
    String fileName, pdfDwnldUrl;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_materi);
        materiField = findViewById(R.id.editText);
        jdulField = findViewById(R.id.etBabMateri);
        db = FirebaseFirestore.getInstance();
    }

    public void pushMateri(final View view) {
        try {
<<<<<<< HEAD
            if (jdulField.getText().toString().equals(null)){
                throw new NullPointerException("Please Fill The Bab Field!!");
            }else {
                try {
                    if (materiField.getText().toString().equals(null) && pdfSelectUri==null ||
=======
            if (jdulField.getText().toString().trim().equals(null)){
                throw new NullPointerException("Please Fill The Bab Field!!");
            }else {
                try {
                    if (materiField.getText().toString().trim().equals(null) && pdfSelectUri==null ||
>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b
                            materiField.getText().toString().equals("") && pdfSelectUri==null){
                            throw new NullPointerException("Please Fill Materi Form Or Selec A PDF File");
                    }else {
                        Log.d(TAG, "BabRefer:" + jdulField.getText().toString());
<<<<<<< HEAD
                        if (pdfSelectUri != null && materiField.getText().toString().equals(null)) {
=======
                        if (pdfSelectUri != null && materiField.getText().toString().trim().equals(null)) {
>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b
                            final StorageReference pdfReference = FirebaseStorage.getInstance().getReference().child("fileMateri").child(fileName);
                            pdfReference.putFile(pdfSelectUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    pdfReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pdfDwnldUrl = uri.toString();
                                            Log.d(TAG, "pdf dwnld url " + pdfDwnldUrl);
                                            materiModelClass modelClass = new materiModelClass();
                                            modelClass.setFileDwnldUrl(pdfDwnldUrl);

                                            collectionReference = db.collection("Materi");
                                            DocumentReference babRefDummy = collectionReference.document(jdulField.getText().toString());
                                            Map<String, Object> dummyMapping = new HashMap<>();
                                            dummyMapping.put("dummy", "dummy");
                                            babRefDummy.set(dummyMapping);
                                            collectionReference.document(jdulField.getText().toString()).collection(jdulField.getText().toString())
                                                    .document(jdulField.getText().toString()).set(modelClass)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "Materi Uploaded");
                                                            Toast.makeText(view.getContext(), "Materi uploaded!!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "Upload Materi failed " + e.getMessage());
                                                    Toast.makeText(view.getContext(), "Upload Materi failed", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Get Download Url PDF failed " + e.getMessage());
                                            Toast.makeText(view.getContext(), "Get Download Url PDF failed", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Uploading PDF failed " + e.getMessage());
                                    Toast.makeText(view.getContext(), "Uploading PDF failed", Toast.LENGTH_LONG).show();
                                }
                            });
<<<<<<< HEAD
                        } else if (pdfSelectUri == null && !materiField.getText().toString().equals(null)) {
                            materiModelClass modelClass = new materiModelClass();
                            modelClass.setMateriContent(materiField.getText().toString());
=======
                        } else if (pdfSelectUri == null && !materiField.getText().toString().trim().equals(null)) {
                            materiModelClass modelClass = new materiModelClass();
                            modelClass.setMateriContent(materiField.getText().toString().trim());
>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b
                            collectionReference = db.collection("Materi");
                            DocumentReference babRefDummy = collectionReference.document(jdulField.getText().toString());
                            Map<String, Object> dummyMapping = new HashMap<>();
                            dummyMapping.put("dummy", "dummy");
                            babRefDummy.set(dummyMapping);
                            collectionReference.document(jdulField.getText().toString()).collection(jdulField.getText().toString())
                                    .document(jdulField.getText().toString()).set(modelClass)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Materi Uploaded");
                                            Toast.makeText(view.getContext(), "Materi uploaded!!", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Upload Materi failed " + e.getMessage());
                                    Toast.makeText(view.getContext(), "Upload Materi failed", Toast.LENGTH_LONG).show();
                                }
                            });

<<<<<<< HEAD
                        }else if (pdfSelectUri != null && !jdulField.getText().toString().equals(null) ){
=======
                        }else if (pdfSelectUri != null && !jdulField.getText().toString().trim().equals(null) ){
>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b
                            final StorageReference pdfReference = FirebaseStorage.getInstance().getReference().child("fileMateri").child(fileName);
                            pdfReference.putFile(pdfSelectUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    pdfReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pdfDwnldUrl = uri.toString();
                                            Log.d(TAG, "pdf dwnld url " + pdfDwnldUrl);

                                            materiModelClass modelClass = new materiModelClass();
                                            modelClass.setFileDwnldUrl(pdfDwnldUrl);
<<<<<<< HEAD
                                            modelClass.setMateriContent(materiField.getText().toString());
=======
                                            modelClass.setMateriContent(materiField.getText().toString().trim());
>>>>>>> bfe16d6cce04ec4d17082ab3cd813ad3f359d98b

                                            collectionReference = db.collection("Materi");
                                            DocumentReference babRefDummy = collectionReference.document(jdulField.getText().toString());
                                            Map<String, Object> dummyMapping = new HashMap<>();
                                            dummyMapping.put("dummy", "dummy");
                                            babRefDummy.set(dummyMapping);
                                            collectionReference.document(jdulField.getText().toString()).collection(jdulField.getText().toString())
                                                    .document(jdulField.getText().toString()).set(modelClass)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "Materi Uploaded");
                                                            Toast.makeText(view.getContext(), "Materi uploaded!!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "Upload Materi failed " + e.getMessage());
                                                    Toast.makeText(view.getContext(), "Upload Materi failed", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Get Download Url PDF failed " + e.getMessage());
                                            Toast.makeText(view.getContext(), "Get Download Url PDF failed", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Uploading PDF failed " + e.getMessage());
                                    Toast.makeText(view.getContext(), "Uploading PDF failed", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else {
                            Toast.makeText(this, "Please Select a PDF file or fill the field Above!!", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "No Materi to upload" + pdfSelectUri + materiField.getText().toString());
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(this, "Please Select a PDF file or fill the field Above!!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Materi and pdf uri empty!!" + materiField.getText().toString() + pdfSelectUri);
                }

            }
        }catch (Exception e){
            Toast.makeText(this, "Please Fill The Bab Field!!", Toast.LENGTH_LONG).show();
        }
    }


    public void pickDocument(View view) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            pdfSelectUri = data.getData();
            pdfUriTemp = pdfSelectUri;
            try {
                file = new File(pdfSelectUri.getPath());
                fileName = file.getName();
                Log.d(TAG, "filename : " + fileName);

            }catch (Exception e){
                Log.d(TAG, "filename error: " + e.getMessage());
                Toast.makeText(this, "Error Getting filename", Toast.LENGTH_LONG).show();
            }
        }
    }

}


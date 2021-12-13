package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
    File file;
    TextView tvHargaMateri;
    CollectionReference collectionReference;
    EditText materiField, jdulField, hargaMateri;
    String fileName, pdfDwnldUrl;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    boolean isBerbayar;
    CheckBox isPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_materi);
        materiField = findViewById(R.id.editText);
        jdulField = findViewById(R.id.etBabMateri);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        isPaid = findViewById(R.id.isPaidMateri);
        hargaMateri = findViewById(R.id.etHargaMateri);
        tvHargaMateri = findViewById(R.id.tvHargaMateri);
        hargaMateri.setVisibility(View.INVISIBLE);
        tvHargaMateri.setVisibility(View.INVISIBLE);

        isPaid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    hargaMateri.setVisibility(View.VISIBLE);
                    tvHargaMateri.setVisibility(View.VISIBLE);
                    isBerbayar = compoundButton.isChecked();
                }else if (!(compoundButton.isChecked())){
                    hargaMateri.setVisibility(View.INVISIBLE);
                    tvHargaMateri.setVisibility(View.INVISIBLE);
                    isBerbayar = compoundButton.isChecked();
                }
            }
        });
    }

    public void pushMateri(final View view) {
        try {
            if (jdulField.getText().toString().equals(null)) {
                throw new NullPointerException("Please Fill The Bab Field!!");
            } else {
                try {
                    if (materiField.getText().toString().equals(null) && pdfSelectUri == null ||
                            materiField.getText().toString().equals("") && pdfSelectUri == null) {
                        throw new NullPointerException("Please Fill Materi Form Or Selec A PDF File");
                    } else {
                        Log.d(TAG, "BabRefer:" + jdulField.getText().toString());
                        if (pdfSelectUri != null && materiField.getText().toString().equals(null)) {
                            //materi ga, pdf ada
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
                                            modelClass.setAuthor(firebaseAuth.getCurrentUser().getDisplayName());
                                            modelClass.setPaidmateri(isBerbayar);
                                            modelClass.setMateriContent(null);
                                            if (hargaMateri.getText().toString().equals(null)
                                                    || hargaMateri.getText().toString().equals("")){
                                                modelClass.setHargaMateri(null);
                                            }else {
                                                Double hrgMtr = Double.parseDouble(hargaMateri.getText().toString());
                                                modelClass.setHargaMateri(hrgMtr);
                                            }

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
                        } else if (pdfSelectUri == null && !materiField.getText().toString().equals(null)) {
                            //pdf ga, materi ada
                            materiModelClass modelClass = new materiModelClass();
                            modelClass.setMateriContent(materiField.getText().toString());
                            modelClass.setAuthor(firebaseAuth.getCurrentUser().getDisplayName());
                            modelClass.setPaidmateri(isBerbayar);
                            if (hargaMateri.getText().toString().equals(null)
                                    || hargaMateri.getText().toString().equals("")){
                                modelClass.setHargaMateri(null);
                            }else {
                                Double hrgMtr = Double.parseDouble(hargaMateri.getText().toString());
                                modelClass.setHargaMateri(hrgMtr);
                            }

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

                        } else if (pdfSelectUri != null && !jdulField.getText().toString().equals(null)) {
                            //both ada
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
                                            modelClass.setMateriContent(materiField.getText().toString());
                                            modelClass.setFileDwnldUrl(pdfDwnldUrl);
                                            modelClass.setAuthor(firebaseAuth.getCurrentUser().getDisplayName());
                                            modelClass.setPaidmateri(isBerbayar);
                                            if (hargaMateri.getText().toString().equals(null)
                                                    || hargaMateri.getText().toString().equals("")){
                                                modelClass.setHargaMateri(null);
                                            }else {
                                                Double hrgMtr = Double.parseDouble(hargaMateri.getText().toString());
                                                modelClass.setHargaMateri(hrgMtr);
                                            }

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
                        } else {
                            Toast.makeText(this, "Please Select a PDF file or fill the field Above!!", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "No Materi to upload" + pdfSelectUri + materiField.getText().toString());
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Please Select a PDF file or fill the field Above!!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Materi and pdf uri empty!!" + materiField.getText().toString() + pdfSelectUri);
                }

            }
        } catch (Exception e) {
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


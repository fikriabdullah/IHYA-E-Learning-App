package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class editMateriMain extends AppCompatActivity {
    private Button saveChange, changeDocument;
    private CheckBox cbIsPaid;
    private EditText EditcontentMateri, EtHargaMateri;
    private TextView tvHargaMateri;
    private static final String TAG ="editMateriMain";
    String pdfDwnldUrl, contentMateri, DocRefMateri;
    FirebaseFirestore db;
    Boolean isPaid;
    String HargaMateri;
    Double hargaMateri;
    FirebaseAuth firebaseAuth;
    CollectionReference collectionReference;
    File file;
    private static final int PICK_FILE_REQUEST_CODE =1;
    private Uri pdfSelectUri;
    private Uri pdfUriTemp;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_materi_main);
        saveChange = findViewById(R.id.saveChange);
        cbIsPaid = findViewById(R.id.edtisPaidMateri);
        changeDocument = findViewById(R.id.ChangeDocument);
        EditcontentMateri = findViewById(R.id.editMateriContent);
        EtHargaMateri = findViewById(R.id.etedHargaMateri);
        tvHargaMateri = findViewById(R.id.tvedHargaMateri);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        cbIsPaid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    EtHargaMateri.setVisibility(View.VISIBLE);
                    tvHargaMateri.setVisibility(View.VISIBLE);
                    isPaid = compoundButton.isChecked();
                }else if (!(compoundButton.isChecked())){
                    EtHargaMateri.setVisibility(View.INVISIBLE);
                    tvHargaMateri.setVisibility(View.INVISIBLE);
                    isPaid = compoundButton.isChecked();
                }
            }
        });

        DocRefMateri = getIntent().getStringExtra("babMateriRef");
        Log.d(TAG, "babMateri"+DocRefMateri);

        db.collection("Materi").document(DocRefMateri).collection(DocRefMateri)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    materiModelClass modelClass = documentSnapshot.toObject(materiModelClass.class);
                    contentMateri = modelClass.getMateriContent();
                    pdfDwnldUrl = modelClass.getFileDwnldUrl();
                    isPaid = modelClass.isPaidmateri();
                    hargaMateri = modelClass.getHargaMateri();
                    Log.d(TAG, "isPaid" + isPaid);
                    Log.d(TAG, "cntn1 : " + contentMateri + "\n" + "url1 : " + pdfDwnldUrl);
                    if (contentMateri.length() == 0 ){ //check if materi content is null, => hide pdf download
                        //get pdf only, show default screen
                        if (isPaid){
                            //materi berbayar, check box, show edit harga
                            cbIsPaid.setVisibility(View.VISIBLE);
                            cbIsPaid.setChecked(true);
                            tvHargaMateri.setVisibility(View.VISIBLE);
                            EtHargaMateri.setVisibility(View.VISIBLE);
                            HargaMateri = String.valueOf(hargaMateri);
                            EtHargaMateri.setText(HargaMateri);
                        }else {
                            //materi gratis,
                            cbIsPaid.setVisibility(View.VISIBLE);
                            cbIsPaid.setChecked(false);
                            tvHargaMateri.setVisibility(View.INVISIBLE);
                            EtHargaMateri.setVisibility(View.INVISIBLE);

                        }
                        Log.d(TAG, "pdf only available " + pdfDwnldUrl);
                        EditcontentMateri.setText(R.string.materi_not_available);
                        changeDocument.setVisibility(View.VISIBLE);
                        changeDocument.setClickable(true);
                    }else {//materi is not null
                        //check if pdf is null, => get pdf and text
                        if (pdfDwnldUrl != null ){
                            if (isPaid){
                                //materi berbayar, check box, show edit harga
                                cbIsPaid.setVisibility(View.VISIBLE);
                                cbIsPaid.setChecked(true);
                                tvHargaMateri.setVisibility(View.VISIBLE);
                                EtHargaMateri.setVisibility(View.VISIBLE);
                                HargaMateri = String.valueOf(hargaMateri);
                                EtHargaMateri.setText(HargaMateri);
                            }else {
                                //materi gratis,
                                cbIsPaid.setVisibility(View.VISIBLE);
                                cbIsPaid.setChecked(false);
                                tvHargaMateri.setVisibility(View.INVISIBLE);
                                EtHargaMateri.setVisibility(View.INVISIBLE);
                            }

                            Log.d(TAG, "both available : " + pdfDwnldUrl);
                            EditcontentMateri.setText(contentMateri);
                            EditcontentMateri.setMovementMethod(new ScrollingMovementMethod());
                            changeDocument.setVisibility(View.VISIBLE);
                            changeDocument.setClickable(true);
                        }else {
                            if (isPaid){
                                //materi berbayar, check box, show edit harga
                                cbIsPaid.setVisibility(View.VISIBLE);
                                cbIsPaid.setChecked(true);
                                tvHargaMateri.setVisibility(View.VISIBLE);
                                EtHargaMateri.setVisibility(View.VISIBLE);
                                HargaMateri = String.valueOf(hargaMateri);
                                EtHargaMateri.setText(HargaMateri);
                            }else {
                                //materi gratis,
                                cbIsPaid.setVisibility(View.VISIBLE);
                                cbIsPaid.setChecked(false);
                                tvHargaMateri.setVisibility(View.INVISIBLE);
                                EtHargaMateri.setVisibility(View.INVISIBLE);
                            }
                            Log.d(TAG, "pdf not available" + pdfDwnldUrl);
                            changeDocument.setVisibility(View.INVISIBLE);
                            changeDocument.setClickable(false);
                            EditcontentMateri.setText(contentMateri);
                            EditcontentMateri.setMovementMethod(new ScrollingMovementMethod());
                        }
                    }
                }
            }
        });

    }

    public void SaveChange(final View view) {
        if (pdfSelectUri != null && EditcontentMateri.getText().toString().equals(null)) {
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
                            modelClass.setPaidmateri(isPaid);
                            modelClass.setMateriContent(null);
                            if (EtHargaMateri.getText().toString().equals(null)
                                    || EtHargaMateri.getText().toString().equals("")){
                                modelClass.setHargaMateri(null);
                            }else {
                                Double hrgMtr = Double.parseDouble(EtHargaMateri.getText().toString());
                                modelClass.setHargaMateri(hrgMtr);
                            }

                            collectionReference = db.collection("Materi");
                            DocumentReference babRefDummy = collectionReference.document(DocRefMateri);
                            Map<String, Object> dummyMapping = new HashMap<>();
                            dummyMapping.put("dummy", "dummy");
                            babRefDummy.set(dummyMapping);
                            collectionReference.document(DocRefMateri).collection(DocRefMateri)
                                    .document(DocRefMateri).set(modelClass)
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
        } else if (pdfSelectUri == null && !EditcontentMateri.getText().toString().equals(null)) {
            //pdf ga, materi ada
            materiModelClass modelClass = new materiModelClass();
            modelClass.setMateriContent(EditcontentMateri.getText().toString());
            modelClass.setAuthor(firebaseAuth.getCurrentUser().getDisplayName());
            modelClass.setPaidmateri(isPaid);
            if (EtHargaMateri.getText().toString().equals(null)
                    || EtHargaMateri.getText().toString().equals("")){
                modelClass.setHargaMateri(null);
            }else {
                Double hrgMtr = Double.parseDouble(EtHargaMateri.getText().toString());
                modelClass.setHargaMateri(hrgMtr);
            }

            collectionReference = db.collection("Materi");
            DocumentReference babRefDummy = collectionReference.document(DocRefMateri);
            Map<String, Object> dummyMapping = new HashMap<>();
            dummyMapping.put("dummy", "dummy");
            babRefDummy.set(dummyMapping);
            collectionReference.document(DocRefMateri).collection(DocRefMateri)
                    .document(DocRefMateri).set(modelClass)
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

        } else if (pdfSelectUri != null && !DocRefMateri.equals(null)) {
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
                            modelClass.setMateriContent(EditcontentMateri.getText().toString());
                            modelClass.setFileDwnldUrl(pdfDwnldUrl);
                            modelClass.setAuthor(firebaseAuth.getCurrentUser().getDisplayName());
                            modelClass.setPaidmateri(isPaid);
                            if (EtHargaMateri.getText().toString().equals(null)
                                    || EtHargaMateri.getText().toString().equals("")){
                                modelClass.setHargaMateri(null);
                            }else {
                                Double hrgMtr = Double.parseDouble(EtHargaMateri.getText().toString());
                                modelClass.setHargaMateri(hrgMtr);
                            }

                            collectionReference = db.collection("Materi");
                            DocumentReference babRefDummy = collectionReference.document(DocRefMateri);
                            Map<String, Object> dummyMapping = new HashMap<>();
                            dummyMapping.put("dummy", "dummy");
                            babRefDummy.set(dummyMapping);
                            collectionReference.document(DocRefMateri).collection(DocRefMateri)
                                    .document(DocRefMateri).set(modelClass)
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
        }
    }

    public void changeDocument(View view) {
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
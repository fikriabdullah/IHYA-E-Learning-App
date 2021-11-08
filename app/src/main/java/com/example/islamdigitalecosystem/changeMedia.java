package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class changeMedia extends AppCompatActivity {
    private Button saveMedia, changeImg, changeAudio;
    MediaPlayer mediaPlayer;
    private ImageView mediaPlaceHld;
    private String getAudioDwnldUrl, getImgDwnldUrl, BabReference;
    private Singleton babref;
    int getDocumentRef, documentReferences;
    private MediaRecorder mRecorder;
    Uri imageSelectUri, imageSelectUriTemp, imgDownloadUrl;
    Uri audioRecordUri, AudioRecordUriNew;
    private static final String TAG = "changeMedia";
    private File file;
    FirebaseFirestore db;
    StorageReference storage;
    private static final String RECORD_LOG = "recordLog : ";
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_media);
        saveMedia = findViewById(R.id.btSaveMedia);
        changeImg = findViewById(R.id.changeImage);
        changeAudio = findViewById(R.id.changeAudio);
        mediaPlaceHld = findViewById(R.id.mediaPlaceHld);
        getAudioDwnldUrl = getIntent().getStringExtra("AudioDwnldUrl");
        getImgDwnldUrl = getIntent().getStringExtra("imgDwnldUrl");
        getDocumentRef = getIntent().getIntExtra("documentRef", 0);
        babref = Singleton.getInstance();
        BabReference = babref.getBabReference();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        loadOldMedia();
        Log.d(TAG, "Intent Extras : " + getImgDwnldUrl + "\n" + getDocumentRef +"\n" +getAudioDwnldUrl+"\n"+BabReference);
        documentReferences = getDocumentRef + 1;
    }
    public void loadOldMedia(){
        if (getAudioDwnldUrl != null ){
            changeImg.setVisibility(View.INVISIBLE);
            mediaPlaceHld.setClickable(true);
        }else if (getImgDwnldUrl != null){
            changeAudio.setVisibility(View.INVISIBLE);
            mediaPlaceHld.setClickable(false);
            ImageView ivBasicImage = (ImageView) findViewById(R.id.mediaPlaceHld);
            Picasso.with(changeMedia.this).load(getImgDwnldUrl).into(ivBasicImage);
        }
    }

    public void playAudio(View view) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(getAudioDwnldUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException e){
            Log.d(TAG, "Media Player error : " + e.getCause());
        }
    }

    public void saveMedia(View view) {
        //findout how to get babref w/o using intent
        //update new Media uri to firebase frstr and frbase strg !!WARNING ADD THE DOCREF +1
        //if success, send back to edit quiz main page
        final Context context = view.getContext();
        if (getAudioDwnldUrl != null){
            //save to field audiodwnldurl on firebase
            if (audioRecordUri != null){
                final StorageReference filePath = storage.child("Audio").child(file.getName());
                Log.d(TAG, "documentref : " + documentReferences);
                filePath.putFile(audioRecordUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                AudioRecordUriNew = uri;
                                Log.d(TAG, "new Audio Dwnld Url : " + AudioRecordUriNew);
                                db.collection("quiz").document(BabReference).collection(BabReference)
                                        .document(String.valueOf("Question"+documentReferences)).update("audioDwnldUrl", AudioRecordUriNew.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Audio downld url updated ");
                                                Intent intent = new Intent(changeMedia.this, EditKuis.class);
                                                intent.putExtra("BabReference", BabReference);
                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Update Media Failed!!"+ e.getMessage());
                                        Toast.makeText(context, "Update Media Failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Get Dwnld URL failed " + e.getMessage());
                                Toast.makeText(context, "Get Download Media Failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Put audio file failed " + e.getMessage());
                        Toast.makeText(context, "Upload audio File Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }else {
                Toast.makeText(this, "Tekan Change Media Untuk Mengupdate Soal Suara", Toast.LENGTH_LONG).show();
            }

        }else if (getImgDwnldUrl != null){
            //save to field imgdwnldurl on firbase
            if (imageSelectUri != null){
                double imageName = Math.random();
                final String imgName = String.valueOf(imageName);
                final StorageReference imgReference = FirebaseStorage.getInstance().getReference().child("image").child(imgName);
                Log.d(TAG, "documentref : " + documentReferences);
                imgReference.putFile(imageSelectUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imgDownloadUrl = uri;
                                Log.d(TAG, "new image dwnld url" + imgDownloadUrl);
                                db.collection("quiz").document(BabReference).collection(BabReference)
                                        .document(String.valueOf("Question"+documentReferences)).update("imgDwnldUrl", imgDownloadUrl.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Img dwnld Url updated ");
                                                Intent intent = new Intent(changeMedia.this, EditKuis.class);
                                                intent.putExtra("BabReference", BabReference);
                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Udate Dwnld Img url failed"+e.getMessage());
                                        Toast.makeText(context, "Update Media Failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "get img dwnld url failed"+e.getMessage());
                                Toast.makeText(context, "get Download URL Media Failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Put image failed" + e.getMessage());
                        Toast.makeText(context, "Upload Image Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
            }else {
                Toast.makeText(this, "Tekan Change Image Untuk Pilih Gambar", Toast.LENGTH_LONG).show();
            }
    }

    public void changeImage(View view) {
        //pick image, get uri
        openImageExplorer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageSelectUri = data.getData();
            imageSelectUriTemp = imageSelectUri;
            Picasso.with(this).load(imageSelectUri).fit().into(mediaPlaceHld);
            Log.d(TAG, "media uri : " + imageSelectUri);
        }
    }

    private void openImageExplorer() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void changeAudio(View view) {
        changeAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (ActivityCompat.checkSelfPermission(changeMedia.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(changeMedia.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(changeMedia.this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
                        ActivityCompat.requestPermissions(changeMedia.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
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
            Toast.makeText(this, "Recording Started!!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(RECORD_LOG, "Recording Failed : " + e.getMessage());
        }
    }

    private void stopRecording() {
        mRecorder.stop();
        Log.d(TAG, "Recording Stopped");
        mRecorder.release();
        audioRecordUri = Uri.fromFile(new File(file.getAbsolutePath()));
        Log.d(TAG, "new Audio Record uri : " + audioRecordUri);
    }
}
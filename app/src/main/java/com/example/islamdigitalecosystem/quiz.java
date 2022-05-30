package com.example.islamdigitalecosystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.islamdigitalecosystem.be.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class quiz extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    TextView tvQuestion;
//    ProgressBar loadQuis;
    private static final String TAG = "MyActivity";
    ImageView imageQuestion;
    String userAnswer;
    int questionNow;
    int qNum;
    String docRef;
    int questionTotal;
    int questionCount = 0;
    int questionCorrect = 0;
    ProgressBar progressBar;
    String FinalBabRefereence;

    RadioButton rbAnswer1, rbAnswer2, rbAnswer3, rbAnswer4;
    AlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        loading = Util.setPD(this, "Loading..");
        tvQuestion = findViewById(R.id.question);
        imageQuestion = findViewById(R.id.questionImage);
        rbAnswer1 = findViewById(R.id.option1);
        rbAnswer2 = findViewById(R.id.option2);
        rbAnswer3 = findViewById(R.id.option3);
        rbAnswer4 = findViewById(R.id.option4);
        progressBar = findViewById(R.id.progressBar1);
//        loadQuis = findViewById(R.id.loadQuiz);
        questionNow = 1;
        qNum = 1;

        docRef = "Question" + qNum;
//        loadQuis.setVisibility(View.INVISIBLE);

        FinalBabRefereence = getIntent().getStringExtra("BabReference");
        Log.d(TAG, "babRef : " + FinalBabRefereence);

        getQuestionSet();
        getquestionTotal();
        progressBar.setProgress(0);
    }

    public void getQuestionSet() {
        loading.show();
//        loadQuis.setVisibility(View.VISIBLE);
//        loadQuis.setIndeterminate(true);
        AndroidNetworking.get("https://ihya-api.herokuapp.com/Quiz/readOneQ/{bab}/{QNumber}")
                .addPathParameter("bab", FinalBabRefereence)
                .addPathParameter("QNumber",docRef )
                .setTag(this)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(Question.class, new ParsedRequestListener<Question>() {
                    @Override
                    public void onResponse(Question question) {
                       String Quest =  question.getQuestion();
                       String opt1 = question.getOpt1();
                       String opt2 = question.getOpt2();
                       String opt3 = question.getOpt3();
                       String opt4 = question.getOpt4();

                        if (Quest == null){
                            tvQuestion.setVisibility(View.INVISIBLE);
                            imageQuestion.setImageResource(R.drawable.ic_play_audio);
                            Log.d(TAG, "question result : " + Quest);
                            getQuestionImage(); //check and load audio

                        }else {
                            tvQuestion.setVisibility(View.VISIBLE);
                            tvQuestion.setText(Quest);
                            getQuestionImage();//load question image using picasso
                        }
                        rbAnswer1.setText(opt1);
                        rbAnswer2.setText(opt2);
                        rbAnswer3.setText(opt3);
                        rbAnswer4.setText(opt4);
                    }
                    @Override
                    public void onError(ANError anError) {
                        loading.dismiss();
                        Log.d(TAG, "Getting Question Errror : " + anError.getCause());
                    }
                });
            }

    public void getQuestionImage(){
        AndroidNetworking.get("https://ihya-api.herokuapp.com/Quiz/readOneQ/{bab}/{QNumber}")
                .addPathParameter("bab", FinalBabRefereence)
                .addPathParameter("QNumber", docRef)
                .setTag(this)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(Question.class, new ParsedRequestListener<Question>() {
                    @Override
                    public void onResponse(Question question) {
                        if (question.getAudioDwnldUrl() == null){
                            imageQuestion.setVisibility(View.VISIBLE);
                            Log.d(TAG, "image dwnld url : "+question.getAudioDwnldUrl());
                            String imageUri = question.getImgDwnldUrl();
                            ImageView ivBasicImage = (ImageView) findViewById(R.id.questionImage);
                            Picasso.with(quiz.this).load(imageUri).into(ivBasicImage);
                        }else {
                            imageQuestion.setImageResource(R.drawable.ic_play_audio);
                            //load audio
                        }
//                        loadQuis.setIndeterminate(false);
//                        loadQuis.setVisibility(View.INVISIBLE);
                        loading.dismiss();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "Getting Question Resource Errror : " + anError.getCause());
                        loading.dismiss();
                    }
                });
    }

    public void playAudio(View view) {
        loading.show();
        AndroidNetworking.get("https://ihya-api.herokuapp.com/Quiz/readOneQ/{bab}/{QNumber}")
                .setTag(this)
                .setPriority(Priority.IMMEDIATE)
                .addPathParameter("bab", FinalBabRefereence)
                .addPathParameter("QNumber", docRef)
                .build()
                .getAsObject(Question.class, new ParsedRequestListener<Question>() {
                    @Override
                    public void onResponse(Question response) {
                        loading.dismiss();
                        String audioUrl = response.getAudioDwnldUrl();
                        mediaPlayer = new MediaPlayer();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mediaPlayer.setAudioAttributes(
                                    new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                            );
                        }else{
                            Toast.makeText(view.getContext(), "Media player only run on lolipop os and above", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            mediaPlayer.setDataSource(audioUrl);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }catch (IOException e){
                            Log.d(TAG, "Media Player error : " + e.getCause());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        loading.dismiss();
                        Log.d(TAG, "Download Audio URL error : " + anError.getCause());
                    }
                });
    }

    public void getquestionTotal(){
        AndroidNetworking.get("https://ihya-api.herokuapp.com/Quiz/readQuestion/{bab}")
                .addPathParameter("bab", FinalBabRefereence)
                .setPriority(Priority.IMMEDIATE)
                .setTag(this)
                .build()
                .getAsObjectList(Question.class, new ParsedRequestListener<List<Question>>() {
                    @Override
                    public void onResponse(List<Question> response) {
                       questionTotal = response.size();
                        Log.d(TAG, "Bab Question Count : " + questionTotal);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onBackPressed();
                        Log.d(TAG, "get question count err : " + anError.getCause());
                    }
                });
    }

    public void showNextQuestion() {
        if (questionTotal >= questionNow) {
            qNum++;
            docRef = "Question" + qNum;
            Log.d(TAG, "Question Stage, success " + questionNow);
            Log.d(TAG, "Question Count After Next : " + questionTotal);
            Log.d(TAG, "Document next Reference : " + docRef);
            getQuestionSet();
        } else if (questionTotal <= questionNow) {
            Log.d(TAG, "Question Count After Next, failed : " + questionTotal);
            Log.d(TAG, "Question Stage, failed " + questionNow);
        }

        if(questionCount >= questionTotal){
                getScore(quiz.this);
        }
    }

    public void checkAnswer() {
        loading.show();
        AndroidNetworking.get("https://ihya-api.herokuapp.com/Quiz/readOneQ/{bab}/{QNumber}")
                .setTag(this)
                .addPathParameter("bab", FinalBabRefereence)
                .addPathParameter("QNumber", docRef)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(Question.class, new ParsedRequestListener<Question>() {
                    @Override
                    public void onResponse(Question response) {
                        questionCount++;
                        loading.dismiss();
                        Log.d("checkAnswer", response.toString());
                        String corectAnswer = response.getCrAnswer();
                        if (userAnswer.equals(corectAnswer)) {
                            questionNow++;
                            questionCorrect++;
                            openDialogFragmentCorrect();
                            clearAnswer();
                            showNextQuestion();
                            updateProgressBar();
                        } else {
                            openDialogFragmentWrong();
                            clearAnswer();
                            showNextQuestion();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        loading.dismiss();
                        Log.d(TAG, "Get crAnswer Error : " + anError.getCause());
                    }
                });
    }

    public void getInput() {
        if (rbAnswer1.isChecked()) {
            userAnswer = rbAnswer1.getText().toString();
        } else if (rbAnswer2.isChecked()) {
            userAnswer = rbAnswer2.getText().toString();
        } else if (rbAnswer3.isChecked()) {
            userAnswer = rbAnswer3.getText().toString();
        } else {
            userAnswer = rbAnswer4.getText().toString();
        }
    }

    public void clearAnswer() {
        rbAnswer1.setChecked(false);
        rbAnswer2.setChecked(false);
        rbAnswer3.setChecked(false);
        rbAnswer4.setChecked(false);
    }

    public void updateProgressBar() {
        if (questionTotal >= questionNow){
            double qNow = questionNow;
            double prg = qNow / questionTotal;
            Log.d(TAG, "Question Count : " + questionTotal);
            Log.d(TAG, "Question Stage : " + questionNow);
            Log.d(TAG, "prg : " + prg);
            double progres = prg * 100;
            progressBar.setProgress((int) progres);
            Log.d(TAG, "progres bar stat : " + progres);
        }
    }

    public void openDialogFragmentWrong() {
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "My Fragment");
    }

    public void openDialogFragmentCorrect() {
        DialogFragment_correct dialogFragment = new DialogFragment_correct();
        dialogFragment.show(getSupportFragmentManager(), "My Fragment");
    }

    public void pick01(View view) {
        rbAnswer1.setChecked(true);
        rbAnswer2.setChecked(false);
        rbAnswer3.setChecked(false);
        rbAnswer4.setChecked(false);

        rbAnswer1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    rbAnswer2.setChecked(false);
                    rbAnswer3.setChecked(false);
                    rbAnswer4.setChecked(false);
                }
            }
        });
    }

    public void pick02(View view) {
        rbAnswer1.setChecked(false);
        rbAnswer2.setChecked(true);
        rbAnswer3.setChecked(false);
        rbAnswer4.setChecked(false);

        rbAnswer2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    rbAnswer1.setChecked(false);
                    rbAnswer3.setChecked(false);
                    rbAnswer4.setChecked(false);
                }
            }
        });
    }

    public void pick03(View view) {
        rbAnswer1.setChecked(false);
        rbAnswer2.setChecked(false);
        rbAnswer3.setChecked(true);
        rbAnswer4.setChecked(false);

        rbAnswer3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    rbAnswer1.setChecked(false);
                    rbAnswer2.setChecked(false);
                    rbAnswer4.setChecked(false);
                }
            }
        });
    }

    public void pick04(View view) {
        rbAnswer1.setChecked(false);
        rbAnswer2.setChecked(false);
        rbAnswer3.setChecked(false);
        rbAnswer4.setChecked(true);

        rbAnswer4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    rbAnswer1.setChecked(false);
                    rbAnswer2.setChecked(false);
                    rbAnswer3.setChecked(false);
                }
            }
        });
    }

    public void confirm(View view) {
        if (rbAnswer1.isChecked() || rbAnswer2.isChecked() ||
                rbAnswer3.isChecked() || rbAnswer4.isChecked()) {
            getInput();
            Log.d(TAG, userAnswer);
            checkAnswer();
        } else {
            Toast.makeText(this, "Pilih Jawaban", Toast.LENGTH_LONG).show();
        }
    }

    public void getScore(Context c){
        double totalQuiz = 100.0 / questionTotal;
        double calcScore = totalQuiz * questionCorrect;
        double totalScore = (int)(calcScore + 0.5);
        NumberFormat decimalFormat = new DecimalFormat("0.#");
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setIcon(R.drawable.score);
        builder.setTitle("Score");
        builder.setMessage("Quiz selesai dikerjakan\nNilai kamu : "+String.valueOf(decimalFormat.format(totalScore)));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
                startActivity(new Intent(c,home.class));
                finishAffinity();
            }
        });

//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                // Do nothing
//                dialog.dismiss();
//            }
//        });

        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.getWindow().setGravity(Gravity.BOTTOM);
        alert.show();
    }
}

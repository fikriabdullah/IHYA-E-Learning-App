package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.IOException;

public class quiz extends AppCompatActivity {
    FirebaseFirestore firebaseDatabase;
    CollectionReference question;

    MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();
    private Handler mHandler1 = new Handler();
    private Handler mHandler2 = new Handler();
    private Handler mHandler3 = new Handler();
    TextView tvQuestion;
    private static final String TAG = "MyActivity";
    ImageView imageQuestion;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String userAnswer;
    TextView textView, scoreUser;
    Integer userScore;
    int questionTotal;
    int questionNow;
    int qNum;
    String docRef;
    int iRef;
    String imageRef;
    int questionCount;
    ProgressBar progressBar;

    RadioButton rbAnswer1, rbAnswer2, rbAnswer3, rbAnswer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        tvQuestion = findViewById(R.id.question);
        imageQuestion = findViewById(R.id.questionImage);
        rbAnswer1 = findViewById(R.id.option1);
        rbAnswer2 = findViewById(R.id.option2);
        rbAnswer3 = findViewById(R.id.option3);
        rbAnswer4 = findViewById(R.id.option4);
        progressBar = findViewById(R.id.progressBar1);
        questionNow = 1;
        iRef = 1;


        firebaseDatabase = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        question = firebaseDatabase.collection(uploadQuestion.babrefImp);

        showQuestion();
        getQuestionImage();
        getQuestionSet();
        getQuestionCount();
        progressBar.setProgress(0);


    }

    public void getQuestionSet() {
        DocumentReference documentReference;
        documentReference = question.document(docRef); //kalo jawaban bener ini ganti harusnya
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Question questionread = documentSnapshot.toObject(Question.class);
                String questionResult = questionread.getQuestion();
                String Option1 = questionread.getOpt1();
                String Option2 = questionread.getOpt2();
                String Option3 = questionread.getOpt3();
                String Option4 = questionread.getOpt4();
                tvQuestion.setText(questionResult);
                rbAnswer1.setText(Option1);
                rbAnswer2.setText(Option2);
                rbAnswer3.setText(Option3);
                rbAnswer4.setText(Option4);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(quiz.this, "Question Retrieve Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.getMessage());
            }
        });
    }
    public void getQuestionImage(){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child(iRef + "");
        try {
            final File file = File.createTempFile("image", "png");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageQuestion.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(quiz.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getNextQuestionImage() {
        if (questionCount >= questionNow){
            iRef++;
            imageRef = iRef + "";
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference().child(iRef + "");
            try {
                final File file = File.createTempFile("image", "png");
                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        imageQuestion.setImageBitmap(bitmap);
                       // Log.d(TAG, "dwnld Uerl : " + file);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(quiz.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Image Reference : " + imageRef);
            Log.d(TAG, "question stage : " +questionNow);
        }
    }

    public void showQuestion() {
        qNum = 0;
        docRef = "Question" + qNum;
        Log.d(TAG, "DocRef : " + qNum);
        Log.d(TAG, "question stage : " + questionNow);
    }

    public void showNextQuestion() {
        if (questionCount >= questionNow) {
            qNum++;
            docRef = "Question" + qNum;
            Log.d(TAG, "Question Stage, success " + questionNow);
            Log.d(TAG, "Question Count After Next : " + questionCount);
            Log.d(TAG, "Document next Reference : " + docRef);
            getQuestionSet();
        } else if (questionCount <= questionNow) {
            Log.d(TAG, "Question Count After Next, failed : " + questionCount);
            Log.d(TAG, "Question Stage, failed " + questionNow);
            Toast.makeText(this, "bikin end screen, show score", Toast.LENGTH_LONG).show();
        }
    }

    public void checkAnswer() {
        final DocumentReference documentReference;
        documentReference = question.document(docRef);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Question questionAnswer = documentSnapshot.toObject(Question.class);
                String corectAnswer = questionAnswer.getCrAnswer();

                if (userAnswer.equals(corectAnswer)) {
                    questionNow++;
                    openDialogFragmentCorrect();
                    showNextQuestion();
                    getNextQuestionImage();
                    clearAnswer();
                    updateProgressBar();

                } else {
                    openDialogFragmentWrong();
                    clearAnswer();
                }
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

    public void getQuestionCount() {
        question.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    questionCount = task.getResult().size();
                    Log.d(TAG, "Question Total on method : " + task.getResult().size());
                } else {
                    Log.d(TAG, "Error getting Documents : " + task.getException());
                }
            }
        });
    }

    public void updateProgressBar() {
        if (questionCount >= questionNow){
            double qNow = questionNow;
            double prg = qNow / questionCount;
            Log.d(TAG, "Question Count : " + questionCount);
            Log.d(TAG, "Question Stage : " + questionNow);
            Log.d(TAG, "prg : " + prg);
            double progres = prg * 100;
            progressBar.setProgress((int) progres);
            Log.d(TAG, "progres bar stat : " + progres);
        }
    }

    public void clue(View view) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alf);
        }
        mediaPlayer.start();
    }

    public void openDialogFragmentWrong() {
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "My Fragment");
    }

    public void openDialogFragmentCorrect() {
        DialogFragment_correct dialogFragment = new DialogFragment_correct();
        dialogFragment.show(getSupportFragmentManager(), "My Fragment");
    }


    private Runnable mIntentRunnable = new Runnable() {
        @Override
        public void run() {
            // startActivity(new Intent(quiz.this, quiz2.class));
        }
    };

    public void pick01(View view) {
        rbAnswer1.setChecked(true);
    }


    private Runnable mIntentRunnable1 = new Runnable() {
        @Override
        public void run() {
            //startActivity(new Intent(quiz.this, quiz2.class));
        }
    };

    public void pick02(View view) {
        rbAnswer2.setChecked(true);

        //openDialogFragmentWrong();
        //mHandler1.postDelayed(mIntentRunnable1, 2000);
    }


    private Runnable mIntentRunnable2 = new Runnable() {
        @Override
        public void run() {
            //startActivity(new Intent(quiz.this, quiz2.class));
        }
    };

    public void pick03(View view) {
        rbAnswer3.setChecked(true);
    }

    ;

    private Runnable mIntentRunnable3 = new Runnable() {
        @Override
        public void run() {
            //startActivity(new Intent(quiz.this, quiz2.class));
        }
    };

    public void pick04(View view) {
        rbAnswer4.setChecked(true);
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
}

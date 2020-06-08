package com.example.islamdigitalecosystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.view.View;
import android.widget.TextView;

public class quiz extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();
    private Handler mHandler1 = new Handler();
    private Handler mHandler2 = new Handler();
    private Handler mHandler3 = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

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
            startActivity(new Intent(quiz.this, quiz2.class));
        }
    };

    public void pick01(View view) {
        openDialogFragmentCorrect();
        mHandler.postDelayed(mIntentRunnable, 1500);
    }


    private Runnable mIntentRunnable1 = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(quiz.this, quiz2.class));
        }
    };

    public void pick02(View view) {
        openDialogFragmentWrong();
        mHandler1.postDelayed(mIntentRunnable1, 1000);
    }


    private Runnable mIntentRunnable2 = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(quiz.this, quiz2.class));
        }
    };

    public void pick03(View view) {
        openDialogFragmentWrong();
        mHandler2.postDelayed(mIntentRunnable2, 1000);
    }

    ;

    private Runnable mIntentRunnable3 = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(quiz.this, quiz2.class));
        }
    };

    public void pick04(View view) {
        openDialogFragmentWrong();
        mHandler3.postDelayed(mIntentRunnable3, 1000);
    }
}

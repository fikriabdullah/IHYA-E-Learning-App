package com.example.islamdigitalecosystem;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class quiz extends AppCompatActivity {
    TextView textView, textView1;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        textView = findViewById(R.id.question);


    }


    public void clue(View view) {
        if (mediaPlayer ==  null){
            mediaPlayer = MediaPlayer.create(this, R.raw.alf);
        }
        mediaPlayer.start();
    }
}

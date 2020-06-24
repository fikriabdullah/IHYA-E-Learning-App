package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
public class pilihanSoal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilihan_soal);
    }

    public void soalSuara(View view) {
        Intent intent = new Intent(pilihanSoal.this, soalSuara.class);
        startActivity(intent);
    }

    public void soalText(View view) {
        Intent intent = new Intent(pilihanSoal.this, uploadQuestion.class);
        startActivity(intent);
    }
}

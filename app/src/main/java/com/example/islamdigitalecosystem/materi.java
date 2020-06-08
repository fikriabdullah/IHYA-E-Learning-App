package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class materi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materi);
    }

    public void kisahNabi(View view) {
        startActivity(new Intent(materi.this, kisah.class));
    }

    public void hijariyah(View view) {
        startActivity(new Intent(materi.this, level1.class));
    }
}
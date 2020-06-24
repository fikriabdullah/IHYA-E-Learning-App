package com.example.islamdigitalecosystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardGuru extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_guru);
    }

    public void uploadQuiz(View view) {
        Intent intent = new Intent(DashboardGuru.this, pilihanSoal.class);
        startActivity(intent);
    }
}

package com.example.islamdigitalecosystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardGuru extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_guru);
    }

    public void uploadQuiz(View view) {
        Intent intent = new Intent(DashboardGuru.this, pilihanSoal.class);
        startActivity(intent);
    }

    public void uploadMateri(View view) {
        Intent intent = new Intent(DashboardGuru.this, uploadMateri.class);
        startActivity(intent);
    }

    public void gotoForum(View view) {
        Intent intent = new Intent(DashboardGuru.this, forumMain.class);
        startActivity(intent);
    }

    public void EditQuiz(View view) {
        Intent intent = new Intent(DashboardGuru.this, BabListEditKuis.class);
        startActivity(intent);
    }
}

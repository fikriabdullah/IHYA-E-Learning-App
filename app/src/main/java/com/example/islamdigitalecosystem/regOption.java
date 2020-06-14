package com.example.islamdigitalecosystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class regOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_option);
    }

    public void regTeacher(View view) {
        startActivity(new Intent(regOption.this, register_guru.class));
    }

    public void regStudent(View view) {
        startActivity(new Intent(regOption.this, register_guru.class));
    }
}

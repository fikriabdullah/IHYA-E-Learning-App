package com.example.islamdigitalecosystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class loginOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_option);
    }

    public void logTeacher(View view) {
        startActivity(new Intent(loginOption.this, login_guru.class));
    }

    public void logStudent(View view) {
        startActivity(new Intent(loginOption.this, login.class));
    }

}

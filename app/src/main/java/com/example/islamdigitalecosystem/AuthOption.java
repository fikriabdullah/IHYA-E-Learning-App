package com.example.islamdigitalecosystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AuthOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_option);
    }


    public void login(View view) {
        Intent intent = new Intent(AuthOption.this, login.class);
        startActivity(intent);
    }

    public void regis(View view) {
        Intent intent = new Intent(AuthOption.this, regis.class);
        startActivity(intent);
    }
}

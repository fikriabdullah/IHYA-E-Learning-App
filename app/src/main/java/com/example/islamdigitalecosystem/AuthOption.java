package com.example.islamdigitalecosystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AuthOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_option);
    }



    public void login(View view) {
        Intent intent = new Intent(AuthOption.this, loginOption.class);
        startActivity(intent);
    }

    public void regis(View view) {
        Intent intent = new Intent(AuthOption.this, regOption.class);
        startActivity(intent);
    }
}

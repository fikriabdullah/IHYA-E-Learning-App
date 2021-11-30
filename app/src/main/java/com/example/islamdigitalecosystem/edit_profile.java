package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class edit_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

    }

    public void logut(View view) { startActivity(new Intent(edit_profile.this, login.class));
    }
}

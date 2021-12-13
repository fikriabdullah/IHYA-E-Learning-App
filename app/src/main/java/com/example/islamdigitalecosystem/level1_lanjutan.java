package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

public class level1_lanjutan extends AppCompatActivity {
    MediaPlayer player;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level1_lanjutan);

    }

    public void lanjut(View view) {startActivity(new Intent(level1_lanjutan.this, level1_lanjutan2.class));
    }
}
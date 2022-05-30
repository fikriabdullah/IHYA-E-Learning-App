package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

public class membaca_jilid extends AppCompatActivity {
    MediaPlayer player;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membaca_jilid);

    }

    public void lanjut(View view) {startActivity(new Intent(membaca_jilid.this, membaca_jilid2.class));
    }
}
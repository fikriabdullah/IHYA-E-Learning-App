package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

public class latihan extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latihan);

    }

    public void masukLatihan(View view) {startActivity(new Intent(latihan.this, level1.class));
    }

    public void membaca(View view) {startActivity(new Intent(latihan.this, latihan_membaca.class));
    }
    public void membaca2(View view) {startActivity(new Intent(latihan.this, membaca_jilid.class));
    }
}

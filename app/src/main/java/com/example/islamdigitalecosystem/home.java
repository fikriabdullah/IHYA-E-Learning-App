package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.animation.type.SlideAnimation;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    FlipperLayout flipperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        flipperLayout = (FlipperLayout) findViewById(R.id.imageSlider);
        setLayout();
    }

    private void setLayout() {
        int image[] = {
                R.drawable.kata1,
                R.drawable.kata2,
                R.drawable.kata4,
                R.drawable.kata3
        };
        for (int i = 0; i < 4; i++) {
            FlipperView view = new FlipperView(getBaseContext());
            view.setImageDrawable(image[i]);
            flipperLayout.addFlipperView(view);
            view.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                @Override
                public void onFlipperClick(FlipperView flipperView) {

                }
            });
        }
    }

    public void latihankuy(View view) {
        startActivity(new Intent(home.this, latihan.class));
    }

    public void klik(View view) {
        startActivity(new Intent(home.this, materi.class));
    }

    public void baca(View view) {startActivity(new Intent(home.this, Main.class));
    }

    public void quizyok(View view) {
        startActivity(new Intent(home.this, quiz.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home){

        }
        return false;
    }
    private void showFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout,fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    public void YokNgobs(View view) {
        startActivity(new Intent(home.this, forumMain.class));
    }
}

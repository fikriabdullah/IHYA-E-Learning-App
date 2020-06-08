package com.example.islamdigitalecosystem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.animation.type.SlideAnimation;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class home extends AppCompatActivity {
DrawerLayout drawerLayout;
NavigationView navigationView;
androidx.appcompat.widget.Toolbar toolbar;
FlipperLayout flipperLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        flipperLayout=(FlipperLayout) findViewById(R.id.imageSlider);
        setLayout();
    }
    private void setLayout(){
        int image[] = {
                R.drawable.kata1,
                R.drawable.kata2,
                R.drawable.kata4,
                R.drawable.kata3
        };
        for (int i = 0; i <4; i++) {
            FlipperView view=new FlipperView(getBaseContext());
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

    public void baca(View view) {
    }
}

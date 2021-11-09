package com.example.islamdigitalecosystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.animation.type.SlideAnimation;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import fragments.homeFragment;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardGuru extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    FlipperLayout flipperLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_guru);
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

    public void uploadQuiz(View view) {
        Intent intent = new Intent(DashboardGuru.this, pilihanSoal.class);
        startActivity(intent);
    }

    public void uploadMateri(View view) {
        Intent intent = new Intent(DashboardGuru.this, uploadMateri.class);
        startActivity(intent);
    }

    public void gotoForum(View view) {
        Intent intent = new Intent(DashboardGuru.this, forumMain.class);
        startActivity(intent);
    }

    public void EditQuiz(View view) {
        Intent intent = new Intent(DashboardGuru.this, BabListEditKuis.class);
        startActivity(intent);
    }
    public void EditMateri(View view) {
        Intent intent = new Intent(DashboardGuru.this, edit_materi.class);
        startActivity(intent);
    }
}

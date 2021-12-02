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
import android.util.Log;
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
    MeowBottomNavigation bottomNavigation;
    private final int ID_Home = 1;
    private static final String TAG = "homeMainAct: ";
    private final int ID_profile = 2;
    private Object FrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_guru);
        flipperLayout = (FlipperLayout) findViewById(R.id.imageSlider);
        setLayout();
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_Home,R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_profile,R.drawable.account));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                if(item.getId() == 1){
                    Log.d(TAG, "item id : " + item.getId()+ "\nStarting Dashboard");
                    Intent intent = new Intent(DashboardGuru.this, DashboardGuru.class);
                    startActivity(intent);
                }else if (item.getId() == 2){
                    Log.d(TAG, "item id : " + item.getId() + "\nStarting Edit Profile");
                    Intent intent = new Intent(DashboardGuru.this, edit_profile.class);
                    startActivity(intent);
                }

            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Log.d(TAG, ""+item.getId());
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String Name;
                switch (item.getId()){
                    case ID_Home: Name = "Home";
                        break;
                    case ID_profile: Name = "notif";
                        break;
                    default:Name= "Home";
                }
            }
        });
        bottomNavigation.setCount(ID_Home,"2");
        bottomNavigation.show(ID_Home,true);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

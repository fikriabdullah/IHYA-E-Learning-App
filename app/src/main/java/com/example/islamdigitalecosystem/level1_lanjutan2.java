package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;

public class level1_lanjutan2  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    MediaPlayer player;
    NavigationView navigationView;
    MeowBottomNavigation bottomNavigation;
    private final int ID_Home = 1;
    private static final String TAG = "homeMainAct: ";
    private final int ID_profile = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leve1_lanjutan2);
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_Home,R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_profile,R.drawable.account));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                if(item.getId() == 1){
                    Log.d(TAG, "item id : " + item.getId()+ "\nStarting Edit Profile");
                    Intent intent = new Intent(level1_lanjutan2.this, home.class);
                    startActivity(intent);
                }else if (item.getId() == 2){
                    Log.d(TAG, "item id : " + item.getId() + "\nStarting Edit Profile");
                    Intent intent = new Intent(level1_lanjutan2.this, edit_profile_student.class);
                    startActivity(intent);
                }

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

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Log.d(TAG, ""+item.getId());
            }
        });


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

}
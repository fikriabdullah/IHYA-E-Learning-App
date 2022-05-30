package com.example.islamdigitalecosystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import technolifestyle.com.imageslider.FlipperLayout;

public class edit_profile_student extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    ActionBarDrawerToggle toggle;
    FlipperLayout flipperLayout;
    MeowBottomNavigation bottomNavigation;
    private final int ID_Home = 1;
    private static final String TAG = "homeMainAct: ";
    private final int ID_profile = 2;
    private Object FrameLayout;
    TextView studentPhone, studentEmail, studentRole, studentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_student);
        studentPhone = findViewById(R.id.studentPhone);
        studentName = findViewById(R.id.studentName);
        studentRole = findViewById(R.id.studentRole);
        studentEmail = findViewById(R.id.studentEmail);

        readUserData();

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_Home,R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_profile,R.drawable.account));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                if(item.getId() == 1){
                    Log.d(TAG, "item id : " + item.getId()+ "\nStarting Home");
                    Intent intent = new Intent(edit_profile_student.this, home.class);
                    startActivity(intent);
                }else if (item.getId() == 2){
                    Log.d(TAG, "item id : " + item.getId() + "\nStarting Edit Profile");
                    Intent intent = new Intent(edit_profile_student.this, edit_profile_student.class);
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
                    case ID_profile: Name = "edit_profile";
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

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Log.d(TAG, "onReselect : " + item.getId());
            }
        });

        bottomNavigation.setCount(ID_Home,"2");
        bottomNavigation.show(ID_profile,true);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void readUserData(){
        firebaseAuth = FirebaseAuth.getInstance();

        String child = "Student";
        DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference().child("UserDatabase").child(child);
        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
                UserCntr userData = dataSnapshot.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).getValue(UserCntr.class);

                String userName = userData.username;
                studentName.setText(userName);

                String userPhone = userData.NoTelp;
                studentPhone.setText(userPhone);

                String userEmail = userData.Email;
                studentEmail.setText(userEmail);

                String userRole = userData.Role;
                studentRole.setText(userRole);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Get User Data Error : " + databaseError.getMessage());
            }
        });
    }

    public void logut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(edit_profile_student.this, login.class));
    }

    public void editProfile(View view) {
        startActivity(new Intent(edit_profile_student.this, EditProfileStudent.class));
    }
}

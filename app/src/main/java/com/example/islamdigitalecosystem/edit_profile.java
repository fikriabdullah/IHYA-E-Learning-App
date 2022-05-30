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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import technolifestyle.com.imageslider.FlipperLayout;

public class edit_profile extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    TextView teacherPhone, teacherRole, teacherName, teacherEmail;
    FlipperLayout flipperLayout;
    FirebaseAuth firebaseAuth;
    MeowBottomNavigation bottomNavigation;
    private final int ID_Home = 1;
    private static final String TAG = "editProfile: ";
    private final int ID_profile = 2;
    private Object FrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        teacherPhone = findViewById(R.id.teacherPhone);
        teacherName = findViewById(R.id.teacherName);
        teacherRole = findViewById(R.id.teacherRole);
        teacherEmail = findViewById(R.id.teacherEmail);

        readUserData();

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_Home,R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_profile,R.drawable.account));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                if(item.getId() == 1){
                    Log.d(TAG, "item id : " + item.getId()+ "\nStarting Dashboard");
                    Intent intent = new Intent(edit_profile.this, DashboardGuru.class);
                    startActivity(intent);
                }else if (item.getId() == 2){
                    Log.d(TAG, "item id : " + item.getId() + "\nStarting Edit Profile");
                    Intent intent = new Intent(edit_profile.this, edit_profile.class);
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

        bottomNavigation.setCount(ID_Home,"2");
        bottomNavigation.show(ID_profile,true);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void readUserData(){
        firebaseAuth = FirebaseAuth.getInstance();
        String child = "Guru";
        DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference().child("UserDatabase").child(child);
        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren();
                UserCntr userData = dataSnapshot.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).getValue(UserCntr.class);

                String userName = userData.username;
                teacherName.setText(userName);

                String userPhone = userData.NoTelp;
                teacherPhone.setText(userPhone);

                String userEmail = userData.Email;
                teacherEmail.setText(userEmail);

                String userRole = userData.Role;
                teacherRole.setText(userRole);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Get User Data Error : " + databaseError.getMessage());
            }
        });
    }

    public void logut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(edit_profile.this, login.class));
    }

    public void editProfile(View view) {
        startActivity(new Intent(edit_profile.this, EditProfileTeacher.class));
    }
}

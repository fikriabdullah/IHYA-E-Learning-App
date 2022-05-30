package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.islamdigitalecosystem.be.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditProfileTeacher extends AppCompatActivity {
    EditText teacherName, teacherEmail,teacherPhone;
    TextView teacherRole;
    FirebaseAuth firebaseAuth;
    AlertDialog loading;
    FirebaseDatabase database;
    private static final String TAG = "FormEditProfileTeach: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_teacher);
        teacherPhone = findViewById(R.id.teacherPhone);
        teacherName = findViewById(R.id.teacherName);
        teacherRole = findViewById(R.id.teacherRole);
        teacherEmail = findViewById(R.id.teacherEmail);
        loading = Util.setPD(this, "Loading..");

        readUserData();
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

    public void saveProfile(View view) {
        loading.show();
        database = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UserProfileChangeRequest teacherProfileUpdateReq = new UserProfileChangeRequest.Builder()
                .setDisplayName(teacherName.getText().toString())
                .build();
        firebaseUser.updateProfile(teacherProfileUpdateReq).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "Update username success, update user email");
                firebaseUser.updateEmail(teacherEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "update email user success, saving new profile to db");
                        UserCntr teacherNewProfile = new UserCntr(teacherName.getText().toString(),
                                teacherEmail.getText().toString(), teacherPhone.getText().toString(), teacherRole.getText().toString());
                        database.getReference("UserDatabase").child("Guru")
                                .child(firebaseAuth.getCurrentUser().getUid()).setValue(teacherNewProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    loading.hide();
                                    Toast.makeText(getApplicationContext(), "New Profile Saved", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(EditProfileTeacher.this, edit_profile.class));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loading.hide();
                                Log.d(TAG, "Saving new profile failed " + e.getMessage());
                                Toast.makeText(getApplicationContext(), "Saving New Profile Failed ", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading.hide();
                        Log.d(TAG, "Updating User Email Failed" + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Saving New Profile Failed ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.hide();
                Log.d(TAG, "update user name failed : " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Saving new profile Failed !!", Toast.LENGTH_LONG).show();
            }
        });
    }
    }

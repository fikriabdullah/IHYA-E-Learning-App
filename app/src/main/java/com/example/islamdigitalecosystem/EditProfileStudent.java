package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.system.ErrnoException;
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

public class EditProfileStudent extends AppCompatActivity {
    EditText studentPhone,studentEmail, studentName;
    TextView studentRole;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    AlertDialog loading;
    private static final String TAG = "FormEditProfileStud: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_student2);
        studentPhone = findViewById(R.id.studentPhone);
        studentName = findViewById(R.id.studentName);
        studentRole = findViewById(R.id.studentRole);
        studentEmail = findViewById(R.id.studentEmail);

        readUserData();
        loading = Util.setPD(this, "Loading..");
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

    public void saveProfile(View view) {
        loading.show();
        database = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UserProfileChangeRequest studentProfileChangeReq = new UserProfileChangeRequest.Builder()
                .setDisplayName(studentName.getText().toString())
                .build();
        firebaseUser.updateProfile(studentProfileChangeReq).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "Update username success, update user email");
                firebaseUser.updateEmail(studentEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "update email user success, saving new profile to db");
                        UserCntr studentNewProfile = new UserCntr(studentName.getText().toString(),
                                studentEmail.getText().toString(), studentPhone.getText().toString(), studentRole.getText().toString());
                        database.getReference("UserDatabase").child("Student")
                                .child(firebaseAuth.getCurrentUser().getUid()).setValue(studentNewProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    loading.hide();
                                    Toast.makeText(getApplicationContext(), "New Profile Saved", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(EditProfileStudent.this, edit_profile_student.class));
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
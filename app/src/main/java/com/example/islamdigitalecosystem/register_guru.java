package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

public class register_guru extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText nama;
    EditText kontak;
    EditText email;
    EditText password;
    private static final String TAG = "Register Guru";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_guru);

        nama = findViewById(R.id.nama1);
        kontak = findViewById(R.id.notelp1);
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);

        firebaseAuth = firebaseAuth.getInstance();
    }

    public void regis1(View view) {
        final String username = nama.getText().toString();
        final String Email = email.getText().toString();
        final String Password = password.getText().toString();
        final String NoTelp = kontak.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Creating new user done");
                    UserCntr userCntr = new UserCntr(
                            username,
                            Email,
                            NoTelp
                    );
                    FirebaseDatabase.getInstance().getReference("UserDatabase").child("Student")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userCntr)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Putting user data done");
                                        firebaseAuth.signInWithEmailAndPassword(Email, Password)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        Log.d(TAG, "Sign In User Done");
                                                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                                .setDisplayName(username).build();

                                                        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                startActivity(new Intent(register_guru.this, home.class));
                                                                Log.d(TAG, "Giving Username a name : " + user.getDisplayName());
                                                                Log.d(TAG, "Sending to Home");
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(TAG, "Updating user DN failed : " + e.getMessage());
                                                            }
                                                        });

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Sign In Failed : " + e.getMessage());
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Putting user data to db failed : " + e.getMessage());
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Creating new User Failed : " + e.getMessage());
            }
        });

    }
}

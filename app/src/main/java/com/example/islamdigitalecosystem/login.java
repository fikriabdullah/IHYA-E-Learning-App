package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.islamdigitalecosystem.be.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class login extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPrefEdit;
    String child;
    private static final String TAG = " Login : ";

    EditText email;
    EditText password;
    CheckBox checkBox;

    AlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loading = Util.setPD(this, "Loading..");
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailLg);
        password = findViewById(R.id.passwordLg);
        checkBox = findViewById(R.id.checkBox);

        sharedPreferences =getSharedPreferences("com.example.islamdigitalecosystem", MODE_PRIVATE);
        sharedPrefEdit = sharedPreferences.edit();

        checkSharedPref();

    }

    private void checkSharedPref(){
        String Scheckbox = sharedPreferences.getString(getString(R.string.checkbox), "False");
        String Semail = sharedPreferences.getString(getString(R.string.email), "");
        String Spassword = sharedPreferences.getString(getString(R.string.password), "");

        email.setText(Semail);
        password.setText(Spassword);

        if (Scheckbox.equals("True")){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }
    }

    public void masuk(View view) {
        if (checkBox.isChecked()){
            sharedPrefEdit.putString(getString(R.string.checkbox), "True");
            sharedPrefEdit.commit();

            String vName = email.getText().toString();
            sharedPrefEdit.putString(getString(R.string.email), vName);
            sharedPrefEdit.commit();

            String vPassword = password.getText().toString();
            sharedPrefEdit.putString(getString(R.string.password), vPassword);
            sharedPrefEdit.commit();
        }else {
            sharedPrefEdit.putString(getString(R.string.checkbox), "False");
            sharedPrefEdit.commit();


            sharedPrefEdit.putString(getString(R.string.email), "");
            sharedPrefEdit.commit();


            sharedPrefEdit.putString(getString(R.string.password), "");
            sharedPrefEdit.commit();
        }

        try {
            if (email.getText().toString().equals(null)||email.getText().toString().length() == 0){
                throw new NullPointerException("Silahkan Isi Field Email Diatas");
            }else if(password.getText().toString().equals(null)||password.getText().toString().length()==0){
                throw new NullPointerException("Silahkan Isi Field Password Diatas");
            }else {
                Log.d(TAG, "email\n"+email.getText().toString()+"\npassword"+password.getText().toString());
                loading.show();
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            child = "Guru";
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            reference = firebaseDatabase.getReference().child("UserDatabase").child(child);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getChildren();
                                    String Role = dataSnapshot.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("Role").getValue(String.class);
                                    Log.d(TAG, "Role of this user : " + Role);
                                    if (Role == null){
                                        child = "Student";
                                        reference = firebaseDatabase.getReference().child("UserDatabase").child(child);
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getChildren();
                                                String Role = dataSnapshot.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("Role").getValue(String.class);
                                                Log.d(TAG, "Role of this user : " + Role);
//                                              loading.dismiss();
                                                startActivity(new Intent(login.this, home.class));
                                                loading.hide();
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                loading.hide();
                                                Log.d(TAG, "get role error : " + databaseError.getMessage());
                                            }
                                        });
                                    }else {
//                                      loading.hide();
                                        startActivity(new Intent(login.this, DashboardGuru.class));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    loading.hide();
                                    Log.d(TAG, "get role error : " + databaseError.getMessage());
                                }
                            });
                        }
                        else {
                            loading.hide();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }catch(NullPointerException e){
            Log.d(TAG, "Field Empty : " + e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void resetEmail(View view) {
        try {
            if (email.getText().toString().equals(null)||email.getText().toString().length()==0) {
                throw new NullPointerException("Silahkan Isi Field Email Diatas");
            }else {
                loading.show();
                firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG, "reset link sent");
                                    loading.hide();
                                    Toast.makeText(getApplicationContext(), "Reset Password Link Has Been Sent To Your Registered Email, Check your Email", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }catch (NullPointerException e){
            Log.d(TAG, "Email field empty");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}

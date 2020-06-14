package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class regis extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText nama;
    EditText kontak;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        nama = findViewById(R.id.nama);
        kontak = findViewById(R.id.notelp);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        firebaseAuth = firebaseAuth.getInstance();
    }

    public void regis(View view) {
        final String username = nama.getText().toString();
        final String Email = email.getText().toString();
        final String Password = password.getText().toString();
        final String NoTelp = kontak.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(Email, Password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
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
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Registrasi Sukses", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent (regis.this, login.class));
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });


    }
}

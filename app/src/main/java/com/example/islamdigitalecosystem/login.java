package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPrefEdit;

    EditText email;
    EditText password;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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

        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(login.this, home.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

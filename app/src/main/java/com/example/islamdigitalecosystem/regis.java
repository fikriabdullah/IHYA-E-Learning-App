package com.example.islamdigitalecosystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiEnterpriseConfig;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PrivateKey;
import java.util.Objects;

public class regis extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    EditText nama;
    EditText kontak;
    EditText email;
    EditText password;
    Spinner selectRole;
    String Role;
    private static final String TAG = "register : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        nama = findViewById(R.id.nama);
        kontak = findViewById(R.id.notelp);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        selectRole = findViewById(R.id.spinner);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Role = String.valueOf(selectRole.getSelectedItem());

    }

    public void regis(View view) {
        final String username = nama.getText().toString();
        final String Email = email.getText().toString();
        final String Password = password.getText().toString();
        final String NoTelp = kontak.getText().toString();
        Role = String.valueOf(selectRole.getSelectedItem());

        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Creating new user done");
                    UserCntr userCntr = new UserCntr(
                            username,
                            Email,
                            NoTelp,
                            Role
                    );
                    String chld;
                    if(Role.equals("Murid")){
                        chld = "Student";
                    }else {
                        chld = "Guru";
                    }
                    FirebaseDatabase.getInstance().getReference("UserDatabase").child(chld)
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
                                                                if(Role.equals("Guru")){
                                                                    startActivity(new Intent(regis.this, DashboardGuru.class));
                                                                    Log.d(TAG, "Giving Username a name : " + user.getDisplayName());
                                                                    Log.d(TAG, "Sending to Dashboard");
                                                                }else if(Role.equals("Murid")){
                                                                    startActivity(new Intent(regis.this, home.class));
                                                                    Log.d(TAG, "Giving Username a name : " + user.getDisplayName());
                                                                    Log.d(TAG, "Sending to Home");
                                                                }
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

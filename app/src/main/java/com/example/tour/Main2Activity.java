package com.example.tour;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tour.Data.MainData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import maes.tech.intentanim.CustomIntent;

public class Main2Activity extends AppCompatActivity {
    private Button singUpBtn;
    private EditText nameET, emailET, passwordET, repasswordET, cellNumberET;
    private String uid, name, email, password, reTypePassword, cellNumber;
    private Double cellNumberIn;
    private FirebaseAuth mAuth;

    // Write to the database
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(Main2Activity.this,"fadein-to-fadeout");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        singUpBtn = findViewById(R.id.singUpBtn);
        nameET = findViewById(R.id.nameSingUpET);
        emailET = findViewById(R.id.emailSingUpET);
        passwordET = findViewById(R.id.passwordSingUpET);
        repasswordET = findViewById(R.id.reTypepasswordSingUpET);
        cellNumberET = findViewById(R.id.cellnumberSingUpET);

        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                password = passwordET.getText().toString().trim();
                reTypePassword = repasswordET.getText().toString().trim();
                cellNumber = cellNumberET.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(Main2Activity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty() || !email.contains("@")) {
                    Toast.makeText(Main2Activity.this, "Please enter a valied email addrss.", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(Main2Activity.this, "Please enter password.", Toast.LENGTH_SHORT).show();
                } else if (!password.contains(reTypePassword)) {
                    Toast.makeText(Main2Activity.this, "Password do not match.", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(Main2Activity.this, "Please enter 6 digit password.", Toast.LENGTH_SHORT).show();
                } else if (cellNumber.isEmpty() || cellNumber.length() < 11) {
                    Toast.makeText(Main2Activity.this, "Please enter 11 digit number with out contry code.", Toast.LENGTH_SHORT).show();
                } else {
                    createAccount(email, password);
                    //Toast.makeText(Main2Activity.this, "You enter :"+name+" "+email+" "+password+" "+cellNumber, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void createAccount(final String email, final String password) {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uid = task.getResult().getUser().getUid();
                            saveSingUpData(new MainData(uid, name, email, Double.valueOf(cellNumber)));
                        } else {
                            Toast.makeText(Main2Activity.this, "Account not create successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void saveSingUpData(MainData mainData) {
        // Write to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("tourUser");
        //uid = myRef.push().getKey();

        myRef.child(uid).setValue(mainData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Main2Activity.this, "User info saved successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main2Activity.this,ShowActivity.class));
                    CustomIntent.customType(Main2Activity.this,"left-to-right");
                } else {
                    Toast.makeText(Main2Activity.this, "User info not save successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

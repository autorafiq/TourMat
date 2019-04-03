package com.example.tour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tour.Data.MainData;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText passwordForgotET, reTypepasswordForgotET;
    private Button savePasswordBtn;
    private String password, reTypePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        passwordForgotET=findViewById(R.id.passwordForgotET);
        reTypepasswordForgotET=findViewById(R.id.reTypepasswordForgotET);
        savePasswordBtn=findViewById(R.id.savePasswordForgotBtn);
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password=passwordForgotET.getText().toString().trim();
                reTypePassword=reTypepasswordForgotET.getText().toString().trim();
                checkPasswordValidattion(password,reTypePassword);
            }
        });
    }

    private void checkPasswordValidattion(String password, String reTypePassword) {
        if (password.isEmpty()) {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter a password.", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter 6 digit password.", Toast.LENGTH_SHORT).show();
        } else {
            //sing in code
            Toast.makeText(this, "You enter: "+password+"/", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
            finish();

        }
    }


}

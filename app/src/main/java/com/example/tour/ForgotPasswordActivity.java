package com.example.tour;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tour.Data.MainData;
import com.example.tour.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import maes.tech.intentanim.CustomIntent;

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ActivityForgotPasswordBinding binding;
    private String email;

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(ForgotPasswordActivity.this,"bottom-to-up");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();

        binding.resetPasswordEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        email = binding.resetPasswordEmailET.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ForgotPasswordActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_LONG).show();
                }
            }
        });

        startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));

    }


}

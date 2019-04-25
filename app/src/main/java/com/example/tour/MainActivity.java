package com.example.tour;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tour.Data.MainData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.nio.CharBuffer;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private static int SPLASH_TIME_OUT = 5000;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.loadingA);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startCheekAnimation();
                goTo();
            }
        },SPLASH_TIME_OUT);


    }

    private void goTo() {
        if (user != null) {
            startActivity(new Intent(MainActivity.this, ShowActivity.class));
            CustomIntent.customType(MainActivity.this,"left-to-right");
        } else {
            startActivity(new Intent(MainActivity.this, SingInActivity.class));
            CustomIntent.customType(MainActivity.this,"right-to-left");
        }
    }
    private void startCheekAnimation() {

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 0f).setDuration(3000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lottieAnimationView.setProgress((Float) animation.getAnimatedValue());
            }
        });
        if (lottieAnimationView.getProgress() == 0f) {
            animator.start();
        } else {
            lottieAnimationView.setProgress(0f);
        }
    }


}

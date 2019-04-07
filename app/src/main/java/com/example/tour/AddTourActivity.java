package com.example.tour;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tour.Data.Data;
import com.example.tour.Data.MainData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTourActivity extends AppCompatActivity {
    private EditText tourNameET, tourDescriptionET, startDateET, endDateET, budgetET, testET;
    private Button saveTourInfoBtn;
    private DatePickerDialogFragment mDatePickerDialogFragment;
    private String uid, tourName, tourDescription;
    private long startDate, endDate;
    private double budget;
    private FirebaseAuth mAuth;
    // Write to the database
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);
        mAuth = FirebaseAuth.getInstance();
        mDatePickerDialogFragment = new DatePickerDialogFragment();
        //init
        tourNameET = findViewById(R.id.tourNameET);
        tourDescriptionET = findViewById(R.id.tourDescriptionET);
        startDateET = findViewById(R.id.startDateAddTourET);
        endDateET = findViewById(R.id.endDateAddTourET);
        budgetET = findViewById(R.id.tourBudgetET);
        saveTourInfoBtn = findViewById(R.id.saveTourInfoAddTourBtn);
        testET = findViewById(R.id.testET);
        startDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialogFragment.setFlag(mDatePickerDialogFragment.FLAG_START_DATE);
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "datepicker");
                startDateET.setText(mDatePickerDialogFragment.getStartDate());
            }
        });
        endDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialogFragment.setFlag(mDatePickerDialogFragment.FLAG_END_DATE);
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "datepicker");
                endDateET.setText(mDatePickerDialogFragment.getEndDate());
            }
        });
        saveTourInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourName = tourNameET.getText().toString();
                tourDescription = tourDescriptionET.getText().toString();
                budget = Double.valueOf(budgetET.getText().toString());
                startDate = mDatePickerDialogFragment.getStartDateLongValue();
                endDate = mDatePickerDialogFragment.getEndDateLongValue();
                if (tourName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your tour name.", Toast.LENGTH_SHORT).show();
                } else if (tourDescription.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter something about your tour.", Toast.LENGTH_SHORT).show();
                } else if (budget == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your budget.", Toast.LENGTH_SHORT).show();
                } else if (startDate == 0 || endDate == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your tour date range.", Toast.LENGTH_SHORT).show();
                } else {
                    saveTourInfo(new Data(tourName, tourDescription, startDate, endDate, budget));
                }
            }
        });
    }

    private void saveTourInfo(Data data) {
        String user = mAuth.getCurrentUser().getUid();
        // Write to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("tourUser").child(user);
        uid = myRef.push().getKey();
        data.setTourUid(uid);
        myRef.child("event").child(uid).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddTourActivity.this, "Tour info saved successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ShowActivity.class));
                } else {
                    Toast.makeText(AddTourActivity.this, "Tour info not save successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}






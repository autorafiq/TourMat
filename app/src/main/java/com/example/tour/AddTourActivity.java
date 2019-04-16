package com.example.tour;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.example.tour.databinding.ActivityAddTourBinding;
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

import maes.tech.intentanim.CustomIntent;

public class AddTourActivity extends AppCompatActivity {
    private ActivityAddTourBinding binding;
    private SimpleDateFormat dateSDF = new SimpleDateFormat("dd/MM/yyyy");
    private EditText tourNameET, tourDescriptionET, startDateET, endDateET, budgetET;
    private Button saveTourInfoBtn;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_tour);
        mAuth = FirebaseAuth.getInstance();

        binding.startDateAddTourET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickStartDate();
            }
        });
        binding.endDateAddTourET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickEndDate();
            }
        });
        binding.saveTourInfoAddTourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourName = binding.tourNameET.getText().toString();
                tourDescription = binding.tourDescriptionET.getText().toString();
                budget = Double.valueOf(binding.tourBudgetET.getText().toString());

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

    private void pickEndDate() {
        DatePickerDialog.OnDateSetListener dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String selectedDate1 = day + "/" + month + "/" + year;
                Date date1 = new Date();
                try {
                    date1 = dateSDF.parse(selectedDate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endDate = date1.getTime();
                binding.endDateAddTourET.setText(selectedDate1);

            }
        };
        Calendar calendar1 = Calendar.getInstance();
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH);
        int day = calendar1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog1 = new DatePickerDialog(AddTourActivity.this, dateSetListener1, year, month, day);
        datePickerDialog1.show();
    }

    private void pickStartDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String selectedDate = day + "/" + month + "/" + year;
                Date date = new Date();
                try {
                    date = dateSDF.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startDate = date.getTime();
                binding.startDateAddTourET.setText(selectedDate);

            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTourActivity.this, dateSetListener, year, month, day);
        datePickerDialog.show();
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
                    startActivity(new Intent(AddTourActivity.this, ShowActivity.class));
                    CustomIntent.customType(AddTourActivity.this, "fadein-to-fadeout");
                    finish();
                } else {
                    Toast.makeText(AddTourActivity.this, "Tour info not save successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
}






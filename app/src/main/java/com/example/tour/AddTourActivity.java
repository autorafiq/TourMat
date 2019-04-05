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

import com.example.tour.Data.MainData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTourActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText tourNameET, tourDescriptionET, dateStartET, dateEndET, tourBudgetET;
    private Button saveTourInfoBtn;

    private Calendar myCalendar;
    private DatePickerDialogFragment mDatePickerDialogFragment;
    private String uid;
    private String tourName, tourDescription, budget;
    private long startDate, endDate;
    private float budgetD;

    // Write to the database
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private MainData mainData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);
        tourNameET = findViewById(R.id.tourNameAddTourET);
        tourDescriptionET = findViewById(R.id.tourDescriptionAddTourET);
        dateStartET = findViewById(R.id.startDateAddTourET);
        dateEndET = findViewById(R.id.endDateAddTourET);
        tourBudgetET = findViewById(R.id.budgetAddTourET);
        saveTourInfoBtn = findViewById(R.id.saveTourInfoAddTourBtn);

        mDatePickerDialogFragment = new DatePickerDialogFragment();

        tourName = tourNameET.getText().toString();
        tourDescription = tourDescriptionET.getText().toString();
        budget = tourBudgetET.getText().toString();
        budgetD = 0;


        startDate = mDatePickerDialogFragment.getStartDateLongValue();
        endDate = mDatePickerDialogFragment.getEndDateLongValue();
        uid = getIntent().getStringExtra("uid");
        mainData = new MainData(uid, tourName, tourDescription, startDate, endDate, budgetD);

        dateStartET.setOnClickListener(this);
        dateEndET.setOnClickListener(this);
        saveTourInfoBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.startDateAddTourET) {
            mDatePickerDialogFragment.setFlag(DatePickerDialogFragment.FLAG_START_DATE);
            dateStartET.setText(mDatePickerDialogFragment.getA());
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");


        } else if (id == R.id.endDateAddTourET) {
            mDatePickerDialogFragment.setFlag(DatePickerDialogFragment.FLAG_END_DATE);
            dateEndET.setText(mDatePickerDialogFragment.getA1());
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");

        } else if (id == R.id.saveTourInfoAddTourBtn) {

            // Write to the database
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("tourUser").child("tourInfo");


            mainData.setUid(uid);
            myRef.child(uid).setValue(mainData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddTourActivity.this, "Tour info saved successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddTourActivity.this, "Tour info not save successfully.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }

}
    /*public void saveInfo(View view) {
        startActivity(new Intent(AddTourActivity.this, ShowActivity.class));
    }
}*/



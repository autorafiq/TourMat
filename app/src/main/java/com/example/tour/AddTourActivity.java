package com.example.tour;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTourActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText dateStartET, dateEndET;
    private Calendar myCalendar;

    private DatePickerDialogFragment mDatePickerDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);
        dateStartET = findViewById(R.id.startDateAddTourET);
        dateEndET = findViewById(R.id.endDateAddTourET);

        mDatePickerDialogFragment = new DatePickerDialogFragment();

        dateStartET.setOnClickListener(this);
        dateEndET.setOnClickListener(this);


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

        }


    }



}



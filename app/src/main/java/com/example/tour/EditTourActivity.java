package com.example.tour;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.tour.Data.Data;
import com.example.tour.databinding.ActivityEditTourBinding;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class EditTourActivity extends AppCompatActivity {
    ActivityEditTourBinding binding;

    private String tourId;
    private FirebaseAuth mAuth;
    // Write to the database
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String user;
    private String tourName, tourDescription;
    private long startDate, endDate;
    private double budget;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateSDF = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(EditTourActivity.this, "right-to-left");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_tour);
        tourId = getIntent().getStringExtra("tourUid").toString();
        //Toast.makeText(this, "Tour Id: " + tourId, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getUid();
        // Write to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("tourUser").child(user).child("event").child(tourId);

        getDataFromDB();
        binding.editStartDateAddTourET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickStartDate();
            }
        });
        binding.editEndDateAddTourET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickEndDate();
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
                binding.editEndDateAddTourET.setText(selectedDate1);

            }
        };
        Calendar calendar1 = Calendar.getInstance();
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH);
        int day = calendar1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog1 = new DatePickerDialog(EditTourActivity.this, dateSetListener1, year, month, day);
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
                binding.editStartDateAddTourET.setText(selectedDate);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditTourActivity.this, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void getDataFromDB() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Data readData = dataSnapshot.getValue(Data.class);
                    binding.editTourNameET.setText(readData.getTourName());
                    binding.editTourDescriptionET.setText(readData.getTourDescription());
                    startDate = readData.getStartDate();
                    endDate = readData.getEndDate();
                    binding.editStartDateAddTourET.setText(dateSDF.format(startDate));
                    binding.editEndDateAddTourET.setText(dateSDF.format(endDate));
                    binding.editTourBudgetET.setText(String.valueOf(readData.getBudget()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditTourActivity.this, "Error :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.editSaveTourInfoAddTourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourName = binding.editTourNameET.getText().toString();
                tourDescription = binding.editTourDescriptionET.getText().toString();
                budget = Double.valueOf(binding.editTourBudgetET.getText().toString());

                if (tourName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your tour name.", Toast.LENGTH_SHORT).show();
                } else if (tourDescription.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter something about your tour.", Toast.LENGTH_SHORT).show();
                } else if (budget == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your budget.", Toast.LENGTH_SHORT).show();
                } else if (startDate == 0 || endDate == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your tour date range.", Toast.LENGTH_SHORT).show();
                } else {
                    updateEditedData(new Data(tourName, tourDescription, startDate, endDate, budget));
                }
            }
        });
    }

    private void updateEditedData(Data data) {
        //Toast.makeText(this, ""+data.getStartDate()+data.getEndDate()+data.getTourName()+data.getTourDescription()+data.getBudget(), Toast.LENGTH_SHORT).show();
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("tourName", data.getTourName());
        updateData.put("tourDescription", data.getTourDescription());
        updateData.put("startDate", data.getStartDate());
        updateData.put("endDate", data.getEndDate());
        updateData.put("budget", data.getBudget());
        myRef.updateChildren(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditTourActivity.this, "Tour Info Updated.", Toast.LENGTH_SHORT).show();
                    CustomIntent.customType(EditTourActivity.this, "left-to-right");
                    finish();
                }
            }
        });
    }
}

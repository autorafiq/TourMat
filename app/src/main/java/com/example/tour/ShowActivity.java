package com.example.tour;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tour.Data.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private TourRecyclerViewAdapter tourRecyclerViewAdapter;
    private ArrayList<Data> dataList;
    private String user;
    private Button startDateBtn, endDateBtn, searchBtn;
    private long startDate, endDate;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateSDF = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        recyclerView = findViewById(R.id.recyclerView);
        startDateBtn = findViewById(R.id.searchStartDateBtn);
        endDateBtn = findViewById(R.id.searchEndDateBtn);
        searchBtn = findViewById(R.id.searchDateBtn);
        dataList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser().getUid();
        userRef = database.getReference("tourUser").child(user).child("event");
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        startDateBtn.setText(selectedDate);

                    }
                };
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ShowActivity.this, dateSetListener, year, month, day);
                datePickerDialog.show();





            }

        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        endDateBtn.setText(selectedDate1);

                    }
                };
                Calendar calendar1 = Calendar.getInstance();
                int year = calendar1.get(Calendar.YEAR);
                int month = calendar1.get(Calendar.MONTH);
                int day = calendar1.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(ShowActivity.this, dateSetListener1, year, month, day);
                datePickerDialog1.show();

            }

        });

        // get Data from FireBase Database
        getDataFromDatabase();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.orderByChild("startDate").startAt(String.valueOf(startDate)).endAt(String.valueOf(endDate)).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot searchData: dataSnapshot.getChildren()){
                                Data currentData = searchData.getValue(Data.class);
                                dataList.add(currentData);
                                tourRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tourRecyclerViewAdapter = new TourRecyclerViewAdapter(dataList, getApplicationContext());
        recyclerView.setAdapter(tourRecyclerViewAdapter);


        getFlottingButton();
    }

    private void getDataFromDatabase() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Data allData = data.getValue(Data.class);
                        dataList.add(allData);
                        tourRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowActivity.this, "Can not read from DataBase.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getFlottingButton() {
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.ic_add_black_24dp);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:

        ImageView detailsIcon = new ImageView(this);
        detailsIcon.setImageResource(R.drawable.ic_details_black_24dp);
        SubActionButton button1 = itemBuilder.setContentView(detailsIcon).build();

        ImageView memoriesIcon = new ImageView(this);
        memoriesIcon.setImageResource(R.drawable.ic_center_focus_weak_black_24dp);
        SubActionButton button2 = itemBuilder.setContentView(memoriesIcon).build();

        ImageView moneyIcon = new ImageView(this);
        moneyIcon.setImageResource(R.drawable.ic_attach_money_black_24dp);
        SubActionButton button3 = itemBuilder.setContentView(moneyIcon).build();

        ImageView logoutIcon = new ImageView(this);
        logoutIcon.setImageResource(R.drawable.ic_call_missed_outgoing_black_24dp);
        SubActionButton button4 = itemBuilder.setContentView(logoutIcon).build();


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                // ...
                .attachTo(actionButton)
                .build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowActivity.this, AddTourActivity.class)); //Go back to home page
                finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowActivity.this, AddMemoriesActivity.class));
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowActivity.this, AddExpenseActivity.class));
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //End user session
                startActivity(new Intent(ShowActivity.this, SingInActivity.class)); //Go back to home page
                finish();
            }
        });

    }

    /*@Override
    public void respond(String data) {
        //Toast.makeText(this, ""+data, Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("tourId", data);
        // set Fragmentclass Arguments
        MemoriesFragment fragobj = new MemoriesFragment();
        fragobj.setArguments(bundle);


    }*/
}

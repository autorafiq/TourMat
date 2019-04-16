package com.example.tour;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.tour.Data.Data;
import com.example.tour.databinding.ActivitySearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class SearchActivity extends AppCompatActivity {
    private long startDate, endDate;
    private ActivitySearchBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;

    private SearAdapter searAdapter;
    private ArrayList<Data> dataList;

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        dataList = new ArrayList<>();
        startDate = getIntent().getLongExtra("startDate", 0);
        endDate = getIntent().getLongExtra("endDate", 0);
        //Toast.makeText(this, "Start : " + startDate + " EndDate : " + endDate, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser().getUid();
        userRef = database.getReference("tourUser").child(user).child("event");
        userRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Data allData = data.getValue(Data.class);
                        long stDate = allData.getStartDate();
                        long enDate = allData.getEndDate();
                        if (stDate == startDate || stDate > startDate) {
                            if (enDate == endDate || enDate < endDate) {
                                Data newData = new Data();
                                newData.setTourName(allData.getTourName());
                                newData.setTourDescription(allData.getTourDescription());
                                newData.setStartDate(allData.getStartDate());
                                newData.setEndDate(allData.getEndDate());
                                newData.setBudget(allData.getBudget());
                                dataList.add(newData);
                                searAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Can not read from DataBase.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.searchRecyclerView.setHasFixedSize(true);
        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searAdapter = new SearAdapter(dataList, getApplicationContext());
        binding.searchRecyclerView.setAdapter(searAdapter);
        binding.backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(SearchActivity.this, "up-to-bottom");
    }
}

package com.example.tour;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tour.Data.TourExpense;
import com.example.tour.databinding.ActivityAddExpenseBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class AddExpenseActivity extends AppCompatActivity {
    ActivityAddExpenseBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<TourExpense> dataList;
    private String eventId, userId;
    private double budget, remainBudget;

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(AddExpenseActivity.this, "right-to-left");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_expense);
        eventId = getIntent().getStringExtra("tourUidA");
        budget = getIntent().getDoubleExtra("budget", 0.0);
        //remainBudget=getIntent().getDoubleExtra("remainBudget", 0.0);
        Toast.makeText(this, ""+eventId+budget+"  / "+remainBudget, Toast.LENGTH_SHORT).show();
        dataList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        myRef = database.getReference("tourUser").child(userId).child("event").child(eventId);
        binding.saveTourCostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTourToDatabase();
            }
        });
        binding.showTourCostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddExpenseActivity.this, ExpenseShowActivity.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("budget",budget);
                intent.putExtra("remainBudget", remainBudget);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CustomIntent.customType(AddExpenseActivity.this, "left-to-right");
            }
        });

    }

    private void saveTourToDatabase() {
        String expenseDescription = binding.tourExpenseDescriptionET.getText().toString().trim();
        String tourCost = binding.tourCostET.getText().toString();
        double cheekTourCost = Double.parseDouble(tourCost);
        //cheekBlance(tourCost);
        if (budget == cheekTourCost || budget > cheekTourCost) {
            getRemainingBalance();

            if (remainBudget == cheekTourCost || remainBudget > cheekTourCost){
                Toast.makeText(this, ""+remainBudget, Toast.LENGTH_SHORT).show();
                TourExpense tourExpense = new TourExpense(expenseDescription, tourCost);
                String expenseId = myRef.push().getKey();
                tourExpense.setCostId(expenseId);
                myRef.child("expenses").child(expenseId).setValue(tourExpense).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddExpenseActivity.this, "Tour expense add successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddExpenseActivity.this, ExpenseShowActivity.class);
                            intent.putExtra("eventId", eventId);
                            intent.putExtra("budget",budget);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            CustomIntent.customType(AddExpenseActivity.this, "left-to-right");
                        }
                    }
                });
            }else {
                Toast.makeText(this, "You can spend only "+remainBudget, Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, "You cross your budget.", Toast.LENGTH_SHORT).show();
        }


    }

    private void getRemainingBalance() {
       DatabaseReference databaseReferenc1 = FirebaseDatabase.getInstance().getReference("tourUser").child(userId).child("event").child(eventId);
       databaseReferenc1.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               remainBudget=Double.parseDouble(dataSnapshot.child("remainBudget").getValue().toString().trim());
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void cheekBlance(String tourCost) {

    }


}

package com.example.tour;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tour.Data.TourExpense;
import com.example.tour.databinding.ActivityUpdateExpenseBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class UpdateExpenseActivity extends AppCompatActivity {
    private String eventId, expenseId, userId;
    private ActivityUpdateExpenseBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private double budget, remainBudget, valueOfCostTaken;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_expense);
        eventId = getIntent().getStringExtra("eventId").toString();
        expenseId = getIntent().getStringExtra("expenseId").toString();
        budget = getIntent().getDoubleExtra("budget", 0.0);

        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("tourUser").child(userId).child("event").child(eventId).child("expenses").child(expenseId);
        //Toast.makeText(this, "" + eventId + "/Expense: " + expenseId, Toast.LENGTH_SHORT).show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists())) {
                    binding.updateTourExpenseDescriptionET.setText(dataSnapshot.child("expenseDescription").getValue().toString());
                    binding.updateTourCostET.setText(dataSnapshot.child("tourCost").getValue().toString());
                    valueOfCostTaken = Double.parseDouble(dataSnapshot.child("tourCost").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.updateShowTourCostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpenseShowActivity.class);
                intent.putExtra("eventId", eventId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        getRemainingBalance();
        binding.updateSaveTourCostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueOfCost = binding.updateTourCostET.getText().toString();
                //Toast.makeText(UpdateExpenseActivity.this, ""+valueOfCost, Toast.LENGTH_SHORT).show();

                double value=remainBudget+valueOfCostTaken;
                if (value == Double.parseDouble(valueOfCost) || value > Double.parseDouble(valueOfCost)) {
                    TourExpense tourExpense = new TourExpense(binding.updateTourExpenseDescriptionET.getText().toString().trim(),valueOfCost );
                    tourExpense.setCostId(expenseId);
                    databaseReference.setValue(tourExpense).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Data updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ExpenseShowActivity.class);
                                intent.putExtra("eventId", eventId);
                                intent.putExtra("budget", budget);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                CustomIntent.customType(UpdateExpenseActivity.this, "right-to-left");
                                finish();
                            }
                        }
                    });
                }else {

                    Toast.makeText(UpdateExpenseActivity.this, "You can spend only "+value+"", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(UpdateExpenseActivity.this, "right-to-left");
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

}

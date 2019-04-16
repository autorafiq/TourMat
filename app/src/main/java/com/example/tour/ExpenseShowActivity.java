package com.example.tour;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.tour.Data.Image;
import com.example.tour.Data.TourExpense;
import com.example.tour.databinding.ActivityExpenseShowBinding;
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

public class ExpenseShowActivity extends AppCompatActivity {
    ActivityExpenseShowBinding binding;
    private String eventId, userId;
    private List<TourExpense> expenseList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ExpenseAdapter expenseAdapter;
    private double budget, balance, remainBudget;


    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(ExpenseShowActivity.this, "right-to-left");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense_show);

        eventId = getIntent().getStringExtra("eventId");
        budget = getIntent().getDoubleExtra("budget", 0.0);
        remainBudget=getIntent().getDoubleExtra("remainBudget", 0.0);
        //Toast.makeText(this, ""+eventId, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        expenseList = new ArrayList<>();
        userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("tourUser").child(userId).child("event").child(eventId);
        databaseReference.child("expenses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    expenseList.clear();
                    double sumOfCost = 0.0;
                    for (DataSnapshot loadedData : dataSnapshot.getChildren()) {
                        TourExpense tourExpense = loadedData.getValue(TourExpense.class);
                        sumOfCost += Double.parseDouble(tourExpense.getTourCost());
                        expenseList.add(tourExpense);
                    }
                    //Toast.makeText(ExpenseShowActivity.this, "Toatal cost  "+sumOfCost, Toast.LENGTH_LONG).show();
                    balance = budget - sumOfCost;
                    binding.totalCostTV.setText(String.valueOf(sumOfCost));
                    binding.balanceAmountTV.setText(String.valueOf(balance));
                    //update balance to event item
                   databaseReference.child("remainBudget").setValue(balance);
                }

                binding.expenseViewPB.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ExpenseShowActivity.this, "Error :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                binding.expenseViewPB.setVisibility(View.INVISIBLE);
            }
        });
        binding.expenseRecyclerView.setHasFixedSize(true);
        binding.expenseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(ExpenseShowActivity.this, expenseList);
        binding.expenseRecyclerView.setAdapter(expenseAdapter);
        expenseAdapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String text = expenseList.get(position).getExpenseDescription();
                //Toast.makeText(ExpenseShowActivity.this, "" + text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEdit(int position) {
                TourExpense selectEditItem = expenseList.get(position);
                final String editKey = selectEditItem.getCostId();
                //Toast.makeText(ExpenseShowActivity.this, "Edit...", Toast.LENGTH_SHORT).show();
                Intent editIntent = new Intent(ExpenseShowActivity.this, UpdateExpenseActivity.class);
                editIntent.putExtra("eventId", eventId);
                editIntent.putExtra("expenseId", editKey);
                editIntent.putExtra("budget", budget);
                editIntent.putExtra("remainBudget", remainBudget);
                editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(editIntent);
                CustomIntent.customType(ExpenseShowActivity.this, "left-to-right");
            }

            @Override
            public void onDelete(int position) {
                TourExpense selectEditItem = expenseList.get(position);
                final String editKey = selectEditItem.getCostId();
                Toast.makeText(ExpenseShowActivity.this, ""+editKey, Toast.LENGTH_SHORT).show();
                databaseReference.child("expenses").child(editKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ExpenseShowActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                            expenseAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });
    }



}

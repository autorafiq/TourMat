package com.example.tour;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExpenseShowActivity extends AppCompatActivity {
    ActivityExpenseShowBinding binding;
    private String eventId;
    private List<TourExpense> expenseList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense_show);

        eventId = getIntent().getStringExtra("eventId");
        //Toast.makeText(this, ""+eventId, Toast.LENGTH_SHORT).show();
        binding.expenseRecyclerView.setHasFixedSize(true);
        binding.expenseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        expenseList = new ArrayList<>();
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("tourUser").child(userId).child("event").child(eventId).child("expenses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    expenseList.clear();
                    for (DataSnapshot loadedData : dataSnapshot.getChildren()) {
                        TourExpense tourExpense = loadedData.getValue(TourExpense.class);
                        expenseList.add(tourExpense);

                    }

                }

                binding.expenseViewPB.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ExpenseShowActivity.this, "Error :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                binding.expenseViewPB.setVisibility(View.INVISIBLE);
            }
        });

        expenseAdapter = new ExpenseAdapter(ExpenseShowActivity.this, expenseList);
        binding.expenseRecyclerView.setAdapter(expenseAdapter);
        expenseAdapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String text = expenseList.get(position).getExpenseDescription();
                Toast.makeText(ExpenseShowActivity.this, "" + text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEdit(int position) {
                Toast.makeText(ExpenseShowActivity.this, "Edit...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(int position) {
                Toast.makeText(ExpenseShowActivity.this, "Delete...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

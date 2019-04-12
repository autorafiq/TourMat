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

public class UpdateExpenseActivity extends AppCompatActivity {
    private String eventId, expenseId;
    ActivityUpdateExpenseBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_expense);
        eventId = getIntent().getStringExtra("eventId").toString();
        expenseId = getIntent().getStringExtra("expenseId").toString();
        mAuth = FirebaseAuth.getInstance();

        String userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("tourUser").child(userId).child("event").child(eventId).child("expenses").child(expenseId);
        //Toast.makeText(this, "" + eventId + "/Expense: " + expenseId, Toast.LENGTH_SHORT).show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists())) {
                    binding.updateTourExpenseDescriptionET.setText(dataSnapshot.child("expenseDescription").getValue().toString());
                    binding.updateTourCostET.setText(dataSnapshot.child("tourCost").getValue().toString());
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
        binding.updateSaveTourCostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourExpense tourExpense = new TourExpense(binding.updateTourExpenseDescriptionET.getText().toString(), binding.updateTourCostET.getText().toString());
                tourExpense.setCostId(expenseId);
                databaseReference.setValue(tourExpense).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Data updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }
}

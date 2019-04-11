package com.example.tour;


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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    ActivityAddExpenseBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<TourExpense> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_expense);

        dataList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding.saveTourCostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTourToDatabase();
            }
        });

    }

    private void saveTourToDatabase() {
        String expenseDescription = binding.tourExpenseDescriptionET.getText().toString().trim();
        double tourCost = Double.parseDouble(binding.tourCostET.getText().toString());
        String userId = mAuth.getCurrentUser().getUid();
        String eventId = getIntent().getStringExtra("tourUid");
        myRef = database.getReference("tourUser").child(userId).child("event").child(eventId);
        TourExpense tourExpense = new TourExpense(expenseDescription,tourCost);
        String expenseId = myRef.push().getKey();
        tourExpense.setCostId(expenseId);
        myRef.child("expenses").child(eventId).setValue(tourExpense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AddExpenseActivity.this, "Tour expense add successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

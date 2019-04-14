package com.example.tour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tour.Data.Data;
import com.example.tour.databinding.ShowTourItemBinding;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class TourRecyclerViewAdapter extends RecyclerView.Adapter<TourRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Data> dataList;
    private DatePickerDialogFragment mDatePickerDialogFragment = new DatePickerDialogFragment();
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private String user;
    public TourRecyclerViewAdapter(List<Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ShowTourItemBinding showTourItemBinding = ShowTourItemBinding.inflate(layoutInflater, viewGroup, false);
        return new ViewHolder(showTourItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Data currentData = dataList.get(i);
        viewHolder.binding.tourNameTV.setText(currentData.getTourName());
        viewHolder.binding.tourDescriptionTV.setText(currentData.getTourDescription());
        viewHolder.binding.startDateTV.setText(mDatePickerDialogFragment.getDateFormate(currentData.getStartDate()));
        viewHolder.binding.endDateTV.setText(mDatePickerDialogFragment.getDateFormate(currentData.getEndDate()));
        viewHolder.binding.budgetTV.setText(String.valueOf(currentData.getBudget()));
        viewHolder.binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, EditTourActivity.class);
                intent.putExtra("tourUid", currentData.getTourUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                CustomIntent.customType(context, "left-to-right");
            }
        });

        viewHolder.binding.memoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddMemoriesActivity.class);
                intent.putExtra("tourUid", currentData.getTourUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                CustomIntent.customType(context, "left-to-right");
            }

        });

        viewHolder.binding.budgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intentA = new Intent(context, AddExpenseActivity.class);
                intentA.putExtra("tourUidA", currentData.getTourUid());
                intentA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentA);
                CustomIntent.customType(context, "left-to-right");
            }
        });
        viewHolder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                user = mAuth.getCurrentUser().getUid();
                String selectecItem = currentData.getTourUid();
                userRef = database.getReference("tourUser").child(user).child("event");
                userRef.child(selectecItem).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Item Deleted.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //private TextView nameTV, descriptionTV, startDateTV, endDateTV, budgetTV;
        private ShowTourItemBinding binding;
        public ViewHolder(@NonNull ShowTourItemBinding showTourItemBinding) {
            super(showTourItemBinding.getRoot());
            binding = showTourItemBinding;
        }
    }
}

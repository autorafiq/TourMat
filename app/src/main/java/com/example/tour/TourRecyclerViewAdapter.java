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
import com.google.firebase.auth.FirebaseAuth;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.List;

public class TourRecyclerViewAdapter extends RecyclerView.Adapter<TourRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Data> dataList;
    private DatePickerDialogFragment mDatePickerDialogFragment = new DatePickerDialogFragment();
    private FirebaseAuth mAuth;
//    FragmentCommunication fragmentCommunication;

    public TourRecyclerViewAdapter(List<Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
//        this.fragmentCommunication=fragmentCommunication;
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
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.binding.memoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, AddMemoriesActivity.class);
                intent.putExtra("tourUid", currentData.getTourUid());
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

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
            }
        });
        viewHolder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
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

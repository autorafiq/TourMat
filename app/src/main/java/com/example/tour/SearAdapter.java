package com.example.tour;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tour.Data.Data;
import com.example.tour.databinding.ShowSearchItemBinding;
import com.example.tour.databinding.ShowTourItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class SearAdapter extends RecyclerView.Adapter<SearAdapter.ViewHolder> {
    private Context context;
    private List<Data> dataList;
    private SimpleDateFormat dateSDF = new SimpleDateFormat("dd/MM/yyyy");
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private String user;
    public SearAdapter(List<Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ShowSearchItemBinding searchItemBinding = ShowSearchItemBinding.inflate(layoutInflater, viewGroup, false);
        return new ViewHolder(searchItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearAdapter.ViewHolder viewHolder, int i) {
        final Data currentData = dataList.get(i);
        viewHolder.binding.searchTourNameTV.setText(currentData.getTourName());
        viewHolder.binding.searchTourDescriptionTV.setText(currentData.getTourDescription());
        viewHolder.binding.searchStartDateTV.setText(dateSDF.format(currentData.getStartDate()));
        viewHolder.binding.searchEndDateTV.setText(dateSDF.format(currentData.getEndDate()));
        viewHolder.binding.searchBudgetTV.setText(String.valueOf(currentData.getBudget()));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ShowSearchItemBinding binding;
        public ViewHolder(@NonNull ShowSearchItemBinding searchItemBinding) {
            super(searchItemBinding.getRoot());
            binding = searchItemBinding;
        }

    }
}

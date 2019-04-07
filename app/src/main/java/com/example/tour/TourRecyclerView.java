package com.example.tour;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tour.Data.Data;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.List;

public class TourRecyclerView extends RecyclerView.Adapter<TourRecyclerView.ViewHolder> {
    private Context context;
    private List<Data> dataList;
    private DatePickerDialogFragment mDatePickerDialogFragment = new DatePickerDialogFragment();

    public TourRecyclerView(List<Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.show_tour_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Data currentData = dataList.get(i);
        viewHolder.nameTV.setText(currentData.getTourName());
        viewHolder.descriptionTV.setText(currentData.getTourDescription());
        viewHolder.startDateTV.setText(mDatePickerDialogFragment.getDateFormate(currentData.getStartDate()));
        viewHolder.endDateTV.setText(mDatePickerDialogFragment.getDateFormate(currentData.getEndDate()));
        viewHolder.budgetTV.setText(String.valueOf(currentData.getBudget()));



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTV, descriptionTV, startDateTV, endDateTV, budgetTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.tourNameTV);
            descriptionTV = itemView.findViewById(R.id.tourDescriptionTV);
            startDateTV = itemView.findViewById(R.id.startDateTV);
            endDateTV = itemView.findViewById(R.id.endDateTV);
            budgetTV = itemView.findViewById(R.id.budgetTV);

        }
    }
}

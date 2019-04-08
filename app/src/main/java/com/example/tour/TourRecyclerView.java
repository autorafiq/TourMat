package com.example.tour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tour.Data.Data;
import com.example.tour.databinding.ShowTourItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.List;

public class TourRecyclerView extends RecyclerView.Adapter<TourRecyclerView.ViewHolder> {

    private Context context;
    private List<Data> dataList;
    private DatePickerDialogFragment mDatePickerDialogFragment = new DatePickerDialogFragment();
    private FirebaseAuth mAuth;

    public TourRecyclerView(List<Data> dataList, Context context) {
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Data currentData = dataList.get(i);
        viewHolder.binding.tourNameTV.setText(currentData.getTourName());
        viewHolder.binding.tourDescriptionTV.setText(currentData.getTourDescription());
        viewHolder.binding.startDateTV.setText(mDatePickerDialogFragment.getDateFormate(currentData.getStartDate()));
        viewHolder.binding.endDateTV.setText(mDatePickerDialogFragment.getDateFormate(currentData.getEndDate()));
        viewHolder.binding.budgetTV.setText(String.valueOf(currentData.getBudget()));

        ImageView icon = new ImageView(context); // Create an icon
        icon.setImageResource(R.drawable.ic_add_black_24dp);

        FloatingActionButton actionButton = new FloatingActionButton.Builder((Activity)context)
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder((Activity)context);
        // repeat many times:

        ImageView detailsIcon = new ImageView(context);
        detailsIcon.setImageResource(R.drawable.ic_details_black_24dp);
        SubActionButton button1 = itemBuilder.setContentView(detailsIcon).build();

        ImageView memoriesIcon = new ImageView(context);
        memoriesIcon.setImageResource(R.drawable.ic_center_focus_weak_black_24dp);
        SubActionButton button2 = itemBuilder.setContentView(memoriesIcon).build();

        ImageView moneyIcon = new ImageView(context);
        moneyIcon.setImageResource(R.drawable.ic_attach_money_black_24dp);
        SubActionButton button3 = itemBuilder.setContentView(moneyIcon).build();

        ImageView logoutIcon = new ImageView(context);
        logoutIcon.setImageResource(R.drawable.ic_call_missed_outgoing_black_24dp);
        SubActionButton button4 = itemBuilder.setContentView(logoutIcon).build();


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder((Activity)context)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                // ...
                .attachTo(actionButton)
                .build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //End user session
                context.startActivity(new Intent(context, SingInActivity.class)); //Go back to home page
                /*finish();*/
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

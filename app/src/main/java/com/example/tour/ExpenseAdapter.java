package com.example.tour;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tour.Data.Image;
import com.example.tour.Data.TourExpense;
import com.example.tour.databinding.ShowExpenseListBinding;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private Context context;
    private List<TourExpense> expenseList;
    private OnItemClickListener listener;

    public ExpenseAdapter(Context context, List<TourExpense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ShowExpenseListBinding showExpenseListBinding = ShowExpenseListBinding.inflate(layoutInflater,viewGroup,false);
        return new ViewHolder(showExpenseListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TourExpense expense = expenseList.get(i);
        viewHolder.binding.expenseTypeTV.setText(expense.getExpenseDescription());
        viewHolder.binding.amountTV.setText(String.valueOf(expense.getTourCost()));

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ShowExpenseListBinding binding;
        public ViewHolder(@NonNull ShowExpenseListBinding showExpenseListBinding) {
            super(showExpenseListBinding.getRoot());
            binding=showExpenseListBinding;
            showExpenseListBinding.getRoot().setOnClickListener(this);
            showExpenseListBinding.getRoot().setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener!=null){
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Please select below");
            MenuItem edit = menu.add(Menu.NONE,1,1,"Edit");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");
            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (listener!=null){
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            listener.onEdit(position);
                            return true;
                        case 2:
                            listener.onDelete(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onEdit(int position);
        void onDelete(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}

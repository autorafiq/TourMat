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

import com.example.tour.Data.Data;
import com.example.tour.Data.Image;
import com.example.tour.databinding.ShowMemoriesListBinding;
import com.example.tour.databinding.ShowTourItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private List<Image> imageList;
    private OnItemClickListener listener;


    public ImageAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ShowMemoriesListBinding showMemoriesListBinding = ShowMemoriesListBinding.inflate(layoutInflater, viewGroup, false);
        return new ViewHolder(showMemoriesListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Image currentImage = imageList.get(i);
        viewHolder.binding.cardTextView.setText(currentImage.getImageCaption());
        Picasso.with(context).load(currentImage.getImageUri())
                .placeholder(R.mipmap.ic_launcher_round)
                .fit().centerCrop().into(viewHolder.binding.cardImageViewId);


    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private ShowMemoriesListBinding binding;

        public ViewHolder(@NonNull ShowMemoriesListBinding showMemoriesListBinding) {
            super(showMemoriesListBinding.getRoot());
            binding = showMemoriesListBinding;
            showMemoriesListBinding.getRoot().setOnClickListener(this);
            showMemoriesListBinding.getRoot().setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int positon = getAdapterPosition();
                if (positon != RecyclerView.NO_POSITION) {
                    listener.OnItemClick(positon);
                }

            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Item");
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (listener != null) {
                int positon = getAdapterPosition();
                if (positon != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()){
                        case 1:
                            listener.OnEdit(positon);
                            return true;
                        case 2:
                            listener.OnDelete(positon);
                            return true;
                    }
                }

            }

            return false;
    }
}

public interface OnItemClickListener {
    void OnItemClick(int position);
    void OnEdit(int position);
    void OnDelete(int position);

}

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

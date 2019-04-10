package com.example.tour;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ShowMemoriesListBinding binding;

        public ViewHolder(@NonNull ShowMemoriesListBinding showMemoriesListBinding) {
            super(showMemoriesListBinding.getRoot());
            binding = showMemoriesListBinding;
        }
    }
}

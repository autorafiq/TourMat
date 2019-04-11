package com.example.tour;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tour.Data.Data;
import com.example.tour.Data.Image;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemoriesFragment extends Fragment {
/*private RecyclerView imageRecyclerView;
private ImageAdapter imageAdapter;
private List<Image>  imageList;
private DatabaseReference databaseReference;*/

//    Bundle arguments;

    public MemoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memories, container, false);
        /*imageRecyclerView=view.findViewById(R.id.imageRecyclerView);
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        imageList=new ArrayList<>();*/
        /*//tourId
        String tourId = getArguments().getString("tourId");*/

       /* String userId = mAuth.getCurrentUser().getUid();
        String eventId = getIntent().getStringExtra("tourUid");
        myRef = database.getReference("tourUser").child(userId).child("event").child(eventId);*/

        //databaseReference= FirebaseDatabase.getInstance().getReference().child()

        /*final FragmentActivity c = getActivity();
        recyclerV = container.findViewById(R.id.recyclerView);

        ArrayList<Data> dataArrayList = new ArrayList<>();
        dataArrayList.add(new Data("Test", "Local", 1212121201, 1234567890, 50));
        recyclerV.setLayoutManager(new LinearLayoutManager());
        TourRecyclerViewAdapter adapter = new TourRecyclerViewAdapter(dataArrayList, getContext());
        recyclerV.setAdapter(adapter);
        //  set item animator to DefaultAnimator
        recyclerV.setItemAnimator(new DefaultItemAnimator());*/
        return view;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arguments = getArguments();


        if (arguments!=null){
            String name= arguments.get("tourUid").toString();
            Toast.makeText(getActivity(), ""+name, Toast.LENGTH_SHORT).show();

            }
    }*/
}

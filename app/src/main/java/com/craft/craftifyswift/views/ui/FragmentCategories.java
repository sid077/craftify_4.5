package com.craft.craftifyswift.views.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.craft.craftifyswift.R;
import com.craft.craftifyswift.models.WallpaperCategoryPojo;
import com.craft.craftifyswift.viewmodel.MainViewModel;
import com.craft.craftifyswift.views.adapter.CatagoryRecyclerVIewAdapter;

import java.util.ArrayList;


public class FragmentCategories extends Fragment {
    private RecyclerView recyclerView ;
    private MainViewModel viewModel;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.catagory_fragment,container,false);
        recyclerView = root.findViewById(R.id.recyclerViewCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false));
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.fetchCategory();
       final Observer<ArrayList<WallpaperCategoryPojo>> observer = new Observer<ArrayList<WallpaperCategoryPojo>>() {
            @Override
            public void onChanged(ArrayList<WallpaperCategoryPojo> arrayList) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                recyclerView.setAdapter(new CatagoryRecyclerVIewAdapter(getActivity().getApplicationContext(),getFragmentManager(),arrayList));


            }
        };
        viewModel.getLiveDataCatagories().observe(this,observer);
        int x=0;
    }


}

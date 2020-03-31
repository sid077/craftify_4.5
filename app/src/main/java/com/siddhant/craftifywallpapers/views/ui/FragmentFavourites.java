package com.siddhant.craftifywallpapers.views.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.repositories.AppDatabase;
import com.siddhant.craftifywallpapers.viewmodel.MainViewModel;
import com.siddhant.craftifywallpapers.views.adapter.FavoriteRecyclerViewAdapter;

import java.util.List;

public class FragmentFavourites extends Fragment {
    private RecyclerView recyclerView ;
    MainViewModel viewModel;
    List<WallpaperFavPojo> wallpaperFavPojoList;
    private AppDatabase database;
    private MainActivity mainActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.favorite_fragment,container,false);
        recyclerView = root.findViewById(R.id.recyclerViewFavorite);

        mainActivity = (MainActivity) getActivity();
        viewModel = mainActivity.viewModel;
        database = mainActivity.getDatabase();

int x=0;
        final Context context = getActivity().getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                viewModel.setAllFav(database);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                       // viewModel.setWallpaperFavLiveData();
                        Observer<List<WallpaperFavPojo>> wallpaperFavPojoObserver = new Observer<List<WallpaperFavPojo>>() {
                            @Override
                            public void onChanged(final List<WallpaperFavPojo> wallpaperFavPojos) {
                                wallpaperFavPojoList = wallpaperFavPojos;
                                Log.i("nn","datachanged");
                                recyclerView.setLayoutManager(new GridLayoutManager(mainActivity.getApplicationContext(),2));
                                recyclerView.setAdapter(new FavoriteRecyclerViewAdapter(mainActivity.getApplicationContext(),wallpaperFavPojoList,viewModel, database,getFragmentManager()));
                                recyclerView.getAdapter().notifyDataSetChanged();

                            }
                        };
                     viewModel.getWallpaperFavLiveData().observe(FragmentFavourites.this,wallpaperFavPojoObserver);
                    }
                });



            }
        }).start();


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),2));
        recyclerView.setAdapter(new FavoriteRecyclerViewAdapter(getActivity().getApplicationContext(),wallpaperFavPojoList, viewModel,database, getFragmentManager()));


        Log.i("nn","oncreate");



        return root;

    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerView.getAdapter().notifyDataSetChanged();
        Log.i("mm","onstart");


    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.getAdapter().notifyDataSetChanged();
        Log.i("mm","onres");
    }

    @Override
    public void onDestroyView() {
        Log.i("called","ondestory() fav");
        super.onDestroyView();
//        if(database !=null && viewModel!=null){
//            viewModel.closeDatabase(database);
//        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // viewModel.setWallpaperFavLiveData();
    }

}

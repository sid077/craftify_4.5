package com.siddhant.craftifywallpapers.views.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.WallpaperApiResponsePojo;
import com.siddhant.craftifywallpapers.models.WallpaperPBPojo;
import com.siddhant.craftifywallpapers.views.adapter.PBWallpapersRecyclerViewAdapter;
import com.siddhant.craftifywallpapers.views.adapter.TrendingRecyclerViewAdapter;

import java.util.List;

public class BottomSheetSpecialWalp extends BottomSheetDialogFragment {
    RecyclerView recyclerViewPb,recyclerViewPexels;
   // MainActivity mainActivity ;
    private CardView cardViewTrending;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_all_trending,container,false);
       cardViewTrending = view.findViewById(R.id.cardViewTrending);
        recyclerViewPb = view.findViewById(R.id.recyclerViewPB);
//        recyclerViewPexels = view.findViewById(R.id.recyclerViewPexels);
//       mainActivity = (MainActivity) getActivity();
//        Bundle bundle = getArguments();
//        String query = bundle.getString("cat","Wallpapers");
//        mainActivity.viewModel.loadPbPhotos(query,"vertical",null,0,0,null,null);
//        if(list!=null){
//            recyclerViewPb.setAdapter(new PBWallpapersRecyclerViewAdapter(getContext(),getFragmentManager(),list,mainActivity.viewModel,mainActivity.getDatabase()));
//            recyclerViewPb.setLayoutManager(new GridLayoutManager(getContext(),2));
//        }

//        if(recyclerViewPexels.getVisibility()==View.VISIBLE) {
//            mainActivity.viewModel.loadTrending("Wallpapers", null);
//            final Observer<WallpaperApiResponsePojo> responsePojoObserver = new Observer<WallpaperApiResponsePojo>() {
//                @Override
//                public void onChanged(WallpaperApiResponsePojo wallpaperApiResponsePojo) {
//
//                    recyclerViewPexels.setAdapter(new TrendingRecyclerViewAdapter(getActivity().getApplicationContext(), getFragmentManager(), wallpaperApiResponsePojo, mainActivity.viewModel, mainActivity.getDatabase()));
//                    //layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
//                    recyclerViewPexels.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//                    //    progressBar.setVisibility(View.GONE);
//
//                }
//
//            };
//            mainActivity.viewModel.getLiveData().observe(this, responsePojoObserver);
      //  }
       return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cardViewTrending.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        String query = bundle.getString("cat","Wallpapers");
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.viewModel.loadPbPhotos(query,"vertical",null,0,0,null,null);
        cardViewTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTrending fragment = new FragmentTrending();
                Bundle bundle = new Bundle();
                bundle.putString("query", "Wallpapers");
                bundle.putString("category", "Wallpapers");
                fragment.setArguments(bundle);
                fragment.show(getFragmentManager(), "category");
            }
        });
        mainActivity.viewModel.mutableLiveDataWallpaperPB.observe(BottomSheetSpecialWalp.this, new Observer<List<WallpaperPBPojo.Hits>>() {
            @Override
            public void onChanged(List<WallpaperPBPojo.Hits> hits) {
                recyclerViewPb.setAdapter(new PBWallpapersRecyclerViewAdapter(getContext(),getFragmentManager(),hits,mainActivity.viewModel,mainActivity.getDatabase()));
            recyclerViewPb.setLayoutManager(new GridLayoutManager(getContext(),2));
            }
        });
    }
}

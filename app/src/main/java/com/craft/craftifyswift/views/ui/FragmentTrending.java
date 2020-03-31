package com.craft.craftifyswift.views.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.craft.craftifyswift.models.WallpaperApiResponsePojo;
import com.craft.craftifyswift.viewmodel.MainViewModel;
import com.craft.craftifyswift.views.adapter.TrendingRecyclerViewAdapter;
import com.craft.craftifyswift.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.craft.craftifyswift.R;


public class FragmentTrending extends BottomSheetDialogFragment {
    private RecyclerView recyclerView ;
    private MainViewModel viewModel;
    private String query;
    private MainActivity mainActivity;
    private ProgressBar progressBar;
    private GridLayoutManager layoutManager;
    private int pastVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean loading= true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.trending_fragment,container,false);
        progressBar = root.findViewById(R.id.progressBarTrending);
        progressBar.setVisibility(View.VISIBLE);

        Log.i("called","oncreate");
        recyclerView = root.findViewById(R.id.recyclerviewTrendingMain);
        Bundle bundle = getArguments();
        query = bundle.getString("query");
        mainActivity = (MainActivity) getActivity();
        viewModel = mainActivity.viewModel;
        viewModel.loadTrending(query,progressBar);
        final Observer<WallpaperApiResponsePojo> responsePojoObserver = new Observer<WallpaperApiResponsePojo>() {
            @Override
            public void onChanged(WallpaperApiResponsePojo wallpaperApiResponsePojo) {

                recyclerView.setAdapter(new TrendingRecyclerViewAdapter(getActivity().getApplicationContext(),getFragmentManager(),wallpaperApiResponsePojo,viewModel,mainActivity.getDatabase()));
                layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
                recyclerView.setLayoutManager(layoutManager);
            //    progressBar.setVisibility(View.GONE);

            }

        };
        viewModel.getLiveData().observe(this,responsePojoObserver);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                  if(dy>0){
                      visibleItemCount = layoutManager.getChildCount();
                      totalItemCount = layoutManager.getItemCount();
                      pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                      if(loading){
                          if(visibleItemCount+pastVisibleItem>=totalItemCount){
                              loading = false;
                              Log.d("now","load");
                          }
                      }
                  }
                }
            });
        return root;

    }

    @Override
    public void onStart() {
        super.onStart();







    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


}

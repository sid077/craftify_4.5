package com.siddhant.craftifywallpapers.views.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.siddhant.craftifywallpapers.R;

public class InfoBottomSheet extends BottomSheetDialogFragment {
    TextView wallpaperUrl,heigth,width,photographerName,photographerUrl;
    AdView adViewBannner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_bottom_sheet,container,false);
        wallpaperUrl = view.findViewById(R.id.textViewScUrlInfo);
        heigth = view.findViewById(R.id.textViewHeightInfo);
        width= view.findViewById(R.id.textViewWidthInfo);
        photographerName = view.findViewById(R.id.textViewPhotographerNameInfo);
        photographerUrl = view.findViewById(R.id.textViewPhotographerUrlInfo);

        adViewBannner = view.findViewById(R.id.adView2);

        adViewBannner.loadAd(new AdRequest.Builder().build());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        String photgrapherUrl = bundle.getString("photoGrapherUrl");
      String height=  bundle.getString("height");
       String width = bundle.getString("width");
       String photoUrl= bundle.getString("photoUrl");
        String photoGrapherName = bundle.getString("photographer");
        wallpaperUrl.setText(photoUrl);
        this.heigth.setText(height);
        this.width.setText(width);
        this.photographerUrl.setText(photgrapherUrl);
        this.photographerName.setText(photoGrapherName);




    }
}

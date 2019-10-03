package com.siddhant.craftifywallpapers.views.ui;

import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.siddhant.craftifywallpapers.R;

public class BottomSheetWallpaperType extends BottomSheetDialogFragment {
    private CardView cardViewPortrait,cardViewLandscape,cardViewOriginal;
    ImageView imageViewPortrait,imageViewLandscape,imageOriginal;
    PreviewWallpaperActivity previewWallpaperActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallpaper_type_bottom_sheet,container,false);
        previewWallpaperActivity = (PreviewWallpaperActivity) getActivity();
        cardViewOriginal = view.findViewById(R.id.cardViewOriginal);
        cardViewPortrait = view.findViewById(R.id.cardViewPortrait);
        cardViewLandscape = view.findViewById(R.id.cardViewLandScape);
        imageViewLandscape = view.findViewById(R.id.imageButtonSetWallpaperLandscape);
        imageOriginal = view.findViewById(R.id.imageButtonSetWallpaperOriginal);
        imageViewPortrait = view.findViewById(R.id.imageButtonSetWallpaperPortrait);
        if(MainActivity.isNightModeEnabled)
        {
            imageViewPortrait.setImageResource(R.drawable.ic_collections_white_24dp);
            imageOriginal.setImageResource(R.drawable.ic_wallpaper_white_24dp);
            imageViewLandscape.setImageResource(R.drawable.ic_landscape_white_24dp);
        }

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        cardViewPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewWallpaperActivity.progressBar.setVisibility(View.VISIBLE);
                // progressBar.setProgress(0);
                Point p = new Point();
                WindowManager windowManager = previewWallpaperActivity.getWindowManager();
                windowManager.getDefaultDisplay().getSize(p);
                final int height = p.y, width = p.x;
                previewWallpaperActivity.setAsWallpaper(0,width,height);
                dismiss();

            }
        });
        cardViewOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewWallpaperActivity.progressBar.setVisibility(View.VISIBLE);
                //  progressBar.setProgress(0);
                previewWallpaperActivity.setAsWallpaper(1,0,0);
                dismiss();

            }
        });
        cardViewLandscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewWallpaperActivity.progressBar.setVisibility(View.VISIBLE);
                //  progressBar.setProgress(0);
                previewWallpaperActivity.setAsWallpaper(2,0,0);
                dismiss();
            }
        });
    }

}

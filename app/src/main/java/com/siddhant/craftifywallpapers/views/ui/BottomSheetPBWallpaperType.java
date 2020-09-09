package com.siddhant.craftifywallpapers.views.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.siddhant.craftifywallpapers.R;

public class BottomSheetPBWallpaperType extends BottomSheetDialogFragment {
    private CardView cardViewPortrait,cardViewLandscape,cardViewOriginal;
    ImageView imageViewPortrait,imageViewLandscape,imageOriginal;
    PreviewPBWallpaperActivity previewPBWallpaperActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallpaper_type_bottom_sheet,container,false);
        previewPBWallpaperActivity = (PreviewPBWallpaperActivity) getActivity();
        cardViewOriginal = view.findViewById(R.id.cardViewOriginal);
        cardViewPortrait = view.findViewById(R.id.cardViewPortrait);
        cardViewLandscape = view.findViewById(R.id.cardViewLandScape);
        imageViewLandscape = view.findViewById(R.id.imageButtonSetWallpaperLandscape);
        imageOriginal = view.findViewById(R.id.imageButtonSetWallpaperOriginal);
        imageViewPortrait = view.findViewById(R.id.imageButtonSetWallpaperPortrait);
        if(MainActivity.isNightModeEnabled)
        {
//            imageViewPortrait.setImageResource(R.drawable.ic_collections_24dp);
//            imageOriginal.setImageResource(R.drawable.ic_wallpaper_24dp);
//            imageViewLandscape.setImageResource(R.drawable.ic_landscape_24dp);
        }

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        cardViewPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewPBWallpaperActivity.progressBar.setVisibility(View.VISIBLE);
                // progressBar.setProgress(0);
                Point p = new Point();
                WindowManager windowManager = previewPBWallpaperActivity.getWindowManager();
                windowManager.getDefaultDisplay().getSize(p);
                final int height = p.y, width = p.x;
                previewPBWallpaperActivity.setAsWallpaper(0,width,height);
                dismiss();

            }
        });
        cardViewOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewPBWallpaperActivity.progressBar.setVisibility(View.VISIBLE);
                //  progressBar.setProgress(0);
                previewPBWallpaperActivity.setAsWallpaper(1,0,0);
                dismiss();

            }
        });
        cardViewLandscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewPBWallpaperActivity.progressBar.setVisibility(View.VISIBLE);
                //  progressBar.setProgress(0);
                previewPBWallpaperActivity.setAsWallpaper(2,0,0);
                dismiss();
            }
        });
    }

}

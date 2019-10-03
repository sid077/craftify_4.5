package com.siddhant.craftifywallpapers.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.repositories.AppDatabase;
import com.siddhant.craftifywallpapers.viewmodel.MainViewModel;
import com.siddhant.craftifywallpapers.views.ui.InfoBottomSheet;
import com.siddhant.craftifywallpapers.views.ui.MainActivity;
import com.siddhant.craftifywallpapers.views.ui.PreviewWallpaperActivity;

import java.io.Serializable;
import java.util.List;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<WallpaperFavPojo>wallpaperFavPojoList;
    MainViewModel mainViewModel;
    AppDatabase database;
    FragmentManager fragmentManager;

    public FavoriteRecyclerViewAdapter(Context context, List<WallpaperFavPojo> wallpaperFavPojoList, MainViewModel mainViewModel, AppDatabase database, FragmentManager fragmentManager) {
        this.context = context;
        this.wallpaperFavPojoList = wallpaperFavPojoList;
        this.mainViewModel = mainViewModel;
        this.database = database;
        this.fragmentManager = fragmentManager;
        int x=0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.favorite_raw,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(MainActivity.isNightModeEnabled)
            holder.imageButtonInfo.setImageResource(R.drawable.ic_info_white_24dp);
        holder.checkbox.setChecked(true);
        Glide.with(context).load(wallpaperFavPojoList.get(position).getPortraitUrl()).thumbnail(0.1f).into(holder.imageViewWallpaper);
        holder.imageViewWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PreviewWallpaperActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("wallpaperFavDetails", (Serializable) wallpaperFavPojoList);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        holder.imageButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photographerUrl=wallpaperFavPojoList.get(position).getPhotographerUrl();
                String height = String.valueOf(wallpaperFavPojoList.get(position).getHeight());
                String width = String.valueOf(wallpaperFavPojoList.get(position).getWidth());
                String photoUrl = String.valueOf(wallpaperFavPojoList.get(position).getPhotographerUrl());
                String photographer = wallpaperFavPojoList.get(position).getPhotographerName();
                Bundle bundle = new Bundle();
                bundle.putString("photoGrapherUrl",photographerUrl);
                bundle.putString("height",height);
                bundle.putString("width",width);
                bundle.putString("photoUrl",photoUrl);
                bundle.putString("photographer",photographer);
                InfoBottomSheet infoBottomSheet = new InfoBottomSheet();
                infoBottomSheet.setArguments(bundle);
                infoBottomSheet.show(fragmentManager,"info");
            }
        });
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mainViewModel.performInsertionFavDb(wallpaperFavPojoList.get(position), database);
                            }
                            catch (SQLiteConstraintException e){
                                e.printStackTrace();
                            }
                            int x =0;
                        }
                    }).start();

                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {




                            mainViewModel.performDeletion(wallpaperFavPojoList.get(position),database);




                        }
                    }).start();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(wallpaperFavPojoList==null)
            return 0;
        return wallpaperFavPojoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButtonInfo;
        ImageView imageViewWallpaper;
        CheckBox checkbox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButtonInfo = itemView.findViewById(R.id.imageButtonInfoFavMain);
            imageViewWallpaper = itemView.findViewById(R.id.imageViewWallFavMain);
            checkbox = itemView.findViewById(R.id.checkboxFavMain);

        }
    }

}

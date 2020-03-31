package com.siddhant.craftifywallpapers.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.WallpaperApiResponsePojo;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.repositories.AppDatabase;
import com.siddhant.craftifywallpapers.viewmodel.MainViewModel;
import com.siddhant.craftifywallpapers.views.ui.InfoBottomSheet;
import com.siddhant.craftifywallpapers.views.ui.MainActivity;
import com.siddhant.craftifywallpapers.views.ui.PreviewWallpaperActivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrendingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    WallpaperApiResponsePojo wallpaperApiResponsePojo;
    private FragmentManager fragmentManager;
    private MainViewModel mainViewModel;
    private final List<WallpaperFavPojo> wallpaperFavPojoList;
    AppDatabase database;


    public TrendingRecyclerViewAdapter(Context context, FragmentManager fragmentManager,
                                       WallpaperApiResponsePojo wallpaperApiResponsePojo, MainViewModel mainViewModel, AppDatabase database) {
        this.context = context;
        this.wallpaperApiResponsePojo = wallpaperApiResponsePojo;
        this.fragmentManager = fragmentManager;
        this.mainViewModel = mainViewModel;
        wallpaperFavPojoList = mainViewModel.getWallpaperFavLiveData().getValue();

        Collections.sort(wallpaperApiResponsePojo.getWallpaperPojos());
        this.database = database;

        int x=0;

    }
    @Override
    public int getItemViewType(int position) {
        if(position %6==0&&position!=0){
            return 1;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType==1) {
            View nativeExpressLayoutView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.ad_view,
                    parent, false);

            return new NativeExpressAdViewHolder(nativeExpressLayoutView);


        }
 view = LayoutInflater.from(context).inflate(R.layout.trending_raw,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);
        if(viewType==1){
            final NativeExpressAdViewHolder nativeExpressHolder = (NativeExpressAdViewHolder) viewHolder;
//
            AdLoader adLoader = new AdLoader.Builder(context, getAdID())
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            NativeTemplateStyle styles = new
                                    NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(context.getResources().getColor(R.color.colorPrimary))).build();


                            nativeExpressHolder.template.setStyles(styles);
                            nativeExpressHolder.template.setNativeAd(unifiedNativeAd);

                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }

        else {

            ViewHolder holder = (ViewHolder) viewHolder;
            if (MainActivity.isNightModeEnabled)
                holder.imageButtonInfo.setImageResource(R.drawable.ic_info_white_24dp);

            if (wallpaperFavPojoList != null) {

                if (mainViewModel.searchForObject(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getId(), wallpaperFavPojoList))

                    holder.checkboxFav.setChecked(true);
            }
            Glide.with(context).load(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getSrcUrl().
                    getPortrait()).thumbnail(0.1f).into(holder.imageViewWallpaper);
            holder.imageButtonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String photographerUrl = wallpaperApiResponsePojo.getWallpaperPojos().get(position).getPhotographerUrl();
                    String height = String.valueOf(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getHeight());
                    String width = String.valueOf(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getWidth());
                    String photoUrl = String.valueOf(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getUrl());
                    String photographer = wallpaperApiResponsePojo.getWallpaperPojos().get(position).getPhotographerName();
                    Bundle bundle = new Bundle();
                    bundle.putString("photoGrapherUrl", photographerUrl);
                    bundle.putString("height", height);
                    bundle.putString("width", width);
                    bundle.putString("photoUrl", photoUrl);
                    bundle.putString("photographer", photographer);
                    InfoBottomSheet infoBottomSheet = new InfoBottomSheet();
                    infoBottomSheet.setArguments(bundle);
                    infoBottomSheet.show(fragmentManager, "info");
                }
            });
            holder.imageViewWallpaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PreviewWallpaperActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("wallpaperDetails", (Serializable) wallpaperApiResponsePojo.getWallpaperPojos());
                    intent.putExtra("position", position);
                    context.startActivity(intent);

                }
            });
            holder.checkboxFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    final WallpaperFavPojo favPojo = new WallpaperFavPojo();
                    favPojo.setHeight(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getHeight());
                    favPojo.setWidth(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getWidth());
                    favPojo.setId(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getId());
                    favPojo.setLandScape(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getSrcUrl().getLandscape());
                    favPojo.setPortraitUrl(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getSrcUrl().getPortrait());
                    favPojo.setPhotographerName(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getPhotographerName());
                    favPojo.setOriginalUrl(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getSrcUrl().getOriginal());
                    favPojo.setPhotographerUrl(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getPhotographerUrl());
                    favPojo.setSmallUrl(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getSrcUrl().getSmall());
                    favPojo.setTinyUrl(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getSrcUrl().getTiny());
                    favPojo.setUrl(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getUrl());
                    favPojo.setUrl(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getSrcUrl().getOriginal());
                    favPojo.setUrl(wallpaperApiResponsePojo.getWallpaperPojos().get(position).getPhotographerUrl());
                    if (isChecked) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mainViewModel.performInsertionFavDb(favPojo, database);
                                } catch (SQLiteConstraintException e) {
                                    e.printStackTrace();
                                }
                                int x = 0;
                            }
                        }).start();

                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {


                                mainViewModel.performDeletion(favPojo, database);
                            }
                        }).start();

                    }
                }
            });

        }

    }



    @Override
    public int getItemCount() {
        return wallpaperApiResponsePojo.getWallpaperPojos().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButtonInfo;
        ImageView imageViewWallpaper;
        CheckBox checkboxFav;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButtonInfo = itemView.findViewById(R.id.imageButtonInfoTrandingRaw);
            imageViewWallpaper = itemView.findViewById(R.id.imageViewWallTreendingRaw);
            checkboxFav = itemView.findViewById(R.id.checkboxFavTrendingRaw);
        }
    }
    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

       private final TemplateView template;

        NativeExpressAdViewHolder(View view) {
            super(view);

            template = view.findViewById(R.id.nativeAd);
            AdRequest adRequest = new AdRequest.Builder().build();


        }
    }
    private String getAdID(){
        int rand = new Random().nextInt(4);
        String [] ids = new String[]{"ca-app-pub-2724635946881674/7224318519","ca-app-pub-2724635946881674/7316543540",
                "ca-app-pub-2724635946881674/6235164658","ca-app-pub-2724635946881674/7554865622"};
        return ids[rand];

    }
}

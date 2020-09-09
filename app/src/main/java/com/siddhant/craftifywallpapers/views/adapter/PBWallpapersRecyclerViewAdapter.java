package com.siddhant.craftifywallpapers.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.WallpaperPBPojo;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.repositories.AppDatabase;
import com.siddhant.craftifywallpapers.viewmodel.MainViewModel;
import com.siddhant.craftifywallpapers.views.ui.InfoBottomSheet;
import com.siddhant.craftifywallpapers.views.ui.MainActivity;
import com.siddhant.craftifywallpapers.views.ui.PreviewPBWallpaperActivity;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class PBWallpapersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    List<WallpaperPBPojo.Hits>wallpaperApiResponsePojo;
    private FragmentManager fragmentManager;
    private MainViewModel mainViewModel;
    private final List<WallpaperFavPojo> wallpaperFavPojoList;
    AppDatabase database;


    public PBWallpapersRecyclerViewAdapter(Context context, FragmentManager fragmentManager,
                                           List<WallpaperPBPojo.Hits> wallpaperApiResponsePojo, MainViewModel mainViewModel, AppDatabase database) {
        this.context = context;
        this.wallpaperApiResponsePojo = wallpaperApiResponsePojo;
        this.fragmentManager = fragmentManager;
        this.mainViewModel = mainViewModel;
        wallpaperFavPojoList = mainViewModel.getWallpaperFavLiveData().getValue();

       // Collections.sort(wallpaperApiResponsePojo.getWallpaperPojos());
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
//        if(viewType==1) {
//            View nativeExpressLayoutView = LayoutInflater.from(
//                    parent.getContext()).inflate(R.layout.ad_view,
//                    parent, false);
//
//            return new NativeExpressAdViewHolder(nativeExpressLayoutView);
//
//
//        }
 view = LayoutInflater.from(context).inflate(R.layout.trending_raw,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);
//        if(viewType==1){
//            final NativeExpressAdViewHolder nativeExpressHolder = (NativeExpressAdViewHolder) viewHolder;
////
//            AdLoader adLoader = new AdLoader.Builder(context, getAdID())
//                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//                        @Override
//                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                            NativeTemplateStyle styles = new
//                                    NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(context.getResources().getColor(R.color.colorPrimary))).build();
//
//
//                            nativeExpressHolder.template.setStyles(styles);
//                            nativeExpressHolder.template.setNativeAd(unifiedNativeAd);
//
//                        }
//                    })
//                    .build();
//
//            adLoader.loadAd(new AdRequest.Builder().build());
//        }



            ViewHolder holder = (ViewHolder) viewHolder;

        Glide.with(context).load(wallpaperApiResponsePojo.get(position).getLargeUrl()
        ).override(((ViewHolder) viewHolder).imageViewWallpaper.getWidth(),((ViewHolder) viewHolder).imageViewWallpaper.getHeight())
        .centerCrop().thumbnail(0.1f).into(holder.imageViewWallpaper);
//            if (MainActivity.isNightModeEnabled)
                holder.imageButtonInfo.setImageResource(R.drawable.ic_info_white_24dp);

//            if (wallpaperFavPojoList != null) {
//
//                if (mainViewModel.searchForObject(wallpaperApiResponsePojo.get(position).getId(), wallpaperFavPojoList))
//
//                    holder.checkboxFav.setChecked(true);
//            }
            if(wallpaperApiResponsePojo.get(position).getLikes()!=null&&Integer.parseInt(wallpaperApiResponsePojo.get(position).getLikes())>=100){

                holder.imageViewPopUlar.setVisibility(View.VISIBLE);
            }

            holder.imageButtonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String photographerUrl = wallpaperApiResponsePojo.get(position).getUserId();
                    String height = String.valueOf(wallpaperApiResponsePojo.get(position).getImageHeight());
                    String width = String.valueOf(wallpaperApiResponsePojo.get(position).getImageWidth());
                    String photoUrl = String.valueOf(wallpaperApiResponsePojo.get(position).getLargeUrl());
                    String photographer = wallpaperApiResponsePojo.get(position).getUser();
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
                    Intent intent = new Intent(context, PreviewPBWallpaperActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("wallpaperDetails", (Serializable) wallpaperApiResponsePojo);
                    intent.putExtra("position", position);
                    context.startActivity(intent);

                }
            });
            holder.checkboxFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    final WallpaperFavPojo favPojo = new WallpaperFavPojo();
                    favPojo.setHeight(Integer.parseInt(wallpaperApiResponsePojo.get(position).getImageHeight()));
                    favPojo.setWidth(Integer.parseInt(wallpaperApiResponsePojo.get(position).getImageWidth()));
                    favPojo.setId(Integer.parseInt(wallpaperApiResponsePojo.get(position).getUserId()));

                    favPojo.setLandScape(wallpaperApiResponsePojo.get(position).getLargeUrl());
             favPojo.setPortraitUrl(wallpaperApiResponsePojo.get(position).getLargeUrl());
                    favPojo.setPhotographerName(wallpaperApiResponsePojo.get(position).getUser());
                    favPojo.setOriginalUrl(wallpaperApiResponsePojo.get(position).getLargeUrl());
                    favPojo.setPortraitUrl(wallpaperApiResponsePojo.get(position).getLargeUrl());
                    favPojo.setLandScape(wallpaperApiResponsePojo.get(position).getLargeUrl());

                  //  favPojo.setPhotographerUrl(wallpaperApiResponsePojo.get(position));
                   // favPojo.setSmallUrl(wallpaperApiResponsePojo.get(position).getSrcUrl().getSmall());
                  //  favPojo.setTinyUrl(wallpaperApiResponsePojo.get(position).getSrcUrl().getTiny());
                  //  favPojo.setUrl(wallpaperApiResponsePojo.get(position).getUrl());
                   // favPojo.setUrl(wallpaperApiResponsePojo.get(position).getSrcUrl().getOriginal());
                  //  favPojo.setUrl(wallpaperApiResponsePojo.get(position).getPhotographerUrl());
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



    @Override
    public int getItemCount() {
        return wallpaperApiResponsePojo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButtonInfo;
        ImageView imageViewWallpaper;
        CheckBox checkboxFav;
       ImageView imageViewPopUlar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButtonInfo = itemView.findViewById(R.id.imageButtonInfoTrandingRaw);
            imageViewWallpaper = itemView.findViewById(R.id.imageViewWallTreendingRaw);
            checkboxFav = itemView.findViewById(R.id.checkboxFavTrendingRaw);
            imageViewPopUlar = itemView.findViewById(R.id.imageViewPopular);
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

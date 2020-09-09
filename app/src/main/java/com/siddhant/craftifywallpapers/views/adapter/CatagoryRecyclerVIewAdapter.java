package com.siddhant.craftifywallpapers.views.adapter;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.WallpaperCategoryPojo;
import com.siddhant.craftifywallpapers.views.ui.FragmentTrending;
import com.siddhant.craftifywallpapers.views.ui.MainActivity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CatagoryRecyclerVIewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   ArrayList<WallpaperCategoryPojo> arrayList ;
    FragmentManager fragmentManager ;
    public CatagoryRecyclerVIewAdapter(Context context, FragmentManager fragmentManager, ArrayList<WallpaperCategoryPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragmentManager = fragmentManager;
    }

    Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
//        if(viewType==1) {
//            View nativeExpressLayoutView = LayoutInflater.from(
//                  parent.getContext()).inflate(R.layout.ad_view,
//                    parent, false);
//
//            return new NativeExpressAdViewHolder(nativeExpressLayoutView);
//
//
//        }

         view = LayoutInflater.from(context).inflate(R.layout.catagories_raw,parent,false);

        return new ViewHolderCat(view);
    }

    @Override
    public int getItemViewType(int position) {
        if(position %6==0&&position!=0){
            return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);
//        if(viewType==1){
//            final NativeExpressAdViewHolder nativeExpressHolder = (NativeExpressAdViewHolder) viewHolder;
////           // NativeExpressAdView adView = (NativeExpressAdView) arrayList.get(position);
////            NativeExpressAdView adView = new NativeExpressAdView(context);
////            adView.setAdUnitId("ca-app-pub-3940256099942544/2247696110");
////            adView.setAdSize(AdSize.BANNER);
////            ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
////            adCardView.addView(adView);
////
////            adView.loadAd(new AdRequest.Builder().addTestDevice("EAE4A7B4BE3B374C2520417ABB8BB617").build());
////            if (adCardView.getChildCount() > 0) {
////                adCardView.removeAllViews();
////            }
////            if (adView.getParent() != null) {
////                ((ViewGroup) adView.getParent()).removeView(adView);
////            }
//            AdLoader adLoader = new AdLoader.Builder(context,getAdID())
//                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//                        @Override
//                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                            NativeTemplateStyle styles = new
//                                    NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(context.getResources().getColor(R.color.colorPrimary))).build();
//
//
//                           nativeExpressHolder.template.setStyles(styles);
//                            nativeExpressHolder.template.setNativeAd(unifiedNativeAd);
//
//                        }
//                    })
//                    .build();
//
//            adLoader.loadAd(new AdRequest.Builder().build());
//        }


//        else {
            final ViewHolderCat holder = (ViewHolderCat) viewHolder;


        Glide.with(context)
                .asBitmap()
                .load(arrayList.get(position).getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(holder.imageView.getWidth(),holder.imageView.getHeight())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        if (resource != null && !resource.isRecycled()) {
                            Palette palette = Palette.from(resource).generate();
                            Palette.Swatch swatch = getMostPopulousSwatch(palette);
                            if(swatch != null) {
                                int startColor = ContextCompat.getColor(context, R.color.gnt_gray);
                                int endColor = swatch.getRgb();


                                   holder.constraintLayout.setBackground(new ColorDrawable(endColor));
                                 //  Bitmap bitmap = Bitmap.createScaledBitmap(resource,holder.imageView.getWidth(),holder.imageView.getHeight(),false);
                                   holder.imageView.setImageBitmap(resource);




                            }
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
            holder.textViewName.setText(arrayList.get(position).getName());
//        Picasso.get().load(arrayList.get(position).getUrl()).fit().into(holder.imageView);
            //Glide.with(context).load(arrayList.get(position).getUrl()).placeholder(R.drawable.th1).thumbnail(0.1f).into(holder.imageView);
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTrending fragment = new FragmentTrending();
                    Bundle bundle = new Bundle();
                    bundle.putString("query", arrayList.get(position).getName());
                    bundle.putString("category", arrayList.get(position).getName());
                    fragment.setArguments(bundle);
                    fragment.show(fragmentManager, "category" + position);

                    try {
                        MainActivity.firebaseAnalytics.setUserProperty("category", arrayList.get(position).getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                }
            });

       // }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderCat extends RecyclerView.ViewHolder {
        TextView textViewName;
        ConstraintLayout constraintLayout;
        ImageView imageView;



        public ViewHolderCat(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewCategoryNameRaw);
            imageView = itemView.findViewById(R.id.imageViewCat);
            constraintLayout = itemView.findViewById(R.id.constraitLayoutCatagory);

        }
    }
    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        private final TemplateView template;

        NativeExpressAdViewHolder(View view) {
            super(view);
            // NativeExpressAdView
//            mAdView = view.findViewById(R.id.nativeAd);
            template = view.findViewById(R.id.nativeAd);
            AdRequest adRequest = new AdRequest.Builder().build();
         //   mAdView.loadAd(adRequest);

        }
    }
    private String getAdID(){
        int rand = new Random().nextInt(4);
        String [] ids = new String[]{"ca-app-pub-2724635946881674/7224318519","ca-app-pub-2724635946881674/7316543540",
                "ca-app-pub-2724635946881674/6235164658","ca-app-pub-2724635946881674/7554865622"};
        return ids[rand];

    }
    Palette.Swatch getMostPopulousSwatch(Palette palette) {
        Palette.Swatch mostPopulous = null;
        if (palette != null) {
            for (Palette.Swatch swatch : palette.getSwatches()) {
                if (mostPopulous == null || swatch.getPopulation() > mostPopulous.getPopulation()) {
                    mostPopulous = swatch;
                }
            }
        }
        return mostPopulous;
    }
}

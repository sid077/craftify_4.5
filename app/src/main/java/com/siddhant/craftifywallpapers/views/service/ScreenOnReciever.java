package com.siddhant.craftifywallpapers.views.service;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.siddhant.craftifywallpapers.models.WallpaperApiResponsePojo;
import com.siddhant.craftifywallpapers.repositories.WallpaperApiService;
import com.siddhant.craftifywallpapers.viewmodel.DownloadImage;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScreenOnReciever extends BroadcastReceiver {
    private WallpaperApiResponsePojo pojo;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.pexels.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            WallpaperApiService wallpaperApiService = retrofit.create(WallpaperApiService.class);
            Call<WallpaperApiResponsePojo> call = wallpaperApiService.getTrendingWallpapers("abstract");

            call.enqueue(new Callback<WallpaperApiResponsePojo>() {
                @Override
                public void onResponse(Call<WallpaperApiResponsePojo> call, Response<WallpaperApiResponsePojo> response) {
                    String code = response.headers().get("X-Ratelimit-Remaining");
                    Log.i("Remaining requests ", code);
                    if (Integer.parseInt(code) <= 0) {
                        return;
                    }
                    pojo = response.body();
                    if (pojo != null) {
                        final WallpaperManager manager = WallpaperManager.getInstance(context);
                        int nos = new Random().nextInt(pojo.getWallpaperPojos().size());
                        Bitmap wallpaper = null;
                        Glide.with(context).asBitmap().load(pojo.getWallpaperPojos().get(nos).getSrcUrl().getPortrait()).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                try {
                                    manager.setBitmap(resource);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
//                        try {
//                            Glide.with(context).as.load(pojo.getWallpaperPojos().get(nos).getSrcUrl().getPortrait()).addListener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
//                                    try {
//                                        manager.setBitmap(bitmap);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    return false;
//                                }
//                            });
//                            wallpaper = new DownloadImage().execute(pojo.getWallpaperPojos().get(nos).getSrcUrl().getPortrait()).get();
//                            manager.setBitmap(wallpaper);
//                            wallpaper.recycle();
//                            manager.forgetLoadedWallpaper();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        catch (Exception ex){
//                            ex.printStackTrace();
//                        }

                    }

                }

                @Override
                public void onFailure(Call<WallpaperApiResponsePojo> call, Throwable t) {

                }
            });
        }
    }
}

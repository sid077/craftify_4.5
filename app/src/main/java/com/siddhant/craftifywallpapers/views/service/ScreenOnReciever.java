package com.siddhant.craftifywallpapers.views.service;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.repositories.AppDatabase;
import com.siddhant.craftifywallpapers.viewmodel.MainViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ScreenOnReciever extends BroadcastReceiver {
    private List<WallpaperFavPojo> wallpaperFavPojos;
   // MutableLiveData<List<WallpaperFavPojo>> mutableLiveData;
    AppDatabase db;



    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            db = Room.databaseBuilder(context, AppDatabase.class,"RoomDb").build();
            if(db != null)
                 new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final List<WallpaperFavPojo> wallpaperFavPojos = db.wallpaperFavDao().getAll();

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                              //  mutableLiveData.setValue(wallpaperFavPojos);
                                if (wallpaperFavPojos != null) {

                                    final WallpaperManager manager = WallpaperManager.getInstance(context);
                                    int nos = new Random().nextInt(wallpaperFavPojos.size());
                                    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                                    final int height1 = metrics.heightPixels;
                                    final int width1 = metrics.widthPixels;
                                    manager.suggestDesiredDimensions(width1, height1);
                                    manager.setWallpaperOffsetSteps(1,1);
                                    Glide.with(context).asBitmap().timeout(10000).override(width1,height1).load(wallpaperFavPojos.get(nos).getPortraitUrl()).into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            try {
                                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(resource,width1,height1,false);
                                                manager.setBitmap(scaledBitmap);
                                                manager.setWallpaperOffsetSteps(1,1);
                                                manager.suggestDesiredDimensions(width1,height1);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    manager.setBitmap(scaledBitmap,null,true,WallpaperManager.FLAG_LOCK);
                                                }

                                                manager.forgetLoadedWallpaper();
                                                scaledBitmap.recycle();

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {

                                        }
                                    });
                                }
                            }
                        });
                        int x=0;

                    }

                }).start();





//            viewModel.getWallpaperFavLiveData().observe(((AutoWallpaperChanger)context), new Observer<List<WallpaperFavPojo>>() {
//                        @Override
//                        public void onChanged(List<WallpaperFavPojo> wallpaperFavPojos) {
//
//                        }
//                    });
//            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.pexels.com")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            WallpaperApiService wallpaperApiService = retrofit.create(WallpaperApiService.class);
//            Call<WallpaperApiResponsePojo> call = wallpaperApiService.getTrendingWallpapers("abstract");
//
//            call.enqueue(new Callback<WallpaperApiResponsePojo>() {
//                @Override
//                public void onResponse(Call<WallpaperApiResponsePojo> call, Response<WallpaperApiResponsePojo> response) {
//                    String code = response.headers().get("X-Ratelimit-Remaining");
//                    Log.i("Remaining requests ", code);
//                    if (Integer.parseInt(code) <= 0) {
//                        return;
//                    }
//                    wa = response.body();
//                    if (wa != null) {
//                        final WallpaperManager manager = WallpaperManager.getInstance(context);
//                        int nos = new Random().nextInt(wa.getWallpaperPojos().size());
//                        Bitmap wallpaper = null;
//                        Glide.with(context).asBitmap().load(wa.getWallpaperPojos().get(nos).getSrcUrl().getPortrait()).into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                try {
//                                    manager.setBitmap(resource);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });
//                        try {
//                            Glide.with(context).as.load(wa.getWallpaperPojos().get(nos).getSrcUrl().getPortrait()).addListener(new RequestListener<Drawable>() {
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
//                            wallpaper = new DownloadImage().execute(wa.getWallpaperPojos().get(nos).getSrcUrl().getPortrait()).get();
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

//                    }

//                }
//
//                @Override
//                public void onFailure(Call<WallpaperApiResponsePojo> call, Throwable t) {
//
//                }
//            });
        }
    }
}

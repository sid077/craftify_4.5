package com.siddhant.craftifywallpapers.views.ui;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.WallpaperPBPojo;
import com.siddhant.craftifywallpapers.models.WallpaperPojo;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.viewmodel.DownloadImage;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.listeners.OnDismissListener;
import com.stfalcon.imageviewer.listeners.OnImageChangeListener;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unchecked")
public class PreviewPBWallpaperActivity extends AppCompatActivity {

    private ArrayList<String> stringUrl;
    private ArrayList<WallpaperPBPojo.Hits> wallpaperPojo;
    ProgressBar progressBar;

    private ArrayList<WallpaperFavPojo> wallpaperFavPojos;
    private ImageButton imageButtonInfo;
    private ImageView imageViewLockscreeen,imageViewWallpaper;
    private CardView cardViewLockscreen,cardViewHomeScreen;
    private Thread th;
    private InterstitialAd interstitialAd;
    private static final String INTERSTITIAL_ID ="ca-app-pub-2724635946881674/2370892871";
    public static final String TEST_INT_ID="ca-app-pub-3940256099942544/1033173712";

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview_wallpaper);
        final View view = View.inflate(getApplicationContext(),R.layout.set_wallpaper_preview_options,null);
        imageButtonInfo = view.findViewById(R.id.imageButtonInfoPreview);
        imageViewWallpaper = view.findViewById(R.id.imageViewSetAsWallpaper);
        imageViewLockscreeen = view.findViewById(R.id.imageViewSetAsLockscreen);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        int currentMode = getResources().getConfiguration().uiMode& Configuration.UI_MODE_NIGHT_MASK;
        if(MainActivity.isNightModeEnabled){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            imageButtonInfo.setImageResource(R.drawable.ic_info_white_24dp);
            imageViewWallpaper.setImageResource(R.drawable.ic_wallpaper_24dp);
            imageViewLockscreeen.setImageResource(R.drawable.ic_screen_lock_portrait_24dp);
        }


        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(INTERSTITIAL_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        cardViewHomeScreen = view.findViewById(R.id.cardViewHomeScreen);

        cardViewLockscreen = view.findViewById(R.id.cardViewLockscreen);

        progressBar = view.findViewById(R.id.progressBarSetWallpaper);
//        imageViewLockscreeen.setImageResource(R.drawable.ic_screen_lock_portrait_24dp);
//        imageViewWallpaper.setImageResource(R.drawable.ic_wallpaper_24dp);
//        imageButtonInfo.setImageResource(R.drawable.ic_info_black_24dp);

        Intent intent = getIntent();
        stringUrl = new ArrayList<>();
        position = intent.getIntExtra("position", 0);
        wallpaperPojo = (ArrayList<WallpaperPBPojo.Hits>) intent.getSerializableExtra("wallpaperDetails");
        if(wallpaperPojo==null){
            wallpaperFavPojos = (ArrayList<WallpaperFavPojo>) intent.getSerializableExtra("wallpaperFavDetails");
            for(int i = 0; i< wallpaperFavPojos.size(); i++) {
                stringUrl.add(wallpaperFavPojos.get(i).getPortraitUrl());
            }
        }
        else {

            for (int i = 0; i < wallpaperPojo.size(); i++) {
                stringUrl.add(wallpaperPojo.get(i).getLargeUrl());
            }
        }

        new StfalconImageViewer.Builder<>(this, stringUrl, new ImageLoader<String>() {
            @Override
            public void loadImage(ImageView imageView, String image) {
                Glide.with(getApplicationContext()).load(image).into(imageView);
            }
        }).withDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();

            }
        }).withImageChangeListener(new OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                setPosition(position);

                int x=0;
            }
        }).withOverlayView(view).withStartPosition(position).allowZooming(true).allowSwipeToDismiss(true).show();


        imageButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photographerUrl;
                String  height,width,photoUrl,photographer;
                if(wallpaperPojo != null){
                     photographerUrl=wallpaperPojo.get(position).getUserId();
                     height = String.valueOf(wallpaperPojo.get(position).getImageHeight());
                     width = String.valueOf(wallpaperPojo.get(position).getImageWidth());
                     photoUrl = String.valueOf(wallpaperPojo.get(position).getUserImageUrl());
                     photographer = wallpaperPojo.get(position).getUser();
                }else {
                    photographerUrl = wallpaperFavPojos.get(position).getPhotographerUrl();
                     height = String.valueOf(wallpaperFavPojos.get(position).getHeight());
                    width = String.valueOf(wallpaperFavPojos.get(position).getWidth());
                     photoUrl = String.valueOf(wallpaperFavPojos.get(position).getPhotographerUrl());
                     photographer = wallpaperFavPojos.get(position).getPhotographerName();
                }
                Bundle bundle = new Bundle();
                bundle.putString("photoGrapherUrl",photographerUrl);
                bundle.putString("height",height);
                bundle.putString("width",width);
                bundle.putString("photoUrl",photoUrl);
                bundle.putString("photographer",photographer);
                InfoBottomSheet infoBottomSheet = new InfoBottomSheet();
                infoBottomSheet.setArguments(bundle);
                infoBottomSheet.show(getSupportFragmentManager(),"info");
            }
        });



        cardViewHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheet = new BottomSheetPBWallpaperType();
                bottomSheet.show(getSupportFragmentManager(),"wallpaperType");
            }
        });
        cardViewLockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {


                DownloadImage downloadImage = new DownloadImage(progressBar,getApplicationContext());
                try {



                    Bitmap wallpaperBitmap;

                    wallpaperBitmap = downloadImage.execute(wallpaperPojo.get(position).getLargeUrl()).get();
                    WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                    if(wallpaperBitmap==null)
                        throw new NullPointerException();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        manager.setBitmap(wallpaperBitmap,null,true,WallpaperManager.FLAG_LOCK);
                    }
                    wallpaperBitmap.recycle();
                    wallpaperBitmap.recycle();
                    manager.forgetLoadedWallpaper();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Snackbar.make(((Activity) getApplicationContext()).findViewById(android.R.id.content),"Lock screen changed", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),"Lock screen changed",Toast.LENGTH_LONG).show();
                            progressBar.setProgress(100);

                            if(interstitialAd.isLoaded())
                                interstitialAd.show();
                        }
                    });

                }catch (ExecutionException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getApplicationContext(),"Some error occurred, please check your internet connection!",Toast.LENGTH_SHORT).show();

                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Some error occurred, please check your internet connection!",Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Some error occurred, please check your internet connection!",Toast.LENGTH_SHORT).show();

                        }
                    });
                }catch (NullPointerException e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Some error occurred, please check your internet connection!",Toast.LENGTH_SHORT).show();

                        }
                    });                    }
                finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                }

                    }
                });
                thread.start();
                th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (thread.isAlive()){
                            progressBar.incrementProgressBy(2);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        progressBar.setProgress(0);

                    }
                });
                th.start();

            }
        });
    }



    void setAsWallpaper(final int category, final int width, final int height){
        final Thread thread = new Thread(new Runnable() {

             String landscapeUrl;
             String portraitUrl;
             String originalUrl;

            @Override
            public void run() {
                if(wallpaperPojo==null){
                    landscapeUrl = wallpaperFavPojos.get(position).getLandScape();
                    portraitUrl = wallpaperFavPojos.get(position).getPortraitUrl();
                    originalUrl = wallpaperFavPojos.get(position).getOriginalUrl();
                }else{
                    landscapeUrl = wallpaperPojo.get(position).getLargeUrl();
                    portraitUrl = wallpaperPojo.get(position).getLargeUrl();
                    originalUrl = wallpaperPojo.get(position).getLargeUrl();
                }


                if(category == 0) {
                    try{
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(portraitUrl)
                            .override(width, height).timeout(10000)
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    WallpaperManager manager = (WallpaperManager) getSystemService(WALLPAPER_SERVICE);
                                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

                                    int width1 = displayMetrics.widthPixels;

                                    int height1 = displayMetrics.heightPixels;
                                    manager.suggestDesiredDimensions(width1, height1);
                                    manager.setWallpaperOffsetSteps(1,1);
                                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(resource, width1, height1, false);

                                    try {

                                        manager.setBitmap(scaledBitmap);


                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            manager.setBitmap(scaledBitmap, null, true, WallpaperManager.FLAG_LOCK);
                                        }
                                        manager.forgetLoadedWallpaper();
                                        scaledBitmap.recycle();


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }



                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                }
                catch(Exception e) {
                e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Some error occurred, please check your internet connection!",Toast.LENGTH_SHORT).show();

                        }
                    });

                }finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Home screen changed", Toast.LENGTH_SHORT).show();
                                        progressBar.setProgress(100);
                                        if (interstitialAd.isLoaded())
                                            interstitialAd.show();

                                    }
                                });
                            }
                        });
                    }



                }
                else if(category == 1){
                    DownloadImage downloadImage = new DownloadImage(progressBar,getApplicationContext());
                    WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                    try {
                        Bitmap wallpaperBitmap = downloadImage.execute(originalUrl).get();
                        if(wallpaperBitmap==null)
                            throw new NullPointerException();
                        manager.setBitmap(wallpaperBitmap);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            manager.setBitmap(wallpaperBitmap,null,true,WallpaperManager.FLAG_LOCK);
                        }
                       // manager.forgetLoadedWallpaper();
                     //   wallpaperBitmap.recycle();

                        manager.forgetLoadedWallpaper();
                        wallpaperBitmap.recycle();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              Toast.makeText(getApplicationContext(),"Home screen changed",Toast.LENGTH_SHORT).show();
                                progressBar.setProgress(100);


                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Some error occurred, please check your internet connection!",Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                    finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                if(interstitialAd.isLoaded())
                                    interstitialAd.show();
                            }
                        });


                    }

                }
                else {
                    try {
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(portraitUrl)
                                .override(width, height).timeout(10000)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        WallpaperManager manager = (WallpaperManager) getSystemService(WALLPAPER_SERVICE);
                                        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

                                        int width1 = displayMetrics.widthPixels;

                                        int height1 = displayMetrics.heightPixels;
                                        manager.suggestDesiredDimensions(width1, height1);
                                        manager.setWallpaperOffsetSteps(1,1);
                                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(resource, width1, height1, false);
                                        try {
                                         //   manager.suggestDesiredDimensions(width1, height1);
                                            manager.setBitmap(scaledBitmap);
                                           // manager.setWallpaperOffsetSteps(1,1);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                manager.setBitmap(scaledBitmap, null, true, WallpaperManager.FLAG_LOCK);
                                            }
                                            manager.forgetLoadedWallpaper();
                                            scaledBitmap.recycle();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"Home screen changed",Toast.LENGTH_SHORT).show();
                                                progressBar.setProgress(100);

                                            }
                                        });
//
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Some error occurred, please check your internet connection!",Toast.LENGTH_SHORT).show();
//                                if(interstitialAd.isLoaded())
//                                    interstitialAd.show();


                            }
                        });
                    }
                    finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                if(interstitialAd.isLoaded())
                                    interstitialAd.show();

                            }
                        });
                    }
                    }


            }

        });
        thread.start();



    }

}

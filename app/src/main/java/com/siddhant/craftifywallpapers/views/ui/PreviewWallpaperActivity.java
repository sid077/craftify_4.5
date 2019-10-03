package com.siddhant.craftifywallpapers.views.ui;

import androidx.annotation.DimenRes;
import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.siddhant.craftifywallpapers.R;
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

public class PreviewWallpaperActivity extends AppCompatActivity {

    private ArrayList<String> stringUrl;
    private ArrayList<WallpaperPojo> wallpaperPojo;
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
            imageViewWallpaper.setImageResource(R.drawable.ic_wallpaper_white_24dp);
            imageViewLockscreeen.setImageResource(R.drawable.ic_screen_lock_portrait_white_24dp);
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
//        imageViewLockscreeen.setImageResource(R.drawable.ic_screen_lock_portrait_black_24dp);
//        imageViewWallpaper.setImageResource(R.drawable.ic_wallpaper_black_24dp);
//        imageButtonInfo.setImageResource(R.drawable.ic_info_black_24dp);

        Intent intent = getIntent();
        stringUrl = new ArrayList<>();
        position = intent.getIntExtra("position", 0);
        wallpaperPojo = (ArrayList<WallpaperPojo>) intent.getSerializableExtra("wallpaperDetails");
        if(wallpaperPojo==null){
            wallpaperFavPojos = (ArrayList<WallpaperFavPojo>) intent.getSerializableExtra("wallpaperFavDetails");
            for(int i = 0; i< wallpaperFavPojos.size(); i++) {
                stringUrl.add(wallpaperFavPojos.get(i).getPortraitUrl());
            }
        }
        else {

            for (int i = 0; i < wallpaperPojo.size(); i++) {
                stringUrl.add(wallpaperPojo.get(i).getSrcUrl().getPortrait());
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
                     photographerUrl=wallpaperPojo.get(position).getPhotographerUrl();
                     height = String.valueOf(wallpaperPojo.get(position).getHeight());
                     width = String.valueOf(wallpaperPojo.get(position).getWidth());
                     photoUrl = String.valueOf(wallpaperPojo.get(position).getPhotographerUrl());
                     photographer = wallpaperPojo.get(position).getPhotographerName();
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
                BottomSheetDialogFragment bottomSheet = new BottomSheetWallpaperType();
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

                    wallpaperBitmap = downloadImage.execute(wallpaperPojo.get(position).getSrcUrl().getOriginal()).get();
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
                            Toast.makeText(getApplicationContext(),"Lockscreen changed",Toast.LENGTH_LONG).show();
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
                    landscapeUrl = wallpaperPojo.get(position).getSrcUrl().getLandscape();
                    portraitUrl = wallpaperPojo.get(position).getSrcUrl().getPortrait();
                    originalUrl = wallpaperPojo.get(position).getSrcUrl().getOriginal();
                }


                if(category == 0){
                    DownloadImage downloadImage = new DownloadImage(progressBar,getApplicationContext());
                    try {

                        Bitmap wallpaperBitmap;

                        wallpaperBitmap = downloadImage.execute(landscapeUrl).get();
                        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(wallpaperBitmap,width,height,false);
                        if(wallpaperBitmap==null)
                            throw new NullPointerException();

                        manager.setBitmap(scaledBitmap);
                        wallpaperBitmap.recycle();
                        scaledBitmap.recycle();
                        manager.forgetLoadedWallpaper();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(),"Wallpaper Changed Successfully!",Toast.LENGTH_SHORT).show();
                                progressBar.setProgress(100);
                                if(interstitialAd.isLoaded())
                                    interstitialAd.show();

                            }
                        });

                    } catch (ExecutionException e) {
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
                else if(category == 1){
                    DownloadImage downloadImage = new DownloadImage(progressBar,getApplicationContext());
                    WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                    try {
                        Bitmap wallpaperBitmap = downloadImage.execute(portraitUrl).get();
                        if(wallpaperBitmap==null)
                            throw new NullPointerException();
                        manager.setBitmap(wallpaperBitmap);
                        manager.forgetLoadedWallpaper();
                        wallpaperBitmap.recycle();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(),"Wallpaper Changed Successfully!",Toast.LENGTH_SHORT).show();
                                progressBar.setProgress(100);
                                if(interstitialAd.isLoaded())
                                    interstitialAd.show();

                            }
                        });

                    } catch (ExecutionException e) {
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
                else {

                    try {
                        DownloadImage downloadImage = new DownloadImage(progressBar, getApplicationContext());
                        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                        Bitmap wallpaperBitmap = downloadImage.execute(originalUrl).get();
                        if(wallpaperBitmap==null)
                            throw new NullPointerException();
                        manager.setBitmap(wallpaperBitmap);
                        wallpaperBitmap.recycle();

                        manager.forgetLoadedWallpaper();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(),"Wallpaper Changed Successfully!",Toast.LENGTH_SHORT).show();
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

}
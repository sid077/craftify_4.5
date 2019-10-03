package com.siddhant.craftifywallpapers.views.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.WallpaperApiResponsePojo;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.viewmodel.DownloadImage;
import com.siddhant.craftifywallpapers.viewmodel.MainViewModel;
import com.siddhant.craftifywallpapers.views.adapter.FavoriteRecyclerViewAdapter;
import com.siddhant.craftifywallpapers.views.adapter.TrendingRecyclerViewAdapter;
import com.siddhant.craftifywallpapers.views.ui.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class AutoWallpaperChanger extends LifecycleService {
    public Timer timer = null;
    Handler handler = new Handler();
    int id = 101;
    public static  int i = 0,interval;
    private ArrayList<String> url = new ArrayList<>();

    MainViewModel viewModel ;
    private Intent i1;
    private String category;
    private WallpaperApiResponsePojo responsePojo;

    @Override
    public void onCreate() {
        super.onCreate();




    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Toast.makeText(this, "Craftify wallpapers:stopping auto wallpaper changer...", Toast.LENGTH_SHORT).show();

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        interval = intent.getIntExtra("interval",1);
        category = intent.getStringExtra("category");
        viewModel = new MainViewModel();

       viewModel.loadTrending(category,null);
        final Observer<WallpaperApiResponsePojo> responsePojoObserver = new Observer<WallpaperApiResponsePojo>() {



            @Override
            public void onChanged(WallpaperApiResponsePojo wallpaperApiResponsePojo) {
                responsePojo = wallpaperApiResponsePojo;
                if (timer != null)
                    timer.cancel();
                else
                    timer = new Timer();
                timer.scheduleAtFixedRate(new MyTimer(),0,interval);

            }
        };
        viewModel.getLiveData().observe(this,responsePojoObserver);

        i1 = intent;
        int intervalInMillis;
        intervalInMillis = interval;
        String name = "SID" ;
        String CHANNEL_ID = getPackageName();
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription("hello");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setContentText("Wallpaper will change after an interval of "+ TimeUnit.MILLISECONDS.toMinutes(interval) +" minutes.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setColor(Color.BLACK)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                .build();
        startForeground(1, notification);






        return START_NOT_STICKY;
    }
    class MyTimer extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    try {

                        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
                        if(i!=40){
                            int nos = new Random().nextInt(responsePojo.getWallpaperPojos().size());
                            Bitmap wallpaper = new DownloadImage().execute(responsePojo.getWallpaperPojos().get(nos).getSrcUrl().getPortrait()).get();
                            manager.setBitmap(wallpaper);
                            wallpaper.recycle();
                            manager.forgetLoadedWallpaper();
                            i++;
                        }
                        else {
                            i=0;
                        }

                    }  catch (IOException e) {
                        Toast.makeText(getApplicationContext(),"Unable to start service!",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        stopSelf();

                    } catch (InterruptedException e) {
                        Toast.makeText(getApplicationContext(),"Unable to start service!",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        stopSelf();

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Unable to start service!",Toast.LENGTH_SHORT).show();

                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Unable to start service!",Toast.LENGTH_SHORT).show();
                        stopSelf();

                    }
                }
            });
        }
    }


}

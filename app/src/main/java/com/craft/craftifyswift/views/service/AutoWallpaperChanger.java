package com.craft.craftifyswift.views.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;

import com.craft.craftifyswift.models.WallpaperApiResponsePojo;
import com.craft.craftifyswift.models.database.WallpaperFavPojo;
import com.craft.craftifyswift.repositories.AppDatabase;
import com.craft.craftifyswift.views.ui.MainActivity;
import com.craft.craftifyswift.R;

import java.util.List;

public class AutoWallpaperChanger extends LifecycleService {
//    public Timer timer = null;
//    Handler handler = new Handler();
//    public static int i = 0, interval;
    private WallpaperApiResponsePojo pojo;
    private ScreenOnReciever reciever;
    private MutableLiveData<List<WallpaperFavPojo>> mutableLiveData = new MutableLiveData<>();
    private AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(reciever!=null);
            unregisterReceiver(reciever);
            if(db!=null)
                db.close();
//        timer.cancel();
//        Toast.makeText(this, "service stopped successfully", Toast.LENGTH_SHORT).show();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
      //  interval = intent.getIntExtra("timeInMillis", 50000);

        reciever = new ScreenOnReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(reciever,filter);




        String name = "SID";
        String CHANNEL_ID = getPackageName();
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("hello");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setContentText("Wallpaper will change on phone unlock every time " )
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setContentIntent(pendingIntent)
                .setColor(Color.BLACK)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                .build();
        startForeground(1, notification);


//        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.pexels.com")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        WallpaperApiService wallpaperApiService = retrofit.create(WallpaperApiService.class);
//        Call<WallpaperApiResponsePojo> call = wallpaperApiService.getTrendingWallpapers("abstract");
//
//        call.enqueue(new Callback<WallpaperApiResponsePojo>() {
//            @Override
//            public void onResponse(Call<WallpaperApiResponsePojo> call, Response<WallpaperApiResponsePojo> response) {
//                String code = response.headers().get("X-Ratelimit-Remaining");
//                Log.i("Remaining requests ", code);
//                if (Integer.parseInt(code) <= 0) {
//                    return;
//                }
//                pojo = response.body();
//
//            }
//
//            @Override
//            public void onFailure(Call<WallpaperApiResponsePojo> call, Throwable t) {
//
//            }
//        });
//        timer = new Timer();
//        timer.scheduleAtFixedRate(new MyTimer(), 0, interval);


        return START_NOT_STICKY;
    }

//    class MyTimer extends TimerTask {
//        @Override
//        public void run() {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (pojo != null) {
//                        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
//                        int nos = new Random().nextInt(pojo.getWallpaperPojos().size());
//                        Bitmap wallpaper = null;
//                        try {
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
//
//                    }
//
////
//                }
//            });
//        }


    }



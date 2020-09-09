package com.siddhant.craftifywallpapers.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.siddhant.craftifywallpapers.models.WallpaperApiResponsePojo;
import com.siddhant.craftifywallpapers.models.WallpaperCategoryPojo;
import com.siddhant.craftifywallpapers.models.WallpaperPBPojo;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.repositories.AppDatabase;
import com.siddhant.craftifywallpapers.repositories.WallpaperApiService;
import com.siddhant.craftifywallpapers.repositories.WallpaperPBApiService;
import com.siddhant.craftifywallpapers.views.ui.MainActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class MainViewModel extends ViewModel implements MainViewModelInterface{

    private ArrayList<WallpaperCategoryPojo> arrayListCategories;
    private MutableLiveData<List<WallpaperFavPojo>> wallpaperLiveData = new MutableLiveData<>();
    private List<WallpaperFavPojo> wallpaperFavPojos;
    public MutableLiveData<List<WallpaperPBPojo.Hits>> mutableLiveDataWallpaperPB = new MutableLiveData<>();
    public  MutableLiveData<List<String>> specialsCat = new MutableLiveData<>();

    public MutableLiveData<WallpaperApiResponsePojo> getLiveData() {
        return liveData;
    }
    public MutableLiveData<ArrayList<WallpaperCategoryPojo>> liveDataCatagories = new MutableLiveData<>();

    private MutableLiveData<WallpaperApiResponsePojo> liveData = new MutableLiveData<>();

    public  void loadTrending(String query, final ProgressBar progressBar){

    Log.i("Req state","req made");
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.pexels.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    WallpaperApiService wallpaperApiService = retrofit.create(WallpaperApiService.class);
    Call<WallpaperApiResponsePojo> call = wallpaperApiService.getTrendingWallpapers(query);

    call.enqueue(new Callback<WallpaperApiResponsePojo>() {
        @Override
        public void onResponse(Call<WallpaperApiResponsePojo> call, Response<WallpaperApiResponsePojo> response) {
           String code =  response.headers().get("X-Ratelimit-Remaining");

//           Log.i("Remaining requests ",code);
           if(Integer.parseInt(code)<=0){
               return;
           }

            liveData.setValue(response.body());


           if(progressBar != null)
           progressBar.setVisibility(View.INVISIBLE);
            int cb=0;
        }

        @Override
        public void onFailure(Call<WallpaperApiResponsePojo> call, Throwable t) {
            if(progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
        int bb=0;
        }
    });
    int x =0;
}
public void loadPbPhotos(String imageType,
                         String orientation,String category, int minWidth, int minHeight,
                        String[] colors,  String order){
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://pixabay.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    if(imageType==null)
        imageType = "Wallpapers";
    final WallpaperPBApiService wallpaperPBApiService = retrofit.create(WallpaperPBApiService.class);
    Call<WallpaperPBPojo> call = wallpaperPBApiService.getWallpapers("10793550-60f993eb666d1f9d4de1f02ab", imageType,"all",
             "vertical",null, minWidth,  minHeight, colors, false,true, order,70);

    call.enqueue(new Callback<WallpaperPBPojo>() {
        @Override
        public void onResponse(Call<WallpaperPBPojo> call, Response<WallpaperPBPojo> response) {
            if(response.isSuccessful()){
                mutableLiveDataWallpaperPB.postValue(response.body().getHits());
            }
            else {
                Log.d("VIEWMODELMAIN","PB CALL UNSUCCESSFUL");
            }
        }

        @Override
        public void onFailure(Call<WallpaperPBPojo> call, Throwable t) {
            Log.d("VIEWMODELMAIN","PB CALL FAILED");
        }
    });
}

    @Override
    public void fetchCategory() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("catagories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCategories = new ArrayList<>();
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    arrayListCategories.add( d.getValue(WallpaperCategoryPojo.class));


                }
                Collections.sort(arrayListCategories,WallpaperCategoryPojo.categoryPojoComparator);
                liveDataCatagories.setValue(arrayListCategories);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public AppDatabase getDatabaseInstance(Context context) {

        AppDatabase db = Room.databaseBuilder(context,AppDatabase.class,"RoomDb").build();
        return db;
    }

    public MutableLiveData<ArrayList<WallpaperCategoryPojo>> getLiveDataCatagories() {
        return liveDataCatagories;
    }
    @Override
    public void performInsertionFavDb(WallpaperFavPojo wallpaperFavPojo, AppDatabase appDatabase){

        setAllFav(appDatabase);
        wallpaperFavPojos.add(wallpaperFavPojo);
        setWallpaperFavLiveData();

        appDatabase.wallpaperFavDao().insert(wallpaperFavPojo);
        int x=0;

    }

    @Override
    public void performDeletion(WallpaperFavPojo favPojo, AppDatabase db) {
    if(wallpaperFavPojos.remove(favPojo))
        wallpaperLiveData.postValue(wallpaperFavPojos);

        db.wallpaperFavDao().delete(favPojo);
    }

                public MutableLiveData<List<WallpaperFavPojo>> getWallpaperFavLiveData() {
                    return wallpaperLiveData;
                }

                public void setWallpaperFavLiveData() {
                    this.wallpaperLiveData.postValue(wallpaperFavPojos);
                }

    @Override
    public void setAllFav(AppDatabase appDatabase){
        if(appDatabase != null)
        wallpaperFavPojos = appDatabase.wallpaperFavDao().getAll();



    }

    @Override
    public void closeDatabase(AppDatabase appDatabase) {
        if(appDatabase.isOpen()){
            appDatabase.close();
        }
    }
    @Override
    public  boolean searchForObject(int id, List<WallpaperFavPojo> wallpaperFavPojoList) {

        for(WallpaperFavPojo pojo:wallpaperFavPojoList){
            if( pojo.getId() ==id)
                return true;
        }
        return false;
    }

    @Override
    public void startServiceForAutoChange(MainActivity mainActivity, int firstDigit, int lastDigit, String timeFormat, String category, Intent intent) {

        int interval;
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo networkInfo1 = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(connectivityManager!=null) {
            if (networkInfo.getState()==NetworkInfo.State.CONNECTED || networkInfo1 .getState()==NetworkInfo.State.CONNECTED) {


//                if (timeFormat.equalsIgnoreCase("minutes")) {
//                    interval = (firstDigit * 10 + lastDigit) * 60000;
//                } else {
//                    interval = (firstDigit * 10 + lastDigit) * 3600000;
//                }
//                intent.putExtra("interval", interval);
//                intent.putExtra("category", category);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mainActivity.startForegroundService(intent);

                } else {
                    mainActivity.startService(intent);


                }
            } else {
                Toast.makeText(mainActivity.getApplicationContext(), "It seems you are not connected to the internet, please check your connection!", Toast.LENGTH_LONG).show();
                return;
            }
        }

    }

    @Override
    public void stopService(final int processId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(processId);
            }
        }).start();
    }

    @Override
    public void specialcatagory() {
        List<String> specials = new ArrayList<>();
        specials.add("Quotes");

        specials.add("Flowers");
        specials.add("Sunset");

        specials.add("Scenery");
        specials.add("Emoji");
      //  specials.add("Flags");
        specials.add("Vectors");
        specials.add("Cartoons");

        specialsCat.postValue(specials);



    }

    public void addFeedbackToFirebase(float rating, int stars, String feedback) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String uid = UUID.randomUUID().toString();
        reference.child("feedbacks").child(uid).child("rating").setValue(rating);
        reference.child("feedbacks").child(uid).child("stars").setValue(stars);
        reference.child("feedbacks").child(uid).child("feedback").setValue(feedback);

    }
}

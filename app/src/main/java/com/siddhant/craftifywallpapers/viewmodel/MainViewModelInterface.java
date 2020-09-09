package com.siddhant.craftifywallpapers.viewmodel;

import android.content.Context;
import android.content.Intent;

import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.repositories.AppDatabase;
import com.siddhant.craftifywallpapers.views.ui.MainActivity;

import java.io.Serializable;
import java.util.List;

public interface MainViewModelInterface extends Serializable {
void fetchCategory();
AppDatabase getDatabaseInstance(Context context);
    void performInsertionFavDb(WallpaperFavPojo wallpaperFavPojo, AppDatabase appDatabase);

    void performDeletion(WallpaperFavPojo favPojo, AppDatabase db);
   void setAllFav(AppDatabase appDatabase);
     void closeDatabase(AppDatabase appDatabase);

    boolean searchForObject(int id, List<WallpaperFavPojo> wallpaperFavPojoList);
     void startServiceForAutoChange(MainActivity mainActivity, int firstDigit, int lastDigit, String timeFormat, String category, Intent intent);

    void stopService(int intent);
    void specialcatagory();
}

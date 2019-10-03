package com.siddhant.craftifywallpapers.repositories;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.siddhant.craftifywallpapers.models.WallpaperPojo;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavDao;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;

@Database(entities = {WallpaperFavPojo.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WallpaperFavDao wallpaperFavDao();
}

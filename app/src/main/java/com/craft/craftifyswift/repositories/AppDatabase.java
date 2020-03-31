package com.craft.craftifyswift.repositories;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.craft.craftifyswift.models.database.WallpaperFavDao;
import com.craft.craftifyswift.models.database.WallpaperFavPojo;

@Database(entities = {WallpaperFavPojo.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WallpaperFavDao wallpaperFavDao();
}

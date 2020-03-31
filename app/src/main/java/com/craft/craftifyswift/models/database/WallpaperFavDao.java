package com.craft.craftifyswift.models.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface WallpaperFavDao  {
    @Query("select * From WallpaperFavPojo")
    List<WallpaperFavPojo> getAll();

    @Delete
    void delete(WallpaperFavPojo wallpaperFavPojo);

    @Insert
    void insert(WallpaperFavPojo wallpaperFavPojo);
}

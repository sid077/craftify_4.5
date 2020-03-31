package com.craft.craftifyswift.models.database;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class WallpaperFavPojo implements Serializable {
    @PrimaryKey
    int id;

    @ColumnInfo
    int width;

    @ColumnInfo
    int height;

    @ColumnInfo
    String  url;
    @ColumnInfo
    String photographerName;

    public String getPhotographerName() {
        return photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }

    @ColumnInfo
    String photographerUrl;

    @ColumnInfo
    String portraitUrl;

    @ColumnInfo
    String smallUrl;

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getTinyUrl() {
        return tinyUrl;
    }

    public void setTinyUrl(String tinyUrl) {
        this.tinyUrl = tinyUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getLandScape() {
        return landScape;
    }

    public void setLandScape(String landScape) {
        this.landScape = landScape;
    }

    @ColumnInfo
    String tinyUrl;

    @ColumnInfo
    String originalUrl;

    @ColumnInfo
    String landScape;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotographerUrl() {
        return photographerUrl;
    }

    public void setPhotographerUrl(String photographerUrl) {
        this.photographerUrl = photographerUrl;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        return this.getId() == ((WallpaperFavPojo) obj).getId();
    }
}

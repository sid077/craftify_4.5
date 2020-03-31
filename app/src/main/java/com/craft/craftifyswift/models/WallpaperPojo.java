package com.craft.craftifyswift.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WallpaperPojo implements Serializable,Comparable{
    @SerializedName("id")
    int id;
    @SerializedName("width")
    int width;
    @SerializedName("height")
    int height;
    @SerializedName("url")
    String  url;
    @SerializedName("photographer")
    String photographerName;

    public String getPhotographerName() {
        return photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }

    @SerializedName("photographer_url")
    String photographerUrl;

    public WallpaperSrcUrlPojo getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(WallpaperSrcUrlPojo srcUrl) {
        this.srcUrl = srcUrl;
    }

    @SerializedName("src")
    WallpaperSrcUrlPojo srcUrl;





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
    public int compareTo(Object o) {
        if(getId()<((WallpaperPojo)o).getId())
        return 1;
        else
            return 0;
    }
}

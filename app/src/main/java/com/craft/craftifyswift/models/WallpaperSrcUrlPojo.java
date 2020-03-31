package com.craft.craftifyswift.models;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WallpaperSrcUrlPojo implements Serializable {
    @ColumnInfo(name = "original")
    @SerializedName("original")
    String original;

    @ColumnInfo(name = "large")
    @SerializedName("large")
    String large;

    @ColumnInfo(name ="large2x")
    @SerializedName("large2x")
    String large2x;

    @ColumnInfo(name ="medium")
    @SerializedName("medium")
    String medium;

    @ColumnInfo(name ="small")
    @SerializedName("small")
    String small;
    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getLarge2x() {
        return large2x;
    }

    public void setLarge2x(String large2x) {
        this.large2x = large2x;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getLandscape() {
        return landscape;
    }

    public void setLandscape(String landscape) {
        this.landscape = landscape;
    }

    public String getTiny() {
        return tiny;
    }

    public void setTiny(String tiny) {
        this.tiny = tiny;
    }

    @ColumnInfo(name ="portrait")
    @SerializedName("portrait")
    String portrait;

    @ColumnInfo(name = "landscape")
    @SerializedName("landscape")
    String landscape;

    @ColumnInfo(name = "tiny")
    @SerializedName("tiny")
    String tiny;
}

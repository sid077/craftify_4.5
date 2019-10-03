package com.siddhant.craftifywallpapers.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WallpaperApiResponsePojo {
    @SerializedName("total_results")
    int totalPage;
    @SerializedName("page")
    int page;
    @SerializedName("per_page")
    int perPage;
    @SerializedName("photos")
    List<WallpaperPojo> wallpaperPojos = new ArrayList<>();
    String nextPage;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public List<WallpaperPojo> getWallpaperPojos() {
        return wallpaperPojos;
    }

    public void setWallpaperPojos(List<WallpaperPojo> wallpaperPojos) {
        this.wallpaperPojos = wallpaperPojos;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}

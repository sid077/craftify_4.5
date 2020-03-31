package com.siddhant.craftifywallpapers.models;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class WallpaperCategoryPojo{
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;
   public static Comparator<WallpaperCategoryPojo> categoryPojoComparator = new Comparator<WallpaperCategoryPojo>() {
        @Override
        public int compare(WallpaperCategoryPojo o1, WallpaperCategoryPojo o2) {
            String name1=o1.getName();
            String name2 = o2.getName();
            return name1.compareTo(name2);
        }
    };
//    @Override
//    public int compareTo(WallpaperCategoryPojo o) {
//        WallpaperCategoryPojo pojo = (WallpaperCategoryPojo) o;
//        try {
//            Log.d("new comp", String.valueOf(this.getName().compareTo(pojo.getName())));
//            if (this.getName().compareTo(pojo.getName()) > 0) {
//                return 1;
//            }
//            return 0;
//        }catch (Exception e){
//            e.printStackTrace();
//            return 0;
//        }
//    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}

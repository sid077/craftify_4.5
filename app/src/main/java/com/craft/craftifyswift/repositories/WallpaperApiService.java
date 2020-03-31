package com.craft.craftifyswift.repositories;

import com.craft.craftifyswift.models.WallpaperApiResponsePojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WallpaperApiService {

    @Headers("Authorization: 563492ad6f9170000100000114b600ee9f564948886cfa06f720417a")
    @GET("v1/search?&per_page=80&page=1")
    Call<WallpaperApiResponsePojo> getTrendingWallpapers(@Query("query")String query);



}

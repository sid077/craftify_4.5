package com.siddhant.craftifywallpapers.repositories;

import com.siddhant.craftifywallpapers.models.WallpaperApiResponsePojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WallpaperApiService {

    @Headers("Authorization: YOUR_API_KEY")
    @GET("v1/search?&per_page=80&page=1")
    Call<WallpaperApiResponsePojo> getTrendingWallpapers(@Query("query")String query);



}

package com.siddhant.craftifywallpapers.repositories;

import com.siddhant.craftifywallpapers.models.WallpaperApiResponsePojo;
import com.siddhant.craftifywallpapers.models.WallpaperPBPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WallpaperPBApiService {

    @GET("/api/")
    Call<WallpaperPBPojo> getWallpapers(@Query("key") String key,@Query("q") String query,@Query("image_type")String imageType,
                                        @Query("orientation")String orientation, @Query("category") String category,
                                        @Query("min_width")int minWidth, @Query("min_height")int minHeight,
                                        @Query("colors")String[] colors,@Query("editors_choice")Boolean editorsChoice, @Query("safesearch") Boolean safeSearch,@Query("order") String order,@Query("per_page") int perPage);
}

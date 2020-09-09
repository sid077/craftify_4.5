package com.siddhant.craftifywallpapers.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WallpaperPBPojo implements Serializable {
    @SerializedName("total")
    String total;
    @SerializedName("totalHits")
    String totalHits;

    public class Hits implements Serializable {
        @SerializedName("id")
        String id;
        @SerializedName("pageURL")
        String pageUrl;
        @SerializedName("previewURL")
        String previewUrl;
        @SerializedName("previewWidth")
        String previewWidth;
        @SerializedName("previewHeight")
        String previewHeight;
        @SerializedName("largeImageURL")
        String largeUrl;
        @SerializedName("imageWidth")
        String imageWidth;
        @SerializedName("imageHeight")
        String imageHeight;
        @SerializedName("imageSize")
        String imageSize;
        @SerializedName("views")
        String views;
        @SerializedName("downloads")
        String downlaods;
        @SerializedName("webFormatURL")
        String webformaturl;

        public String getWebformaturl() {
            return webformaturl;
        }

        public void setWebformaturl(String webformaturl) {
            this.webformaturl = webformaturl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public String getPreviewUrl() {
            return previewUrl;
        }

        public void setPreviewUrl(String previewUrl) {
            this.previewUrl = previewUrl;
        }

        public String getPreviewWidth() {
            return previewWidth;
        }

        public void setPreviewWidth(String previewWidth) {
            this.previewWidth = previewWidth;
        }

        public String getPreviewHeight() {
            return previewHeight;
        }

        public void setPreviewHeight(String previewHeight) {
            this.previewHeight = previewHeight;
        }

        public String getLargeUrl() {
            return largeUrl;
        }

        public void setLargeUrl(String largeUrl) {
            this.largeUrl = largeUrl;
        }

        public String getImageWidth() {
            return imageWidth;
        }

        public void setImageWidth(String imageWidth) {
            this.imageWidth = imageWidth;
        }

        public String getImageHeight() {
            return imageHeight;
        }

        public void setImageHeight(String imageHeight) {
            this.imageHeight = imageHeight;
        }

        public String getImageSize() {
            return imageSize;
        }

        public void setImageSize(String imageSize) {
            this.imageSize = imageSize;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getDownlaods() {
            return downlaods;
        }

        public void setDownlaods(String downlaods) {
            this.downlaods = downlaods;
        }

        public String getFavourites() {
            return favourites;
        }

        public void setFavourites(String favourites) {
            this.favourites = favourites;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getUserImageUrl() {
            return userImageUrl;
        }

        public void setUserImageUrl(String userImageUrl) {
            this.userImageUrl = userImageUrl;
        }

        @SerializedName("favourites")
        String favourites;
        @SerializedName("likes")
        String likes;
        @SerializedName("user_id")
        String userId;
        @SerializedName("user")
        String user;
        @SerializedName("userImageUrl")
        String  userImageUrl;

    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(String totalHits) {
        this.totalHits = totalHits;
    }

    public List<Hits> getHits() {
        return hits;
    }

    public void setHits(List<Hits> hits) {
        this.hits = hits;
    }

    @SerializedName("hits")
    List<Hits> hits;
}

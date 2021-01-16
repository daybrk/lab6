package com.example.lab6.api;

import com.example.lab6.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrAPI {

    @GET("services/rest/?method=flickr.photos.getRecent&api_key=5e99410e98d651b967afe398a42eaad6&extras=url_s&format=json&nojsoncallback=1")
    Call<Example> getRecent();

    @GET("services/rest/?method=flickr.photos.search&api_key=5e99410e98d651b967afe398a42eaad6&extras=url_s&format=json&nojsoncallback=1")
    Call<Example> getSearchPhotos(@Query("text") String keyWord);

}

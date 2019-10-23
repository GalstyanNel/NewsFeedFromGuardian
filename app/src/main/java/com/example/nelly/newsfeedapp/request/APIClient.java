package com.example.nelly.newsfeedapp.request;


import com.example.nelly.newsfeedapp.models.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface APIClient {

    @GET("/search")
    Call<NewsResponse> getAllNews(@Header("api-key") String apiKey, @Header("Accept") String accept, @Query("show-fields") String showFields,
                                  @Query("page") int currentPage);
}

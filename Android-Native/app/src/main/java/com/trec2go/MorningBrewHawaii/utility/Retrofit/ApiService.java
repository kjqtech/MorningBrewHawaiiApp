package com.trec2go.MorningBrewHawaii.utility.Retrofit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {

    @POST()
    Call<String> postService(@Url String url, @Body RequestBody requestBody);

    @GET()
    Call<String> getService(@Url String url);

}

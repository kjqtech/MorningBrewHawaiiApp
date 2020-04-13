package com.trec2go.MorningBrewHawaii.utility.Retrofit;


import com.trec2go.MorningBrewHawaii.utility.AppConstants;

public class APIUtils {


    public static final String BASE_URL = AppConstants.APP_BASE_URL;

    public static ApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }

}

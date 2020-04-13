package com.trec2go.MorningBrewHawaii.utility.Retrofit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {

            HttpLoggingInterceptor Logging=new HttpLoggingInterceptor();
            Logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder http=new OkHttpClient.Builder();
            http.addInterceptor(Logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(http.build())
                    .build();
        }
        return retrofit;
    }

}

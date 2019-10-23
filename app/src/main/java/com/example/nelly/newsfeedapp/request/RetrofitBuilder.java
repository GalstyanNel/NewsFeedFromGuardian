package com.example.nelly.newsfeedapp.request;


import android.content.Context;

import com.example.nelly.newsfeedapp.utils.ConnectionHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.nelly.newsfeedapp.utils.Constants.BASE_URL;
import static com.example.nelly.newsfeedapp.utils.Constants.HEADER_CACHE_CONTROL;
import static com.example.nelly.newsfeedapp.utils.Constants.HEADER_PRAGMA;

public class RetrofitBuilder {

    private static Retrofit retrofit;

    public static OkHttpClient getOkHttp(final Context context, Cache myCache)

    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(myCache)
                .addNetworkInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    CacheControl cacheControl;
                    if (ConnectionHelper.hasNetwork(context)) {
                        cacheControl = new CacheControl.Builder()
                                .maxAge(0, TimeUnit.SECONDS)
                                .build();
                    } else {
                        cacheControl = new CacheControl.Builder()
                                .maxStale(7, TimeUnit.DAYS)
                                .build();
                    }

                    return response.newBuilder()

                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                            .build();
                })
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if (!ConnectionHelper.hasNetwork(context)) {
                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxStale(7, TimeUnit.DAYS)
                                .build();

                        request = request.newBuilder()
                                .removeHeader(HEADER_PRAGMA)
                                .removeHeader(HEADER_CACHE_CONTROL)
                                .cacheControl(cacheControl)
                                .build();
                    }
                    return chain.proceed(request);
                })
                .build();
        return okHttpClient;
    }


    public static Retrofit getRetrofitInstance(OkHttpClient client) {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}

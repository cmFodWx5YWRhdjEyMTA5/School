package com.video.aashi.school.adapters;

import com.video.aashi.school.APIUrl;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {







   public static Retrofit retrofit = null;
        public static Retrofit getApiCLient() {

            if (retrofit == null) {
                retrofit = new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                        (GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
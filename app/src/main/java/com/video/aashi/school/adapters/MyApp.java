package com.video.aashi.school.adapters;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.video.aashi.school.adapters.TypefaceUtil;

public class MyApp extends Application {

    @Override
    public void onCreate() {


        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "font/mont.ttf");
        super.onCreate();
    }
}
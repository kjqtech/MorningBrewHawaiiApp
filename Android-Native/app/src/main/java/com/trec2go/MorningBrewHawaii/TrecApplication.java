package com.trec2go.MorningBrewHawaii;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class TrecApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/arial.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

}

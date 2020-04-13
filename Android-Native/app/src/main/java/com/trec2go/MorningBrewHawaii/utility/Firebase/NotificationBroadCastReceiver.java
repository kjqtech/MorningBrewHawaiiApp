package com.trec2go.MorningBrewHawaii.utility.Firebase;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadCastReceiver extends BroadcastReceiver {

    String notiType;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        notiType = intent.getStringExtra("notiType");

    }


}

package com.trec2go.MorningBrewHawaii;


import android.app.Service;
        import android.os.IBinder;
        import android.content.Intent;
        import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DemoApplication extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Looper.prepare();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(this, SweetAlertDialog.NORMAL_TYPE, "Hi Jaina ! Welcome to Asferi Collection", new DialogCallBackListner() {
            @Override
            public void setPositiveListner() {
                //Cliked Ok
            }

            @Override
            public void setNegativeListner() {

            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Looper.myLooper().quit();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}

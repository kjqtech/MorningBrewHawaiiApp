package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.utility.DemoApplication;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.utility.Config;
import com.trec2go.MorningBrewHawaii.utility.HttpsTrustManager;
import com.trec2go.MorningBrewHawaii.utility.Preference;

import org.json.JSONArray;

import cn.pedant.SweetAlert.SweetAlertDialog;

//Pakage name to be changed

public class SplashActivity extends AppCompatActivity {

    Activity activity;
    Context context;
    ImageView img_splash, img_logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;
        context = this;
        HttpsTrustManager.allowAllSSL();


        img_splash = findViewById(R.id.img_splash);
        img_logo = findViewById(R.id.img_logo);

        if (Preference.getString(context, Preference.IS_OPENED_FIRST_TIME).equalsIgnoreCase("")) {
            Preference.setColor(context, Preference.splash_back, Color.parseColor("#000000"));
            Preference.setString(context, Preference.GLOBAL_UNAME, "MorningBrewHawaii");
        }
        img_splash.setBackgroundColor(Preference.getColor(context, Preference.splash_back));
        //img_logo.setImageResource(R.drawable.placeholderimage);
        img_logo.setImageResource(R.drawable.logo);



        try {
            JSONArray jsonArray = new JSONArray(" ");

        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                //startService(getCurrentFocus());
                if (getIntent().hasExtra("FROM_NOTIFICATION")) {
                    String notificationTitle = getIntent().getStringExtra("NOTIFICATION_TITLE");
                    String notificationBody = getIntent().getStringExtra("NOTIFICATION_BODY");

                    Log.e("notificationMessage", "message=" + notificationTitle);
                    Log.e("notificationMessage", "message=" + notificationBody);
                    AlertDialogActivity.sweetPopup(context, SweetAlertDialog.NORMAL_TYPE, notificationBody, notificationTitle, new DialogCallBackListner() {
                        @Override
                        public void setPositiveListner() {
                            if (Preference.getString(context, Preference.IS_OPENED_FIRST_TIME).equalsIgnoreCase("")) {
                                Preference.setString(context, Preference.IS_OPENED_FIRST_TIME, "False");
                                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                i.putExtra("from", "splash");  //$$$
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(SplashActivity.this, SelectStoreLocationActivity.class);
                                i.putExtra("from", "login");
                                startActivity(i);
                                finish();
                            }

                        }

                        @Override
                        public void setNegativeListner() {

                        }
                    });
                }
                else {
                    if (Preference.getString(context, Preference.IS_OPENED_FIRST_TIME).equalsIgnoreCase("")) {
                        Preference.setString(context, Preference.IS_OPENED_FIRST_TIME, "False");
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        i.putExtra("from", "splash");  //$$$
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SplashActivity.this, SelectStoreLocationActivity.class);
                        i.putExtra("from", "login");
                        startActivity(i);
                        finish();
                    }
                }










               /*else if(Preference.getString(context,Preference.BRANCH_ID).equalsIgnoreCase("")) {
                    Intent i = new Intent(SplashActivity.this, SelectStoreLocationActivity.class);
                    i.putExtra("from","login");
                    startActivity(i);
                    finish();
                }  //$$$ comment else if & else

                else {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    //Intent i = new Intent(SplashActivity.this, SelectStoreLocationActivity.class); //$$$ change HomeActivity to selectStoreLocation

                    startActivity(i);
                    finish();
                }*/


            }
        }, Config.splash_time_out);

    }



}

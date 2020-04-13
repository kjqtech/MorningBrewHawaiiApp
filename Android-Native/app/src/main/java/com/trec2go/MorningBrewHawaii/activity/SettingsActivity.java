package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.utility.Preference;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {

    ImageView img_back_arrow;
    LinearLayout ll_recent_orders,ll_coupons,ll_address,ll_personal_details;

    Activity activtiy;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        activtiy = this;
        context = this;

        ll_address = findViewById(R.id.ll_address);
        ll_coupons = findViewById(R.id.ll_coupons);
        ll_personal_details = findViewById(R.id.ll_personal_details);
        ll_recent_orders = findViewById(R.id.ll_recent_orders);
        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        ll_recent_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activtiy, RecentOrderActivity.class);
                startActivity(intent);
            }
        });

        ll_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activtiy, CouponsActivity.class);
                startActivity(intent);
            }
        });

        ll_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preference.setString(context, Preference.from, "");
                Intent intent = new Intent(activtiy, AddressActivity.class);
                startActivity(intent);
            }
        });

        ll_personal_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activtiy, PersonalDetail.class);
                startActivity(intent);
            }
        });



    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

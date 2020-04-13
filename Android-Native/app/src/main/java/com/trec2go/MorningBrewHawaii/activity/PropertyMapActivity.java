package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jsibbold.zoomage.ZoomageView;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import com.squareup.picasso.Picasso;

public class PropertyMapActivity extends Activity  {

    ImageView img_back_arrow, iv_property_map;
    Activity activity;
    Context context;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_map);

         activity = this;
         context = this;

        Utility.createProgressDialoge(context,"Loading");

        img_back_arrow = findViewById(R.id.img_back_arrow);

        ZoomageView imageZoom = (ZoomageView)findViewById(R.id.myZoomageView);
        Picasso.with(context).load(Preference.getString(context, Preference.PROPERTY_IMAGE)).placeholder(R.drawable.placeholderimage)
                .into(imageZoom, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Utility.dissmisProgress();
            }

            @Override
            public void onError() {

            }
        });

        // iv_property_map = findViewById(R.id.iv_property_map);

        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }



}

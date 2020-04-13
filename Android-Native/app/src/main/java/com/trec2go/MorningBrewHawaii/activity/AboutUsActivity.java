package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.utility.Preference;

public class AboutUsActivity extends AppCompatActivity {

    ImageView img_back_arrow;
    TextView tv_about_Us;
    Activity activity;
    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        activity = this;
        context = this;

        img_back_arrow = findViewById(R.id.img_back_arrow);
        tv_about_Us = findViewById(R.id.tv_about_Us);

        String about_us = android.text.Html.fromHtml(Preference.getString(context, Preference.ABOUT_US)).toString();
        tv_about_Us.setText(about_us);

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

package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TermsAndUseActivity extends AppCompatActivity {

    ImageView img_back_arrow;
    TextView tv_about_Us;
    Activity activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_use);

        activity = this;
        context = this;

        img_back_arrow = findViewById(R.id.img_back_arrow);
        tv_about_Us = findViewById(R.id.tv_about_Us);

        termsApi();

        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public void termsApi() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));

        ApiCall.volleyPostApi(context, AppConstants.terms, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equalsIgnoreCase("1")){
                        String data = jsonObject.getString("data");
                        tv_about_Us.setText(android.text.Html.fromHtml(data).toString());
                    }else {

                    }
                    Utility.dissmisProgress();

                } catch (JSONException e) {
                    Utility.dissmisProgress();

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.response_error), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        //Cliked Ok
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
            }

            @Override
            public void noInternetConnection() {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.no_internet_connection), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        //Cliked Ok
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
            }
        });
    }


}

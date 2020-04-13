package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.RecentOrdersAdapter;
import com.trec2go.MorningBrewHawaii.bean.RecentOrdersBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RecentOrderActivity extends AppCompatActivity {

    RecyclerView rv_list;
    ArrayList<RecentOrdersBean> ar_list2; //array list
    public static RecentOrdersAdapter ad_list2; //Adapter

    Activity activity;
    Context context;
    ImageView img_back_arrow;

    TextView tv_no_data_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_order);

        context = this;
        activity = this;

        img_back_arrow = findViewById(R.id.img_back_arrow);
        tv_no_data_available = findViewById(R.id.tv_no_data_available);
        rv_list = findViewById(R.id.rv_list);
        ar_list2 = new ArrayList<RecentOrdersBean>();
        ad_list2 = new RecentOrdersAdapter(context,activity,ar_list2);

        LinearLayoutManager layoutManager2  = new LinearLayoutManager(activity);
        rv_list.setLayoutManager(layoutManager2);
        rv_list.setAdapter(ad_list2);

        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecentOrdersAPI();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void RecentOrdersAPI(){
        //status 1=>Dont show progress 2=>show progress
        Utility.createProgressDialoge(context,"Loading");
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        map.put("user_id", Preference.getString(context,Preference.USER_ID));
        ApiCall.volleyPostApi(context, AppConstants.my_order,map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ar_list2.clear();
                    ar_list2.addAll((Collection<? extends RecentOrdersBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<RecentOrdersBean>>() {}.getType()));
                    ad_list2.updateList(ar_list2);
                    if(ar_list2.isEmpty()){
                        tv_no_data_available.setVisibility(View.VISIBLE);
                    }else {
                        tv_no_data_available.setVisibility(View.GONE);
                    }
                    Utility.dissmisProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }
            }

            @Override
            public void onError(String error) {
                Utility.dissmisProgress();
            }

            @Override
            public void noInternetConnection() {
                Utility.dissmisProgress();
            }
        });

    }
}

package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.CouponsAdapter;
import com.trec2go.MorningBrewHawaii.bean.CouponBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CouponsActivity extends AppCompatActivity {

    RecyclerView rv_list;
    ArrayList<CouponBean> ar_list2; //array list
    public static CouponsAdapter ad_list2; //Adapter

    Activity activity;
    Context context;
    ImageView img_back_arrow;
    LinearLayout ll_header, ll_note;
    TextView tv_no_data_available;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        context = this;
        activity = this;

        img_back_arrow = findViewById(R.id.img_back_arrow);
        tv_no_data_available = findViewById(R.id.tv_no_data_available);
        ll_header = findViewById(R.id.ll_header);
        ll_note = findViewById(R.id.ll_note);
        rv_list = findViewById(R.id.rv_list);
        ar_list2 = new ArrayList<CouponBean>();
        ad_list2 = new CouponsAdapter(context,activity,ar_list2);

        LinearLayoutManager layoutManager2  = new LinearLayoutManager(activity);
        rv_list.setLayoutManager(layoutManager2);
        rv_list.setAdapter(ad_list2);

        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        GEtCouponsAPI();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    //API Calling
    public void GEtCouponsAPI(){
        //status 1=>Dont show progress 2=>show progress
        Utility.createProgressDialoge(context,"Loading");
        Map<String,String> map = new HashMap<>();
        map.put("branch_id", Preference.getString(context,Preference.BRANCH_ID));
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        ar_list2.clear();
        ApiCall.volleyPostApi(context, AppConstants.list_coupons,map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equalsIgnoreCase("1")){
                        tv_no_data_available.setVisibility(View.GONE);
                        //ll_header.setVisibility(View.VISIBLE);
                        //ll_note.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        ar_list2.addAll((Collection<? extends CouponBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<CouponBean>>() {}.getType()));
                    }else {
                        tv_no_data_available.setText(msg);
                        tv_no_data_available.setVisibility(View.VISIBLE);
                        //ll_header.setVisibility(View.GONE);
                        //ll_note.setVisibility(View.GONE);
                    }
                    ad_list2.updateList(ar_list2);
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

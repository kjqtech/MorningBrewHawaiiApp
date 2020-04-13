package com.trec2go.MorningBrewHawaii.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.SelectLocationAdapter;
import com.trec2go.MorningBrewHawaii.bean.StoreLocationBean;
import com.trec2go.MorningBrewHawaii.commonapicall.GetImageListAPI;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Config;
import com.trec2go.MorningBrewHawaii.utility.ImageManipulation;
import com.trec2go.MorningBrewHawaii.utility.ItemOffsetDecoration;
import com.trec2go.MorningBrewHawaii.utility.Location.LocationHelper;
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

//Package name to be changed

public class SelectStoreLocationActivity extends AppCompatActivity implements LocationHelper.OnLocationCompleteListener {

    RecyclerView rv_list1; //recycler view
    ArrayList<StoreLocationBean> ar_list1; //array list
    public static SelectLocationAdapter ad_list1; //Adapter

    Activity activity;
    Context context;

    ImageView img_bk, img_logo, img_back_arrow;
    LinearLayout ll_alpha;
    TextView tv_store_location, tv_no_storelocation;

    ImageView btn_online_order;

    String doordash_link;

    String from;
    //Location
    final public static int REQUEST_CODE_ASK_PERMISSIONS = 123;

    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION
    };

    LocationHelper locationHelper;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store_location);

        activity = this;
        context = this;

        from = getIntent().getExtras().getString("from");
        //homescreen => from Home screen
        //login => from login
        //skip => from login screen skip click

        init();
        getlatlng();

    }

    public void init() {
        tv_store_location = findViewById(R.id.tv_store_location);
        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_logo = findViewById(R.id.img_logo);
        img_bk = findViewById(R.id.img_bk);
        ll_alpha = findViewById(R.id.ll_alpha);
        tv_no_storelocation = findViewById(R.id.tv_no_storelocation);
        btn_online_order = findViewById(R.id.btn_online_order);


        ImageManipulation.loadImage(context, Preference.getString(context, Preference.LOGO), img_logo);
        ImageManipulation.loadImage(context, Preference.getString(context, Preference.BACKGROUND_IMAGE), img_bk);

        if (from.equalsIgnoreCase("homescreen")) {
            tv_store_location.setTextColor(getResources().getColor(R.color.white));
            img_back_arrow.setVisibility(View.VISIBLE);
            img_logo.setVisibility(View.GONE);
            img_bk.setVisibility(View.VISIBLE);
            ll_alpha.setVisibility(View.VISIBLE);
            img_back_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        } else if (from.equalsIgnoreCase("login") || from.equalsIgnoreCase("skip")) {
            tv_store_location.setTextColor(getResources().getColor(R.color.white));
            img_back_arrow.setVisibility(View.GONE);
            if (!islandcape()) {
                img_logo.setVisibility(View.VISIBLE);
            } else {
                img_logo.setVisibility(View.GONE);
            }
            img_bk.setVisibility(View.VISIBLE);
            ll_alpha.setVisibility(View.VISIBLE);
        }


        if (!islandcape()) {
            setRecycler();
        } else {
            setLandRecycler();
        }

        listner();

    }

    public void setRecycler() {
        rv_list1 = findViewById(R.id.rv_list);
        ar_list1 = new ArrayList<StoreLocationBean>();
        ad_list1 = new SelectLocationAdapter(context, activity, ar_list1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        rv_list1.setLayoutManager(layoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen._5sdp);
        rv_list1.addItemDecoration(itemDecoration);

        rv_list1.setAdapter(ad_list1);


    }

    public void setLandRecycler() {
        rv_list1 = findViewById(R.id.rv_list);
        ar_list1 = new ArrayList<StoreLocationBean>();
        ad_list1 = new SelectLocationAdapter(context, activity, ar_list1);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        rv_list1.setLayoutManager(gridLayoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen._5sdp);
        rv_list1.addItemDecoration(itemDecoration);

        rv_list1.setAdapter(ad_list1);


    }


    public void listner() {

        GetImageListAPI.GetImageList(context, new GetImageListAPI.GetImageAPIListner() {
            @Override
            public void OnChange() {
                ImageManipulation.loadImage(context, Preference.getString(context, Preference.LOGO), img_logo);
                ImageManipulation.loadImage(context, Preference.getString(context, Preference.BACKGROUND_IMAGE), img_bk);
            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int newOrientation = newConfig.orientation;

        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Do certain things when the user has switched to landscape.
            setLandRecycler();
            img_logo.setVisibility(View.GONE);
        } else {
            if (from.equalsIgnoreCase("login") || from.equalsIgnoreCase("skip")) {
                img_logo.setVisibility(View.VISIBLE);
            }
            setRecycler();
        }
    }


    //API CAlling
    public void StoreLocationAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("lat", Config.latitude);
        map.put("long", Config.longitude);
        ApiCall.volleyPostApi(context, AppConstants.location, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray == null) {
                        tv_no_storelocation.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_storelocation.setVisibility(View.GONE);
                        ar_list1.clear();
                        ar_list1.addAll((Collection<? extends StoreLocationBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<StoreLocationBean>>() {
                        }.getType()));
                        ad_list1.updateList(ar_list1);

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



    //Get location
    public void getlatlng() {
        int hasGetLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasGetLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Settings.ACTION_MANAGE_OVERLAY_PERMISSION}, REQUEST_CODE_ASK_PERMISSIONS);
            }
            Log.e("getlatLong", "if");
        } else {
            locationHelper = new LocationHelper(context, this);
            Log.e("getlatLong", "else");


        }
        StoreLocationAPI();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        getlatlng();
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    public void getLocationUpdate(Location location) {
        Config.latitude = String.valueOf(location.getLatitude());
        Config.longitude = String.valueOf(location.getLongitude());

       /* Config.latitude = "37.44525815";
        Config.longitude = "-122.27101285";*/

//        StoreLocationAPI();
    }

    @Override
    public void onError(ConnectionResult connectionResult, Status status, String error) {
        Log.e("onError", "onError");
        if (connectionResult != null) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(activity,
                            LocationHelper.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        } else if (status != null) {
            // Location is not available, but we can ask permission from users
            try {
                status.startResolutionForResult(activity, LocationHelper.REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean islandcape() {
        return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

}

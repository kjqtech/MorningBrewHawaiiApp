package com.trec2go.MorningBrewHawaii.commonapicall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.activity.HomeActivity;
import com.trec2go.MorningBrewHawaii.adapter.HomeGirdRecyclerViewAdapter;
import com.trec2go.MorningBrewHawaii.bean.HomeGridBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetHomeApi {
    public static ArrayList<HomeGridBean> list = new ArrayList<>();

    public static void GetHome(final Context context) {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }
        ApiCall.volleyPostApi(context, AppConstants.home, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    Preference.setString(context, Preference.STORAGE_Delivery, jsonObject.optString("delivery"));
                    Preference.setString(context, Preference.STORAGE_Pickup, jsonObject.optString("pickup"));
                    Preference.setString(context, Preference.BRANCH_NAME, jsonObject.optString("branch_name"));
                    Preference.setString(context, Preference.PAYMENT_METHOD, jsonObject.optString("payment_method"));
                    Preference.setString(context, Preference.IS_DONATION_VISIBLE, jsonObject.optString("donation"));
                    if (Preference.getString(context, Preference.IS_DONATION_VISIBLE).equalsIgnoreCase("1")) {
                        Preference.setString(context, Preference.DONATION, jsonObject.optString("donation_link"));
                    }

                    Preference.setString(context, Preference.IS_TABLE, jsonObject.optString("booking"));
                    Preference.setString(context, Preference.IS_APPOINTMENT, jsonObject.optString("appointment"));

                    Preference.setString(context, Preference.IS_PROPERTY_MAP_VISIBLE, jsonObject.optString("property_map"));
                    Preference.setString(context, Preference.MENU_TEXT, jsonObject.optString("menu_text"));
                    Preference.setString(context, Preference.PROPERTY_IMAGE, jsonObject.optString("property_image"));
                    Preference.setString(context, Preference.ABOUT_US, jsonObject.optString("about_us"));
                    Preference.setString(context, Preference.DOORDASH_TEXT, jsonObject.optString("doordash_text"));
                    Preference.setString(context, Preference.DOORDASH_LINK, jsonObject.optString("doordash_link"));
                    Preference.setString(context, Preference.IS_MENU, jsonObject.optString("menu"));


                 /*   Intent intent = new Intent(activity, HomeActivity.class);
                    activity.startActivity(intent);
                    activity.finish();*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utility.dissmisProgress();
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

    public static void GetHomeHomeActivity(final Context context, final HomeGirdRecyclerViewAdapter ad_list) {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));

        ApiCall.volleyPostApi(context, AppConstants.home, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    Preference.setString(context, Preference.STORAGE_Delivery, jsonObject.optString("delivery"));
                    Preference.setString(context, Preference.STORAGE_Pickup, jsonObject.optString("pickup"));
                    Preference.setString(context, Preference.BRANCH_NAME, jsonObject.optString("branch_name"));
                    Preference.setString(context, Preference.PAYMENT_METHOD, jsonObject.optString("payment_method"));
                    Preference.setString(context, Preference.IS_DONATION_VISIBLE, jsonObject.optString("donation"));
                    if (Preference.getString(context, Preference.IS_DONATION_VISIBLE).equalsIgnoreCase("1")) {
                        Preference.setString(context, Preference.DONATION, jsonObject.optString("donation_link"));
                    }

                    Preference.setString(context, Preference.IS_TABLE, jsonObject.optString("booking"));
                    Preference.setString(context, Preference.IS_APPOINTMENT, jsonObject.optString("appointment"));

                    Preference.setString(context, Preference.IS_PROPERTY_MAP_VISIBLE, jsonObject.optString("property_map"));
                    Preference.setString(context, Preference.MENU_TEXT, jsonObject.optString("menu_text"));
                    Preference.setString(context, Preference.PROPERTY_IMAGE, jsonObject.optString("property_image"));
                    Preference.setString(context, Preference.ABOUT_US, jsonObject.optString("about_us"));
                    Preference.setString(context, Preference.DOORDASH_TEXT, jsonObject.optString("doordash_text"));
                    Preference.setString(context, Preference.DOORDASH_LINK, jsonObject.optString("doordash_link"));
                    Preference.setString(context, Preference.IS_MENU, jsonObject.optString("menu"));
                    list.clear();
                    ArrayList<HomeGridBean> dataSet = getSampleDataSet(context);
                    ad_list.updateList(dataSet);

                 /*   Intent intent = new Intent(activity, HomeActivity.class);
                    activity.startActivity(intent);
                    activity.finish();*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utility.dissmisProgress();
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

    public static ArrayList<HomeGridBean> getSampleDataSet(Context context) {
        if (Preference.getString(context, Preference.IS_MENU).equalsIgnoreCase("1")) {
            if(!Preference.getString(context, Preference.MENU_TEXT).equalsIgnoreCase("")){
                list.add(new HomeGridBean(Preference.getString(context, Preference.MENU_TEXT), R.drawable.menu));
                list.add(new HomeGridBean("Special Offers", R.drawable.special_offer));
            }
        }
        list.add(new HomeGridBean("Location", R.drawable.compass));
        if (Preference.getString(context, Preference.IS_TABLE).equalsIgnoreCase("1")) {
            list.add(new HomeGridBean("Reservation", R.drawable.reservation));
        }
        if (Preference.getString(context, Preference.IS_APPOINTMENT).equalsIgnoreCase("1")) {
            list.add(new HomeGridBean("Appointment", R.drawable.appointment));
        }
        if (!Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
            if (!Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                list.add(new HomeGridBean("Setting", R.drawable.setting));
            }
        }
        if (Preference.getString(context, Preference.IS_DONATION_VISIBLE).equalsIgnoreCase("1")) {
            list.add(new HomeGridBean("Donations", R.drawable.donation_white));
        }
        list.add(new HomeGridBean("About Us", R.drawable.about_us));
        if (Preference.getString(context, Preference.IS_PROPERTY_MAP_VISIBLE).equalsIgnoreCase("1")) {
            list.add(new HomeGridBean("Property Map", R.drawable.map_property));
        }
        return list;
    }

    public static void GetHomeSelectStoreLocation(final Context context, final Activity activity) {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        ApiCall.volleyPostApi(context, AppConstants.home, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    Preference.setString(context, Preference.STORAGE_Delivery, jsonObject.optString("delivery"));
                    Preference.setString(context, Preference.STORAGE_Pickup, jsonObject.optString("pickup"));
                    Preference.setString(context, Preference.BRANCH_NAME, jsonObject.optString("branch_name"));
                    Preference.setString(context, Preference.PAYMENT_METHOD, jsonObject.optString("payment_method"));
                    Preference.setString(context, Preference.IS_DONATION_VISIBLE, jsonObject.optString("donation"));
                    if (Preference.getString(context, Preference.IS_DONATION_VISIBLE).equalsIgnoreCase("1")) {
                        Preference.setString(context, Preference.DONATION, jsonObject.optString("donation_link"));
                    }
                    Preference.setString(context, Preference.IS_PROPERTY_MAP_VISIBLE, jsonObject.optString("property_map"));
                    Preference.setString(context, Preference.IS_TABLE, jsonObject.optString("booking"));
                    Preference.setString(context, Preference.MENU_TEXT, jsonObject.optString("menu_text"));
                    Preference.setString(context, Preference.PROPERTY_IMAGE, jsonObject.optString("property_image"));
                    Preference.setString(context, Preference.ABOUT_US, jsonObject.optString("about_us"));
                    Preference.setString(context, Preference.IS_APPOINTMENT, jsonObject.optString("appointment"));
                    Preference.setString(context, Preference.DOORDASH_TEXT, jsonObject.optString("doordash_text"));
                    Preference.setString(context, Preference.DOORDASH_LINK, jsonObject.optString("doordash_link"));
                    Preference.setString(context, Preference.IS_MENU, jsonObject.optString("menu"));


                    Intent intent = new Intent(activity, HomeActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utility.dissmisProgress();
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

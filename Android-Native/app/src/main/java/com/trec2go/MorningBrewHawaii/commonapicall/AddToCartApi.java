package com.trec2go.MorningBrewHawaii.commonapicall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.activity.HomeActivity;
import com.trec2go.MorningBrewHawaii.activity.LoginActivity;
import com.trec2go.MorningBrewHawaii.activity.MenuActivity;
import com.trec2go.MorningBrewHawaii.activity.YourBagActivity;
import com.trec2go.MorningBrewHawaii.adapter.ExpandableListAdapter;
import com.trec2go.MorningBrewHawaii.bean.AddToCartBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddToCartApi {

    ExpandableListAdapter listAdapter;

    String from = "";
    //Add to Cart API
    public static void addToCartAPI(final Context context, final Activity activity, final AddToCartBean addToCartBean){
        /*Utility.log_api_failure(listAdapter.getAdvancedmenu());
        Utility.log_api_failure(listAdapter.getAdvancedmenuQuantity());
        Utility.log_api_failure(listAdapter.getExtraCharge());*/

        Utility.createProgressDialoge(context,"Loading");
        final Map<String,String> map = new HashMap<>();
        map.put("menu_id", addToCartBean.menu_id);
        map.put("user_id", Preference.getString(context,Preference.USER_ID));
        map.put("quantity", addToCartBean.quantity);
        map.put("item_discount", addToCartBean.item_discount);
        map.put("menuadd", "");
        map.put("choose_sides", "");
        map.put("main_menu", "Y");
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        map.put("amount", addToCartBean.amount);
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        map.put("advance_menu", addToCartBean.advance_menu);
        map.put("advance_menu_qty", addToCartBean.advance_menu_qty);
        map.put("advance_menu_extracharge", addToCartBean.advance_menu_extracharge);
        map.put("special_requests", addToCartBean.special_requests);
        ApiCall.volleyPostApi(context, AppConstants.add_to_cart,map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                if (addToCartBean.is_catering.equalsIgnoreCase("0")) {
                    Preference.setString(context, Preference.CATERING_STATUS, "0");

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Utility.log_api_request(String.valueOf(map));
                        Utility.log(response);
                        if (jsonArray != null) {
                            String order_id = jsonArray.getJSONObject(0).getString("order_id");
                            String order_ids = Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID);

                            if (!order_id.equalsIgnoreCase("")) {
                                if (!order_ids.equalsIgnoreCase("")) {
                                    JSONArray jsonArray1 = new JSONArray(order_ids);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("order_id", order_id);
                                    jsonArray1.put(jsonObject);
                                    Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, jsonArray1.toString());
                                } else {
                                    JSONArray jsonArray1 = new JSONArray();
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("order_id", order_id);
                                    jsonArray1.put(jsonObject);
                                    Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, jsonArray1.toString());
                                }

                                if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    intent.putExtra("from", "Order_Detail");
                                    activity.startActivity(intent);
                                    activity.finish();
                                } else {
                                    Intent intent = new Intent(activity, HomeActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            }
                        }
                        Utility.dissmisProgress();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.dissmisProgress();
                    }

                }else{
                    Preference.setString(context, Preference.CATERING_STATUS, "1");

                    try {
                        JSONArray jsonArray= new JSONArray(response);
                        Utility.log_api_request(String.valueOf(map));
                        Utility.log(response);
                        if(jsonArray!=null){
                            String order_id = jsonArray.getJSONObject(0).getString("order_id");
                            String order_ids = Preference.getString(context,Preference.CATERING_CART_ID);

                            if(!order_id.equalsIgnoreCase("")){
                                if(!order_ids.equalsIgnoreCase("")){
                                    JSONArray jsonArray1 = new JSONArray(order_ids);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("order_id",order_id);
                                    jsonArray1.put(jsonObject);
                                    Preference.setString(context,Preference.CATERING_CART_ID,jsonArray1.toString());
                                }else {
                                    JSONArray jsonArray1 = new JSONArray();
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("order_id",order_id);
                                    jsonArray1.put(jsonObject);
                                    Preference.setString(context,Preference.CATERING_CART_ID,jsonArray1.toString());
                                }

                                if(Preference.getString(context,Preference.USER_ID).equalsIgnoreCase("")){
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    intent.putExtra("from", "Order_Detail");
                                    activity.startActivity(intent);
                                    activity.finish();
                                }else {
                                    Intent intent = new Intent(activity, HomeActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            }
                        }
                        Utility.dissmisProgress();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.dissmisProgress();
                    }

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

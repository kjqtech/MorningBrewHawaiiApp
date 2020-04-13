package com.trec2go.MorningBrewHawaii.commonapicall;

import android.content.Context;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RemoveItemFromCart {


    public interface RemoveItemFromCartListner{

         void onResponse();
    }

    public static void RemoveItemCartAPI(final Context context, String cart_id, final RemoveItemFromCartListner removeItemFromCartListner){
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        map.put("cart_order_id",cart_id);
        ApiCall.volleyPostApi(context, AppConstants.delete_cart_update_all,map,new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String status = jsonObject1.getString("status");
                    if(status.equalsIgnoreCase("1")){
                        removeItemFromCartListner.onResponse();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void noInternetConnection() {

            }
        });

    }

}

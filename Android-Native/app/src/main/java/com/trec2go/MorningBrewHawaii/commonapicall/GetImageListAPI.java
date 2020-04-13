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

public class GetImageListAPI {


    public interface GetImageAPIListner {
        public void OnChange();
    }


    public static void GetImageList(final Context context, final GetImageAPIListner getImageAPIListner) {
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("logo", Preference.getString(context, Preference.LOGO));
        map.put("background_image", Preference.getString(context, Preference.BACKGROUND_IMAGE));
        map.put("menu_background_image", Preference.getString(context, Preference.MENU_BACKGROUND_IMAGE));
        ApiCall.volleyPostApi(context, AppConstants.get_background_images, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String logo = jsonObject.optString("logo");
                        String background_image = jsonObject.optString("background_image");
                        String menu_background_iamge = jsonObject.optString("menu_background_image");
                        String base_url = AppConstants.IMAGE_BASE_URL1 + Preference.getString(context, Preference.GLOBAL_UNAME) + AppConstants.IMAGE_BASE_URL2 + Preference.getString(context, Preference.GLOBAL_UNAME) + "/";
                        Preference.setString(context, Preference.LOGO, base_url + logo);
                        Preference.setString(context, Preference.BACKGROUND_IMAGE, base_url + background_image);
                        Preference.setString(context, Preference.MENU_BACKGROUND_IMAGE, base_url + menu_background_iamge);
                        getImageAPIListner.OnChange();
                    } else if (status.equalsIgnoreCase("2")) {

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

package com.trec2go.MorningBrewHawaii.commonapicall;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.bean.StatesBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GetStateCityListAPI {


    public interface GetStateListAPIListner{

        public void getStates(ArrayList<StatesBean> statesBeanArrayList);
    }

    public interface GetCityListAPIListner{

        public void getCites(ArrayList<String> citiesBeanArrayList);
    }

    public static void GetStatesApi(final Context context,final GetStateListAPIListner getStateListAPIListner){
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        ApiCall.volleyPostApi(context, AppConstants.get_state_list,map,new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<StatesBean> statesBeanArrayList = new ArrayList<>();
                    statesBeanArrayList.addAll((Collection<? extends StatesBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<StatesBean>>() {}.getType()));
                    getStateListAPIListner.getStates(statesBeanArrayList);
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

    public static void GetCitiesApi(final Context context,String state_code,final GetCityListAPIListner getCityListAPIListner){
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));

        if(!state_code.equalsIgnoreCase("")){
            map.put("state_code",state_code);
        }

        ApiCall.volleyPostApi(context, AppConstants.get_city_list,map,new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<String> citiesBeanArrayList = new ArrayList<>();
                    //citiesBeanArrayList.addAll((Collection<? extends String>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<String>>() {}.getType()));

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        citiesBeanArrayList.add(jsonObject.getString("city"));
                    }

                    getCityListAPIListner.getCites(citiesBeanArrayList);
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

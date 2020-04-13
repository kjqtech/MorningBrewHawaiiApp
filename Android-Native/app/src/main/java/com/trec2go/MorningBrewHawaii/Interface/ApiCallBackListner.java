package com.trec2go.MorningBrewHawaii.Interface;

public interface ApiCallBackListner {

    void onResponse(String response);
   /* public void onStatusSucess(String data);
    public void onStatusFailure(String msg);*/
   void onError(String error);

    void noInternetConnection();
}

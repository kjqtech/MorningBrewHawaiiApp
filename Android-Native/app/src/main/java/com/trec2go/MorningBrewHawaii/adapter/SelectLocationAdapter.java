package com.trec2go.MorningBrewHawaii.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.activity.HomeActivity;
import com.trec2go.MorningBrewHawaii.bean.StoreLocationBean;
import com.trec2go.MorningBrewHawaii.commonapicall.GetHomeApi;
import com.trec2go.MorningBrewHawaii.commonapicall.RemoveItemFromCart;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectLocationAdapter extends RecyclerView.Adapter<SelectLocationAdapter.ViewHolder>{

    ArrayList<StoreLocationBean> list;
    Context context;
    Activity activity;


    public SelectLocationAdapter(Context context, Activity activity, ArrayList<StoreLocationBean> list){
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_menu,img_map;
        TextView tv_name,tv_address,tv_phone_no;
        LinearLayout ll_main;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            img_map = itemView.findViewById(R.id.img_map);
            img_menu = itemView.findViewById(R.id.img_menu);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_phone_no = itemView.findViewById(R.id.tv_phone_no);
            ll_main = itemView.findViewById(R.id.ll_main);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_store_location,parent,false);
        ViewHolder vhItem = new ViewHolder(v,viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StoreStatus(list.get(position).id);

//                if(Preference.getString(context,Preference.BRANCH_ID).equalsIgnoreCase(list.get(position).id)){
//                    Preference.setString(context,Preference.BRANCH_ID,list.get(position).id);
//                    GetHomeApi.GetHomeSelectStoreLocation(context, activity);
//                }else {
//                    String oid = "";
//                    String orderid = "";
//                    try {
//                        orderid = Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID);
//                        if(!orderid.equalsIgnoreCase("")){
//                            JSONArray oid_jsonArray = new JSONArray(orderid);
//                            for(int i=0;i<oid_jsonArray.length();i++){
//                                oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
//                            }
//                        }else {
//                            Preference.setString(context,Preference.BRANCH_ID,list.get(position).id);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    Preference.setString(context,Preference.STORAGE_YOUR_BAG_ID,"");
//                    Preference.setString(context,Preference.CATERING_CART_ID,"");
//                    if(!oid.equalsIgnoreCase("")){
//                        RemoveItemFromCart.RemoveItemCartAPI(context,oid.substring(0,oid.length() - 1), new RemoveItemFromCart.RemoveItemFromCartListner() {
//                            @Override
//                            public void onResponse() {
//                                Preference.setString(context,Preference.BRANCH_ID,list.get(position).id);
//                                GetHomeApi.GetHomeSelectStoreLocation(context, activity);
//                            }
//                        });
//                    }else {
//                        Preference.setString(context,Preference.BRANCH_ID,list.get(position).id);
//                        GetHomeApi.GetHomeSelectStoreLocation(context, activity);
//                    }
//
//                }


            }
        });

        holder.img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               String uri = String.format(Locale.ENGLISH, "geo:%f,%f", list.get(position).branch_lat, list.get(position).branch_long);
                String uri = "http://www.google.com/maps/place/" + list.get(position).branch_lat + "," + list.get(position).branch_long;  //$$$ change uri
               // String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", list.get(position).branch_lat,list.get(position).branch_long);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });

        holder.tv_phone_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog.sweetCallingPopup(context, cn.pedant.SweetAlert.SweetAlertDialog.BUTTON_NEGATIVE, holder.tv_phone_no.getText().toString(), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + holder.tv_phone_no.getText()));
                        activity.startActivity(intent);
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
            }
        });

        holder.tv_name.setText(Html.fromHtml(Html.fromHtml(list.get(position).branch_name).toString()));
        holder.tv_address.setText(list.get(position).branch_address + "," + list.get(position).branch_zip);
        holder.tv_phone_no.setText(Utility.stringMatipulation(list.get(position).branch_phone));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<StoreLocationBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void StoreStatus(final String id){
        Utility.createProgressDialoge(context,"Loading");
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        map.put("branch_id", id);
        ApiCall.volleyPostApi(context, AppConstants.store_status,map,new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    int status = object.getInt("status");
                    if(status == 1){

                        if(Preference.getString(context,Preference.BRANCH_ID).equalsIgnoreCase(id)){
                            Preference.setString(context,Preference.BRANCH_ID,id);
                            GetHomeApi.GetHomeSelectStoreLocation(context, activity);                        }else {
                            String oid = "";
                            String orderid = "";
                            try {
                                orderid = Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID);
                                if(!orderid.equalsIgnoreCase("")){
                                    JSONArray oid_jsonArray = new JSONArray(orderid);
                                    for(int i=0;i<oid_jsonArray.length();i++){
                                        oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                                    }
                                }else {
                                    Preference.setString(context,Preference.BRANCH_ID,id);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Preference.setString(context,Preference.STORAGE_YOUR_BAG_ID,"");
                            if(!oid.equalsIgnoreCase("")){
                                RemoveItemFromCart.RemoveItemCartAPI(context,oid.substring(0,oid.length() - 1), new RemoveItemFromCart.RemoveItemFromCartListner() {
                                    @Override
                                    public void onResponse() {
                                        Preference.setString(context,Preference.BRANCH_ID,id);
                                        GetHomeApi.GetHomeSelectStoreLocation(context, activity);
                                    }
                                });
                            }else {
                                Preference.setString(context,Preference.BRANCH_ID,id);
                                GetHomeApi.GetHomeSelectStoreLocation(context, activity);
                            }

                        }

                    }else {
                        String msg = object.getString("msg");
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, cn.pedant.SweetAlert.SweetAlertDialog.ERROR_TYPE, msg, new DialogCallBackListner() {
                            @Override
                            public void setPositiveListner() {
                                //Cliked Ok
                            }

                            @Override
                            public void setNegativeListner() {

                            }
                        });
                    }

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

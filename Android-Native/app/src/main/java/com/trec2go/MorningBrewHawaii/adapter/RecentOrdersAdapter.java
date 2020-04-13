package com.trec2go.MorningBrewHawaii.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.activity.ReorderActivity;
import com.trec2go.MorningBrewHawaii.bean.CategoryBean;
import com.trec2go.MorningBrewHawaii.bean.RecentOrdersBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RecentOrdersAdapter extends RecyclerView.Adapter<RecentOrdersAdapter.ViewHolder> {

    ArrayList<RecentOrdersBean> list;
    Context context;
    Activity activity;
    String status_from;


    public RecentOrdersAdapter(Context context, Activity activity, ArrayList<RecentOrdersBean> list) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_price, tv_ordered_at, tv_name, tv_new_price;
        RelativeLayout rl_main;
        //FloatingActionButton placeholder_img;
        CircularImageView fab;


        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            rl_main = itemView.findViewById(R.id.rl_main);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_ordered_at = itemView.findViewById(R.id.tv_ordered_at);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_new_price = itemView.findViewById(R.id.tv_new_price);
            fab = itemView.findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.login_bk);
            tv_price.setVisibility(View.GONE);
            tv_ordered_at.setVisibility(View.GONE);
            //placeholder_img = itemView.findViewById(R.id.placeholder_img);
            //placeholder_img.show();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_menu, parent, false);
        ViewHolder vhItem = new ViewHolder(v, viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_name.setText("Ordered @ " + list.get(position).date);
        holder.tv_new_price.setText("$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(list.get(position).amount)));
        holder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preference.setString(context, Preference.REORDER_BRANCH_ID, list.get(position).branch_id);
                Preference.setString(context, Preference.REORDER_STATUS, "1");
                faxCheckApi(list.get(position).branch_id, list.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<RecentOrdersBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void faxCheckApi(String branch_id, final RecentOrdersBean recentOrdersBean) {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();

        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("branch_id", branch_id);


        ApiCall.volleyPostApi(context, AppConstants.fax_check, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                Utility.dissmisProgress();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String status = object.getString("status");
                        //String messsage = object.getString("messsage");

                        Preference.setString(context, Preference.FAX_STATUS, status);
                        Intent intent = new Intent(activity, ReorderActivity.class);
                        intent.putExtra("menuItemBean", recentOrdersBean);
                        activity.startActivity(intent);

                        Utility.dissmisProgress();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }

            }

            @Override
            public void onError(String error) {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, activity.getResources().getString(R.string.response_error), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        //Cliked Ok
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
            }

            @Override
            public void noInternetConnection() {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, activity.getResources().getString(R.string.no_internet_connection), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        //Cliked Ok
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
            }
        });
    }


}

package com.trec2go.MorningBrewHawaii.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.bean.AddressBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{

    ArrayList<AddressBean> list;
    Context context;
    Activity activity;
    AddressRowListner addressRowListner;


    public interface AddressRowListner{
        void OnDeleteListner(AddressBean addressBean);
        void OnEditListner(AddressBean addressBean);
        void OnItemViewListener(String street_address, String apartment,String city_state_zip, String id);
    }

    public AddressAdapter(Context context, Activity activity, ArrayList<AddressBean> list,AddressRowListner addressRowListner){
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.addressRowListner = addressRowListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_address,tv_address_2,tv_apartment;
        ImageView img_delete,img_edit;
        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_address_2 = itemView.findViewById(R.id.tv_address_2);
            img_delete = itemView.findViewById(R.id.img_delete);
            img_edit = itemView.findViewById(R.id.img_edit);
            tv_apartment=itemView.findViewById(R.id.tv_apartment);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address,parent,false);
        ViewHolder vhItem = new ViewHolder(v,viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_name.setText(list.get(position).name);
        holder.tv_address.setText(list.get(position).street_address);
        holder.tv_apartment.setText(list.get(position).apt_suite_floor);
        holder.tv_address_2.setText(list.get(position).city_state_zip);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressRowListner.OnItemViewListener(holder.tv_address.getText().toString(), holder.tv_apartment.getText().toString(),holder.tv_address_2.getText().toString(), list.get(position).id);
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addressRowListner.OnDeleteListner(list.get(position));
                DeleteAddressAPI(list.get(position).id, position);
               /* list.remove(position); //$$$ do not remove befor api call
                notifyDataSetChanged();*/
            }
        });
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressRowListner.OnEditListner(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<AddressBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    interface OnItemViewClick {
        void click(View view);
    }

    public void DeleteAddressAPI(String id, final int position){
        //status 1=>Dont show progress 2=>show progress
        Utility.createProgressDialoge(context,"Loading");
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        map.put("id",id);
        ApiCall.volleyPostApi(context, AppConstants.address_delete,map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String status = jsonArray.getJSONObject(0).getString("status");
                    String msg = jsonArray.getJSONObject(0).getString("msg");

                    if(status.equalsIgnoreCase("1")){
                        //Cliked Ok
                        list.remove(position); //$$$ do not remove befor api call
                        notifyDataSetChanged();
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, msg, new DialogCallBackListner() {
                            @Override
                            public void setPositiveListner() {

                            }

                            @Override
                            public void setNegativeListner() {

                            }
                        });


                    }else {
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, msg, new DialogCallBackListner() {
                            @Override
                            public void setPositiveListner() {

                            }

                            @Override
                            public void setNegativeListner() {

                            }
                        });
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
}

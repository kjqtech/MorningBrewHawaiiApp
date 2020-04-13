package com.trec2go.MorningBrewHawaii.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.activity.OrderDetailActivity;
import com.trec2go.MorningBrewHawaii.bean.MenuItemBean;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder>{

    ArrayList<MenuItemBean> list;
    Context context;
    Activity activity;
    String status_from;


    public MenuItemAdapter(Context context, Activity activity, ArrayList<MenuItemBean> list,String status){

        //0 => from home menu
        //1 => from recent orders
        this.status_from = status;
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_price,tv_ordered_at,tv_name,tv_new_price, tv_menu_description,tv_soldout;
        RelativeLayout rl_main;
        CircularImageView fab;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            fab = itemView.findViewById(R.id.fab);
            rl_main = itemView.findViewById(R.id.rl_main);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_ordered_at = itemView.findViewById(R.id.tv_ordered_at);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_menu_description = itemView.findViewById(R.id.tv_menu_description);
            tv_new_price = itemView.findViewById(R.id.tv_new_price);
            tv_soldout=itemView.findViewById(R.id.tv_soldout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_menu,parent,false);
        ViewHolder vhItem = new ViewHolder(v,viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(Preference.getString(context,Preference.STORAGE_YOUR_BAG).equalsIgnoreCase("")){
                    ArrayList<MenuItemBean> list = new ArrayList<>();
                    list.add(list.get(position));
                    String data = new Gson().toJson(list);
                    Preference.setString(context,Preference.STORAGE_YOUR_BAG,data);
                }else {
                    ArrayList<MenuItemBean> list = new ArrayList<>();
                    String cart_storage = Preference.getString(context,Preference.STORAGE_YOUR_BAG);
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(cart_storage);
                        list.clear();
                        list.addAll((Collection<? extends MenuItemBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<MenuItemBean>>() {}.getType()));
                        list.add(list.get(position));
                        String data = new Gson().toJson(list);
                        Preference.setString(context,Preference.STORAGE_YOUR_BAG,data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
                Intent intent = new Intent(activity, OrderDetailActivity.class);
                intent.putExtra("menuItemBean",list.get(position));
                activity.startActivity(intent);
            }
        });
        if(list.get(position).menu_image.equalsIgnoreCase(""))
        {
            holder.fab.setVisibility(View.GONE);
        }
        else {
            holder.fab.setVisibility(View.VISIBLE);
            Picasso.with(context).load(list.get(position).menu_image).into(holder.fab);
        }

        holder.tv_name.setText((Html.fromHtml(Html.fromHtml(list.get(position).menu_title).toString())));
        holder.tv_menu_description.setText(Html.fromHtml(Html.fromHtml(list.get(position).menu_description).toString()));
        if(list.get(position).new_menu_price.equalsIgnoreCase("")){
            holder.tv_new_price.setText("$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(list.get(position).menu_price)));
            holder.tv_price.setVisibility(View.GONE);
        }else {
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.tv_price.setText("$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(list.get(position).menu_price)));
            holder.tv_new_price.setText("$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(list.get(position).new_menu_price)));
        }

        if(status_from.equalsIgnoreCase("0")){
            holder.tv_ordered_at.setVisibility(View.GONE);
        }else {
            holder.tv_ordered_at.setVisibility(View.VISIBLE);
            holder.tv_ordered_at.setText("Ordered @ 30/05/2019 06:30 PM");
        }

        if(list.get(position).soldstatus.equalsIgnoreCase("1")){
            holder.tv_soldout.setVisibility(View.VISIBLE);
        }else {
            holder.tv_soldout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<MenuItemBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

}

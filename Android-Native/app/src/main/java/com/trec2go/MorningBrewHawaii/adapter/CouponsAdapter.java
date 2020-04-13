package com.trec2go.MorningBrewHawaii.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.bean.CouponBean;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder>{

    ArrayList<CouponBean> list;
    Context context;
    Activity activity;
    String status_from;


    public CouponsAdapter(Context context, Activity activity, ArrayList<CouponBean> list) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_row;
        TextView tv_promocode, tv_discount_type, tv_expiration_date;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            ll_row = itemView.findViewById(R.id.ll_row);
            tv_promocode = itemView.findViewById(R.id.tv_promocode);
            tv_discount_type = itemView.findViewById(R.id.tv_discount_type);
            tv_expiration_date = itemView.findViewById(R.id.tv_expiration_date);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_coupon, parent, false);
        ViewHolder vhItem = new ViewHolder(v, viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.ll_row.setVisibility(View.VISIBLE);
        holder.tv_promocode.setText(list.get(position).promo_code);
        if (list.get(position).discount_type.equalsIgnoreCase("f")) {
            holder.tv_discount_type.setText(  list.get(position).discount + " $ off");
        } else if (list.get(position).discount_type.equalsIgnoreCase("p")) {
            holder.tv_discount_type.setText(list.get(position).discount + " % off");
        }
        holder.tv_expiration_date.setText(list.get(position).expiration_date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<CouponBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}

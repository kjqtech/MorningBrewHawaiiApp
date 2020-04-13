package com.trec2go.MorningBrewHawaii.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.bean.CategoryBean;
import com.trec2go.MorningBrewHawaii.bean.SubCategoryBean;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>{

    ArrayList<SubCategoryBean> list;
    Context context;
    Activity activity;
    OnSubCategoryClickListner onSubCategoryClickListner;

    public interface OnSubCategoryClickListner{
        public void OnSubCategoryClick(SubCategoryBean subCategoryBean);
    }


    public SubCategoryAdapter(Context context, Activity activity, ArrayList<SubCategoryBean> list, OnSubCategoryClickListner onSubCategoryClickListner){
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.onSubCategoryClickListner = onSubCategoryClickListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_category_name;
        RelativeLayout rl_main;
        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            tv_category_name = itemView.findViewById(R.id.tv_category_name);
            rl_main = itemView.findViewById(R.id.rl_main);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sub_category,parent,false);
        ViewHolder vhItem = new ViewHolder(v,viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_category_name.setText(list.get(position).name);
        holder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubCategoryClickListner.OnSubCategoryClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<SubCategoryBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

}

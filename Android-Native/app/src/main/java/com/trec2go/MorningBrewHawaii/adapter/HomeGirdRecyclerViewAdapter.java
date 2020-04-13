package com.trec2go.MorningBrewHawaii.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.activity.AboutUsActivity;
import com.trec2go.MorningBrewHawaii.activity.AppointmentActivity;
import com.trec2go.MorningBrewHawaii.activity.MenuActivity;
import com.trec2go.MorningBrewHawaii.activity.PropertyMapActivity;
import com.trec2go.MorningBrewHawaii.activity.SelectStoreLocationActivity;
import com.trec2go.MorningBrewHawaii.activity.SettingsActivity;
import com.trec2go.MorningBrewHawaii.activity.TableReservationActivity;
import com.trec2go.MorningBrewHawaii.bean.CategoryBean;
import com.trec2go.MorningBrewHawaii.bean.HomeGridBean;
import com.trec2go.MorningBrewHawaii.utility.Preference;

import java.util.ArrayList;

public class HomeGirdRecyclerViewAdapter extends RecyclerView.Adapter<HomeGirdRecyclerViewAdapter.MyViewHolder> {

    ArrayList<HomeGridBean> dataSet;
    Context context ;
    Activity activity ;

    public HomeGirdRecyclerViewAdapter(ArrayList<HomeGridBean> dataSet, Context context, Activity activity) {
        this.dataSet = dataSet;
        this.context = context;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_recycler, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_menuname.setText(dataSet.get(position).getMenuName());
        holder.iv_menu.setImageResource(dataSet.get(position).getMenuImage());
        holder.ll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataSet.get(position).getMenuName().equalsIgnoreCase(Preference.getString(context, Preference.MENU_TEXT))){
                    Intent intent = new Intent(activity, MenuActivity.class);
                    intent.putExtra("from", "menu");
                    context.startActivity(intent);
                }else if(dataSet.get(position).getMenuName().equalsIgnoreCase("Location")){
                    Intent intent = new Intent(activity, SelectStoreLocationActivity.class);
                    intent.putExtra("from", "homescreen");
                    context.startActivity(intent);
                    activity.finish();
                }else if(dataSet.get(position).getMenuName().equalsIgnoreCase("Special Offers")){
                    Intent intent = new Intent(activity, MenuActivity.class);
                    intent.putExtra("from", "offer");
                    context.startActivity(intent);
                }else if(dataSet.get(position).getMenuName().equalsIgnoreCase("Reservation")){
                    Intent intent = new Intent(activity, TableReservationActivity.class);
                    context.startActivity(intent);
                }else if(dataSet.get(position).getMenuName().equalsIgnoreCase("Appointment")){
                    Intent intent = new Intent(activity, AppointmentActivity.class);
                    context.startActivity(intent);
                }else if(dataSet.get(position).getMenuName().equalsIgnoreCase("Setting")){
                    Intent intent = new Intent(activity, SettingsActivity.class);
                    context.startActivity(intent);
                }else if(dataSet.get(position).getMenuName().equalsIgnoreCase("Donations")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Preference.getString(context, Preference.DONATION)));
                    activity.startActivity(browserIntent);
                }else if(dataSet.get(position).getMenuName().equalsIgnoreCase("About Us")){
                    Intent intent = new Intent(activity, AboutUsActivity.class);
                    context.startActivity(intent);
                }else if(dataSet.get(position).getMenuName().equalsIgnoreCase("Property Map")){
                    Intent intent = new Intent(activity, PropertyMapActivity.class);
                    activity.startActivity(intent);
                }else {
                    Log.e("HomeGirdRViewAdapter", "There is some issue");;
                }

            }
        });


        //holder.iv_menu.setImageResource(dataSet.get(position).getMenuName());


        /*holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSet.remove(position);
                notifyItemRemoved(position); // this notifies the adapter about item being removed
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_menuname;
        ImageView iv_menu;
        LinearLayout ll_menu;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_menu = itemView.findViewById(R.id.ll_menu);
            tv_menuname = itemView.findViewById(R.id.tv_menu_text);
            iv_menu = itemView.findViewById(R.id.img);
        }
    }

    public void updateList(ArrayList<HomeGridBean> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }
}





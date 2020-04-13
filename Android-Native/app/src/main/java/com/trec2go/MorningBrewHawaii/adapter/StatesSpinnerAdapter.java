package com.trec2go.MorningBrewHawaii.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.bean.StatesBean;

public class StatesSpinnerAdapter extends ArrayAdapter<StatesBean> {

    LayoutInflater flater;

    public StatesSpinnerAdapter(Activity context, int resouceId, int textviewId, List<StatesBean> list){

        super(context,resouceId,textviewId, list);
//        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    private View rowview(View convertView , int position){

        StatesBean rowItem = getItem(position);
        View rowview = convertView;
        flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowview = flater.inflate(R.layout.spinner_textview, null, false);

        TextView txtTitle;
        txtTitle = rowview.findViewById(R.id.textview);
        txtTitle.setText(rowItem.states);

        return rowview;
    }

}

package com.trec2go.MorningBrewHawaii.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class.menu_options;
import com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class.options;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<menu_options> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<menu_options, List<options>> _listDataChild;

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public OnModifiersClickListner onModifiersClickListner;

    public interface OnModifiersClickListner{
        public String onCheckBoxSelected(List<menu_options> _listDataHeader, int groupposition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition);
        public String onCheckBoxUnSelected(List<menu_options> _listDataHeader, int groupposition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition);
        public String onQuantityAdded(List<menu_options> _listDataHeader, int groupposition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition);
        public String onQuantityRemove(List<menu_options> _listDataHeader, int groupposition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition);
        public String onRefreshDefault(List<menu_options> _listDataHeader, int groupposition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition);
    }


    public ExpandableListAdapter(Context context, List<menu_options> listDataHeader,
                                 HashMap<menu_options, List<options>> listChildData,OnModifiersClickListner onModifiersClickListner) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.onModifiersClickListner = onModifiersClickListner;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //final options orderModifierBean = (options) getChild(groupPosition, childPosition);

        _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_items, null);
        }

        TextView tv_title = convertView.findViewById(R.id.tv_title);
        TextView tv_price = convertView.findViewById(R.id.tv_price);
        TextView tv_btn_plus = convertView.findViewById(R.id.tv_btn_plus);
        final TextView tv_counter = convertView.findViewById(R.id.tv_counter);
        TextView tv_btn_minus = convertView.findViewById(R.id.tv_btn_minus);
        final CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(_listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).isChecked);

        tv_title.setText(_listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).name);
        if(_listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).price.equalsIgnoreCase("0") || _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).price.equalsIgnoreCase("")){
            tv_price.setText("N/C");
        }else {
            tv_price.setText("$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(_listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).price)));
        }
        tv_counter.setText(String.valueOf(_listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).getCounter()));


        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    int allow = Integer.parseInt(_listDataHeader.get(groupPosition).optionallow);
                    int current_allow = _listDataHeader.get(groupPosition).currentoptionallow;
                    if(current_allow < allow){

                        _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).setCounter(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getCounter() + 1);
                        tv_counter.setText(String.valueOf(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getCounter()));

                        for(int i =0;i< _listDataChild.get(_listDataHeader.get(groupPosition)).size();i++){
                            if(childPosition == i){
                                _listDataChild.get(_listDataHeader.get(groupPosition)).get(i).setChecked(true);
                                //_listDataChild.get(_listDataHeader.get(groupPosition)).get(i).setCounter(0);
                                String currentoptionallow = onModifiersClickListner.onCheckBoxSelected(_listDataHeader,groupPosition,_listDataChild.get(_listDataHeader.get(groupPosition)).get(i),_listDataChild,childPosition);
                                _listDataHeader.get(groupPosition).currentoptionallow = Integer.parseInt(currentoptionallow);
                            }else {

                                //Debug v1
                            /*if(_listDataChild.get(_listDataHeader.get(groupPosition)).get(i).isChecked){
                                _listDataChild.get(_listDataHeader.get(groupPosition)).get(i).setChecked(false);
                                _listDataChild.get(_listDataHeader.get(groupPosition)).get(i).setCounter(0);
                                String total = onModifiersClickListner.onCheckBoxUnSelected(_listDataHeader,groupPosition,_listDataChild.get(_listDataHeader.get(groupPosition)).get(i));
                                _listDataHeader.get(groupPosition).total_price = Double.parseDouble(total);
                            }*/

                            }
                        }
                    }else {
                        checkBox.setOnCheckedChangeListener(null);
                        checkBox.setChecked(false);
                        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
                    }
                }else {
                    _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).setChecked(false);
                    _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).setCounter(0);
                    String currentoptionallow =  onModifiersClickListner.onCheckBoxUnSelected(_listDataHeader,groupPosition,_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition),_listDataChild,childPosition);
                    _listDataHeader.get(groupPosition).currentoptionallow = Integer.parseInt(currentoptionallow);
                }

                notifyDataSetChanged();
            }
        };

        if(_listDataHeader.get(groupPosition).default_.equalsIgnoreCase("Y")){
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setEnabled(false);
            tv_btn_plus.setOnClickListener(null);
            tv_btn_minus.setOnClickListener(null);


            /*if(childPosition == _listDataChild.get(_listDataHeader.get(groupPosition)).size()-1){
                String currentoptionallow = onModifiersClickListner.onRefreshDefault(_listDataHeader,groupPosition,_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition),_listDataChild,childPosition);
            }*/

        }else {
            checkBox.setEnabled(true);
            checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
            tv_btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).isChecked){
                        _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).setCounter(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getCounter() + 1);
                        tv_counter.setText(String.valueOf(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getCounter()));
                        String currentoptionallow = onModifiersClickListner.onQuantityAdded(_listDataHeader,groupPosition,_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition),_listDataChild,childPosition);
                        _listDataHeader.get(groupPosition).currentoptionallow = Integer.parseInt(currentoptionallow);
                    }else {

                    }
                }
            });

            tv_btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).isChecked){
                        if(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getCounter() <= 1 ){

                        }else {
                            _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).setCounter(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getCounter() - 1);
                            tv_counter.setText(String.valueOf(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).getCounter()));
                            String currentoptionallow = onModifiersClickListner.onQuantityRemove(_listDataHeader,groupPosition,_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition),_listDataChild,childPosition);
                            _listDataHeader.get(groupPosition).currentoptionallow = Integer.parseInt(currentoptionallow);
                        }
                    }
                }
            });
        }




        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        menu_options headerTitle = (menu_options) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_header, null);
        }


        TextView tv_title = convertView.findViewById(R.id.tv_title);
        tv_title.setText(headerTitle.topname);



        if(_listDataHeader.get(groupPosition).default_.equalsIgnoreCase("Y")){
            for(int i=0;i<_listDataChild.get(this._listDataHeader.get(groupPosition)).size();i++){
                _listDataChild.get(_listDataHeader.get(groupPosition)).get(i).setChecked(true);
                _listDataChild.get(_listDataHeader.get(groupPosition)).get(i).setCounter(1);
            }
        }


/*
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText("");


        TextView expand = convertView.findViewById(R.id.tv_expand);
        if (isExpanded) {
            expand.setText("HIDE");
        } else {
            expand.setText("SHOW");
        }
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // frag.expandandcollapse(isExpanded,groupPosition);
                if (isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                else ((ExpandableListView) parent).expandGroup(groupPosition, true);
            }
        });*/
        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    public void updatelist(List<menu_options> _listDataHeader,HashMap<menu_options, List<options>> _listDataChild){
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
    }

    public String getAdvancedmenu(){

        ArrayList<String> advanced_menu = new ArrayList<>();
        for(int i=0;i<_listDataHeader.size();i++){
            String menu = "";
            for(int j=0;j<_listDataChild.get(_listDataHeader.get(i)).size();j++){
                menu = "";
                if(_listDataChild.get(_listDataHeader.get(i)).get(j).isChecked()){
                    menu = _listDataHeader.get(i).topname;
                    menu = menu + "@" + _listDataChild.get(_listDataHeader.get(i)).get(j).name;
                    if(_listDataChild.get(_listDataHeader.get(i)).get(j).price.equalsIgnoreCase("")){
                        menu = menu + "@" + "0";
                    }else {
                        menu = menu + "@" + _listDataChild.get(_listDataHeader.get(i)).get(j).price;
                    }
                }else {
                    continue;
                }


                if(!"".equalsIgnoreCase(menu)){
                    advanced_menu.add(menu);
                }
            }

        }

        String menu_str = advanced_menu.toString();
        String ad_menu_str = menu_str.substring(1, menu_str.length() - 1).replace(", ", ",");


        return ad_menu_str;
    }





    public String getAdvancedmenuQuantity(){

        ArrayList<String> advanced_menu = new ArrayList<>();



        for(int i=0;i<_listDataHeader.size();i++){
            String quantity = "";
            for(int j=0;j<_listDataChild.get(_listDataHeader.get(i)).size();j++){
                quantity = "";
                if(_listDataChild.get(_listDataHeader.get(i)).get(j).isChecked()){
                    quantity = String.valueOf(_listDataChild.get(_listDataHeader.get(i)).get(j).counter);
                }else {
                    continue;
                }

                if(!"".equalsIgnoreCase(quantity)){
                    advanced_menu.add(quantity);
                }

            }



        }

        String menu_str = advanced_menu.toString();
        String ad_menu_str = menu_str.substring(1, menu_str.length() - 1).replace(", ", ",");


        return ad_menu_str;
    }



    public String getExtraCharge(){

        ArrayList<String> advanced_menu = new ArrayList<>();

        for(int i=0;i<_listDataHeader.size();i++){
            String extra_charge = "";
            for(int j=0;j<_listDataChild.get(_listDataHeader.get(i)).size();j++){
                extra_charge = "";
                if(_listDataChild.get(_listDataHeader.get(i)).get(j).isChecked()){
                    //extra_charge = _listDataChild.get(_listDataHeader.get(i)).get(j).extra_charge;
                    if(_listDataChild.get(_listDataHeader.get(i)).get(j).price.equalsIgnoreCase(""))
                    {
                        extra_charge = "0";
                    }
                    else {
                        Log.e("Price" , "Price" + _listDataChild.get(_listDataHeader.get(i)).get(j).price);
                        extra_charge = String.valueOf(Double.parseDouble(_listDataChild.get(_listDataHeader.get(i)).get(j).price)) ;
                        // extra_charge = _listDataChild.get(_listDataHeader.get(i)).get(j).extra_charge ;//$$$ change extracherge

                    }

                }else {
                    continue;
                }
                if(!"".equalsIgnoreCase(extra_charge)){
                    advanced_menu.add(extra_charge );
                }
            }
        }

        if(advanced_menu.isEmpty()){
            return "";

        }else {
            String menu_str = advanced_menu.toString();
            String ad_menu_str = menu_str.substring(1, menu_str.length() - 1).replace(", ", ",");
            return ad_menu_str;

        }



    }

}

package com.trec2go.MorningBrewHawaii.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.bean.YourBagBean;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

public class YourBagItemAdapter extends RecyclerView.Adapter<YourBagItemAdapter.ViewHolder> {

    ArrayList<YourBagBean> list;
    Context context;
    Activity activity;
    String from;
    double itemPrice;
    YourBagClickListner yourBagClickListner;

    public interface YourBagClickListner {
        public void OnRemoveAllClickListner();

        public void OnListReadyListner(ArrayList<YourBagBean> list);

        public void OnRemoveClickListner(ArrayList<YourBagBean> list, int position);
    }

    public YourBagItemAdapter(Context context, Activity activity, ArrayList<YourBagBean> list, String from, YourBagClickListner yourBagClickListner) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.from = from;
        //from => cart (Show remove button)
        //from => review (Dont Show remove button)
        this.yourBagClickListner = yourBagClickListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_price, tv_btn_remove, item_name, item_amount, item_actual_amount, item_discount_label, item_note;
        RelativeLayout rl_main, rl_free, rl_non_free;
        LinearLayout ll_advanced_menu;

        // product loyalty
        TextView item_name_free, tv_btn_remove_free, item_amount_free, item_actual_amount_free, item_discount_label_free, item_note_free;
        LinearLayout ll_advanced_menu_free;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            rl_non_free = itemView.findViewById(R.id.rl_non_free);
            rl_main = itemView.findViewById(R.id.rl_main);
            item_name = itemView.findViewById(R.id.item_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_btn_remove = itemView.findViewById(R.id.tv_btn_remove);
            item_amount = itemView.findViewById(R.id.item_amount);
            item_actual_amount = itemView.findViewById(R.id.item_actual_amount);
            item_discount_label = itemView.findViewById(R.id.item_discount_label);
            item_note = itemView.findViewById(R.id.item_note);
            ll_advanced_menu = itemView.findViewById(R.id.ll_advanced_menu);

            if (from.equalsIgnoreCase("review")) {
                tv_btn_remove.setVisibility(View.GONE);
            }

            item_actual_amount.setVisibility(View.GONE);
            item_discount_label.setVisibility(View.GONE);

            // for free item or product loyalty
            rl_free = itemView.findViewById(R.id.rl_free);
            item_name_free = itemView.findViewById(R.id.item_name_free);
            tv_btn_remove_free = itemView.findViewById(R.id.tv_btn_remove_free);
            item_amount_free = itemView.findViewById(R.id.item_amount_free);
            item_actual_amount_free = itemView.findViewById(R.id.item_actual_amount_free);
            item_discount_label_free = itemView.findViewById(R.id.item_discount_label_free);
            item_note_free = itemView.findViewById(R.id.item_note_free);
            ll_advanced_menu_free = itemView.findViewById(R.id.ll_advanced_menu_free);

            if (from.equalsIgnoreCase("review")) {
                tv_btn_remove.setVisibility(View.GONE);
                tv_btn_remove_free.setVisibility(View.GONE);

            }

            item_actual_amount.setVisibility(View.GONE);
            item_discount_label.setVisibility(View.GONE);
            item_actual_amount_free.setVisibility(View.GONE);
            item_discount_label_free.setVisibility(View.GONE);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_your_bag_item, parent, false);
        ViewHolder vhItem = new ViewHolder(v, viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.rl_free.setVisibility(View.GONE);

        /*holder.tv_btn_remove.setPaintFlags(holder.tv_btn_remove.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.item_name.setText(list.get(position).quantity + " X " + list.get(position).order_items);
        holder.item_amount.setText("$" + list.get(position).item_unit_price);*/
        itemPrice = Integer.parseInt(list.get(position).quantity) * Double.parseDouble(list.get(position).item_unit_price);
        holder.tv_btn_remove.setPaintFlags(holder.tv_btn_remove.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Utility.log("outside");
        holder.item_name.setText(list.get(position).quantity + " X " + Html.fromHtml(Html.fromHtml(list.get(position).order_items).toString()));

        holder.item_amount.setText("$" + list.get(position).item_unit_price);

        if (!list.get(position).product_free_item.equalsIgnoreCase("0")) {
            holder.rl_free.setVisibility(View.VISIBLE);
            int quantity = Integer.parseInt(list.get(position).quantity) - Integer.parseInt(list.get(position).product_free_item);
            itemPrice = Double.parseDouble(list.get(position).item_unit_price) * quantity;

            if(quantity != 0)
            {
                holder.tv_btn_remove.setPaintFlags(holder.tv_btn_remove.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                holder.item_name.setText(quantity + " X " + (Html.fromHtml(list.get(position).order_items)));
                Utility.log("inside");
                holder.item_amount.setText("$" + list.get(position).item_unit_price);
            }
            else {
                holder.rl_non_free.setVisibility(View.GONE);
            }

            holder.tv_btn_remove_free.setPaintFlags(holder.tv_btn_remove_free.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.item_name_free.setText(list.get(position).product_free_item + " X " + Html.fromHtml(list.get(position).order_items).toString());
            Utility.log("insidefree");
            holder.item_amount_free.setText("Free");
        }

        if (!list.get(position).special.equalsIgnoreCase("0")) {
            holder.item_actual_amount.setVisibility(View.VISIBLE);
            holder.item_discount_label.setVisibility(View.VISIBLE);
            holder.item_actual_amount.setText("$" + list.get(position).actual_price);
            holder.item_discount_label.setText("Discount (" + list.get(position).special_discount + "%)");
            holder.item_actual_amount.setPaintFlags(holder.item_actual_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.item_actual_amount.setVisibility(View.GONE);
            holder.item_discount_label.setVisibility(View.GONE);
        }

        if (!list.get(position).special_request.equalsIgnoreCase("")) {
            holder.item_note.setVisibility(View.VISIBLE);
            holder.item_note.setText("Note : " +  (Html.fromHtml(Html.fromHtml(list.get(position).special_request).toString())));
        } else {
            holder.item_note.setVisibility(View.GONE);
        }

        holder.tv_btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yourBagClickListner.OnRemoveClickListner(list, position);

            }
        });

        holder.tv_btn_remove_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yourBagClickListner.OnRemoveClickListner(list, position);
            }
        });



        holder.ll_advanced_menu.removeAllViews();
        if (!list.get(position).advance_menu.equalsIgnoreCase("")) {
            double advance_menu_subitem_price = 0;
            double advance_menu_extracharge_price = 0;

            List<String> advance_menu = Arrays.asList(list.get(position).advance_menu.split("\\s*,\\s*"));
            List<String> advance_menu_quantity = Arrays.asList(list.get(position).advance_menu_quantity.split("\\s*,\\s*"));
            List<String> advance_menu_charge = Arrays.asList(list.get(position).advance_menu_charge.split("\\s*,\\s*"));

            for (int i = 0; i < advance_menu.size(); i++) {
                List<String> alist = Arrays.asList(advance_menu.get(i).split("\\s*@\\s*"));
                if (!alist.isEmpty()) {
                    if (advance_menu_charge.isEmpty()) {
                        holder.ll_advanced_menu.addView(AddAdvancedmenu(alist.get(0) + " ($0.00) "));
                        Log.e("ll_advanced_menu", advance_menu_quantity.get(i) + " X " + alist.get(1) + " " + alist.get(2)); // $$$
                        holder.ll_advanced_menu.addView(AddSubAdvancedmenu(advance_menu_quantity.get(i) + " X " + alist.get(1), alist.get(2)));
                    } else {
                        if (advance_menu_charge.get(i).equalsIgnoreCase("") || advance_menu_charge.get(i).equalsIgnoreCase("null") || advance_menu_charge.get(i).isEmpty()) {
                            holder.ll_advanced_menu.addView(AddAdvancedmenu(alist.get(0) + " ($0.00) "));
                        } else {
                            advance_menu_extracharge_price = advance_menu_extracharge_price + Double.parseDouble(advance_menu_charge.get(i));
                            holder.ll_advanced_menu.addView(AddAdvancedmenu(alist.get(0) + " ($" + advance_menu_charge.get(i) + ") "));
                            Log.e("ll_advanced_menu", advance_menu_charge.get(i)); // $$$

                        }
                        holder.ll_advanced_menu.addView(AddSubAdvancedmenu(advance_menu_quantity.get(i) + " X " + alist.get(1), alist.get(2)));
                    }
                }


                int quantity = Integer.parseInt(advance_menu_quantity.get(i));
                double total = quantity * Double.parseDouble(alist.get(2));
                advance_menu_subitem_price = advance_menu_subitem_price + total;
            }
            double total = advance_menu_subitem_price + itemPrice;
            list.get(position).setFinal_price(total);
        } else {
            list.get(position).setFinal_price(itemPrice);
        }

        if (position == list.size() - 1) {
            yourBagClickListner.OnListReadyListner(list);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<YourBagBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public ArrayList<YourBagBean> getList() {
        return list;
    }

    public RelativeLayout AddAdvancedmenu(String advance_menu) {

        RelativeLayout parent = new RelativeLayout(context);
        parent.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        parent.setPadding(0, 2, 0, 2);

        TextView tv_order_name = new TextView(context);
        tv_order_name.setText(advance_menu);
        tv_order_name.setTextSize(12);
        tv_order_name.setTextColor(context.getResources().getColor(R.color.black));

        parent.addView(tv_order_name);

        return parent;
    }

    public View AddSubAdvancedmenu(String advance_menu, String advance_price) {

        View view = LayoutInflater.from(context).inflate(R.layout.advanced_menu_sub_item, null);

        TextView tv_order_name = view.findViewById(R.id.advanced_item_name);
        tv_order_name.setText("- " + advance_menu);
        tv_order_name.setTextColor(context.getResources().getColor(R.color.text_gray));

        TextView advanced_item_price = view.findViewById(R.id.advanced_item_price);
        advanced_item_price.setText("$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(advance_price)));


        return view;
    }

}

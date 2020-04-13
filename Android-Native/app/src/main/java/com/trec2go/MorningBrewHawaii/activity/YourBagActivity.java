package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.YourBagItemAdapter;
import com.trec2go.MorningBrewHawaii.bean.ReviewOrderBean;
import com.trec2go.MorningBrewHawaii.bean.YourBagBean;
import com.trec2go.MorningBrewHawaii.commonapicall.RemoveItemFromCart;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Config;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class YourBagActivity extends AppCompatActivity {



    Spinner spn_tip, spn_order_type;

    Activity activity;
    Context context;
    ImageView img_back_arrow;

    Button btn_review, btn_continue;
    LinearLayout ll_remove_all;

    RecyclerView rv_list;
    ArrayList<YourBagBean> ar_list; //array list
    public static YourBagItemAdapter ad_list; //Adapter

    EditText edt_promo_code;

    //tips
    ArrayList<String> tips = new ArrayList<>();
    ArrayAdapter adapter;
    LinearLayout ll_delivery_type;
    LinearLayout ll_delivery_amounts;
    LinearLayout ll_tips;

    public static ArrayList<ReviewOrderBean> review_list = new ArrayList<>();


    RelativeLayout rl_surcharge_amount, rl_special_discount, rl_promocode, rl_first_discount, rl_tip, rl_sales_tax;

    TextView tv_item_total, tv_tips, tv_surcharge_amount, tv_tax, tv_total, tv_sales_tax_title, tv_special_discount_ammont, tv_special_discount_heading, tv_promocode_discount, tv_promocode_heading, tv_first_discount_heading, tv_first_discount_ammont;


    double item_total = 0.00;
    double tips_total = 0.00;
    double taxPercentage = 0.00;
    /*
        double deliverychargepercentage = 0.00;
    */
    double surcharge_Amount = 0.00;
    double special_discount_amount = 0.00;
    double promocode_amount = 0.00;
    double first_discount_amount = 0.00;


    String sdType = "";
    String sdAmount = "";

    String firstordertype = "";
    String discountvalue = "";

    String totalsd = "";
    String globalPromocode = "";
    String globalPromocodeDiscountType = "";
    String globalPromocodeDiscount = "";

    Intent intent;


    View.OnClickListener review_onClickListener;

    /*
     * For Displaying digit upto two decimal places there are two possibilities -
     * 1) Firstly, you only want to display decimal digits if it's there.
     * For example - i) 12.10 to be displayed as 12.1, ii) 12.00 to be displayed as 12
     *
     *     DecimalFormat df2 = new DecimalFormat("#.##");
     *
     * */

    /*
     * 2) Secondly, you want to display decimal digits irrespective of decimal present
     * For example -i) 12.10 to be displayed as 12.10. ii) 12 to be displayed as 12.00.Then use-
     * */

    DecimalFormat df2 = new DecimalFormat("#.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_bag);

        context = this;
        activity = this;
        GetHome();

        init();


    }

    public void init() {
        edt_promo_code = findViewById(R.id.edt_promo_code);
        tv_item_total = findViewById(R.id.tv_item_total);
        img_back_arrow = findViewById(R.id.img_back_arrow);
        btn_review = findViewById(R.id.btn_review);
        btn_continue = findViewById(R.id.btn_continue);
        ll_delivery_type = findViewById(R.id.ll_delivery_type);
        ll_remove_all = findViewById(R.id.ll_remove_all);
        ll_delivery_amounts = findViewById(R.id.ll_delivery_amounts);

        rl_surcharge_amount = findViewById(R.id.rl_surcharge_amount);

        rl_special_discount = findViewById(R.id.rl_special_discount);
        tv_special_discount_ammont = findViewById(R.id.tv_special_discount_ammont);
        tv_special_discount_heading = findViewById(R.id.tv_special_discount_heading);

        rl_promocode = findViewById(R.id.rl_promocode);
        tv_promocode_discount = findViewById(R.id.tv_promocode_discount);
        tv_promocode_heading = findViewById(R.id.tv_promocode_heading);

        rl_first_discount = findViewById(R.id.rl_first_discount);
        tv_first_discount_heading = findViewById(R.id.tv_first_discount_heading);
        tv_first_discount_ammont = findViewById(R.id.tv_first_discount_ammont);


        tv_surcharge_amount = findViewById(R.id.tv_surcharge_amount);
        tv_total = findViewById(R.id.tv_total);
        tv_sales_tax_title = findViewById(R.id.tv_sales_tax_title);
        tv_tax = findViewById(R.id.tv_tax);

        tv_tips = findViewById(R.id.tv_tips);
        ll_tips = findViewById(R.id.ll_tips);

        spn_tip = findViewById(R.id.spn_tip);

        spn_order_type = findViewById(R.id.spn_order_type);

        rl_tip = findViewById(R.id.rl_tip);
        rl_sales_tax = findViewById(R.id.rl_sales_tax);
        final ArrayList<String> order_type = new ArrayList<>();
        order_type.add("Select"); //$$$  change Delivery to pickup

        if (Preference.getString(context, Preference.STORAGE_Pickup).equalsIgnoreCase("1")) {
            order_type.add("Pickup"); //$$$  change Delivery to pickup
        }
        if (Preference.getString(context, Preference.STORAGE_Delivery).equalsIgnoreCase("1")) {
            order_type.add("Delivery");  //$$$  change pickup to Delivery
        }
        ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.spinner_textview, order_type);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_order_type.setAdapter(adapter1);
        spn_order_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (order_type.get(position).equalsIgnoreCase("Delivery")) {
                    ll_tips.setVisibility(View.VISIBLE);
                    ll_delivery_amounts.setVisibility(View.VISIBLE);
                } else if (order_type.get(position).equalsIgnoreCase("Pickup")) {
                    ll_tips.setVisibility(View.GONE);
                    ll_delivery_amounts.setVisibility(View.GONE);
                } else {
                    ll_tips.setVisibility(View.GONE);
                    ll_delivery_amounts.setVisibility(View.GONE);
                }
                Updatetotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setAdapter();
        GetYourBagAPI(); //$$$ call GetYourBagAPI
    }

    public void setAdapter() {
        rv_list = findViewById(R.id.rv_list);
        ar_list = new ArrayList<YourBagBean>();
        /* ar_list = getItemList();*/
        ad_list = new YourBagItemAdapter(context, activity, ar_list, "cart", new YourBagItemAdapter.YourBagClickListner() {
            @Override
            public void OnRemoveAllClickListner() {
                if(Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")){
                    Preference.setString(context, Preference.CATERING_CART_ID, "");

                }else {
                    Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");

                }
                finish();
            }

            @Override
            public void OnListReadyListner(ArrayList<YourBagBean> list) {
                item_total = 0;
                for (int i = 0; i < list.size(); i++) {

                    item_total = item_total + list.get(i).final_price;

                }
            }

            @Override
            public void OnRemoveClickListner(ArrayList<YourBagBean> list, int position) {
                if(Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")){

                    try {
                        JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.CATERING_CART_ID));
                        final JSONArray jsonArray = new JSONArray(Preference.getString(context, Preference.CATERING_CART_ID));
                        for (int i = 0; i < oid_jsonArray.length(); i++) {
                            if (oid_jsonArray.getJSONObject(i).getString("order_id").equalsIgnoreCase(list.get(position).id)) {
                                jsonArray.remove(i);
                                break;
                            } else {
                                continue;
                            }
                        }
                        Preference.setString(context, Preference.CATERING_CART_ID, jsonArray.toString());

                        //Update item total
                        item_total = 0;
                        for (int i = 0; i < list.size(); i++) {
                            if (i == position) {

                            } else {

                                item_total = item_total + list.get(i).final_price;

                            }
                        }

                        RemoveItemFromCart.RemoveItemCartAPI(context, list.get(position).id, new RemoveItemFromCart.RemoveItemFromCartListner() {
                            @Override
                            public void onResponse() {
                                if (Preference.getString(context, Preference.CATERING_CART_ID).equalsIgnoreCase("")) {
                                    finish();
                                } else {
                                    JSONArray jsonArray1 = null;
                                    try {
                                        jsonArray1 = new JSONArray(Preference.getString(context, Preference.CATERING_CART_ID));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (jsonArray1 != null) {
                                        if (jsonArray1.length() != 0) {
                                            GetYourBagAPI();
                                        } else {
                                            Preference.setString(context, Preference.CATERING_CART_ID, "");
                                            finish();
                                        }
                                    }
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID
                        ));
                        final JSONArray jsonArray = new JSONArray(Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID));
                        for (int i = 0; i < oid_jsonArray.length(); i++) {
                            if (oid_jsonArray.getJSONObject(i).getString("order_id").equalsIgnoreCase(list.get(position).id)) {
                                jsonArray.remove(i);
                                break;
                            } else {
                                continue;
                            }
                        }
                        Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, jsonArray.toString());

                        //Update item total
                        item_total = 0;
                        for (int i = 0; i < list.size(); i++) {
                            if (i == position) {

                            } else {

                                item_total = item_total + list.get(i).final_price;

                            }
                        }

                        RemoveItemFromCart.RemoveItemCartAPI(context, list.get(position).id, new RemoveItemFromCart.RemoveItemFromCartListner() {
                            @Override
                            public void onResponse() {
                                if (Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID).equalsIgnoreCase("")) {
                                    finish();
                                } else {
                                    JSONArray jsonArray1 = null;
                                    try {
                                        jsonArray1 = new JSONArray(Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (jsonArray1 != null) {
                                        if (jsonArray1.length() != 0) {
                                            GetYourBagAPI();
                                        } else {
                                            Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                            finish();
                                        }
                                    }
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(activity);
        rv_list.setLayoutManager(layoutManager2);
        rv_list.setAdapter(ad_list);

        listners();
    }

    public void listners() {
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        review_onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spn_order_type.getSelectedItem().equals("Select")){
                    com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Select Order Type", new DialogCallBackListner() {
                        @Override
                        public void setPositiveListner() {

                        }
                        @Override
                        public void setNegativeListner() {
                        }
                    });
                    return;

                }

                final ReviewOrderBean reviewOrderBean = new ReviewOrderBean();
                review_list.clear();
                reviewOrderBean.setDelivery("$0.00");


                if (ll_tips.getVisibility() == View.VISIBLE) {
                    intent = new Intent(activity, AddressActivity.class);
                    intent.putExtra("FROM", "YourBagActivity");
                    Preference.setString(context, Preference.from, "YourBagActivity");


                } else {
                    intent = new Intent(activity, ReviewOrderActivity.class);

                }

                //intent.putExtra("item_total", tv_item_total.getText().toString().trim());
                reviewOrderBean.setItem_totle(tv_item_total.getText().toString().trim());
                //intent.putExtra("tax",tv_tax.getText().toString().trim());
                //intent.putExtra("taxpercentage", tv_tax.getText().toString().trim());
                reviewOrderBean.setText_percentage(tv_tax.getText().toString().trim());

                //intent.putExtra("tax", String.valueOf(taxPercentage));
                reviewOrderBean.setTax(String.valueOf(taxPercentage));

                //intent.putExtra("surcharge", tv_surcharge_amount.getText().toString().trim());
                reviewOrderBean.setSurcharge(tv_surcharge_amount.getText().toString().trim());


                if (!edt_promo_code.getText().toString().trim().equalsIgnoreCase("")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
                    if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
                        map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

                    } else {
                        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));

                    }
                    map.put("u_id", Preference.getString(context, Preference.USER_ID));
                    map.put("promo_code", edt_promo_code.getText().toString().trim());


                    ApiCall.volleyPostApi(context, AppConstants.apply_promocode, map, new ApiCallBackListner() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equalsIgnoreCase("1")) {
                                    String promo_code = jsonObject1.getString("promo_code");
                                    globalPromocode = promo_code;
                                    String discount = jsonObject1.getString("discount");
                                    globalPromocodeDiscount = discount;
                                    String discount_type = jsonObject1.getString("discount_type");
                                    globalPromocodeDiscountType = discount_type;


                                    if (discount_type.equalsIgnoreCase("P")) {

                                        rl_special_discount.setVisibility(View.GONE);
                                        rl_promocode.setVisibility(View.VISIBLE);
                                        tv_promocode_heading.setText("Promo Code (" + promo_code + ") Discount (" + discount + "% of " + tv_item_total.getText().toString() + ")");
                                        promocode_amount = (Double.parseDouble(discount) * item_total) / 100;
                                        tv_promocode_discount.setText("-$" + Validation.roundOffTo2DecPlaces(promocode_amount));
                                        Updatetotal();
                                        /*intent.putExtra("sdType", sdType);
                                        intent.putExtra("sdAmount", sdAmount);
                                        intent.putExtra("totlesd", totalsd);*/

                                        reviewOrderBean.setSdType(sdType);
                                        reviewOrderBean.setSdAmount(sdAmount);
                                        reviewOrderBean.setTotlesdt(sdType);

                                       /* intent.putExtra("is_special_applied", Config.TAG_FAILURE);
                                        intent.putExtra("special_discount_heading", tv_special_discount_heading.getText().toString().trim());
                                        intent.putExtra("special_discount_amount", tv_special_discount_ammont.getText().toString().trim());*/

                                        reviewOrderBean.setIs_special_applied(Config.TAG_FAILURE);
                                        reviewOrderBean.setSpecial_discount_heading(tv_special_discount_heading.getText().toString().trim());
                                        reviewOrderBean.setSpecial_discount_amount(tv_special_discount_ammont.getText().toString().trim());

                                        //intent.putExtra("total", tv_total.getText().toString().trim());
                                        reviewOrderBean.setTotal(tv_total.getText().toString().trim());

                                        if (ll_tips.getVisibility() == View.VISIBLE) {
                                            /*intent.putExtra("order_type", "1");
                                            intent.putExtra("delivery", tv_delivery.getText().toString().trim());
                                            intent.putExtra("tips_total", tv_tips.getText().toString().trim());*/


                                            reviewOrderBean.setOrder_type("1");
                                            reviewOrderBean.setTips_totle(tv_tips.getText().toString().trim());

                                        } else {
                                            // intent.putExtra("order_type", "2");
                                            reviewOrderBean.setOrder_type("2");

                                        }
                                       /* intent.putExtra("globalPromocodeDiscountType", globalPromocodeDiscountType);
                                        intent.putExtra("globalPromocode", globalPromocode);
                                        intent.putExtra("globalPromocodeDiscount", globalPromocodeDiscount);
                                        intent.putExtra("promocode_heading", tv_promocode_heading.getText().toString().trim());
                                        intent.putExtra("promocode_discount", tv_promocode_heading.getText().toString().trim());*/
                                        reviewOrderBean.setGloblePromocodeDiscountType(globalPromocodeDiscountType);
                                        reviewOrderBean.setGloblePromocode(globalPromocode);
                                        reviewOrderBean.setGloblePromocodeDiscount(globalPromocodeDiscount);
                                        reviewOrderBean.setPromocode_heading(tv_promocode_heading.getText().toString().trim());
                                        reviewOrderBean.setPromocode_discount(tv_promocode_discount.getText().toString().trim());

                                        //intent.putParcelableArrayListExtra("list", ad_list.getList());
                                        review_list.add(reviewOrderBean);
                                        //Utility.dissmisProgress();
                                        startActivity(intent);

                                    } else if (discount_type.equalsIgnoreCase("M") || discount_type.equalsIgnoreCase("F")) {

                                        if (Double.parseDouble(tv_item_total.getText().toString().substring(1)) > Double.parseDouble(discount)) {

                                            rl_special_discount.setVisibility(View.GONE);
                                            rl_promocode.setVisibility(View.VISIBLE);
                                            tv_promocode_heading.setText("Promo Code (" + promo_code + ") Discount ($" + discount + ")");
                                            tv_promocode_discount.setText("-$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(discount)));
                                            promocode_amount = Double.parseDouble(discount);
                                            Updatetotal();


                                           /* intent.putExtra("sdType", sdType);
                                            intent.putExtra("sdAmount", sdAmount);
                                            intent.putExtra("totlesd", totalsd);*/
/*
                                            intent.putExtra("is_special_applied", Config.TAG_FAILURE);
                                            intent.putExtra("special_discount_heading", tv_special_discount_heading.getText().toString().trim());
                                            intent.putExtra("special_discount_amount", tv_special_discount_ammont.getText().toString().trim());*/

                                            reviewOrderBean.setSdType(sdType);
                                            reviewOrderBean.setSdAmount(sdAmount);
                                            reviewOrderBean.setTotlesdt(sdType);

                                            reviewOrderBean.setIs_special_applied(Config.TAG_FAILURE);
                                            reviewOrderBean.setSpecial_discount_heading(tv_special_discount_heading.getText().toString().trim());
                                            reviewOrderBean.setSpecial_discount_amount(tv_special_discount_ammont.getText().toString().trim());

                                            if (rl_first_discount.getVisibility() == View.VISIBLE) {
                                                /*intent.putExtra("is_first_discount_applied", "1");
                                                intent.putExtra("first_discount_heading", tv_first_discount_heading.getText().toString().trim());
                                                intent.putExtra("first_discount_ammont", tv_first_discount_ammont.getText().toString().trim());
                                                intent.putExtra("firstordertype", firstordertype);
                                                intent.putExtra("discountvalue", discountvalue);*/

                                                reviewOrderBean.setIs_first_discount_applied(firstordertype);
                                                reviewOrderBean.setFirst_discount_heading(tv_first_discount_heading.getText().toString().trim());
                                                reviewOrderBean.setFirst_discount_ammont(tv_first_discount_ammont.getText().toString().trim());
                                                reviewOrderBean.setFirstordertype(firstordertype);
                                                reviewOrderBean.setDiscountvalue(discountvalue);

                                            } else {
                                                /*intent.putExtra("is_first_discount_applied", "0");
                                                intent.putExtra("first_discount_heading", tv_first_discount_heading.getText().toString().trim());
                                                intent.putExtra("first_discount_ammont", tv_first_discount_ammont.getText().toString().trim());
                                                intent.putExtra("firstordertype", firstordertype);
                                                intent.putExtra("discountvalue ", discountvalue);*/

                                                reviewOrderBean.setIs_first_discount_applied(firstordertype);
                                                reviewOrderBean.setFirst_discount_heading(tv_first_discount_heading.getText().toString().trim());
                                                reviewOrderBean.setFirst_discount_ammont(tv_first_discount_ammont.getText().toString().trim());
                                                reviewOrderBean.setFirstordertype(firstordertype);
                                                reviewOrderBean.setDiscountvalue(discountvalue);

                                            }


                                            //intent.putExtra("total", tv_total.getText().toString().trim());
                                            reviewOrderBean.setTotal(tv_total.getText().toString().trim());

                                            if (ll_tips.getVisibility() == View.VISIBLE) {
                                               /* intent.putExtra("order_type", "1");
                                                intent.putExtra("delivery", tv_delivery.getText().toString().trim());
                                                intent.putExtra("tips_total", tv_tips.getText().toString().trim());*/

                                                reviewOrderBean.setOrder_type("1");
                                                reviewOrderBean.setTips_totle(tv_tips.getText().toString().trim());
                                            } else {
                                                //intent.putExtra("order_type", "2");
                                                reviewOrderBean.setOrder_type("2");

                                            }
                                            /*intent.putExtra("globalPromocodeDiscountType", globalPromocodeDiscountType);
                                            intent.putExtra("globalPromocode", globalPromocode);
                                            intent.putExtra("globalPromocodeDiscount", globalPromocodeDiscount);
                                            intent.putExtra("promocode_heading", tv_promocode_heading.getText().toString().trim());
                                            intent.putExtra("promocode_discount", tv_promocode_discount.getText().toString().trim());*/

                                            reviewOrderBean.setGloblePromocodeDiscountType(globalPromocodeDiscountType);
                                            reviewOrderBean.setGloblePromocode(globalPromocode);
                                            reviewOrderBean.setGloblePromocodeDiscount(globalPromocodeDiscount);
                                            reviewOrderBean.setPromocode_heading(tv_promocode_heading.getText().toString().trim());
                                            reviewOrderBean.setPromocode_discount(tv_promocode_discount.getText().toString().trim());
                                            //intent.putParcelableArrayListExtra("list", ad_list.getList());
                                            review_list.add(reviewOrderBean);
                                            //Utility.dissmisProgress();
                                            startActivity(intent);
                                        } else {
                                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "This Promocode can't be applied", new DialogCallBackListner() {
                                                @Override
                                                public void setPositiveListner() {
                                                    Updatetotal();

                                                    if (special_discount_amount < item_total) {
                                                        rl_special_discount.setVisibility(View.VISIBLE);
                                                        //intent.putExtra("is_special_applied", Config.TAG_SUCESS);
                                                        reviewOrderBean.setIs_special_applied(Config.TAG_SUCESS);
                                                    } else {
                                                        rl_special_discount.setVisibility(View.GONE);
                                                        //intent.putExtra("is_special_applied", Config.TAG_FAILURE);
                                                        reviewOrderBean.setIs_special_applied(Config.TAG_FAILURE);

                                                    }
                                                    rl_promocode.setVisibility(View.GONE);

                                                   /* tv_promocode_heading.setText("0");
                                                    tv_promocode_discount.setText("0");*/
                                                    /*intent.putExtra("sdType", sdType);
                                                    intent.putExtra("sdAmount", sdAmount);
                                                    intent.putExtra("totlesd", totalsd);
                                                    intent.putExtra("special_discount_heading", tv_special_discount_heading.getText().toString().trim());
                                                    intent.putExtra("special_discount_amount", tv_special_discount_ammont.getText().toString().trim());
                                                    intent.putExtra("total", tv_total.getText().toString().trim());
*/
                                                    reviewOrderBean.setSdType(sdType);
                                                    reviewOrderBean.setSdAmount(sdAmount);
                                                    reviewOrderBean.setTotlesdt(sdType);
                                                    reviewOrderBean.setIs_special_applied(Config.TAG_FAILURE);
                                                    reviewOrderBean.setSpecial_discount_heading(tv_special_discount_heading.getText().toString().trim());
                                                    reviewOrderBean.setSpecial_discount_amount(tv_special_discount_ammont.getText().toString().trim());
                                                    reviewOrderBean.setTotal(tv_total.getText().toString().trim());

                                                    if (ll_tips.getVisibility() == View.VISIBLE) {
                                                        /*intent.putExtra("order_type", "1");
                                                        intent.putExtra("delivery", tv_delivery.getText().toString().trim());
                                                        intent.putExtra("tips_total", tv_tips.getText().toString().trim());*/

                                                        reviewOrderBean.setOrder_type("1");
                                                        reviewOrderBean.setTips_totle(tv_tips.getText().toString().trim());
                                                    } else {
                                                        // intent.putExtra("order_type", "2");
                                                        reviewOrderBean.setOrder_type("2");

                                                    }
                                                   /* intent.putExtra("globalPromocodeDiscountType", "");
                                                    intent.putExtra("globalPromocode", "");
                                                    intent.putExtra("globalPromocodeDiscount", "");
                                                    intent.putExtra("promocode_heading", "0");
                                                    intent.putExtra("promocode_discount", "0");*/

                                                    reviewOrderBean.setGloblePromocodeDiscountType("");
                                                    reviewOrderBean.setGloblePromocode("");
                                                    reviewOrderBean.setGloblePromocodeDiscount("");
                                                    reviewOrderBean.setPromocode_heading("");
                                                    reviewOrderBean.setPromocode_discount("");
                                                    //intent.putParcelableArrayListExtra("list", ad_list.getList());
                                                    Updatetotal();

                                                    review_list.add(reviewOrderBean);


                                                    //Utility.dissmisProgress();
                                                    startActivity(intent);
                                                    //Cliked Ok
                                                }

                                                @Override
                                                public void setNegativeListner() {

                                                }
                                            });
                                        }

                                    }
                                } else {
                                    //Utility.dissmisProgress();
                                    String msg = jsonObject1.optString("msg");
                                    com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, msg, new DialogCallBackListner() {
                                        @Override
                                        public void setPositiveListner() {

                                            if (rl_special_discount.getVisibility() == View.VISIBLE) {
                                                //intent.putExtra("is_special_applied", Config.TAG_SUCESS);
                                                reviewOrderBean.setIs_special_applied(Config.TAG_SUCESS);
                                            } else {
                                                //intent.putExtra("is_special_applied", Config.TAG_FAILURE);
                                                reviewOrderBean.setIs_special_applied(Config.TAG_FAILURE);
                                            }
                                            rl_promocode.setVisibility(View.GONE);
                                            Updatetotal();

                                           /* intent.putExtra("promocode_heading", "0");
                                            intent.putExtra("promocode_discount", "0");
                                            intent.putExtra("globalPromocodeDiscountType", "0");
                                            intent.putExtra("globalPromocode", "0");

                                            intent.putExtra("special_discount_heading", tv_special_discount_heading.getText().toString().trim());
                                            intent.putExtra("special_discount_amount", tv_special_discount_ammont.getText().toString().trim());

                                            intent.putExtra("sdType", sdType);
                                            intent.putExtra("sdAmount", sdAmount);
                                            intent.putExtra("totlesd", totalsd);*/

                                            reviewOrderBean.setPromocode_heading("");
                                            reviewOrderBean.setPromocode_discount("");
                                            reviewOrderBean.setGloblePromocodeDiscountType("");
                                            reviewOrderBean.setGloblePromocode("");
                                            reviewOrderBean.setGloblePromocodeDiscount("");
                                            reviewOrderBean.setSpecial_discount_heading(tv_special_discount_heading.getText().toString().trim());
                                            reviewOrderBean.setSpecial_discount_amount(tv_special_discount_ammont.getText().toString().trim());
                                            reviewOrderBean.setSdType(sdType);
                                            reviewOrderBean.setSdAmount(sdAmount);
                                            reviewOrderBean.setTotlesdt(totalsd);

                                            if (rl_first_discount.getVisibility() == View.VISIBLE) {
                                                /*intent.putExtra("is_first_discount_applied", "1");
                                                intent.putExtra("first_discount_heading", tv_first_discount_heading.getText().toString().trim());
                                                intent.putExtra("first_discount_ammont", tv_first_discount_ammont.getText().toString().trim());
                                                intent.putExtra("firstordertype", firstordertype);
                                                intent.putExtra("discountvalue", discountvalue);*/

                                                reviewOrderBean.setIs_first_discount_applied(firstordertype);
                                                reviewOrderBean.setFirst_discount_heading(tv_first_discount_heading.getText().toString().trim());
                                                reviewOrderBean.setFirst_discount_ammont(tv_first_discount_ammont.getText().toString().trim());
                                                reviewOrderBean.setFirstordertype(firstordertype);
                                                reviewOrderBean.setDiscountvalue(discountvalue);


                                            } else {
                                                /*intent.putExtra("is_first_discount_applied", "0");
                                                intent.putExtra("first_discount_heading", tv_first_discount_heading.getText().toString().trim());
                                                intent.putExtra("first_discount_ammont", tv_first_discount_ammont.getText().toString().trim());
                                                intent.putExtra("firstordertype", firstordertype);
                                                intent.putExtra("discountvalue ", discountvalue);*/

                                                reviewOrderBean.setIs_first_discount_applied(firstordertype);
                                                reviewOrderBean.setFirst_discount_heading(tv_first_discount_heading.getText().toString().trim());
                                                reviewOrderBean.setFirst_discount_ammont(tv_first_discount_ammont.getText().toString().trim());
                                                reviewOrderBean.setFirstordertype(firstordertype);
                                                reviewOrderBean.setDiscountvalue(discountvalue);

                                            }

                                            //intent.putExtra("total", tv_total.getText().toString().trim());
                                            reviewOrderBean.setTotal(tv_total.getText().toString().trim());
                                            if (ll_tips.getVisibility() == View.VISIBLE) {
                                               /* intent.putExtra("order_type", "1");
                                                intent.putExtra("delivery", tv_delivery.getText().toString().trim());
                                                intent.putExtra("tips_total", tv_tips.getText().toString().trim());*/
                                                reviewOrderBean.setOrder_type("1");
                                                reviewOrderBean.setTips_totle(tv_tips.getText().toString().trim());

                                            } else {
                                                //intent.putExtra("order_type", "2");
                                                reviewOrderBean.setOrder_type("2");

                                            }
                                            review_list.add(reviewOrderBean);

                                            //intent.putParcelableArrayListExtra("list", ad_list.getList());

                                            //Utility.dissmisProgress();
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void setNegativeListner() {
                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }

                        @Override
                        public void noInternetConnection() {

                        }
                    });

                } else {

                    if (rl_special_discount.getVisibility() == View.VISIBLE) {
                        //intent.putExtra("is_special_applied", Config.TAG_SUCESS);
                        reviewOrderBean.setIs_special_applied(Config.TAG_SUCESS);
                    } else {
                        //intent.putExtra("is_special_applied", Config.TAG_FAILURE);
                        reviewOrderBean.setIs_special_applied(Config.TAG_FAILURE);

                    }
                    rl_promocode.setVisibility(View.GONE);
                    Updatetotal();

                    /*intent.putExtra("promocode_heading", "0");
                    intent.putExtra("promocode_discount", "0");
                    intent.putExtra("globalPromocodeDiscountType", "0");
                    intent.putExtra("globalPromocode", "0");

                    intent.putExtra("special_discount_heading", tv_special_discount_heading.getText().toString().trim());
                    intent.putExtra("special_discount_amount", tv_special_discount_ammont.getText().toString().trim());

                    intent.putExtra("sdType", sdType);
                    intent.putExtra("sdAmount", sdAmount);
                    intent.putExtra("totlesd", totalsd);*/
                    reviewOrderBean.setPromocode_heading("");
                    reviewOrderBean.setPromocode_discount("");
                    reviewOrderBean.setGloblePromocodeDiscountType("");
                    reviewOrderBean.setGloblePromocode("");
                    reviewOrderBean.setGloblePromocodeDiscount("");

                    reviewOrderBean.setSpecial_discount_heading(tv_special_discount_heading.getText().toString().trim());
                    reviewOrderBean.setSpecial_discount_amount(tv_special_discount_ammont.getText().toString().trim());
                    reviewOrderBean.setSdType(sdType);
                    reviewOrderBean.setSdAmount(sdAmount);
                    reviewOrderBean.setTotlesdt(totalsd);

                    if (rl_first_discount.getVisibility() == View.VISIBLE) {
                                                /*intent.putExtra("is_first_discount_applied", "1");
                                                intent.putExtra("first_discount_heading", tv_first_discount_heading.getText().toString().trim());
                                                intent.putExtra("first_discount_ammont", tv_first_discount_ammont.getText().toString().trim());
                                                intent.putExtra("firstordertype", firstordertype);
                                                intent.putExtra("discountvalue", discountvalue);*/

                        reviewOrderBean.setIs_first_discount_applied(firstordertype);
                        reviewOrderBean.setFirst_discount_heading(tv_first_discount_heading.getText().toString().trim());
                        reviewOrderBean.setFirst_discount_ammont(tv_first_discount_ammont.getText().toString().trim());
                        reviewOrderBean.setFirstordertype(firstordertype);
                        reviewOrderBean.setDiscountvalue(discountvalue);


                    } else {
                                                /*intent.putExtra("is_first_discount_applied", "0");
                                                intent.putExtra("first_discount_heading", tv_first_discount_heading.getText().toString().trim());
                                                intent.putExtra("first_discount_ammont", tv_first_discount_ammont.getText().toString().trim());
                                                intent.putExtra("firstordertype", firstordertype);
                                                intent.putExtra("discountvalue ", discountvalue);*/

                        reviewOrderBean.setIs_first_discount_applied(firstordertype);
                        reviewOrderBean.setFirst_discount_heading(tv_first_discount_heading.getText().toString().trim());
                        reviewOrderBean.setFirst_discount_ammont(tv_first_discount_ammont.getText().toString().trim());
                        reviewOrderBean.setFirstordertype(firstordertype);
                        reviewOrderBean.setDiscountvalue(discountvalue);

                    }


                    //intent.putExtra("total", tv_total.getText().toString().trim());
                    reviewOrderBean.setTotal(tv_total.getText().toString().trim());
                    if (ll_tips.getVisibility() == View.VISIBLE) {
                        /*intent.putExtra("order_type", "1");
                        intent.putExtra("delivery", tv_delivery.getText().toString().trim());
                        intent.putExtra("tips_total", tv_tips.getText().toString().trim());*/


                        reviewOrderBean.setOrder_type("1");
                        reviewOrderBean.setTips_totle(tv_tips.getText().toString().trim());

                    } else {
                        //intent.putExtra("order_type", "2");
                        reviewOrderBean.setOrder_type("2");

                    }
                    review_list.add(reviewOrderBean);
                    //intent.putParcelableArrayListExtra("list", ad_list.getList());
                    //Utility.dissmisProgress();
                    startActivity(intent);
                }
            }
        };


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_remove_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")){
                    String oid = "";
                    try {
                        JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.CATERING_CART_ID));
                        for (int i = 0; i < oid_jsonArray.length(); i++) {
                            oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Preference.setString(context, Preference.CATERING_CART_ID, "");
                    if (oid.length() != 0) {
                        RemoveItemFromCart.RemoveItemCartAPI(context, oid.substring(0, oid.length() - 1), new RemoveItemFromCart.RemoveItemFromCartListner() {
                            @Override
                            public void onResponse() {
                                if (Preference.getString(context, Preference.CATERING_CART_ID).equalsIgnoreCase("")) {
                                    finish();
                                } else {
                                    JSONArray jsonArray1 = null;
                                    try {
                                        jsonArray1 = new JSONArray(Preference.getString(context, Preference.CATERING_CART_ID));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (jsonArray1 != null) {
                                        //GetYourBagAPI();
                                        if (jsonArray1.length() != 0) {
                                            GetYourBagAPI();
                                        } else {
                                            Preference.setString(context, Preference.CATERING_CART_ID, "");
                                            finish();
                                        }
                                    }
                                }
                            }
                        });
                    } else {

                    }
                }else{
                    String oid = "";
                    try {
                        JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID));
                        for (int i = 0; i < oid_jsonArray.length(); i++) {
                            oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                    if (oid.length() != 0) {
                        RemoveItemFromCart.RemoveItemCartAPI(context, oid.substring(0, oid.length() - 1), new RemoveItemFromCart.RemoveItemFromCartListner() {
                            @Override
                            public void onResponse() {
                                if (Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID).equalsIgnoreCase("")) {
                                    finish();
                                } else {
                                    JSONArray jsonArray1 = null;
                                    try {
                                        jsonArray1 = new JSONArray(Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (jsonArray1 != null) {
                                        //GetYourBagAPI();
                                        if (jsonArray1.length() != 0) {
                                            GetYourBagAPI();
                                        } else {
                                            Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                            finish();
                                        }
                                    }
                                }
                            }
                        });
                    } else {

                    }
                }
            }
        });

        GetYourBagAPI();
        //GetTipsAPI();
        //ApplySalesTaxAPI();
        //SurchargeAmountAPI();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //API Calling

    public void GetHome() {

        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));

        }
        ApiCall.volleyPostApi(context, AppConstants.home, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    Preference.setString(context, Preference.STORAGE_Delivery, jsonObject.optString("delivery"));
                    Preference.setString(context, Preference.STORAGE_Pickup, jsonObject.optString("pickup"));
                    Preference.setString(context, Preference.BRANCH_NAME, jsonObject.optString("branch_name"));
                    Preference.setString(context, Preference.PAYMENT_METHOD, jsonObject.optString("payment_method"));
                    Preference.setString(context, Preference.IS_DONATION_VISIBLE, jsonObject.optString("donation"));
                    if (Preference.getString(context, Preference.IS_DONATION_VISIBLE).equalsIgnoreCase("1")) {
                        Preference.setString(context, Preference.DONATION, jsonObject.optString("donation_link"));
                    }

                    Preference.setString(context, Preference.IS_TABLE, jsonObject.optString("booking"));
                    Preference.setString(context, Preference.IS_APPOINTMENT, jsonObject.optString("appointment"));
                    Preference.setString(context,Preference.DOORDASH_TEXT,jsonObject.optString("doordash_text"));
                    Preference.setString(context,Preference.DOORDASH_LINK,jsonObject.optString("doordash_link"));
                    Preference.setString(context,Preference.IS_MENU,jsonObject.optString("menu"));
                    init();

                 /*   Intent intent = new Intent(activity, HomeActivity.class);
                    activity.startActivity(intent);
                    activity.finish();*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }
                //Utility.dissmisProgress();
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

    public void GetYourBagAPI() {
        Utility.createProgressDialoge(context, "Loading");
        special_discount_amount = 0;
        first_discount_amount = 0;

        String oid = "";
        try {
            if(Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")){
                JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.CATERING_CART_ID));
                for (int i = 0; i < oid_jsonArray.length(); i++) {
                    oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                }
            }else {
                JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID));
                for (int i = 0; i < oid_jsonArray.length(); i++) {
                    oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));
        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));

        }

        map.put("user_id", Preference.getString(context, Preference.USER_ID));

        map.put("oid", oid.substring(0, oid.length() - 1));
        ApiCall.volleyPostApi(context, AppConstants.your_bag, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ar_list.clear();
                    ar_list.addAll((Collection<? extends YourBagBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<YourBagBean>>() {
                    }.getType()));
                    //String data = new Gson().toJson(ar_list);
                    /*Preference.setString(context,Preference.STORAGE_YOUR_BAG,data);*/
                    ad_list.updateList(ar_list);
                    for (int i = 0; i < ar_list.size(); i++) {

                    }


                    if (tips.isEmpty()) {
                        GetTipsAPI();
                    } else if (taxPercentage == 0) {
                        ApplySalesTaxAPI();
                    } else if (surcharge_Amount == 0) {
                        SurchargeAmountAPI();
                    } else if (special_discount_amount == 0) {
                        Special_discount();
                    } else if (first_discount_amount == 0) {
                        firstDiscount();
                    } else {
                        Updatetotal();
                    }

//                    Utility.dissmisProgress();
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


    public void GetTipsAPI() {
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        ApiCall.volleyPostApi(context, AppConstants.get_tips, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    tips.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            tips.add("$" + jsonObject.getString("tips"));
                        }
                    }
                    adapter = new ArrayAdapter(YourBagActivity.this, R.layout.spinner_textview, tips);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_tip.setAdapter(adapter);
                    spn_tip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            tips_total = Double.parseDouble(tips.get(position).substring(1, tips.get(position).length()));
                            if(tips_total == 0.0){
                                rl_tip.setVisibility(View.GONE);
                            }else {
                                rl_tip.setVisibility(View.VISIBLE);
                            }
                            // tv_tips.setText(String.valueOf(tips_total));
                            Updatetotal();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if (!tips.isEmpty()) {
                        tips_total = Double.parseDouble(tips.get(0).substring(1));
                    }
                    //spn_tip.setSelection(0);

                    if (taxPercentage == 0) {
                        ApplySalesTaxAPI();
                    } else if (surcharge_Amount == 0) {
                        SurchargeAmountAPI();
                    } else if (special_discount_amount == 0) {
                        Special_discount();
                    } else if (first_discount_amount == 0) {
                        firstDiscount();
                    } else {
                        Updatetotal();
                    }
//                    Utility.dissmisProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void noInternetConnection() {

            }
        });

    }

    public void ApplySalesTaxAPI() {

        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("user_id", Preference.getString(context, Preference.USER_ID));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));
        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }
        map.put("new", "1");
        ApiCall.volleyPostApi(context, AppConstants.apply_sales_tax, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONObject jsonObject2 = jsonArray.getJSONObject(1);
                    if (jsonObject1.getString("tax") == null) {
                        taxPercentage = 0.00;  //$$$  exicute if
                        tv_sales_tax_title.setText("Sales Tax(" + jsonObject1.getString("tax") + "%)");
                    } else {

                        taxPercentage = Double.parseDouble(jsonObject1.getString("tax"));
                        tv_sales_tax_title.setText("Sales Tax(" + jsonObject1.getString("tax") + "%)");

                    }
                    if (surcharge_Amount == 0) {
                        SurchargeAmountAPI();
                    } else if (special_discount_amount == 0) {
                        Special_discount();
                    } else if (first_discount_amount == 0) {
                        firstDiscount();
                    } else {
                        Updatetotal();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void noInternetConnection() {

            }
        });

    }

    public void SurchargeAmountAPI() {
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));

        }
        ApiCall.volleyPostApi(context, AppConstants.surcharge_amount, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    surcharge_Amount = Double.parseDouble(jsonObject1.getString("surcharge_Amount"));
                    if (jsonObject1.getString("surcharge_Amount").equalsIgnoreCase("0")) {
                        rl_surcharge_amount.setVisibility(View.GONE);
                    } else {
                        rl_surcharge_amount.setVisibility(View.VISIBLE);
                        tv_surcharge_amount.setText("$" + Validation.roundOffTo2DecPlaces(surcharge_Amount));
                    }
                    if (special_discount_amount == 0) {
                        Special_discount();
                    } else if (first_discount_amount == 0) {
                        firstDiscount();
                    } else {
                        Updatetotal();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void noInternetConnection() {

            }
        });

    }

    public void Special_discount() {

        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }
        ApiCall.volleyPostApi(context, AppConstants.special_discount, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Double special_total_amount = Double.parseDouble(jsonObject1.getString("special_total_amount"));
                    totalsd = String.valueOf(special_total_amount);
                    special_discount_amount = Double.parseDouble(jsonObject1.getString("special_discount_amount"));
                    sdAmount = String.valueOf(special_discount_amount);
                    String special_discount_type = jsonObject1.getString("special_discount_type");
                    sdType = special_discount_type;

                    if (jsonObject1.getString("special_total_amount").equalsIgnoreCase("0")) {
                        rl_special_discount.setVisibility(View.GONE);
//                        tv_special_discount_heading.setText("$0.00");
//                        tv_special_discount_ammont.setText("$0.00");
                    } else if (special_total_amount < item_total) {
                        if (special_discount_type.equalsIgnoreCase("P")) {
                            rl_special_discount.setVisibility(View.VISIBLE);
                            tv_special_discount_heading.setText("Special Discount(" + special_discount_amount + "% of " + item_total + ")");
                            // special_discount_amount = (item_total * special_discount_amount) / 100;
                            special_discount_amount = Double.parseDouble(Validation.roundOffTo2DecPlaces((item_total * special_discount_amount) / 100));
                            tv_special_discount_ammont.setText("-$" + Validation.roundOffTo2DecPlaces(special_discount_amount));
                        } else if (special_discount_type.equalsIgnoreCase("F")) {
                            rl_special_discount.setVisibility(View.VISIBLE);
                            tv_special_discount_heading.setText("Special Discount($" + special_discount_amount + " of " + item_total + ")");
                            if (special_discount_amount < item_total) {

                                tv_special_discount_ammont.setText("-$" + Validation.roundOffTo2DecPlaces(special_discount_amount));


                            } else {
                                sdAmount = " ";
//                                special_discount_amount = 0;
                                rl_special_discount.setVisibility(View.GONE);
//                                tv_special_discount_heading.setText("$0.00");
//                                tv_special_discount_ammont.setText("$0.00");
                            }
                        }

                    } else {
//                        special_discount_amount = 0;
//                        tv_special_discount_heading.setText("$0.00");
//                        tv_special_discount_ammont.setText("$0.00");
                        rl_special_discount.setVisibility(View.GONE);

                    }
                    if (first_discount_amount == 0) {
                        firstDiscount();
                    } else {
                        Updatetotal();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void noInternetConnection() {

            }
        });

    }

    private void firstDiscount() {
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));
        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }        map.put("user_id", Preference.getString(context, Preference.USER_ID));

        ApiCall.volleyPostApi(context, AppConstants.first_discount, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Utility.log_api_response(response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("1")) {
                            String first_o_type = object.getString("firstordertype");
                            Double discount_value = Double.parseDouble(object.getString("discountvalue"));
                            if (first_o_type.equalsIgnoreCase("P")) {
                                rl_first_discount.setVisibility(View.VISIBLE);
                                tv_first_discount_heading.setText("First Discount(" + discount_value + "% of " + item_total + ")");
                                first_discount_amount = Double.parseDouble(Validation.roundOffTo2DecPlaces((item_total * discount_value) / 100));
                                tv_first_discount_ammont.setText("-$" + Validation.roundOffTo2DecPlaces(first_discount_amount));

                                firstordertype = "1";
                                discountvalue = String.valueOf(first_discount_amount);

                            } else if (first_o_type.equalsIgnoreCase("A")) {
                                tv_first_discount_heading.setText("First Discount($" + discount_value + " of " + item_total + ")");
                                rl_first_discount.setVisibility(View.VISIBLE);

                                if (discount_value < item_total) {
                                    first_discount_amount = discount_value;
                                    firstordertype = "1";
                                    discountvalue = String.valueOf(discount_value);
                                    tv_first_discount_ammont.setText("-$" + Validation.roundOffTo2DecPlaces(discount_value));

                                } else {
                                    firstordertype = "0";
                                    discountvalue = "";
                                    rl_first_discount.setVisibility(View.GONE);

                                }
                            } else {
                                firstordertype = "0";
                                discountvalue = "";
                                rl_first_discount.setVisibility(View.GONE);

                            }

                        } else {
                            rl_first_discount.setVisibility(View.GONE);
                            firstordertype = "0";
                            discountvalue = "";
                        }
                    }
                    Updatetotal();

                } catch (JSONException e) {

                    Utility.dissmisProgress();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void noInternetConnection() {

            }
        });
        Utility.dissmisProgress();
    }


    public void Updatetotal() {
        double total = 0.00;
        double tax = (item_total * taxPercentage) / 100;


        item_total = Double.parseDouble(Validation.roundOffTo2DecPlaces(item_total));
        tax = Double.parseDouble(Validation.roundOffTo2DecPlaces(tax));

        if(tax == 0.0){
            rl_sales_tax.setVisibility(View.GONE);
        }
        else {
            rl_sales_tax.setVisibility(View.VISIBLE);

        }
        //tax = Math.floor(tax * 100) / 100;
        surcharge_Amount = Double.parseDouble(Validation.roundOffTo2DecPlaces(surcharge_Amount));

        tv_item_total.setText("$" + Validation.roundOffTo2DecPlaces(item_total));
        tv_tax.setText("$" + tax);



        tv_surcharge_amount.setText("$" + Validation.roundOffTo2DecPlaces(surcharge_Amount));



        if (ll_tips.getVisibility() == View.VISIBLE) {
            tips_total = Double.parseDouble(df2.format(tips_total));

            tv_tips.setText("$" + Validation.roundOffTo2DecPlaces(tips_total));
            //   total = item_total + tax + surcharge_Amount + delivery_charge + tips_total - special_discount_amount - promocode_amount - first_discount_amount;
            total = item_total + tax + surcharge_Amount + tips_total - first_discount_amount;
        } else {
            total = item_total + tax + surcharge_Amount  - first_discount_amount;
        }



        if (rl_special_discount.getVisibility() == View.GONE) {
            total = total - promocode_amount;


        } else if (rl_special_discount.getVisibility() == View.VISIBLE) {
            total = total - special_discount_amount;
        }

        tv_total.setText("$" + Validation.roundOffTo2DecPlaces(total));
        btn_review.setOnClickListener(review_onClickListener);

    }

}

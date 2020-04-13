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
import com.trec2go.MorningBrewHawaii.bean.RecentOrdersBean;
import com.trec2go.MorningBrewHawaii.bean.YourBagBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReorderActivity extends AppCompatActivity {
    Spinner spn_tip, spn_order_type;

    Activity activity;
    Context context;
    ImageView img_back_arrow;

    Button btn_review;
    LinearLayout ll_remove_all;
    EditText edt_promo_code;

    RecyclerView rv_list;
    ArrayList<YourBagBean> ar_list; //array list
    public static YourBagItemAdapter ad_list; //Adapter

    //tips
    ArrayList<String> tips = new ArrayList<>();
    ArrayAdapter adapter;
    LinearLayout ll_delivery_type;
    LinearLayout ll_delivery_amounts;
    LinearLayout ll_tips;
    RelativeLayout rl_surcharge_amount, rl_tip, rl_sales_tax;

    TextView tv_item_total, tv_tips, tv_delivery, tv_surcharge_amount, tv_tax, tv_total;
    String oid = "";
    RecentOrdersBean recentOrdersBean;

    double item_total = 0.00;
    double tips_total = 0.00;
    double taxPercentage = 0.00;
    /*
        double deliverychargepercentage = 0.00;
    */
    double surcharge_Amount = 0.00;

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
        setContentView(R.layout.activity_reorder);

        recentOrdersBean = getIntent().getExtras().getParcelable("menuItemBean");
        //Utility.log(recentOrdersBean.id);

        context = this;
        activity = this;
        init();


    }


    public void init() {
        tv_item_total = findViewById(R.id.tv_item_total);
        edt_promo_code = findViewById(R.id.edt_promo_code);
        img_back_arrow = findViewById(R.id.img_back_arrow);
        btn_review = findViewById(R.id.btn_review);
        //btn_continue = findViewById(R.id.btn_continue);
        ll_delivery_type = findViewById(R.id.ll_delivery_type);
        ll_remove_all = findViewById(R.id.ll_remove_all);
        ll_delivery_amounts = findViewById(R.id.ll_delivery_amounts);
        rl_surcharge_amount = findViewById(R.id.rl_surcharge_amount);
        tv_delivery = findViewById(R.id.tv_delivery);
        tv_surcharge_amount = findViewById(R.id.tv_surcharge_amount);
        tv_total = findViewById(R.id.tv_total);
        tv_tax = findViewById(R.id.tv_tax);
        tv_tips = findViewById(R.id.tv_tips);
        ll_tips = findViewById(R.id.ll_tips);

        spn_tip = findViewById(R.id.spn_tip);

        spn_order_type = findViewById(R.id.spn_order_type);

        rl_tip = findViewById(R.id.rl_tip);
        rl_sales_tax = findViewById(R.id.rl_sales_tax);


        final ArrayList<String> order_type = new ArrayList<>();
        if (Preference.getString(context, Preference.STORAGE_Delivery).equalsIgnoreCase("1")) {
            order_type.add("Pickup"); //$$$ chage Delivery to pickup
        }
        if (Preference.getString(context, Preference.STORAGE_Pickup).equalsIgnoreCase("1")) {
            order_type.add("Delivery"); //$$$ chage pickup to Delivery
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
                }

                Updatetotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setAdapter();
    }

    public void setAdapter() {
        rv_list = findViewById(R.id.rv_list);
        ar_list = new ArrayList<YourBagBean>();
        /* ar_list = getItemList();*/
        ad_list = new YourBagItemAdapter(context, activity, ar_list, "review", new YourBagItemAdapter.YourBagClickListner() {
            @Override
            public void OnRemoveAllClickListner() {
                if (Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")) {
                    Preference.setString(context, Preference.CATERING_CART_ID, "");

                } else {
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
                /*try {
                    JSONArray oid_jsonArray = new JSONArray(Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID));
                    JSONArray jsonArray = new JSONArray(Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID));
                    for(int i=0;i<oid_jsonArray.length();i++){
                        if(oid_jsonArray.getJSONObject(i).getString("order_id").equalsIgnoreCase(list.get(position).id)){
                            jsonArray.remove(i);
                            break;
                        }else {
                            continue;
                        }
                    }
                    Preference.setString(context,Preference.STORAGE_YOUR_BAG_ID,jsonArray.toString());

                    //Update item total
                    item_total = 0;
                    for(int i=0;i<list.size();i++){
                        if(i == position){

                        }else {
                            item_total = item_total + list.get(i).final_price;
                        }
                    }

                    RemoveItemFromCart.RemoveItemCartAPI(context, list.get(position).id, new RemoveItemFromCart.RemoveItemFromCartListner() {
                        @Override
                        public void onResponse() {
                            if(Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID).equalsIgnoreCase("")){
                                finish();
                            }else {
                                JSONArray jsonArray1 = null;
                                try {
                                    jsonArray1 = new JSONArray(Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(jsonArray1 != null){
                                    GetYourBagAPI();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
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
                 /*Intent intent = new Intent(activity,ReviewOrderActivity.class);
                intent.putExtra("item_total",tv_item_total.getText().toString().trim());
                intent.putExtra("tax",tv_tax.getText().toString().trim());
                intent.putExtra("surcharge",tv_surcharge_amount.getText().toString().trim());
                intent.putExtra("total",tv_total.getText().toString().trim());
                if(ll_tips.getVisibility() == View.VISIBLE){
                    intent.putExtra("order_type","1");
                    intent.putExtra("delivery",tv_delivery.getText().toString().trim());
                    intent.putExtra("tips_total",tv_tips.getText().toString().trim());
                }else {
                    intent.putExtra("order_type","2");
                }
                intent.putParcelableArrayListExtra("list",ad_list.getList());
                startActivity(intent);*/

                if (!edt_promo_code.getText().toString().trim().equalsIgnoreCase("")) {
                    com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Invalid Promo code", new DialogCallBackListner() {
                        @Override
                        public void setPositiveListner() {
                            //Cliked Ok
                            edt_promo_code.setText("");
                        }

                        @Override
                        public void setNegativeListner() {

                        }
                    });
                } else {
                    if (Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")) {
                        Preference.setString(context, Preference.CATERING_CART_ID, "");

                    } else {
                        Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");

                    }
                    ReorderAPI();
                }
            }
        };

        /*btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        ll_remove_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String oid = "";
                try {
                    JSONArray oid_jsonArray = new JSONArray(Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID));
                    for(int i=0;i<oid_jsonArray.length();i++){
                        oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Preference.setString(context,Preference.STORAGE_YOUR_BAG_ID,"");
                RemoveItemFromCart.RemoveItemCartAPI(context, oid.substring(0,oid.length() - 1), new RemoveItemFromCart.RemoveItemFromCartListner() {
                    @Override
                    public void onResponse() {
                        if(Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID).equalsIgnoreCase("")){
                            finish();
                        }else {
                            JSONArray jsonArray1 = null;
                            try {
                                jsonArray1 = new JSONArray(Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(jsonArray1 != null){
                                GetYourBagAPI();
                            }
                        }
                    }
                });*/
            }
        });

        GetOrderHistoryAPI();
        //GetYourBagAPI();
        //GetTipsAPI();
        //ApplySalesTaxAPI();
        //SurchargeAmountAPI();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //API Calling

    public void GetOrderHistoryAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("u_id", recentOrdersBean.id);
        ApiCall.volleyPostApi(context, AppConstants.view_order, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String globalCartItems = jsonObject.getString("globalCartItems");
                    oid = globalCartItems;
                    String is_catering = jsonObject.getString("is_catering");
                    if (is_catering.equalsIgnoreCase("1")) {
                        Preference.setString(context, Preference.CATERING_STATUS, "1");
                    } else {
                        Preference.setString(context, Preference.CATERING_STATUS, "0");

                    }
                    Utility.dissmisProgress();
                    GetYourBagAPI();
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

    public void GetYourBagAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("user_id", Preference.getString(context, Preference.USER_ID));
        map.put("oid", oid);
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


                    if (tips.isEmpty()) {
                        GetTipsAPI();
                    } else if (taxPercentage == 0) {
                        ApplySalesTaxAPI();
                    } else if (surcharge_Amount == 0) {
                        SurchargeAmountAPI();
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
                    adapter = new ArrayAdapter(context, R.layout.spinner_textview, tips);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_tip.setAdapter(adapter);
                    spn_tip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            tips_total = Double.parseDouble(tips.get(position).substring(1, tips.get(position).length()));
                            if (tips_total == 0.0) {
                                rl_tip.setVisibility(View.GONE);
                            } else {
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
                    } else {
                        Updatetotal();
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

    }

    public void ApplySalesTaxAPI() {
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("user_id", Preference.getString(context, Preference.USER_ID));
        map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));  //$$$ change branch id to REORDER_BRANCH_ID
        map.put("new", "1");
        ApiCall.volleyPostApi(context, AppConstants.apply_sales_tax, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONObject jsonObject2 = jsonArray.getJSONObject(1);
                    taxPercentage = Double.parseDouble(jsonObject1.getString("tax"));
                    //deliverychargepercentage = Double.parseDouble(jsonObject2.getString("deliverycharge"));
                    if (surcharge_Amount == 0) {
                        SurchargeAmountAPI();
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
        map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID)); //$$$ change branch id to REORDER_BRANCH_ID
        ApiCall.volleyPostApi(context, AppConstants.surcharge_amount, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    surcharge_Amount = Double.parseDouble(jsonObject1.getString("surcharge_Amount"));
                    Updatetotal();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }
                Utility.dissmisProgress();

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void noInternetConnection() {

            }
        });

    }

    public void Updatetotal() {
        double total = 0.00;
        double tax = (item_total * taxPercentage) / 100;

        item_total = Double.parseDouble(df2.format(item_total));
        tax = Double.parseDouble(df2.format(tax));
        if (tax == 0.0) {
            rl_sales_tax.setVisibility(View.GONE);
        } else {
            rl_sales_tax.setVisibility(View.VISIBLE);

        }
        surcharge_Amount = Double.parseDouble(df2.format(surcharge_Amount));

        tv_item_total.setText("$" + item_total);
        tv_tax.setText("$" + tax);
        if (surcharge_Amount == 0.00) {
            rl_surcharge_amount.setVisibility(View.GONE);
        } else {
            rl_surcharge_amount.setVisibility(View.VISIBLE);
            tv_surcharge_amount.setText("$" + Validation.roundOffTo2DecPlaces(surcharge_Amount));
        }


        if (ll_tips.getVisibility() == View.VISIBLE) {
            //double delivery_charge = (item_total * deliverychargepercentage) / 100;
            //double delivery_charge = deliverychargepercentage;
            //delivery_charge = Double.parseDouble(df2.format(delivery_charge));
            tips_total = Double.parseDouble(df2.format(tips_total));
            //tv_delivery.setText("$" + Validation.roundOffTo2DecPlaces(delivery_charge));
            tv_tips.setText("$" + Validation.roundOffTo2DecPlaces(tips_total));
            total = item_total + tax + surcharge_Amount/*+ delivery_charge*/ + tips_total;
        } else {
            total = item_total + tax + surcharge_Amount;
        }


        tv_total.setText("$" + df2.format(total));
        btn_review.setOnClickListener(review_onClickListener);

    }

    public void ReorderAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID)); //$$$ change branch id to REORDER_BRANCH_ID
        map.put("u_id", recentOrdersBean.id);
        ApiCall.volleyPostApi(context, AppConstants.re_order, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String ordStr = jsonArray.getJSONObject(0).getString("ordStr");
                    Utility.dissmisProgress();

                    List<String> ordStrlist = Arrays.asList(ordStr.split("\\s*,\\s*"));
                    String order_ids;

                    if (Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")) {
                        order_ids = Preference.getString(context, Preference.CATERING_CART_ID);

                    } else {
                        order_ids = Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID);

                    }


                    if (!ordStr.equalsIgnoreCase("")) {
                        JSONArray jsonArray1;
                        if (!order_ids.equalsIgnoreCase("")) {
                            jsonArray1 = new JSONArray(order_ids);
                        } else {
                            jsonArray1 = new JSONArray();
                        }

                        for (int k = 0; k < ordStrlist.size(); k++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("order_id", ordStrlist.get(k));
                            jsonArray1.put(jsonObject);
                        }
                        if (Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")) {
                            Preference.setString(context, Preference.CATERING_CART_ID, jsonArray1.toString());

                        } else {
                            Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, jsonArray1.toString());
                        }
                        Preference.setString(context, Preference.REORDER_STATUS, "1");
                        Intent intent = new Intent(activity, YourBagActivity.class);
                        startActivity(intent);
                        finish();
                    }

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

package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.bean.AddToCartBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CustomOrederActivity extends AppCompatActivity {

    Activity activity;
    Context context;
    ImageView img_back_arrow, img_cart;
    String from;
    TextView tv_custom_order;
    Button btn_add_to_cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_oreder);

        activity = this;
        context = this;

        //from = getIntent().getStringExtra("CUSTOM_ORDER");

        tv_custom_order = findViewById(R.id.tv_custom_order);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_cart = findViewById(R.id.img_cart);

        tv_custom_order.setText(Preference.getString(context, Preference.CUSTOM_ORDER));

        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCartBean addToCartBean = new AddToCartBean();
                addToCartBean.menu_id = "0";
                addToCartBean.quantity = "";
                addToCartBean.menuadd = "";
                addToCartBean.choose_sides = "";
                addToCartBean.main_menu = "Y";
                addToCartBean.branch_id = Preference.getString(context, Preference.BRANCH_ID);
                addToCartBean.amount = "";
                addToCartBean.gruname = Preference.getString(context, Preference.GLOBAL_UNAME);
                addToCartBean.advance_menu = "";
                addToCartBean.advance_menu_qty = "";
                addToCartBean.advance_menu_extracharge = "";
                addToCartBean.special_requests = tv_custom_order.getText().toString().trim();
                if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {

                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra("from", "Order_Detail");
                    intent.putExtra("addToCartBean", addToCartBean);
                    startActivity(intent);
                    Preference.setString(context, Preference.CUSTOM_ORDER, "");
                    finish();
                } else {

                    AddToCartAPI();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            String data = Preference.getString(context,Preference.STORAGE_YOUR_BAG_ID);
            Utility.log_api_failure(data);
            if(data.equalsIgnoreCase("")){
                LayerDrawable icon = (LayerDrawable) img_cart.getDrawable();
                Utility.setBadgeCount(this, icon, String.valueOf(0));
                img_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Your Cart is Empty", new DialogCallBackListner() {
                            @Override
                            public void setPositiveListner() {
                                //Cliked Ok
                            }

                            @Override
                            public void setNegativeListner() {

                            }
                        });
                    }
                });
            }else {
                JSONArray jsonArray = new JSONArray(data);
                LayerDrawable icon = (LayerDrawable) img_cart.getDrawable();
                Utility.setBadgeCount(this, icon, String.valueOf(jsonArray.length()));
                Utility.log_api_failure(jsonArray.toString());
                img_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Preference.getString(context,Preference.USER_ID).equalsIgnoreCase("")){
                            Intent intent = new Intent(activity, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(activity, YourBagActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Add to Cart API
    public void AddToCartAPI () {
        /*Utility.log_api_failure(listAdapter.getAdvancedmenu());
        Utility.log_api_failure(listAdapter.getAdvancedmenuQuantity());
        Utility.log_api_failure(listAdapter.getExtraCharge());*/

        Utility.createProgressDialoge(context, "Loading");
        final Map<String, String> map = new HashMap<>();
        map.put("menu_id", "0");
        map.put("user_id", Preference.getString(context, Preference.USER_ID));
        map.put("quantity", "");
        map.put("menuadd", "");
        map.put("choose_sides", "");
        map.put("main_menu", "Y");
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        map.put("amount", "");
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("advance_menu", "");
        map.put("advance_menu_qty", "");
        map.put("advance_menu_extracharge", "");
        map.put("special_requests", tv_custom_order.getText().toString().trim());
        ApiCall.volleyPostApi(context, AppConstants.add_to_cart, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Utility.log_api_request(String.valueOf(map));
                    Utility.log(response);
                    if (jsonArray != null) {
                        String order_id = jsonArray.getJSONObject(0).getString("order_id");
                        String order_ids = Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID);

                        if (!order_id.equalsIgnoreCase("")) {
                            if (!order_ids.equalsIgnoreCase("")) {
                                JSONArray jsonArray1 = new JSONArray(order_ids);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("order_id", order_id);
                                jsonArray1.put(jsonObject);
                                Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, jsonArray1.toString());
                            } else {
                                JSONArray jsonArray1 = new JSONArray();
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("order_id", order_id);
                                jsonArray1.put(jsonObject);
                                Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, jsonArray1.toString());
                            }

                            if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
                                Intent intent = new Intent(activity, LoginActivity.class);
                                intent.putExtra("from", "Order_Detail");
                                startActivity(intent);
                                Preference.setString(context, Preference.CUSTOM_ORDER, "");
                                finish();
                            } else {
                                Intent intent = new Intent(activity, YourBagActivity.class);
                                startActivity(intent);
                                Preference.setString(context, Preference.CUSTOM_ORDER, "");
                                finish();
                            }
                        }
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

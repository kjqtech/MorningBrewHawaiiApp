package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.commonapicall.RemoveItemFromCart;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelectCardActivity extends AppCompatActivity {

    Activity activity;
    Context context;
    ImageView img_back_arrow;

    RadioButton rb_existing;
    RadioGroup rb_group;
    Button btn_proceed;

    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);

        activity = this;
        context = this;

        String card_no = getIntent().getExtras().getString("card_no");
        bundle = getIntent().getExtras();

        //init
        rb_existing = findViewById(R.id.rb_existing);
        rb_group = findViewById(R.id.rb_group);
        btn_proceed = findViewById(R.id.btn_proceed);
        img_back_arrow = findViewById(R.id.img_back_arrow);


        rb_existing.setText("Pay with Existing Card XXXXXXXXXXXX" + card_no);
        rb_group.check(R.id.rb_existing);


        //listners

        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = rb_group.getCheckedRadioButtonId();
                switch (i) {
                    case R.id.rb_existing:
                        if (Preference.getString(context, Preference.FAX_STATUS).equalsIgnoreCase("1")) {
                            SaveOrderAuthExistingFax();

                        } else {
                            if (Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")) {
                                CateringPayExistingCardAPI();
                            } else {
                                PayExistingCardAPI();

                            }

                        }
                        break;
                    case R.id.rb_new:
                        Intent intent = new Intent(context, AddNewCardActivity.class);

                        intent.putExtra("globalTax", bundle.getString("globalTax"));
                        intent.putExtra("globalTips", bundle.getString("globalTips"));
                        if (!bundle.getString("globalPromocode").equalsIgnoreCase("") && !bundle.getString("globalPromocodeDiscount").equalsIgnoreCase("")) {
                            intent.putExtra("globalPromocode", bundle.getString("globalPromocode"));
                            intent.putExtra("globalPromocodeDiscountType", bundle.getString("globalPromocodeDiscountType"));
                            intent.putExtra("globalPromocodeDiscount", bundle.getString("globalPromocodeDiscount"));

                        } else {
                            intent.putExtra("globalPromocode", "");
                            intent.putExtra("globalPromocodeDiscountType", "");
                            intent.putExtra("globalPromocodeDiscount", "");

                        }
                        intent.putExtra("globalGrandTotal", bundle.getString("globalGrandTotal"));

                        if (bundle.getString("is_asap").equalsIgnoreCase("1")) {
                            intent.putExtra("is_asap", "1");
                            intent.putExtra("global_delivery_time", "");
                            intent.putExtra("global_pickup_time", "");

                            if (bundle.getString("order_type").equalsIgnoreCase("1")) {
                                //delivery
                                intent.putExtra("globalType_LoggedIn", "D");

                            } else if (bundle.getString("order_type").equalsIgnoreCase("2")) {
                                //pickup
                                intent.putExtra("globalType_LoggedIn", "P");
                            }
                        } else {
                            intent.putExtra("is_asap", "0");

                            if (bundle.getString("order_type").equalsIgnoreCase("1")) {
                                //delivery
                                intent.putExtra("globalType_LoggedIn", "D");
                                intent.putExtra("global_delivery_time", bundle.getString("global_delivery_time"));
                                intent.putExtra("global_pickup_time", "");
                            } else if (bundle.getString("order_type").equalsIgnoreCase("2")) {
                                //pickup
                                intent.putExtra("globalType_LoggedIn", "P");
                                intent.putExtra("global_delivery_time", "");
                                intent.putExtra("global_pickup_time", bundle.getString("global_pickup_time"));
                            }
                        }
                        intent.putExtra("order_type", bundle.getString("order_type"));
                        intent.putExtra("global_advance_order", "");
                        intent.putExtra("global_advance_order_date", "0000-00-00");
                        intent.putExtra("global_special_note", bundle.getString("global_special_note").trim());
                        intent.putExtra("global_delivery_charge", bundle.getString("global_delivery_charge"));


                        intent.putExtra("firstordertype", bundle.getString("firstordertype"));
                        intent.putExtra("discountvalue", bundle.getString("discountvalue"));


                        //intent.putExtra("order_type","cash");
                        intent.putExtra("discountType", "");
                        intent.putExtra("SpecialDiscountType", bundle.getString("SpecialDiscountType"));
                        intent.putExtra("SpecialDiscountAmount", bundle.getString("SpecialDiscountAmount"));
                        intent.putExtra("TotalSpecialAmount", bundle.getString("TotalSpecialAmount"));
                        intent.putExtra("item_total", bundle.getString("item_total"));
                        intent.putExtra("CardTotalAmount", bundle.getString("CardTotalAmount"));
                        intent.putExtra("OnlineOrderSurcharge", bundle.getString("OnlineOrderSurcharge"));


                        startActivity(intent);
                        break;
                }
            }
        });

    }


    //Api calling
    /*public void PayExistingCardAPI() {
        Utility.createProgressDialoge(context, "Loading");

        String oid = "";
        try {
            JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID));
            for (int i = 0; i < oid_jsonArray.length(); i++) {
                oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();
        map.put("globalLoggedIn", Preference.getString(context, Preference.USER_ID));
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("order_id", oid.substring(0, oid.length() - 1));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }       *//* map.put("c_card_no",aedt_card_no.getText().toString().trim());
        map.put("c_year",year);
        map.put("c_month_val",month);
        map.put("c_card_cvc",aedt_security_code.getText().toString().trim());
        map.put("card_type",card_type);*//*
        if (bundle.getString("is_asap").equalsIgnoreCase("1")) {
            map.put("is_asap", "1");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", "");
        } else {
            map.put("is_asap", "0");
            if (bundle.getString("order_type").equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");
                map.put("global_delivery_time", bundle.getString("global_delivery_time"));
                map.put("global_pickup_time", "");
            } else if (bundle.getString("order_type").equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");
                map.put("global_delivery_time", "");
                map.put("global_pickup_time", bundle.getString("global_pickup_time"));
            }
        }

        map.put("order_type", "cash");  //$$$ add key
        map.put("global_street_address", Preference.getString(context, Preference.USER_STREET_ADDRESS));
        map.put("global_apt_suite_floor", Preference.getString(context, Preference.USER_SUITE_FLOOR));
        map.put("global_city_state_zip", Preference.getString(context, Preference.USER_CITY_STATE_ZIP));
        map.put("globalTax", bundle.getString("globalTax"));
        map.put("OnlineOrderSurcharge", bundle.getString("OnlineOrderSurcharge"));
        map.put("globalTips", bundle.getString("globalTips"));
        if (!bundle.getString("globalPromocode").equalsIgnoreCase("") && !bundle.getString("globalPromocodeDiscount").equalsIgnoreCase("")) {
            map.put("globalPromocode", bundle.getString("globalPromocode"));
            map.put("globalPromocodeDiscountType", bundle.getString("globalPromocodeDiscountType"));
            map.put("globalPromocodeDiscount", bundle.getString("globalPromocodeDiscount"));

        } else {
            map.put("globalPromocode", "");
            map.put("globalPromocodeDiscountType", "");
            map.put("globalPromocodeDiscount", "");

        }
        map.put("globalGrandTotal", bundle.getString("globalGrandTotal"));
        map.put("globalCartItems", oid.substring(0, oid.length() - 1));
        map.put("cart_ids", oid.substring(0, oid.length() - 1));

        map.put("global_advance_order", "");
        map.put("global_advance_order_date", "0000-00-00");
        map.put("global_special_note", bundle.getString("global_special_note").trim());
        map.put("global_delivery_charge", bundle.getString("global_delivery_charge"));

        map.put("discountType", "");
        map.put("SpecialDiscountType", bundle.getString("SpecialDiscountType"));
        map.put("SpecialDiscountAmount", bundle.getString("SpecialDiscountAmount"));
        map.put("TotalSpecialAmount", bundle.getString("TotalSpecialAmount"));
        map.put("CardTotalAmount", bundle.getString("CardTotalAmount"));
        map.put("is_save_card", "1");


        ApiCall.volleyPostApi(context, AppConstants.save_order_auth_existing, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {
                        Utility.dissmisProgress();
                        com.trec2go.GregsPizza.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Unsuccessfull", new DialogCallBackListner() {
                            @Override
                            public void setPositiveListner() {
                                //Cliked Ok
                            }

                            @Override
                            public void setNegativeListner() {

                            }
                        });
                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = (JSONArray) json;
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        String order_id = jsonObject.getString("order_id");
                        String branch_order_id = jsonObject.getString("branch_order_id");


                        Utility.dissmisProgress();
                        if (status.equalsIgnoreCase("1")) {

                            com.trec2go.GregsPizza.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, "Payment successfully received by #" + branch_order_id + " Thank you for your order.", new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                    if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                        String oid = "";
                                        String orderid = "";
                                        try {
                                            orderid = Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID);
                                            if (!orderid.equalsIgnoreCase("")) {
                                                JSONArray oid_jsonArray = new JSONArray(orderid);
                                                for (int i = 0; i < oid_jsonArray.length(); i++) {
                                                    oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if (!oid.equalsIgnoreCase("")) {
                                            RemoveItemFromCart.RemoveItemCartAPI(context, oid.substring(0, oid.length() - 1), new RemoveItemFromCart.RemoveItemFromCartListner() {
                                                @Override
                                                public void onResponse() {

                                                    Preference.setString(context, Preference.USER_ID, "");
                                                    if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                                        Preference.setString(context, Preference.USER_EMAIL, "");
                                                    }
                                                    //Preference.setString(context,Preference.USER_EMAIL,"");
                                                    Preference.setString(context, Preference.USER_PHONE, "");
                                                    //Preference.setString(context,Preference.USER_PASSWORD,"");
                                                    Preference.setString(context, Preference.USER_FNAME, "");
                                                    Preference.setString(context, Preference.USER_LNAME, "");
                                                    Preference.setString(context, Preference.USER_STREET_ADDRESS, "");
                                                    Preference.setString(context, Preference.USER_CITY_STATE_ZIP, "");
                                                    Preference.setString(context, Preference.USER_SUITE_FLOOR, "");
                                                    Preference.setString(context, Preference.GUEST_STATUS, "0");

                                                    Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                                    Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            });
                                        } else {

                                            Preference.setString(context, Preference.USER_ID, "");
                                            //Preference.setString(context,Preference.USER_EMAIL,"");
                                            if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                                Preference.setString(context, Preference.USER_EMAIL, "");
                                            }
                                            Preference.setString(context, Preference.USER_PHONE, "");
                                            //Preference.setString(context,Preference.USER_PASSWORD,"");
                                            Preference.setString(context, Preference.USER_FNAME, "");
                                            Preference.setString(context, Preference.USER_LNAME, "");
                                            Preference.setString(context, Preference.USER_STREET_ADDRESS, "");
                                            Preference.setString(context, Preference.USER_CITY_STATE_ZIP, "");
                                            Preference.setString(context, Preference.USER_SUITE_FLOOR, "");
                                            Preference.setString(context, Preference.GUEST_STATUS, "0");

                                            Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                            Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();

                                        }
                                        //Preference.setString(context,Preference.USER_ID,"");
                                        //tv_login_logout.setText("LOGIN");
                                        //img_cart.setVisibility(View.GONE);
                                        //ll_settings.setVisibility(View.GONE);
                                        //Intent intent = new Intent(activity,SelectStoreLocationActivity.class);
                                        //intent.putExtra("from","skip");
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        //startActivity(intent);

                                    } else {
                                        Preference.setString(context, Preference.REORDER_STATUS, "0");
                                        Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                        Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }

                //For redirect to login
                *//*onBackPressed();*//*
            }

            @Override
            public void onError(String error) {
                Utility.dissmisProgress();
                com.trec2go.GregsPizza.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.response_error), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        //Cliked Ok
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
            }

            @Override
            public void noInternetConnection() {
                Utility.dissmisProgress();
                com.trec2go.GregsPizza.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.no_internet_connection), new DialogCallBackListner() {
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
    }*/

    public void PayExistingCardAPI() {
        Utility.createProgressDialoge(context, "Loading");

        String oid = "";
        try {
            JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID));
            for (int i = 0; i < oid_jsonArray.length(); i++) {
                oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();
        map.put("globalLoggedIn", Preference.getString(context, Preference.USER_ID));
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("order_id", oid.substring(0, oid.length() - 1));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }       /* map.put("c_card_no",aedt_card_no.getText().toString().trim());
        map.put("c_year",year);
        map.put("c_month_val",month);
        map.put("c_card_cvc",aedt_security_code.getText().toString().trim());
        map.put("card_type",card_type);*/
        if (bundle.getString("is_asap").equalsIgnoreCase("1")) {
            map.put("is_asap", "1");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", "");

            if (bundle.getString("order_type").equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");

            } else if (bundle.getString("order_type").equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");

            }

        } else {
            map.put("is_asap", "0");
            if (bundle.getString("order_type").equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");
                map.put("global_delivery_time", bundle.getString("global_delivery_time"));
                map.put("global_pickup_time", "");
            } else if (bundle.getString("order_type").equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");
                map.put("global_delivery_time", "");
                map.put("global_pickup_time", bundle.getString("global_pickup_time"));
            }
        }

        map.put("order_type", "cash");  //$$$ add key
        map.put("global_street_address", Preference.getString(context, Preference.USER_STREET_ADDRESS));
        map.put("global_city_state_zip", Preference.getString(context, Preference.USER_CITY_STATE_ZIP));

        map.put("global_apt_suite_floor", Preference.getString(context, Preference.USER_SUITE_FLOOR));
        map.put("globalTax", bundle.getString("globalTax"));
        map.put("OnlineOrderSurcharge", bundle.getString("OnlineOrderSurcharge"));
        map.put("globalTips", bundle.getString("globalTips"));
        if (!bundle.getString("globalPromocode").equalsIgnoreCase("") && !bundle.getString("globalPromocodeDiscount").equalsIgnoreCase("")) {
            map.put("globalPromocode", bundle.getString("globalPromocode"));
            map.put("globalPromocodeDiscountType", bundle.getString("globalPromocodeDiscountType"));
            map.put("globalPromocodeDiscount", bundle.getString("globalPromocodeDiscount"));

        } else {
            map.put("globalPromocode", "");
            map.put("globalPromocodeDiscountType", "");
            map.put("globalPromocodeDiscount", "");

        }
        map.put("globalGrandTotal", bundle.getString("globalGrandTotal"));
        map.put("globalCartItems", oid.substring(0, oid.length() - 1));
        map.put("cart_ids", oid.substring(0, oid.length() - 1));

        map.put("global_advance_order", "");
        map.put("global_advance_order_date", "0000-00-00");
        map.put("global_special_note", bundle.getString("global_special_note").trim());
        map.put("global_delivery_charge", bundle.getString("global_delivery_charge"));

        map.put("firstordertype", bundle.getString("firstordertype"));
        map.put("discountvalue", bundle.getString("discountvalue"));

        map.put("discountType", "");
        map.put("SpecialDiscountType", bundle.getString("SpecialDiscountType"));
        map.put("SpecialDiscountAmount", bundle.getString("SpecialDiscountAmount"));
        map.put("TotalSpecialAmount", bundle.getString("TotalSpecialAmount"));
        map.put("subtotal", bundle.getString("item_total"));
        map.put("CardTotalAmount", bundle.getString("CardTotalAmount"));
        map.put("is_save_card", "1");


        ApiCall.volleyPostApi(context, AppConstants.save_order_auth_existing_first_order, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {
                        Utility.dissmisProgress();
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Unsuccessfull", new DialogCallBackListner() {
                            @Override
                            public void setPositiveListner() {
                                //Cliked Ok
                            }

                            @Override
                            public void setNegativeListner() {

                            }
                        });
                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = (JSONArray) json;
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");


                        Utility.dissmisProgress();
                        if (status.equalsIgnoreCase("1")) {
                            String order_id = jsonObject.getString("order_id");
                            String branch_order_id = jsonObject.getString("branch_order_id");

                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, "Payment successfully received by #" + branch_order_id + " Thank you for your order.", new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                    if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                        String oid = "";
                                        String orderid = "";
                                        try {
                                            orderid = Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID);
                                            if (!orderid.equalsIgnoreCase("")) {
                                                JSONArray oid_jsonArray = new JSONArray(orderid);
                                                for (int i = 0; i < oid_jsonArray.length(); i++) {
                                                    oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if (!oid.equalsIgnoreCase("")) {
                                            RemoveItemFromCart.RemoveItemCartAPI(context, oid.substring(0, oid.length() - 1), new RemoveItemFromCart.RemoveItemFromCartListner() {
                                                @Override
                                                public void onResponse() {

                                                    Preference.setString(context, Preference.USER_ID, "");
                                                    if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                                        Preference.setString(context, Preference.USER_EMAIL, "");
                                                    }
                                                    //Preference.setString(context,Preference.USER_EMAIL,"");
                                                    Preference.setString(context, Preference.USER_PHONE, "");
                                                    //Preference.setString(context,Preference.USER_PASSWORD,"");
                                                    Preference.setString(context, Preference.USER_FNAME, "");
                                                    Preference.setString(context, Preference.USER_LNAME, "");
                                                    Preference.setString(context, Preference.USER_STREET_ADDRESS, "");
                                                    Preference.setString(context, Preference.USER_CITY_STATE_ZIP, "");
                                                    Preference.setString(context, Preference.USER_SUITE_FLOOR, "");
                                                    Preference.setString(context, Preference.GUEST_STATUS, "0");

                                                    Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                                    Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            });
                                        } else {

                                            Preference.setString(context, Preference.USER_ID, "");
                                            //Preference.setString(context,Preference.USER_EMAIL,"");
                                            if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                                Preference.setString(context, Preference.USER_EMAIL, "");
                                            }
                                            Preference.setString(context, Preference.USER_PHONE, "");
                                            //Preference.setString(context,Preference.USER_PASSWORD,"");
                                            Preference.setString(context, Preference.USER_FNAME, "");
                                            Preference.setString(context, Preference.USER_LNAME, "");
                                            Preference.setString(context, Preference.USER_STREET_ADDRESS, "");
                                            Preference.setString(context, Preference.USER_CITY_STATE_ZIP, "");
                                            Preference.setString(context, Preference.USER_SUITE_FLOOR, "");
                                            Preference.setString(context, Preference.GUEST_STATUS, "0");

                                            Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                            Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();

                                        }
                                        //Preference.setString(context,Preference.USER_ID,"");
                                        //tv_login_logout.setText("LOGIN");
                                        //img_cart.setVisibility(View.GONE);
                                        //ll_settings.setVisibility(View.GONE);
                                        //Intent intent = new Intent(activity,SelectStoreLocationActivity.class);
                                        //intent.putExtra("from","skip");
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        //startActivity(intent);

                                    } else {
                                        Preference.setString(context, Preference.REORDER_STATUS, "0");
                                        Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                        Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        } else {
                            String err = jsonObject.getString("err");
                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, err, new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }

                //For redirect to login
                /*onBackPressed();*/
            }

            @Override
            public void onError(String error) {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.response_error), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        //Cliked Ok
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
            }

            @Override
            public void noInternetConnection() {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.no_internet_connection), new DialogCallBackListner() {
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
    }

    public void CateringPayExistingCardAPI() {
        Utility.createProgressDialoge(context, "Loading");

        String oid = "";
        try {
            JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.CATERING_CART_ID));
            for (int i = 0; i < oid_jsonArray.length(); i++) {
                oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();
        map.put("globalLoggedIn", Preference.getString(context, Preference.USER_ID));
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("order_id", oid.substring(0, oid.length() - 1));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }       /* map.put("c_card_no",aedt_card_no.getText().toString().trim());
        map.put("c_year",year);
        map.put("c_month_val",month);
        map.put("c_card_cvc",aedt_security_code.getText().toString().trim());
        map.put("card_type",card_type);*/
        /*if (bundle.getString("is_asap").equalsIgnoreCase("1")) {
            map.put("is_asap", "1");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", "");
        } else {*/
        map.put("is_asap", "0");
        if (bundle.getString("order_type").equalsIgnoreCase("1")) {
            //delivery
            map.put("globalType_LoggedIn", "D");
            map.put("global_delivery_time", bundle.getString("global_delivery_time"));
            map.put("global_pickup_time", "");
        } else if (bundle.getString("order_type").equalsIgnoreCase("2")) {
            //pickup
            map.put("globalType_LoggedIn", "P");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", bundle.getString("global_pickup_time"));
        }
        /*  }*/

        map.put("order_type", "cash");  //$$$ add key
        map.put("global_street_address", Preference.getString(context, Preference.USER_STREET_ADDRESS));
        map.put("global_city_state_zip", Preference.getString(context, Preference.USER_CITY_STATE_ZIP));

        map.put("global_apt_suite_floor", Preference.getString(context, Preference.USER_SUITE_FLOOR));
        map.put("globalTax", bundle.getString("globalTax"));
        map.put("OnlineOrderSurcharge", bundle.getString("OnlineOrderSurcharge"));
        map.put("globalTips", bundle.getString("globalTips"));
        if (!bundle.getString("globalPromocode").equalsIgnoreCase("") && !bundle.getString("globalPromocodeDiscount").equalsIgnoreCase("")) {
            map.put("globalPromocode", bundle.getString("globalPromocode"));
            map.put("globalPromocodeDiscountType", bundle.getString("globalPromocodeDiscountType"));
            map.put("globalPromocodeDiscount", bundle.getString("globalPromocodeDiscount"));

        } else {
            map.put("globalPromocode", "");
            map.put("globalPromocodeDiscountType", "");
            map.put("globalPromocodeDiscount", "");

        }
        map.put("globalGrandTotal", bundle.getString("globalGrandTotal"));
        map.put("globalCartItems", oid.substring(0, oid.length() - 1));
        map.put("cart_ids", oid.substring(0, oid.length() - 1));

        map.put("global_advance_order", "");
        map.put("global_advance_order_date", "0000-00-00");
        map.put("global_special_note", bundle.getString("global_special_note").trim());
        map.put("global_delivery_charge", bundle.getString("global_delivery_charge"));

        map.put("firstordertype", bundle.getString("firstordertype"));
        map.put("discountvalue", bundle.getString("discountvalue"));

        map.put("discountType", "");
        map.put("SpecialDiscountType", bundle.getString("SpecialDiscountType"));
        map.put("SpecialDiscountAmount", bundle.getString("SpecialDiscountAmount"));
        map.put("TotalSpecialAmount", bundle.getString("TotalSpecialAmount"));
        map.put("subtotal", bundle.getString("item_total"));
        map.put("CardTotalAmount", bundle.getString("CardTotalAmount"));
        map.put("is_save_card", "1");

        map.put("global_advance_order_date", ReviewOrderActivity.catering_date);
        map.put("global_advance_order_time", ReviewOrderActivity.catering_time);


        ApiCall.volleyPostApi(context, AppConstants.save_order_auth_existing_first_order_catering, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {
                        Utility.dissmisProgress();
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Unsuccessfull", new DialogCallBackListner() {
                            @Override
                            public void setPositiveListner() {
                                //Cliked Ok
                            }

                            @Override
                            public void setNegativeListner() {

                            }
                        });
                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = (JSONArray) json;
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");


                        Utility.dissmisProgress();
                        if (status.equalsIgnoreCase("1")) {

                            ReviewOrderActivity.catering_date = "";
                            ReviewOrderActivity.catering_time = "";
                            String order_id = jsonObject.getString("order_id");
                            String branch_order_id = jsonObject.getString("branch_order_id");

                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, "Payment successfully received by #" + branch_order_id + " Thank you for your order.", new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                    if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                        String oid = "";
                                        String orderid = "";
                                        try {
                                            orderid = Preference.getString(context, Preference.CATERING_CART_ID);
                                            if (!orderid.equalsIgnoreCase("")) {
                                                JSONArray oid_jsonArray = new JSONArray(orderid);
                                                for (int i = 0; i < oid_jsonArray.length(); i++) {
                                                    oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if (!oid.equalsIgnoreCase("")) {
                                            RemoveItemFromCart.RemoveItemCartAPI(context, oid.substring(0, oid.length() - 1), new RemoveItemFromCart.RemoveItemFromCartListner() {
                                                @Override
                                                public void onResponse() {

                                                    Preference.setString(context, Preference.USER_ID, "");
                                                    if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                                        Preference.setString(context, Preference.USER_EMAIL, "");
                                                    }
                                                    //Preference.setString(context,Preference.USER_EMAIL,"");
                                                    Preference.setString(context, Preference.USER_PHONE, "");
                                                    //Preference.setString(context,Preference.USER_PASSWORD,"");
                                                    Preference.setString(context, Preference.USER_FNAME, "");
                                                    Preference.setString(context, Preference.USER_LNAME, "");
                                                    Preference.setString(context, Preference.USER_STREET_ADDRESS, "");
                                                    Preference.setString(context, Preference.USER_CITY_STATE_ZIP, "");
                                                    Preference.setString(context, Preference.USER_SUITE_FLOOR, "");
                                                    Preference.setString(context, Preference.GUEST_STATUS, "0");

                                                    Preference.setString(context, Preference.CATERING_CART_ID, "");
                                                    Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            });
                                        } else {

                                            Preference.setString(context, Preference.USER_ID, "");
                                            //Preference.setString(context,Preference.USER_EMAIL,"");
                                            if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                                Preference.setString(context, Preference.USER_EMAIL, "");
                                            }
                                            Preference.setString(context, Preference.USER_PHONE, "");
                                            //Preference.setString(context,Preference.USER_PASSWORD,"");
                                            Preference.setString(context, Preference.USER_FNAME, "");
                                            Preference.setString(context, Preference.USER_LNAME, "");
                                            Preference.setString(context, Preference.USER_STREET_ADDRESS, "");
                                            Preference.setString(context, Preference.USER_CITY_STATE_ZIP, "");
                                            Preference.setString(context, Preference.USER_SUITE_FLOOR, "");
                                            Preference.setString(context, Preference.GUEST_STATUS, "0");

                                            Preference.setString(context, Preference.CATERING_CART_ID, "");
                                            Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();

                                        }
                                        //Preference.setString(context,Preference.USER_ID,"");
                                        //tv_login_logout.setText("LOGIN");
                                        //img_cart.setVisibility(View.GONE);
                                        //ll_settings.setVisibility(View.GONE);
                                        //Intent intent = new Intent(activity,SelectStoreLocationActivity.class);
                                        //intent.putExtra("from","skip");
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        //startActivity(intent);

                                    } else {
                                        Preference.setString(context, Preference.REORDER_STATUS, "0");
                                        Preference.setString(context, Preference.CATERING_CART_ID, "");
                                        Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        } else {
                            String err = jsonObject.getString("err");
                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, err, new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }

                //For redirect to login
                /*onBackPressed();*/
            }

            @Override
            public void onError(String error) {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.response_error), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        //Cliked Ok
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
            }

            @Override
            public void noInternetConnection() {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.no_internet_connection), new DialogCallBackListner() {
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
    }

    public void SaveOrderAuthExistingFax() {
        Utility.createProgressDialoge(context, "Loading");

        String oid = "";
        try {
            JSONArray oid_jsonArray = new JSONArray(Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID));
            for (int i = 0; i < oid_jsonArray.length(); i++) {
                oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();
        map.put("globalLoggedIn", Preference.getString(context, Preference.USER_ID));
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("order_id", oid.substring(0, oid.length() - 1));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }       /* map.put("c_card_no",aedt_card_no.getText().toString().trim());
        map.put("c_year",year);
        map.put("c_month_val",month);
        map.put("c_card_cvc",aedt_security_code.getText().toString().trim());
        map.put("card_type",card_type);*/
        if (bundle.getString("is_asap").equalsIgnoreCase("1")) {
            map.put("is_asap", "1");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", "");

            if (bundle.getString("order_type").equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");

            } else if (bundle.getString("order_type").equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");

            }

        } else {
            map.put("is_asap", "0");
            if (bundle.getString("order_type").equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");
                map.put("global_delivery_time", bundle.getString("global_delivery_time"));
                map.put("global_pickup_time", "");
            } else if (bundle.getString("order_type").equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");
                map.put("global_delivery_time", "");
                map.put("global_pickup_time", bundle.getString("global_pickup_time"));
            }
        }

        map.put("order_type", "cash");  //$$$ add key
        map.put("global_street_address", Preference.getString(context, Preference.USER_STREET_ADDRESS));
        map.put("global_city_state_zip", Preference.getString(context, Preference.USER_CITY_STATE_ZIP));

        map.put("global_apt_suite_floor", Preference.getString(context, Preference.USER_SUITE_FLOOR));
        map.put("globalTax", bundle.getString("globalTax"));
        map.put("OnlineOrderSurcharge", bundle.getString("OnlineOrderSurcharge"));
        map.put("globalTips", bundle.getString("globalTips"));
        if (!bundle.getString("globalPromocode").equalsIgnoreCase("") && !bundle.getString("globalPromocodeDiscount").equalsIgnoreCase("")) {
            map.put("globalPromocode", bundle.getString("globalPromocode"));
            map.put("globalPromocodeDiscountType", bundle.getString("globalPromocodeDiscountType"));
            map.put("globalPromocodeDiscount", bundle.getString("globalPromocodeDiscount"));

        } else {
            map.put("globalPromocode", "");
            map.put("globalPromocodeDiscountType", "");
            map.put("globalPromocodeDiscount", "");

        }
        map.put("globalGrandTotal", bundle.getString("globalGrandTotal"));
        map.put("globalCartItems", oid.substring(0, oid.length() - 1));
        map.put("cart_ids", oid.substring(0, oid.length() - 1));

        map.put("global_advance_order", "");
        map.put("global_advance_order_date", "0000-00-00");
        map.put("global_special_note", bundle.getString("global_special_note").trim());
        map.put("global_delivery_charge", bundle.getString("global_delivery_charge"));

        map.put("firstordertype", bundle.getString("firstordertype"));
        map.put("discountvalue", bundle.getString("discountvalue"));

        map.put("discountType", "");
        map.put("SpecialDiscountType", bundle.getString("SpecialDiscountType"));
        map.put("SpecialDiscountAmount", bundle.getString("SpecialDiscountAmount"));
        map.put("TotalSpecialAmount", bundle.getString("TotalSpecialAmount"));
        map.put("subtotal", bundle.getString("item_total"));
        map.put("CardTotalAmount", bundle.getString("CardTotalAmount"));
        map.put("is_save_card", "1");


        ApiCall.volleyPostApi(context, AppConstants.save_order_auth_exiting_fax, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {
                        Utility.dissmisProgress();
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Unsuccessfull", new DialogCallBackListner() {
                            @Override
                            public void setPositiveListner() {
                                //Cliked Ok
                            }

                            @Override
                            public void setNegativeListner() {

                            }
                        });
                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = (JSONArray) json;
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");


                        Utility.dissmisProgress();
                        if (status.equalsIgnoreCase("1")) {

                            String order_id = jsonObject.getString("order_id");
                            String branch_order_id = jsonObject.getString("branch_order_id");

                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, "Payment successfully received by #" + branch_order_id + " Thank you for your order.", new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                    if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                        String oid = "";
                                        String orderid = "";
                                        try {
                                            orderid = Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID);
                                            if (!orderid.equalsIgnoreCase("")) {
                                                JSONArray oid_jsonArray = new JSONArray(orderid);
                                                for (int i = 0; i < oid_jsonArray.length(); i++) {
                                                    oid = oid + oid_jsonArray.getJSONObject(i).getString("order_id") + ",";
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if (!oid.equalsIgnoreCase("")) {
                                            RemoveItemFromCart.RemoveItemCartAPI(context, oid.substring(0, oid.length() - 1), new RemoveItemFromCart.RemoveItemFromCartListner() {
                                                @Override
                                                public void onResponse() {

                                                    Preference.setString(context, Preference.USER_ID, "");
                                                    if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                                        Preference.setString(context, Preference.USER_EMAIL, "");
                                                    }
                                                    //Preference.setString(context,Preference.USER_EMAIL,"");
                                                    Preference.setString(context, Preference.USER_PHONE, "");
                                                    //Preference.setString(context,Preference.USER_PASSWORD,"");
                                                    Preference.setString(context, Preference.USER_FNAME, "");
                                                    Preference.setString(context, Preference.USER_LNAME, "");
                                                    Preference.setString(context, Preference.USER_STREET_ADDRESS, "");
                                                    Preference.setString(context, Preference.USER_CITY_STATE_ZIP, "");
                                                    Preference.setString(context, Preference.USER_SUITE_FLOOR, "");
                                                    Preference.setString(context, Preference.GUEST_STATUS, "0");

                                                    Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                                    Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            });
                                        } else {

                                            Preference.setString(context, Preference.USER_ID, "");
                                            //Preference.setString(context,Preference.USER_EMAIL,"");
                                            if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {
                                                Preference.setString(context, Preference.USER_EMAIL, "");
                                            }
                                            Preference.setString(context, Preference.USER_PHONE, "");
                                            //Preference.setString(context,Preference.USER_PASSWORD,"");
                                            Preference.setString(context, Preference.USER_FNAME, "");
                                            Preference.setString(context, Preference.USER_LNAME, "");
                                            Preference.setString(context, Preference.USER_STREET_ADDRESS, "");
                                            Preference.setString(context, Preference.USER_CITY_STATE_ZIP, "");
                                            Preference.setString(context, Preference.USER_SUITE_FLOOR, "");
                                            Preference.setString(context, Preference.GUEST_STATUS, "0");

                                            Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                            Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();

                                        }
                                        //Preference.setString(context,Preference.USER_ID,"");
                                        //tv_login_logout.setText("LOGIN");
                                        //img_cart.setVisibility(View.GONE);
                                        //ll_settings.setVisibility(View.GONE);
                                        //Intent intent = new Intent(activity,SelectStoreLocationActivity.class);
                                        //intent.putExtra("from","skip");
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        //startActivity(intent);

                                    } else {
                                        Preference.setString(context, Preference.REORDER_STATUS, "0");
                                        Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                        Intent intent = new Intent(activity, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        } else {
                            String err = jsonObject.getString("err");
                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, err, new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dissmisProgress();
                }

                //For redirect to login
                /*onBackPressed();*/
            }

            @Override
            public void onError(String error) {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.response_error), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        //Cliked Ok
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
            }

            @Override
            public void noInternetConnection() {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, getResources().getString(R.string.no_internet_connection), new DialogCallBackListner() {
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
    }


}

package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.StatesSpinnerAdapter;
import com.trec2go.MorningBrewHawaii.bean.AddressBean;
import com.trec2go.MorningBrewHawaii.bean.StatesBean;
import com.trec2go.MorningBrewHawaii.commonapicall.GetStateCityListAPI;
import com.trec2go.MorningBrewHawaii.commonapicall.RemoveItemFromCart;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddNewCardActivity extends AppCompatActivity {

    TextInputLayout til_billing_address, til_billing_zipcode, til_card_no, til_security_code;
    AppCompatEditText aedt_billing_address, aedt_billing_zipcode, aedt_card_no, aedt_security_code;

    Spinner spn_card_type, spn_month, spn_year;
    TextView tv_card_type_error;
    Button btn_submit;
    CheckBox chk_save;

    Activity activity;
    Context context;

    String card_type, month, year;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);

        activity = this;
        context = this;
        bundle = getIntent().getExtras();


        Utility.log(bundle.getString("is_asap"));

        init();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void init() {
        til_billing_address = findViewById(R.id.til_billing_address);
        til_billing_zipcode = findViewById(R.id.til_billing_zipcode);
        til_card_no = findViewById(R.id.til_card_no);
        til_security_code = findViewById(R.id.til_security_code);
        aedt_billing_address = findViewById(R.id.aedt_billing_address);
        aedt_billing_zipcode = findViewById(R.id.aedt_billing_zipcode);
        aedt_card_no = findViewById(R.id.aedt_card_no);
        aedt_security_code = findViewById(R.id.aedt_security_code);
        spn_card_type = findViewById(R.id.spn_card_type);
        spn_month = findViewById(R.id.spn_month);
        spn_year = findViewById(R.id.spn_year);
        tv_card_type_error = findViewById(R.id.tv_card_type_error);
        btn_submit = findViewById(R.id.btn_submit);
        chk_save = findViewById(R.id.chk_save);

        card_type_spn();
        exp_month_spn();
        exp_year_spn();
        listners();

    }

    public void listners() {

        aedt_billing_address.addTextChangedListener(new MyTextWatcher(aedt_billing_address));
        aedt_billing_zipcode.addTextChangedListener(new MyTextWatcher(aedt_billing_zipcode));
        aedt_card_no.addTextChangedListener(new MyTextWatcher(aedt_card_no));
        aedt_security_code.addTextChangedListener(new MyTextWatcher(aedt_security_code));


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    if (Preference.getString(context, Preference.FAX_STATUS).equalsIgnoreCase("1")) {
                        SaveOrderAuthFax();

                    } else {
                        if(Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")){
                            CateringAddNewCardAPI();
                        }else {
                            AddNewCardAPI();

                        }

                    }
                }
            }
        });
    }


    //Spinner

    public void card_type_spn() {
        ArrayList<String> card_type = new ArrayList<>();
        card_type.add("Master Card");
        card_type.add("Visa");
        card_type.add("American Express");
        card_type.add("Discover");
        ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.spinner_textview, card_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_card_type.setAdapter(adapter);
    }

    public void exp_month_spn() {
        ArrayList<String> exp_month = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                exp_month.add("0" + i);
            } else {
                exp_month.add(String.valueOf(i));
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.spinner_textview, exp_month);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_month.setAdapter(adapter);
    }

    public void exp_year_spn() {

        int year = Calendar.getInstance().get(Calendar.YEAR);

        ArrayList<String> exp_year = new ArrayList<>();
        for (int i = year; i <= year + 30; i++) {
            exp_year.add(String.valueOf(i));
        }
        ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.spinner_textview, exp_year);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_year.setAdapter(adapter);
    }


    //Validation

    public boolean isValidate() {
        til_billing_address.setError(null);
        til_billing_zipcode.setError(null);
        til_card_no.setError(null);
        til_security_code.setError(null);

        card_type = ((String) spn_card_type.getSelectedItem());
        month = ((String) spn_month.getSelectedItem());
        year = ((String) spn_year.getSelectedItem());

        if (aedt_billing_address.getText().toString().trim().equalsIgnoreCase("")) {
            til_billing_address.setError("Please Enter Billing Address");
            return false;
        } else if (aedt_billing_zipcode.getText().toString().trim().equalsIgnoreCase("")) {
            til_billing_zipcode.setError("Please Enter Billing Zipcode");
            return false;
        } else if (card_type.trim().equalsIgnoreCase("")) {
            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Please Select Card Type", new DialogCallBackListner() {
                @Override
                public void setPositiveListner() {
                    //Cliked Ok
                }

                @Override
                public void setNegativeListner() {

                }
            });
            return false;
        } else if (aedt_card_no.getText().toString().trim().equalsIgnoreCase("")) {
            til_card_no.setError("Please Enter Card no.");
            return false;
        } else if (aedt_card_no.getText().toString().trim().length() > 16) {
            til_card_no.setError("Please Enter Valid Card no.");
            return false;
        } else if (aedt_security_code.getText().toString().trim().equalsIgnoreCase("")) {
            til_security_code.setError("Please Enter Security Code");
            return false;
        } else if (month.trim().equalsIgnoreCase("")) {
            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Please Select Expiry Month & Year", new DialogCallBackListner() {
                @Override
                public void setPositiveListner() {
                    //Cliked Ok
                }

                @Override
                public void setNegativeListner() {

                }
            });
            return false;
        } else if (year.trim().equalsIgnoreCase("")) {
            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Please Select Expiry Year", new DialogCallBackListner() {
                @Override
                public void setPositiveListner() {
                    //Cliked Ok
                }

                @Override
                public void setNegativeListner() {

                }
            });
            return false;
        } else {
            return true;
        }
    }


    //Api calling
    public void AddNewCardAPI() {
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
        }
        map.put("c_card_no", aedt_card_no.getText().toString().trim());
        map.put("c_year", year);
        map.put("c_month_val", month);
        map.put("c_card_cvc", aedt_security_code.getText().toString().trim());
        map.put("card_type", card_type);
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
        map.put("global_street_address", Preference.getString(context,Preference.USER_STREET_ADDRESS));
        map.put("global_city_state_zip", Preference.getString(context,Preference.USER_CITY_STATE_ZIP));
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
        map.put("subtotal", bundle.getString("item_total"));
        map.put("TotalSpecialAmount", bundle.getString("TotalSpecialAmount"));
        map.put("CardTotalAmount", bundle.getString("CardTotalAmount"));

        map.put("auth_streetaddress", aedt_billing_address.getText().toString().trim());
        map.put("auth_zipcode", aedt_billing_zipcode.getText().toString().trim());

        if (chk_save.isChecked()) {
            map.put("is_save_card", "1");
        } else {
            map.put("is_save_card", "0");
        }


        ApiCall.volleyPostApi(context, AppConstants.save_order_auth_first_order, map, new ApiCallBackListner() {
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
                            Preference.setString(context, Preference.from, "");


                        }
                        else{
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
    public void CateringAddNewCardAPI() {
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
        }
        map.put("c_card_no", aedt_card_no.getText().toString().trim());
        map.put("c_year", year);
        map.put("c_month_val", month);
        map.put("c_card_cvc", aedt_security_code.getText().toString().trim());
        map.put("card_type", card_type);
    /*    if (bundle.getString("is_asap").equalsIgnoreCase("1")) {
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
        /* }*/

        map.put("order_type", "cash");  //$$$ add key
        map.put("global_street_address", Preference.getString(context,Preference.USER_STREET_ADDRESS));
        map.put("global_city_state_zip", Preference.getString(context,Preference.USER_CITY_STATE_ZIP));
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
        map.put("subtotal", bundle.getString("item_total"));
        map.put("TotalSpecialAmount", bundle.getString("TotalSpecialAmount"));
        map.put("CardTotalAmount", bundle.getString("CardTotalAmount"));

        if (chk_save.isChecked()) {
            map.put("is_save_card", "1");
        } else {
            map.put("is_save_card", "0");
        }

        map.put("global_advance_order_date", ReviewOrderActivity.catering_date);
        map.put("global_advance_order_time", ReviewOrderActivity.catering_time);

        map.put("auth_streetaddress", aedt_billing_address.getText().toString().trim());
        map.put("auth_zipcode", aedt_billing_zipcode.getText().toString().trim());


        ApiCall.volleyPostApi(context, AppConstants.save_order_auth_first_order_catering, map, new ApiCallBackListner() {
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
                            Preference.setString(context, Preference.from, "");


                        }
                        else{
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

    public void SaveOrderAuthFax() {
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
        }
        map.put("c_card_no", aedt_card_no.getText().toString().trim());
        map.put("c_year", year);
        map.put("c_month_val", month);
        map.put("c_card_cvc", aedt_security_code.getText().toString().trim());
        map.put("card_type", card_type);
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
        map.put("global_street_address", Preference.getString(context,Preference.USER_STREET_ADDRESS));
        map.put("global_city_state_zip", Preference.getString(context,Preference.USER_CITY_STATE_ZIP));
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
        map.put("subtotal", bundle.getString("item_total"));
        map.put("TotalSpecialAmount", bundle.getString("TotalSpecialAmount"));
        map.put("CardTotalAmount", bundle.getString("CardTotalAmount"));

        map.put("auth_streetaddress", aedt_billing_address.getText().toString().trim());
        map.put("auth_zipcode", aedt_billing_zipcode.getText().toString().trim());

        if (chk_save.isChecked()) {
            map.put("is_save_card", "1");
        } else {
            map.put("is_save_card", "0");
        }


        ApiCall.volleyPostApi(context, AppConstants.save_order_auth_fax, map, new ApiCallBackListner() {
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
                            Preference.setString(context, Preference.from, "");


                        }else{
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

    //TextWatcher
    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.aedt_billing_address:
                    validate(aedt_billing_address, til_billing_address, "Please Enter Billing Address");
                    break;
                case R.id.aedt_billing_zipcode:
                    validate(aedt_billing_zipcode, til_billing_zipcode, "Please Enter Billing Zipcode");
                    break;
                case R.id.aedt_card_no:
                    validate(aedt_card_no, til_card_no, "Please Enter Card no.");
                    break;
                case R.id.aedt_security_code:
                    validate(aedt_security_code, til_security_code, "Please Enter Security Code");
                    break;

            }
        }
    }


    public boolean validate(AppCompatEditText edt, TextInputLayout til, String error) {
        String text = edt.getText().toString();
        if (text.isEmpty()) {
            til.setError(error);
            requestFocus(edt);
            return false;
        } else {
            til.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}

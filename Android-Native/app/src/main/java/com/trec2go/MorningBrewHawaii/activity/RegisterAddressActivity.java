package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.StatesSpinnerAdapter;
import com.trec2go.MorningBrewHawaii.bean.StatesBean;
import com.trec2go.MorningBrewHawaii.commonapicall.GetStateCityListAPI;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterAddressActivity extends AppCompatActivity {

    Activity activity;
    Context context;

    Button btn_save;
    String email, fname, lname, password, phone_number;

    TextInputLayout til_street_address, til_state, til_city, til_pincode,til_apartment;
    AppCompatEditText apt_street_address, apt_state, apt_city, apt_pincode,apt_street_apartment;


    Spinner spn_state;
    StatesSpinnerAdapter statesSpinnerAdapter;
    AutoCompleteTextView autoCompleteCity;
    String state_code;
    String state;
    TextView tv_city_error, tv_state_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);

        activity = this;
        context = this;

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        fname = bundle.getString("fname");
        lname = bundle.getString("lname");
        password = bundle.getString("password");
        phone_number = bundle.getString("phone_number");

        btn_save = findViewById(R.id.btn_save);
        til_street_address = findViewById(R.id.til_street_address);
        til_apartment=findViewById(R.id.til_street_apartment);
        til_state = findViewById(R.id.til_state);
        til_city = findViewById(R.id.til_city);
        til_pincode = findViewById(R.id.til_pincode);
        tv_city_error = findViewById(R.id.tv_city_error);
        tv_state_error = findViewById(R.id.tv_state_error);

        tv_city_error.setVisibility(View.INVISIBLE);
        tv_state_error.setVisibility(View.INVISIBLE);

        apt_street_address = findViewById(R.id.apt_street_address);
        apt_street_apartment=findViewById(R.id.apt_street_apartment);
        apt_state = findViewById(R.id.apt_state);
        apt_city = findViewById(R.id.apt_city);
        apt_pincode = findViewById(R.id.apt_pincode);

        autoCompleteCity = findViewById(R.id.autoCompleteCity);
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isvalidate()) {
                    String address = apt_street_address.getText().toString();
                    String apartment=apt_street_apartment.getText().toString();
                    //String city = apt_city.getText().toString();
                    String city = autoCompleteCity.getText().toString();
                    // String state = apt_state.getText().toString();
                    String zipcode = apt_pincode.getText().toString();
                    RegisterAPI(address, apartment,city, state, zipcode);

                }
            }
        });

        spn_state = findViewById(R.id.spn_state);
        GetStateCityListAPI.GetStatesApi(context, new GetStateCityListAPI.GetStateListAPIListner() {
            @Override
            public void getStates(ArrayList<StatesBean> statesBeanArrayList) {
                statesSpinnerAdapter = new StatesSpinnerAdapter(activity, R.layout.spinner_textview, R.id.textview, statesBeanArrayList);
                statesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_state.setAdapter(statesSpinnerAdapter);
            }
        });


        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                state = ((TextView) parent.getChildAt(0)).getText().toString();
                state_code = ((TextView) parent.getChildAt(0)).getText().toString();
                autoCompleteCity.setText("");
                GetStateCityListAPI.GetCitiesApi(context, state_code, new GetStateCityListAPI.GetCityListAPIListner() {
                    @Override
                    public void getCites(ArrayList<String> citiesBeanArrayList) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, citiesBeanArrayList);
                        autoCompleteCity.setThreshold(2);
                        autoCompleteCity.setAdapter(adapter);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //Validation
    public boolean isvalidate() {

        til_street_address.setError(null);
        til_apartment.setError(null);
        til_state.setError(null);
        til_city.setError(null);
        til_pincode.setError(null);
        tv_city_error.setVisibility(View.INVISIBLE);
        tv_state_error.setVisibility(View.INVISIBLE);

        if (apt_street_address.getText().toString().trim().isEmpty()) {
            til_street_address.setError("Please enter Street Address");
            return false;
        } if (apt_street_apartment.getText().toString().trim().isEmpty()) {
            til_apartment.setError("Please enter Apartment");
            return false;
        }
        else if (state.trim().isEmpty()) {
            tv_state_error.setVisibility(View.VISIBLE);
            til_state.setError("Please enter State");
            return false;
        } else if (autoCompleteCity.getText().toString().trim().isEmpty()) {
            tv_city_error.setVisibility(View.VISIBLE);
            til_city.setError("Please enter City");
            return false;
        } else if (apt_pincode.getText().toString().trim().isEmpty()) {
            til_pincode.setError("Please Enter Zipcode");
            return false;
        } else {
            return true;
        }
    }


    //Api calling
    public void RegisterAPI(String address, String apartment,String city, String state, String zipcode) {
        Utility.createProgressDialoge(context, "Loading");

        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("email", email);
        map.put("f_name", fname);
        map.put("l_name", lname);
        map.put("password", password);
        map.put("phone_no", phone_number);
        map.put("apt_suite_floor",apartment);
        map.put("address", address);
        map.put("city", city);
        map.put("state", state);
        map.put("zipcode", zipcode);
        map.put("fcm_id", Preference.getString(context, Preference.FCM_token));
        map.put("device_type", "android");

        ApiCall.volleyPostApi(context, AppConstants.register, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {
                        Utility.dissmisProgress();
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Login Unsuccessfull", new DialogCallBackListner() {
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
                        JSONObject jsonObject = jsonArray.optJSONObject(0);
                        String status = jsonObject.optString("status");
                        if (status.equalsIgnoreCase("1")) {
                            Utility.dissmisProgress();
                            String user_id = jsonObject.optString("reg_id");
                            String email = jsonObject.optString("email");
                            String password = jsonObject.optString("password");
                            String f_name = jsonObject.optString("f_name");
                            String l_name = jsonObject.optString("l_name");
                            String street_address = jsonObject.optString("street_address");
                            String apt_suite_floor = jsonObject.optString("apt_suite_floor");
                            String city_state_zip = jsonObject.optString("city_state_zip");
                            String phone_no = jsonObject.optString("phone_no");
                            Preference.setString(context, Preference.USER_ID, user_id);
                            Preference.setString(context, Preference.USER_EMAIL, email);
                            Preference.setString(context, Preference.USER_PASSWORD, password);
                            Preference.setString(context, Preference.USER_FNAME, f_name);
                            Preference.setString(context, Preference.USER_LNAME, l_name);
                            Preference.setString(context, Preference.USER_STREET_ADDRESS, street_address);
                            Preference.setString(context, Preference.USER_CITY_STATE_ZIP, city_state_zip);
                            Preference.setString(context, Preference.USER_SUITE_FLOOR, apt_suite_floor);
                            Preference.setString(context, Preference.USER_PHONE, phone_no);
                            /*if(Preference.getString(context,Preference.BRANCH_ID).equalsIgnoreCase("")){
                                Intent intent = new Intent(activity,SelectStoreLocationActivity.class);
                                intent.putExtra("from","login");
                                startActivity(intent);
                            }else {*/   // $$$ comment code
                            Intent intent = new Intent(activity, SelectStoreLocationActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                            intent.putExtra("from", "login");
                            startActivity(intent);
                            finish();
                            /* }*/
                        } else {
                            Utility.dissmisProgress();
                            String error = jsonObject.optString("msg");
                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, error, new DialogCallBackListner() {
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

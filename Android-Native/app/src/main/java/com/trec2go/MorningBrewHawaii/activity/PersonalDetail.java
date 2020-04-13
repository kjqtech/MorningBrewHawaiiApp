package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.StatesSpinnerAdapter;
import com.trec2go.MorningBrewHawaii.bean.AddToCartBean;
import com.trec2go.MorningBrewHawaii.bean.StatesBean;
import com.trec2go.MorningBrewHawaii.commonapicall.AddToCartApi;
import com.trec2go.MorningBrewHawaii.commonapicall.GetStateCityListAPI;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PersonalDetail extends AppCompatActivity {

    Activity activity;
    Context context;
    ImageView img_back_arrow;

    TextInputLayout til_street_address, til_city, til_state, til_zipcode,til_apartment;
    AppCompatEditText aedt_street_address, aedt_city, aedt_state, aedt_zipcode,aedt_street_apartment;

    TextInputLayout til_fname, til_lname, til_email, til_password, til_confirm_password, til_phone_number;
    AppCompatEditText aedt_fname, aedt_lname, aedt_email, aedt_password, aedt_confirm_password, aedt_phone_number;

    Button btn_save;

    Spinner spn_state;
    StatesSpinnerAdapter statesSpinnerAdapter;
    AutoCompleteTextView autoCompleteCity;
    String state_code;
    String state;
    TextView tv_city_error, tv_state_error;
    String city_zip;

    AddToCartBean addToCartBean ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);
        context = this;
        activity = this;

        img_back_arrow = findViewById(R.id.img_back_arrow);

        til_street_address = findViewById(R.id.til_street_address);
        til_apartment=findViewById(R.id.til_street_apartment);
        til_city = findViewById(R.id.til_city);
        til_state = findViewById(R.id.til_state);
        til_zipcode = findViewById(R.id.til_zipcode);
        btn_save = findViewById(R.id.btn_save);

        tv_city_error = findViewById(R.id.tv_city_error);
        tv_state_error = findViewById(R.id.tv_state_error);
        tv_city_error.setVisibility(View.INVISIBLE);
        tv_state_error.setVisibility(View.INVISIBLE);


        autoCompleteCity = findViewById(R.id.autoCompleteCity);
        spn_state = findViewById(R.id.spn_state);

        aedt_street_address = findViewById(R.id.aedt_street_address);
        aedt_street_apartment=findViewById(R.id.aedt_street_apartment);
        aedt_city = findViewById(R.id.aedt_city);
        aedt_state = findViewById(R.id.aedt_state);
        aedt_zipcode = findViewById(R.id.aedt_zipcode);


        til_fname = findViewById(R.id.til_fname);
        til_lname = findViewById(R.id.til_lname);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        til_confirm_password = findViewById(R.id.til_confirm_password);
        til_phone_number = findViewById(R.id.til_phone_number);

        aedt_fname = findViewById(R.id.aedt_fname);
        aedt_lname = findViewById(R.id.aedt_lname);
        aedt_email = findViewById(R.id.aedt_email);
        aedt_password = findViewById(R.id.aedt_password);
        aedt_confirm_password = findViewById(R.id.aedt_confirm_password);
        aedt_phone_number = findViewById(R.id.aedt_phone_number);

        if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
            addToCartBean = getIntent().getParcelableExtra("addToCartBean");
            til_password.setVisibility(View.GONE);
            til_confirm_password.setVisibility(View.GONE);
        } else {
            til_password.setVisibility(View.VISIBLE);
            til_confirm_password.setVisibility(View.VISIBLE);
        }

        String user_id = Preference.getString(context, Preference.USER_ID);
        String email = Preference.getString(context, Preference.USER_EMAIL);
        String user_password = Preference.getString(context, Preference.USER_PASSWORD);
        String fname = Preference.getString(context, Preference.USER_FNAME);
        String lname = Preference.getString(context, Preference.USER_LNAME);
        String address = Preference.getString(context, Preference.USER_STREET_ADDRESS);
        city_zip = Preference.getString(context, Preference.USER_CITY_STATE_ZIP);
        String suite_floor = Preference.getString(context, Preference.USER_SUITE_FLOOR);
        String phone = Preference.getString(context, Preference.USER_PHONE);

        if (!Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
            aedt_fname.setText(fname);
            aedt_lname.setText(lname);
            aedt_street_address.setText(address);
            aedt_street_apartment.setText(suite_floor);
            aedt_email.setText(email);
            aedt_phone_number.setText(phone);
            if (!city_zip.equalsIgnoreCase("")) {
                List<String> city_state_zip = Arrays.asList(city_zip.split("\\s*@@\\s*"));
                if (city_state_zip.size() == 3) {
                    autoCompleteCity.setText(city_state_zip.get(0));
                    //aedt_state.setText(city_state_zip.get(1));
                    aedt_zipcode.setText(city_state_zip.get(2));
                }
            }
        }


        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isvalidate()) {
                    EditProfileAPI();
                }
            }
        });

        GetStateCityListAPI.GetStatesApi(context, new GetStateCityListAPI.GetStateListAPIListner() {
            @Override
            public void getStates(ArrayList<StatesBean> statesBeanArrayList) {

                int position = 0;
                statesSpinnerAdapter = new StatesSpinnerAdapter(activity, R.layout.spinner_textview, R.id.textview, statesBeanArrayList);
                statesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_state.setAdapter(statesSpinnerAdapter);

                if (!city_zip.equalsIgnoreCase("")) {
                    List<String> city_state_zip = Arrays.asList(city_zip.split("\\s*@@\\s*"));
                    if (city_state_zip.size() == 3) {
                        for (int i = 0; i < statesBeanArrayList.size(); i++) {
                            if (city_state_zip.get(1).equalsIgnoreCase(statesBeanArrayList.get(i).states)) {
                                position = i;
                                break;
                            } else {

                            }
                        }
                        spn_state.setSelection(position);

                    }
                } else {

                }


            }
        });


        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                state = ((TextView) parent.getChildAt(0)).getText().toString();
                state_code = ((TextView) parent.getChildAt(0)).getText().toString();
                // autoCompleteCity.setText("");
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
        til_fname.setError(null);
        til_lname.setError(null);
        til_email.setError(null);
        til_password.setError(null);
        til_confirm_password.setError(null);
        til_phone_number.setError(null);
        til_street_address.setError(null);
        til_apartment.setError(null);
        til_city.setError(null);
        til_state.setError(null);
        til_zipcode.setError(null);

        tv_city_error.setVisibility(View.INVISIBLE);
        tv_state_error.setVisibility(View.INVISIBLE);

        if (aedt_fname.getText().toString().isEmpty()) {
            til_fname.setError("Please enter First Name");
            return false;
        } else if (aedt_lname.getText().toString().isEmpty()) {
            til_lname.setError("Please enter Last Name");
            return false;
        } else if (aedt_email.getText().toString().isEmpty()) {
            til_email.setError("Please enter Email Address");
            return false;
        } else if (!Validation.isValidEmail(aedt_email.getText().toString().trim())) {
            til_email.setError("Please enter Valid Email Address");
            return false;
        } else if (aedt_phone_number.getText().toString().isEmpty()) {
            til_phone_number.setError("Please Enter Phone Number");
            return false;
        } else if (!Validation.isValidPhoneNumber(aedt_phone_number.getText())) {
            til_phone_number.setError("Please enter Valid Phone Number");
            return false;
        } else if (aedt_street_address.getText().toString().trim().equalsIgnoreCase("")) {
            til_street_address.setError("Please Enter Street Address");
            return false;
        } else if (aedt_street_apartment.getText().toString().trim().equalsIgnoreCase("")) {
            til_apartment.setError("Please Enter Apartment");
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
        } else if (aedt_zipcode.getText().toString().trim().isEmpty()) {
            til_zipcode.setError("Please Enter Zipcode");
            return false;
        } else {
            if (!Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
                if (aedt_password.getText().toString().isEmpty()) {
                    til_password.setError("Please Enter Password");
                    return false;
                } else if (aedt_confirm_password.getText().toString().isEmpty()) {
                    til_confirm_password.setError("Please Enter Confirm Password");
                    return false;
                } else if (!aedt_password.getText().toString().equalsIgnoreCase(aedt_confirm_password.getText().toString())) {
                    til_confirm_password.setError("Password & Confirm Password must be same");
                    return false;
                } else {
                    return true;

                }
            } else {
                return true;

            }

        }
    }

    //API CAlling
    public void EditProfileAPI() {
        String city = autoCompleteCity.getText().toString().trim();
        String stat = state.toString().trim();
        String zip = aedt_zipcode.getText().toString().trim();
        final String city_zip = city + "@@" + stat + "@@" + zip;

        final String email = aedt_email.getText().toString();
        final String fname = aedt_fname.getText().toString();
        final String lname = aedt_lname.getText().toString();
        final String password = aedt_password.getText().toString();
        final String phone_number = aedt_phone_number.getText().toString();
        final String street_address = aedt_street_address.getText().toString();
        final String street_apartment=aedt_street_apartment.getText().toString();

        Utility.createProgressDialoge(context, "Loading");
        final Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("street_address", street_address);
        map.put("apt_suite_floor", street_apartment);
        map.put("city_state_zip", city_zip);
        map.put("email", email);
        map.put("f_name", fname);
        map.put("l_name", lname);
        if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
            map.put("password", "");
        } else {
            map.put("password", password);
        }
        map.put("phone_no", phone_number);
        if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase(""))  //$$$
        {
            map.put("u_id", "0");
            map.put("guest", "1");

        } else {
            map.put("u_id", Preference.getString(context, Preference.USER_ID));
            map.put("guest", "0");

        }


        ApiCall.volleyPostApi(context, AppConstants.edit_profile, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {
                        Utility.dissmisProgress();
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "", new DialogCallBackListner() {
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
                        String msg = jsonObject.optString("msg");
                        final String guest_id = jsonObject.optString("guest_id");  //$$$


                        if (status.equalsIgnoreCase("1")) {
                            Preference.setString(context, Preference.USER_EMAIL, email);
                            Preference.setString(context, Preference.USER_PASSWORD, password);
                            Preference.setString(context, Preference.USER_FNAME, fname);
                            Preference.setString(context, Preference.USER_LNAME, lname);
                            Preference.setString(context, Preference.USER_STREET_ADDRESS, street_address);
                            Preference.setString(context, Preference.USER_CITY_STATE_ZIP, city_zip);
                            Preference.setString(context, Preference.USER_SUITE_FLOOR, "");
                            Preference.setString(context, Preference.USER_PHONE, phone_number);


                            Utility.dissmisProgress();
                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, msg, new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                    if (!guest_id.equalsIgnoreCase("")) {
                                        Preference.setString(context, Preference.GUEST_STATUS, "1");
                                        Preference.setString(context, Preference.USER_ID, guest_id);
                                        AddToCartApi.addToCartAPI(context, activity, addToCartBean);
                                        /*Intent intent = new Intent(PersonalDetail.this, YourBagActivity.class);
                                        startActivity(intent);
                                        finish();*/
                                    } else {
                                        finish();
                                    }
                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        } else {
                            Utility.dissmisProgress();
                            String err = jsonObject.optString("err");

                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, err, new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                    finish();
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

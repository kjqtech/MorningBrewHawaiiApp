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
import com.trec2go.MorningBrewHawaii.bean.AddressBean;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddAddressActivity extends AppCompatActivity {

    Activity activity;
    Context context;
    ImageView img_back_arrow;

    String from;
    AddressBean addressBean;

    TextInputLayout til_name,til_street_address,til_city,til_state,til_zipcode,til_street_apartment;
    AppCompatEditText aedt_name,aedt_street_address,aedt_city,aedt_state,aedt_zipcode,aedt_street_apartment;

    Button btn_save;


    Spinner spn_state;
    StatesSpinnerAdapter statesSpinnerAdapter;
    AutoCompleteTextView autoCompleteCity;
    String state_code;
    public String state;
    TextView tv_city_error,tv_state_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        context = this;
        activity = this;

        img_back_arrow = findViewById(R.id.img_back_arrow);

        til_name = findViewById(R.id.til_name);
        til_street_address = findViewById(R.id.til_street_address);
        til_city = findViewById(R.id.til_city);
        til_state = findViewById(R.id.til_state);
        til_zipcode = findViewById(R.id.til_zipcode);
        til_street_apartment=findViewById(R.id.til_street_apartment);
        btn_save = findViewById(R.id.btn_save);

        tv_city_error  = findViewById(R.id.tv_city_error);
        tv_state_error  = findViewById(R.id.tv_state_error);
        tv_city_error.setVisibility(View.INVISIBLE);
        tv_state_error.setVisibility(View.INVISIBLE);

        aedt_name = findViewById(R.id.aedt_name);
        aedt_street_address = findViewById(R.id.aedt_street_address);
        aedt_city = findViewById(R.id.aedt_city);
        aedt_state = findViewById(R.id.aedt_state);
        aedt_zipcode = findViewById(R.id.aedt_zipcode);
        autoCompleteCity  = findViewById(R.id.autoCompleteCity);
        spn_state = findViewById(R.id.spn_state);
        aedt_street_apartment=findViewById(R.id.aedt_street_apartment);

        from = getIntent().getExtras().getString("from");
        if(from.equalsIgnoreCase("Edit")){
            addressBean = getIntent().getExtras().getParcelable("data");
            List<String> city_state_zip = Arrays.asList(addressBean.city_state_zip.split("\\s*,\\s*"));
            aedt_name.setText(addressBean.name);
            aedt_street_address.setText(addressBean.street_address);
            aedt_street_apartment.setText(addressBean.apt_suite_floor);
            aedt_city.setText(city_state_zip.get(0));
            autoCompleteCity.setText(city_state_zip.get(0));
            //aedt_state.setText(city_state_zip.get(1));
            aedt_zipcode.setText(city_state_zip.get(2));
        }else {

        }


        GetStateCityListAPI.GetStatesApi(context, new GetStateCityListAPI.GetStateListAPIListner() {
            @Override
            public void getStates(ArrayList<StatesBean> statesBeanArrayList) {

                int position = 0;
                statesSpinnerAdapter = new StatesSpinnerAdapter(activity,R.layout.spinner_textview,R.id.textview,statesBeanArrayList);
                statesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_state.setAdapter(statesSpinnerAdapter);
                
                if(from.equalsIgnoreCase("Edit")){
                    addressBean = getIntent().getExtras().getParcelable("data");
                    List<String> city_state_zip = Arrays.asList(addressBean.city_state_zip.split("\\s*,\\s*"));
                  
                    for(int i=0;i<statesBeanArrayList.size();i++){
                        if(city_state_zip.get(1).equalsIgnoreCase(statesBeanArrayList.get(i).states)){
                            position = i;
                            break;
                        }else {
                            
                        }
                    }


                    spn_state.setSelection(position);
                    
                }else {

                }
                
                
            }
        });


        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                state = ((TextView) parent.getChildAt(0)).getText().toString();
                state_code = ((TextView) parent.getChildAt(0)).getText().toString();
                //autoCompleteCity.setText("");
                if(!from.equalsIgnoreCase("Edit")){
                    autoCompleteCity.setText("");
                }
                GetStateCityListAPI.GetCitiesApi(context, state_code,new GetStateCityListAPI.GetCityListAPIListner() {
                    @Override
                    public void getCites(ArrayList<String> citiesBeanArrayList) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item, citiesBeanArrayList);
                        autoCompleteCity.setThreshold(2);
                        autoCompleteCity.setAdapter(adapter);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidate()){

                   AddEditAddressAPI();

                }
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    //Api Calling

    public void AddEditAddressAPI(){

        String message = "";

        String city = autoCompleteCity.getText().toString().trim();
        String stat = state.trim();
        String zip = aedt_zipcode.getText().toString().trim();
        String city_zip = city + "@@" + stat + "@@" + zip;

        Utility.createProgressDialoge(context,"Loading");
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        map.put("address_u_id",Preference.getString(context,Preference.USER_ID));
        map.put("name",aedt_name.getText().toString());
        map.put("street_address",aedt_street_address.getText().toString());
        map.put("apt_suite_floor",aedt_street_apartment.getText().toString());
        map.put("city_state_zip",city_zip);

        if(from.equalsIgnoreCase("Edit")){
            map.put("address_id",addressBean.id);
            message = "Address Updated";
        }else {
            message = "Address Added";
        }

        final String finalMessage = message;
        ApiCall.volleyPostApi(context, AppConstants.update_user_address, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject){
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
                    }else if (json instanceof JSONArray){
                        JSONArray jsonArray = (JSONArray) json;
                        JSONObject jsonObject = jsonArray.optJSONObject(0);
                        String status = jsonObject.optString("status");
                        if(status.equalsIgnoreCase("1")){
                            Utility.dissmisProgress();
                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, finalMessage, new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                    finish();
                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        }else {
                            Utility.dissmisProgress();

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


    //Validation

    public boolean isValidate(){
        til_name.setError(null);
        til_street_address.setError(null);
        til_city.setError(null);
        til_state.setError(null);
        til_zipcode.setError(null);
        tv_city_error.setVisibility(View.INVISIBLE);
        tv_state_error.setVisibility(View.INVISIBLE);

        if(aedt_name.getText().toString().trim().equalsIgnoreCase("")){
            til_name.setError("Please Enter Name");
            return false;
        }else if(aedt_street_address.getText().toString().trim().equalsIgnoreCase("")){
            til_street_address.setError("Please Enter Street Address");
            return false;
        }else if(aedt_street_apartment.getText().toString().trim().equalsIgnoreCase("")){
            til_street_apartment.setError("Please Enter Apartment");
            return false;
        }
        else if(state.trim().isEmpty()){
            tv_state_error.setVisibility(View.VISIBLE);
            til_state.setError("Please enter State");
            return false;
        }else if(autoCompleteCity.getText().toString().trim().equalsIgnoreCase("")){
            tv_city_error.setVisibility(View.VISIBLE);
            til_city.setError("Please enter City");
            return false;
        }else if(aedt_zipcode.getText().toString().trim().equalsIgnoreCase("")){
            til_zipcode.setError("Please Enter Zipcode");
            return false;
        }else {
            return true;
        }
    }

}

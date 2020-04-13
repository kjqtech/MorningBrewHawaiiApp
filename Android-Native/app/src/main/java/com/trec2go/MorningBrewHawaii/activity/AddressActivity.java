package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;

import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.AddressAdapter;
import com.trec2go.MorningBrewHawaii.bean.AddressBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.ItemOffsetDecoration;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddressActivity extends AppCompatActivity {
    RecyclerView rv_list;
    ArrayList<AddressBean> ar_list2; //array list
    public static AddressAdapter ad_list2; //Adapter

    Activity activity;
    Context context;
    ImageView img_back_arrow, img_add_address;
    TextView tv_no_data_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        context = this;
        activity = this;

        img_back_arrow = findViewById(R.id.img_back_arrow);
        tv_no_data_available = findViewById(R.id.tv_no_data_available);
        img_add_address = findViewById(R.id.img_add_address);


        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Preference.getString(context, Preference.from).equalsIgnoreCase("YourBagActivity")){
                    onBackPressed();

                }else {
                    finish();
                }
            }
        });

        img_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddAddressActivity.class);
                intent.putExtra("from", "Add");
                startActivity(intent);
            }
        });

        if (!islandcape()) {
            setRecycler();
        } else {
            setLandRecycler();
        }

        //GEtAddresssAPI();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Preference.getString(context, Preference.from).equalsIgnoreCase("YourBagActivity")) {
            YourBagActivity.review_list.get(0).setDelivery("$0.00");
        }else {
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int newOrientation = newConfig.orientation;

        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Do certain things when the user has switched to landscape.
            setLandRecycler();
        } else {
            setRecycler();
        }
    }

    public boolean islandcape() {
        return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    public void setRecycler() {
        rv_list = findViewById(R.id.rv_list);
        ar_list2 = new ArrayList<AddressBean>();
        ad_list2 = new AddressAdapter(context, activity, ar_list2, new AddressAdapter.AddressRowListner() {
            @Override
            public void OnDeleteListner(AddressBean addressBean) {
                //DeleteAddressAPI(addressBean.id);
            }

            @Override
            public void OnEditListner(AddressBean addressBean) {
                Intent intent = new Intent(activity, AddAddressActivity.class);
                intent.putExtra("from", "Edit");
                intent.putExtra("data", addressBean);
                startActivity(intent);
            }

            @Override
            public void OnItemViewListener(String street_address, String apartment, String city_state_zip, String id) {

                //if address activity open from yourBag
                if (Preference.getString(context, Preference.from).equalsIgnoreCase("YourBagActivity")) {
                    ApplyDelivryChargeAPI(id, street_address,apartment,city_state_zip);

                }
            }
        });

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(activity);
        rv_list.setLayoutManager(layoutManager2);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen._5sdp);
        rv_list.addItemDecoration(itemDecoration);

        rv_list.setAdapter(ad_list2);
    }

    public void setLandRecycler() {
        rv_list = findViewById(R.id.rv_list);
        ar_list2 = new ArrayList<AddressBean>();
        ad_list2 = new AddressAdapter(context, activity, ar_list2, new AddressAdapter.AddressRowListner() {
            @Override
            public void OnDeleteListner(AddressBean addressBean) {
                //DeleteAddressAPI(addressBean.id);
            }

            @Override
            public void OnEditListner(AddressBean addressBean) {
                Intent intent = new Intent(activity, AddAddressActivity.class);
                intent.putExtra("from", "Edit");
                intent.putExtra("data", addressBean);
                startActivity(intent);
            }

            @Override
            public void OnItemViewListener(String street_address, String apartment, String city_state_zip, String id) {
                Intent intent = getIntent();

                //if address activity open from yourBag
                if (Preference.getString(context, Preference.from).equalsIgnoreCase("YourBagActivity")) {
                    ApplyDelivryChargeAPI(id, street_address,apartment,city_state_zip);
                   /* Intent intent1 = new Intent(activity, ReviewOrderActivity.class);
                    YourBagActivity.review_list.get(0).setDeliveryStreetAddress(street_address);
                    YourBagActivity.review_list.get(0).setDeliverycity_state_zip(city_state_zip);
                    Preference.setString(context,Preference.USER_STREET_ADDRESS, street_address);
                    Preference.setString(context,Preference.USER_CITY_STATE_ZIP, city_state_zip.replace(", ", "@@"));
                    startActivity(intent1);*/
                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        rv_list.setLayoutManager(gridLayoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen._5sdp);
        rv_list.addItemDecoration(itemDecoration);


        //LinearLayoutManager layoutManager2  = new LinearLayoutManager(activity);
        rv_list.setLayoutManager(gridLayoutManager);
        rv_list.setAdapter(ad_list2);


    }

    @Override
    protected void onResume() {
        super.onResume();
        GEtAddresssAPI();
    }

    public void GEtAddresssAPI() {
        //status 1=>Dont show progress 2=>show progress
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("u_id", Preference.getString(context, Preference.USER_ID));
        ApiCall.volleyPostApi(context, AppConstants.my_address, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ar_list2.clear();
                    ar_list2.addAll((Collection<? extends AddressBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<AddressBean>>() {
                    }.getType()));
                    ad_list2.updateList(ar_list2);
                    if (ar_list2.isEmpty()) {
                        tv_no_data_available.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_data_available.setVisibility(View.GONE);
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

    public void ApplyDelivryChargeAPI(String id, final String street_address, final String apartment,final String city_state_zip) {
        Utility.createProgressDialoge(context, "Loading..");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));
        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }
        map.put("user_address_id", id);
        map.put("u_id", Preference.getString(context, Preference.USER_ID));

        ApiCall.volleyPostApi(context, AppConstants.apply_delivery_charge, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                YourBagActivity.review_list.get(0).setDelivery("$" + 0.00);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String deliverycharge_status = object.getString("status");
                        if (deliverycharge_status.equalsIgnoreCase("0")) {
                            String msg = object.getString("msg");

                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, msg, new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });

                        } else {
                            String deliverycharge = object.getString("deliverycharge");

                            YourBagActivity.review_list.get(0).setDelivery("$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(deliverycharge)));

                            Intent intent1 = new Intent(activity, ReviewOrderActivity.class);
                            YourBagActivity.review_list.get(0).setDeliveryStreetAddress(street_address);
                            YourBagActivity.review_list.get(0).setDeliverycity_state_zip(city_state_zip);
                            YourBagActivity.review_list.get(0).setDeliveryApartment(apartment);
                            Preference.setString(context, Preference.USER_STREET_ADDRESS, street_address);
                            Preference.setString(context, Preference.USER_CITY_STATE_ZIP, city_state_zip.replace(", ", "@@"));
                            startActivity(intent1);
                        }


                    }

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


}

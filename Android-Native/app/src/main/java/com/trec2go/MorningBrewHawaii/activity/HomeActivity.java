package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.HomeGirdRecyclerViewAdapter;
import com.trec2go.MorningBrewHawaii.bean.HomeGridBean;
import com.trec2go.MorningBrewHawaii.commonapicall.GetHomeApi;
import com.trec2go.MorningBrewHawaii.commonapicall.GetImageListAPI;
import com.trec2go.MorningBrewHawaii.commonapicall.RemoveItemFromCart;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.GridSpacingItemDecoration;
import com.trec2go.MorningBrewHawaii.utility.ImageManipulation;
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

//Package name to be changed

public class HomeActivity extends AppCompatActivity {

    Activity activity;
    Context context;


    LinearLayout ll_location, ll_menu, ll_appointment, ll_table_reservation, ll_offers, ll_settings, ll_donation, ll_about_us, ll_Property_map, ll_doordash, ll_catering;

    TextView tv_login_logout, tv_menu_text, tv_online_order_link;

    ImageView img_cart, img_logo, img_bk, btn_online_order, img_catering_cart;

    RecyclerView recyclerView;
    HomeGirdRecyclerViewAdapter ad_list;
    ArrayList<HomeGridBean> list = new ArrayList();
    Float spaceInPixels = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activity = this;
        context = this;
        init();


    }

    @Override
    protected void onResume() {
        super.onResume();
        GetHomeApi.GetHomeHomeActivity(context, ad_list);

        if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {

            tv_login_logout.setText("LOGIN");
            //ll_settings.setVisibility(View.GONE);
            img_cart.setVisibility(View.GONE);
            ll_catering.setVisibility(View.GONE);
        } else {
            //$$$ if user login as a guest checkout than user cant see setting
            if (Preference.getString(context, Preference.GUEST_STATUS).equalsIgnoreCase("1")) {

                tv_login_logout.setText("LOGOUT");
                //ll_settings.setVisibility(View.GONE);
                img_cart.setVisibility(View.VISIBLE);
                ll_catering.setVisibility(View.VISIBLE);
            } else {
                tv_login_logout.setText("LOGOUT");
                //ll_settings.setVisibility(View.VISIBLE);
                img_cart.setVisibility(View.VISIBLE);
                ll_catering.setVisibility(View.VISIBLE);
            }
        }



        /*if (Preference.getString(context, Preference.IS_DONATION_VISIBLE).equalsIgnoreCase("1")) {
            ll_donation.setVisibility(View.VISIBLE);
            ll_donation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Preference.getString(context, Preference.DONATION)));
                    startActivity(browserIntent);
                }
            });
        } else {
            ll_donation.setVisibility(View.GONE);
            ll_donation.setOnClickListener(null);
        }

        if (Preference.getString(context, Preference.IS_PROPERTY_MAP_VISIBLE).equalsIgnoreCase("1")) {
            ll_Property_map.setVisibility(View.VISIBLE);
            ll_Property_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PropertyMapActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            ll_Property_map.setVisibility(View.GONE);
            ll_Property_map.setOnClickListener(null);
        }

        if (Preference.getString(context, Preference.IS_TABLE).equalsIgnoreCase("1")) {
            ll_table_reservation.setVisibility(View.VISIBLE);
        } else {
            ll_table_reservation.setVisibility(View.GONE);
        }

        if (Preference.getString(context, Preference.IS_APPOINTMENT).equalsIgnoreCase("1")) {
            ll_appointment.setVisibility(View.VISIBLE);
        } else {
            ll_appointment.setVisibility(View.GONE);
        }*/


        try {
            String data = Preference.getString(context, Preference.CATERING_CART_ID);
            Utility.log_api_failure(data);
            if (data.equalsIgnoreCase("")) {
                LayerDrawable icon = (LayerDrawable) img_catering_cart.getDrawable();
                Utility.setBadgeCount(this, icon, String.valueOf(0));
                ll_catering.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Your Catering Cart is Empty", new DialogCallBackListner() {
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
            } else {
                JSONArray jsonArray = new JSONArray(data);
                LayerDrawable icon = (LayerDrawable) img_catering_cart.getDrawable();
                Utility.setBadgeCount(this, icon, String.valueOf(jsonArray.length()));
                Utility.log_api_failure(jsonArray.toString());
                ll_catering.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Preference.setString(context, Preference.CATERING_STATUS, "1");

                        if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
                            Intent intent = new Intent(activity, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(activity, YourBagActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            String data = Preference.getString(context, Preference.STORAGE_YOUR_BAG_ID);
            Utility.log_api_failure(data);
            if (data.equalsIgnoreCase("")) {
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
            } else {
                JSONArray jsonArray = new JSONArray(data);
                LayerDrawable icon = (LayerDrawable) img_cart.getDrawable();
                Utility.setBadgeCount(this, icon, String.valueOf(jsonArray.length()));
                Utility.log_api_failure(jsonArray.toString());
                img_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Preference.setString(context, Preference.CATERING_STATUS, "0");

                        if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.putExtra("from", "Order_Detail");
                            startActivity(intent);
                            finish();
                        } else {
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

    public void init() {
        img_logo = findViewById(R.id.img_logo);
        img_bk = findViewById(R.id.img_bk);
        ImageManipulation.loadImage(context, Preference.getString(context, Preference.LOGO), img_logo);
        ImageManipulation.loadImage(context, Preference.getString(context, Preference.BACKGROUND_IMAGE), img_bk);


        /*ll_settings = findViewById(R.id.ll_settings);
        ll_offers = findViewById(R.id.ll_offers);
        ll_appointment = findViewById(R.id.ll_appointment);
        ll_location = findViewById(R.id.ll_location);
        ll_donation = findViewById(R.id.ll_donation);
        ll_about_us = findViewById(R.id.ll_about_us);
        ll_Property_map = findViewById(R.id.ll_Property_map);
        ll_menu = findViewById(R.id.ll_menu);
        tv_menu_text = findViewById(R.id.tv_menu_text);
        tv_menu_text.setText(Preference.getString(context, Preference.MENU_TEXT));
        ll_table_reservation = findViewById(R.id.ll_table_reservation);*/
        tv_login_logout = findViewById(R.id.tv_login_logout);
        img_cart = findViewById(R.id.img_cart);
        ll_doordash = findViewById(R.id.ll_doordash);
        tv_online_order_link = findViewById(R.id.tv_online_order_link);
        btn_online_order = findViewById(R.id.btn_online_order);
        img_catering_cart = findViewById(R.id.img_catering_cart);
        ll_catering = findViewById(R.id.ll_catering);

        if (!Preference.getString(context, Preference.DOORDASH_LINK).equalsIgnoreCase("")) {
            if (!Preference.getString(context, Preference.DOORDASH_TEXT).equalsIgnoreCase("")) {
                tv_online_order_link.setText(Preference.getString(context, Preference.DOORDASH_TEXT));
                ll_doordash.setVisibility(View.VISIBLE);

            } else {
                ll_doordash.setVisibility(View.GONE);
            }

        } else {
            ll_doordash.setVisibility(View.GONE);

        }


        ArrayList<HomeGridBean> dataSet = GetHomeApi.getSampleDataSet(context);
        recyclerView = (RecyclerView) findViewById(R.id.grid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        // ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, 0);
        ad_list = new HomeGirdRecyclerViewAdapter(dataSet, context, activity);
        recyclerView.setAdapter(ad_list);


        if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
            tv_login_logout.setText("LOGIN");
            img_cart.setVisibility(View.GONE);
        } else {
            img_cart.setVisibility(View.VISIBLE);
            tv_login_logout.setText("LOGOUT");
        }

        listner();
    }



    public void listner() {
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, YourBagActivity.class);
                startActivity(intent);
            }
        });

        /*ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SelectStoreLocationActivity.class);
                intent.putExtra("from", "homescreen");
                startActivity(intent);
                finish();
            }
        });

        ll_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                startActivity(intent);
            }
        });

        ll_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AppointmentActivity.class);
                startActivity(intent);
            }
        });

        ll_table_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TableReservationActivity.class);
                startActivity(intent);
            }
        });

        ll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MenuActivity.class);
                intent.putExtra("from", "menu");
                startActivity(intent);
            }
        });

        ll_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MenuActivity.class);
                intent.putExtra("from", "offer");
                startActivity(intent);
            }
        });
        ll_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AboutUsActivity.class);
                startActivity(intent);
            }
        });*/

        tv_login_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                } else {
                    LogoutAPI();
                }

            }
        });


        GetImageListAPI.GetImageList(context, new GetImageListAPI.GetImageAPIListner() {
            @Override
            public void OnChange() {
                ImageManipulation.loadImage(context, Preference.getString(context, Preference.LOGO), img_logo);
                ImageManipulation.loadImage(context, Preference.getString(context, Preference.BACKGROUND_IMAGE), img_bk);
            }
        });

        btn_online_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(Preference.getString(context, Preference.DOORDASH_LINK)));
                startActivity(viewIntent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void LogoutAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("user_id", Preference.getString(context, Preference.USER_ID));
        ApiCall.volleyPostApi(context, AppConstants.logout, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {

                        JSONObject jsonObject = (JSONObject) json;
                        String status = jsonObject.optString("status");
                        String msg = jsonObject.optString("msg");

                        if (status.equalsIgnoreCase("1")) {
                            Utility.dissmisProgress();
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
                                        Preference.setString(context, Preference.STORAGE_Delivery, "");
                                        Preference.setString(context, Preference.STORAGE_Pickup, "");
                                        Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                        Preference.setString(context, Preference.CATERING_CART_ID, "");

                                        Preference.setString(context, Preference.BRANCH_ID, "");
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

                                    }
                                });
                            } else {
                                Preference.setString(context, Preference.STORAGE_Delivery, "");
                                Preference.setString(context, Preference.STORAGE_Pickup, "");
                                Preference.setString(context, Preference.STORAGE_YOUR_BAG_ID, "");
                                Preference.setString(context, Preference.CATERING_CART_ID, "");

                                Preference.setString(context, Preference.BRANCH_ID, "");
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

                            }
                            //Preference.setString(context,Preference.USER_ID,"");
                            tv_login_logout.setText("LOGIN");
                            img_cart.setVisibility(View.GONE);
                            //ll_settings.setVisibility(View.GONE);
                            Intent intent = new Intent(activity, SelectStoreLocationActivity.class);
                            intent.putExtra("from", "skip");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    Utility.dissmisProgress();
                }
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

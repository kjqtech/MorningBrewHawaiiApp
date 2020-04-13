package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.CategoryAdapter;
import com.trec2go.MorningBrewHawaii.adapter.MenuItemAdapter;
import com.trec2go.MorningBrewHawaii.adapter.SubCategoryAdapter;
import com.trec2go.MorningBrewHawaii.bean.CategoryBean;
import com.trec2go.MorningBrewHawaii.bean.MenuItemBean;
import com.trec2go.MorningBrewHawaii.bean.SubCategoryBean;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuActivity extends AppCompatActivity {


    RecyclerView rv_category_list;
    ArrayList<CategoryBean> ar_list1; //array list
    public static CategoryAdapter ad_list1; //Adapter

    RecyclerView rv_sub_category_list;
    ArrayList<SubCategoryBean> ar_list3; //array list
    public static SubCategoryAdapter ad_list3; //Adapter

    RecyclerView rv_menu_list;
    ArrayList<MenuItemBean> ar_list2; //array list
    public static MenuItemAdapter ad_list2; //Adapter

    Activity activity;
    Context context;
    ImageView img_back_arrow, img_cart, img_menu_bk, img_catering_cart;

    String from;
    TextView tv_title, tv_category_name, tv_no_data_available, tv_sub_category_name, tv_error;
    LinearLayout ll_subcategory, ll_custom_order, ll_catering;
    NestedScrollView nested_scroll;

    FloatingActionButton fab;

    EditText et_custom_order;
    Button btn_add_to_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        context = this;
        activity = this;

        from = getIntent().getExtras().getString("from");
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();


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
        tv_title = findViewById(R.id.tv_title);
        tv_no_data_available = findViewById(R.id.tv_no_data_available);
        tv_category_name = findViewById(R.id.tv_category_name);
        ll_subcategory = findViewById(R.id.ll_subcategory);
//        ll_subcategory.setVisibility(View.GONE);
        tv_sub_category_name = findViewById(R.id.tv_sub_category_name);
        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_cart = findViewById(R.id.img_cart);
        img_menu_bk = findViewById(R.id.img_menu_bk);
        fab = findViewById(R.id.fab);
        ll_custom_order = findViewById(R.id.ll_custom_order);
        et_custom_order = findViewById(R.id.et_custom_order);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        img_catering_cart = findViewById(R.id.img_catering_cart);
        ll_catering = findViewById(R.id.ll_catering);
        tv_error = findViewById(R.id.tv_error);
        if (from.equalsIgnoreCase("menu")) {
            tv_title.setText(Preference.getString(context, Preference.MENU_TEXT));
        } else {
            tv_title.setText("Special Offers");
        }

        //ImageManipulation.loadImage(context,Preference.getString(context,Preference.MENU_BACKGROUND_IMAGE),img_menu_bk);
        if (Preference.getString(context, Preference.MENU_BACKGROUND_IMAGE).equalsIgnoreCase("") || Preference.getString(context, Preference.MENU_BACKGROUND_IMAGE).equalsIgnoreCase(" ") || Preference.getString(context, Preference.MENU_BACKGROUND_IMAGE).equalsIgnoreCase("null")) {
            Picasso.with(context).load(R.drawable.noimage).into(img_menu_bk);
        } else {
            Picasso.with(context).load(Preference.getString(context, Preference.MENU_BACKGROUND_IMAGE)).fit().centerCrop()./*memoryPolicy(MemoryPolicy.NO_STORE).*/placeholder(R.drawable.placeholderimage).into(img_menu_bk);
        }

        setRv_category_list();
        setRv_sub_category_list();
        setRv_menu_list();
        listner();
    }

    public void listner() {
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HomeActivity.class);
                startActivity(intent);      // $$$ navigate back to home
                finish();
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_custom_order.getText().toString().trim().equalsIgnoreCase("")){
                    tv_error.setVisibility(View.VISIBLE);
                }
                else {
                    tv_error.setVisibility(View.GONE);
                    Preference.setString(context, Preference.CUSTOM_ORDER, et_custom_order.getText().toString());
                    Intent intent = new Intent(activity, CustomOrederActivity.class);
                    //intent.putExtra("CUSTOM_ORDER", et_custom_order.getText().toString().trim());
                    //intent.putExtra("", )
                    startActivity(intent);
                }

            }
        });

        if (from.equalsIgnoreCase("menu")) {
            CategoryAPI();
        } else {
            Special_menu_CategoryAPI();
        }

    }

    public void setRv_category_list() {
        rv_category_list = findViewById(R.id.rv_category_list);
        ar_list1 = new ArrayList<CategoryBean>();
        ad_list1 = new CategoryAdapter(context, activity, ar_list1, new CategoryAdapter.OnCategoryClickListner() {
            @Override
            public void OnCategoryClick(CategoryBean categoryBean) {
                tv_category_name.setText(Html.fromHtml(Html.fromHtml(categoryBean.name).toString()));
                if (categoryBean.has_sub_category.equalsIgnoreCase("no")) {
                    ll_subcategory.setVisibility(View.GONE);
                    if (from.equalsIgnoreCase("menu")) {
                        MenuAPI(categoryBean.id, 2);
                    } else {
                        Special_menu_MenuAPI(categoryBean.id, 2);
                    }
                } else if (categoryBean.has_sub_category.equalsIgnoreCase("yes")) {
                    SubCategoryAPI(categoryBean.id);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rv_category_list.setLayoutManager(layoutManager);
        rv_category_list.setAdapter(ad_list1);
    }



    public void setRv_sub_category_list() {
        rv_sub_category_list = findViewById(R.id.rv_sub_category_list);
        ar_list3 = new ArrayList<SubCategoryBean>();
        ad_list3 = new SubCategoryAdapter(context, activity, ar_list3, new SubCategoryAdapter.OnSubCategoryClickListner() {
            @Override
            public void OnSubCategoryClick(SubCategoryBean subCategoryBean) {
                if (from.equalsIgnoreCase("menu")) {
                    MenuAPI(subCategoryBean.id, 2);
                } else {
                    Special_menu_MenuAPI(subCategoryBean.id, 2);
                }
                tv_sub_category_name.setText(Html.fromHtml(Html.fromHtml(subCategoryBean.name).toString()));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rv_sub_category_list.setLayoutManager(layoutManager);
        rv_sub_category_list.setAdapter(ad_list3);

    }

    public void setRv_menu_list() {
        rv_menu_list = findViewById(R.id.rv_menu_list);
        ar_list2 = new ArrayList<MenuItemBean>();
        ad_list2 = new MenuItemAdapter(context, activity, ar_list2, "0");
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(activity);
        rv_menu_list.setLayoutManager(layoutManager2);
        rv_menu_list.setAdapter(ad_list2);
    }

    //API Calling
    public void CategoryAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        Utility.log("GLOBAL_UNAME====" + Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        ApiCall.volleyPostApi(context, AppConstants.category_list, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ar_list1.clear();
                    ar_list1.addAll((Collection<? extends CategoryBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<CategoryBean>>() {
                    }.getType()));
                    ad_list1.updateList(ar_list1);
                    //Utility.dissmisProgress();
                    if (!ar_list1.isEmpty()) {
                        /*Preference.setString(context,Preference.STORAGE_CATEGORY,ar_list1.get(0).toString());*/


                        rv_category_list.setVisibility(View.VISIBLE);
                        rv_sub_category_list.setVisibility(View.VISIBLE);
                        rv_menu_list.setVisibility(View.VISIBLE);
                        tv_no_data_available.setVisibility(View.GONE);

                        if(ar_list1.get(0).has_sub_category.equalsIgnoreCase("yes")){
                            SubCategoryAPI(ar_list1.get(0).id);
                        }else {
                            tv_sub_category_name.setVisibility(View.GONE);
                            MenuAPI(ar_list1.get(0).id, 1);
                        }
                        tv_category_name.setText(ar_list1.get(0).name);

                    } else {
                        tv_category_name.setVisibility(View.GONE);
                        tv_sub_category_name.setVisibility(View.GONE);
                        tv_no_data_available.setVisibility(View.VISIBLE);
                        rv_category_list.setVisibility(View.GONE);
                        rv_sub_category_list.setVisibility(View.GONE);
                        rv_menu_list.setVisibility(View.GONE);
                        Utility.dissmisProgress();
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

    public void SubCategoryAPI(String cat_id) {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("cat_id", cat_id);

        ApiCall.volleyPostApi(context, AppConstants.sub_category_list, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ar_list3.clear();
                    ar_list3.addAll((Collection<? extends SubCategoryBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<SubCategoryBean>>() {
                    }.getType()));
                    ad_list3.updateList(ar_list3);
                    if (!ar_list3.isEmpty()) {
                        ll_subcategory.setVisibility(View.VISIBLE);
                        rv_sub_category_list.setVisibility(View.VISIBLE);
                        rv_menu_list.setVisibility(View.VISIBLE);
                        tv_no_data_available.setVisibility(View.GONE);
                        MenuAPI(ar_list3.get(0).id, 1);
                        tv_sub_category_name.setText(ar_list3.get(0).name);

                    } else {
                        ll_subcategory.setVisibility(View.GONE);
                        tv_sub_category_name.setVisibility(View.GONE);
                        tv_no_data_available.setVisibility(View.VISIBLE);
                        rv_sub_category_list.setVisibility(View.GONE);
                        rv_menu_list.setVisibility(View.GONE);
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

    public void Special_menu_CategoryAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        ApiCall.volleyPostApi(context, AppConstants.special_choose_category, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ar_list1.clear();
                    ar_list1.addAll((Collection<? extends CategoryBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<CategoryBean>>() {
                    }.getType()));
                    ad_list1.updateList(ar_list1);
                    Utility.dissmisProgress();
                    if (!ar_list1.isEmpty()) {
                        /*Preference.setString(context,Preference.STORAGE_CATEGORY,ar_list1.get(0).toString());*/
                        // MenuAPI(ar_list1.get(0).id,1);
                        rv_category_list.setVisibility(View.VISIBLE);
                        rv_sub_category_list.setVisibility(View.VISIBLE);
                        rv_menu_list.setVisibility(View.VISIBLE);
                        tv_no_data_available.setVisibility(View.GONE);

                        Special_menu_MenuAPI(ar_list1.get(0).id, 1);
                        tv_category_name.setText(ar_list1.get(0).name);
                    } else {
                        //        Utility.dissmisProgress();    $$$ dissmiss progress bar
                        tv_no_data_available.setVisibility(View.VISIBLE);
                        rv_category_list.setVisibility(View.GONE);
                        rv_sub_category_list.setVisibility(View.GONE);
                        rv_menu_list.setVisibility(View.GONE);
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

    public void MenuAPI(String id, int status) {
        //status 1=>Dont show progress 2=>show progress
        if (status == 1) {

        } else if (status == 2) {
            Utility.createProgressDialoge(context, "Loading");
        }

        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("cat_id", id);
        ApiCall.volleyPostApi(context, AppConstants.menu_list, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ar_list2.clear();
                    ar_list2.addAll((Collection<? extends MenuItemBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<MenuItemBean>>() {
                    }.getType()));
                    ad_list2.updateList(ar_list2);
                    if (ar_list2.isEmpty()) {
                        if (tv_category_name.getText().toString().equalsIgnoreCase("Custom Order")) {
                            ll_custom_order.setVisibility(View.VISIBLE);
                            tv_no_data_available.setVisibility(View.GONE);

                        } else {
                            ll_custom_order.setVisibility(View.GONE);
                            tv_no_data_available.setVisibility(View.VISIBLE);
                        }

                    } else {
                        ll_custom_order.setVisibility(View.GONE);
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

    public void Special_menu_MenuAPI(String id, int status) {
        //status 1=>Dont show progress 2=>show progress
        if (status == 1) {

        } else if (status == 2) {
            Utility.createProgressDialoge(context, "Loading");
        }

        String url = "";
        if (id.equalsIgnoreCase("0")) {
            url = AppConstants.todays_special_category;
        } else {
            url = AppConstants.special_menu;
        }

        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("cat_id", id);
        ApiCall.volleyPostApi(context, url, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ar_list2.clear();
                    ar_list2.addAll((Collection<? extends MenuItemBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<MenuItemBean>>() {
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

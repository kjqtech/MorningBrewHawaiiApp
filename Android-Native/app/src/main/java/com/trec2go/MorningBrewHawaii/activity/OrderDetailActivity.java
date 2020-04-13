package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.ExpandableListAdapter;
import com.trec2go.MorningBrewHawaii.bean.AddToCartBean;
import com.trec2go.MorningBrewHawaii.bean.MenuItemBean;
import com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class.menu_options;
import com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class.options;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.ImageManipulation;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderDetailActivity extends AppCompatActivity {

    Activity activity;
    Context context;
    ImageView img_back_arrow, img_cart;



    //Increament Counter
    int counter = 1;
    TextView tv_btn_plus, tv_counter,tv_soldout,tv_btn_minus;
    LinearLayout ll_show_minus, ll_add, ll_catering;


    //item Detail
    TextView tv_item_name, tv_item_price, tv_item_discount, tv_description;
    TextView tv_total;
    EditText edit_text;
    Button btn_add_to_cart;


    //Expandable List Veiw
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    List<menu_options> listDataHeader;
    HashMap<menu_options, List<options>> listDataChild;

    //OrderDetail Bean
    MenuItemBean menuItemBean;
    MenuItemBean advancedmenuItemBean;

    ImageView img_order, img_catering_cart;
    String item_discount = "0";


    double total = 0; //total value
    double actual_total = 0; //per item total
    double actual_quantity_total = 0; //item total with total quantity
    double modifier_total = 0; //modifier total
    double final_total = 0;

    DecimalFormat df2 = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        context = this;
        activity = this;

        menuItemBean = getIntent().getExtras().getParcelable("menuItemBean");

        View footer = getLayoutInflater().inflate(R.layout.expandable_footer, expListView, false); //$$$ set header in expandableview
        View header = getLayoutInflater().inflate(R.layout.expandeble_header, expListView, false);  // $$$ change name view to footer


        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_order = header.findViewById(R.id.img_order);
        img_order.setVisibility(View.INVISIBLE);
        tv_item_discount = header.findViewById(R.id.tv_item_discount);
        tv_description = header.findViewById(R.id.tv_description);
        img_cart = findViewById(R.id.img_cart);
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, YourBagActivity.class);
                startActivity(intent);
                finish();
            }
        });

        img_catering_cart = findViewById(R.id.img_catering_cart);
        ll_catering = findViewById(R.id.ll_catering);

        ll_add = header.findViewById(R.id.ll_add);
        tv_counter = header.findViewById(R.id.tv_counter);
        tv_btn_minus = header.findViewById(R.id.tv_btn_minus);
        tv_btn_plus = header.findViewById(R.id.tv_btn_plus);
        ll_show_minus = header.findViewById(R.id.ll_show_minus);

        tv_item_name = header.findViewById(R.id.tv_item_name);
        tv_item_price = header.findViewById(R.id.tv_item_price);
        tv_soldout = header.findViewById(R.id.tv_soldout);


        // LinearLayout footerLayout = (LinearLayout) view.findViewById(R.id.footer_layout); // $$$ Comment code
        tv_total = footer.findViewById(R.id.tv_total);
        edit_text = footer.findViewById(R.id.edit_text);
        edit_text.setText("");


        updateCounter();

        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                updateCounter();
                updateTotal();
            }
        });

        tv_btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                updateCounter();
                updateTotal();
            }
        });

        ll_show_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCounter();
            }
        });

        tv_btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                updateCounter();
                updateTotal();
            }
        });

        //Expandable list
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.exp_list);

        // preparing list data
        //prepareListData();
        listDataHeader = new ArrayList<menu_options>();
        listDataChild = new HashMap<menu_options, List<options>>();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, new ExpandableListAdapter.OnModifiersClickListner() {

            @Override
            public String onCheckBoxSelected(List<menu_options> _listDataHeader, int groupPosition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition) {
                return modifierUpdate(_listDataHeader, groupPosition, options, listDataChild, childPosition);
            }

            @Override
            public String onCheckBoxUnSelected(List<menu_options> _listDataHeader, int groupPosition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition) {
                return modifierUpdate(_listDataHeader, groupPosition, options, listDataChild, childPosition);
            }

            @Override
            public String onQuantityAdded(List<menu_options> _listDataHeader, int groupPosition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition) {
                return modifierUpdate(_listDataHeader, groupPosition, options, listDataChild, childPosition);
            }

            @Override
            public String onQuantityRemove(List<menu_options> _listDataHeader, int groupPosition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition) {
                return modifierUpdate(_listDataHeader, groupPosition, options, listDataChild, childPosition);
            }

            @Override
            public String onRefreshDefault(List<menu_options> _listDataHeader, int groupposition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition) {
                // return modifierDefaultUpdate(_listDataHeader,listDataChild);
                return "";
            }
        });


         btn_add_to_cart = footer.findViewById(R.id.btn_add_to_cart);  //$$$ change view to footer.findviewbyid
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listDataHeader.size() != 0) {
                    for (int i = 0; i < listDataHeader.size(); i++) {
                        if (listDataHeader.get(i).compulsory.equalsIgnoreCase("Y")) {
                            int is_modifier_compalsory = 0;

                            for (int j = 0; j < listDataChild.get(listDataHeader.get(i)).size(); j++) {
                                if (listDataChild.get(listDataHeader.get(i)).get(j).isChecked()) {
                                    break;
                                } else {
                                    is_modifier_compalsory++;
                                }
                            }
/*
                            if (i < listDataHeader.size()) {
*/
                                if (is_modifier_compalsory == listDataChild.get(listDataHeader.get(i)).size()) {
                                    com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Please Select Minimum " + listDataHeader.get(i).optionallow + " " + listDataHeader.get(i).topname, new DialogCallBackListner() {
                                        @Override
                                        public void setPositiveListner() {
                                            //Cliked Ok
                                        }

                                        @Override
                                        public void setNegativeListner() {

                                        }
                                    });
                                    break;
                                } else {
                                    if (i == listDataHeader.size() -1) {
                                        GoToYourBag();

                                    }
                                }
                          /*  }*/
                        } else if (i == listDataHeader.size() -1) {
                            GoToYourBag();
                        }

                    }

                } else {
                   GoToYourBag();

                }


                /*if(Preference.getString(context,Preference.STORAGE_YOUR_BAG).equalsIgnoreCase("")){
                    ArrayList<MenuItemBean> list = new ArrayList<>();
                    list.add(menuItemBean);
                    String data = new Gson().toJson(list);
                    Preference.setString(context,Preference.STORAGE_YOUR_BAG,data);
                }else {
                    ArrayList<MenuItemBean> list = new ArrayList<>();
                    String cart_storage = Preference.getString(context,Preference.STORAGE_YOUR_BAG);
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(cart_storage);
                        list.clear();
                        list.addAll((Collection<? extends MenuItemBean>) new Gson().fromJson(String.valueOf(jsonArray), new TypeToken<ArrayList<MenuItemBean>>() {}.getType()));
                        list.add(menuItemBean);
                        String data = new Gson().toJson(list);
                        Preference.setString(context,Preference.STORAGE_YOUR_BAG,data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/

            }
        });
        expListView.addHeaderView(header); //$$$ add header in expandableview
        expListView.addFooterView(footer);


        // setting list adapter
        expListView.setAdapter(listAdapter);


        expListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // disallow the onTouch for your scrollable parent view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                /*if (groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;*/
            }
        });

        MenuDetailAPI();

    }

    private void GoToYourBag(){
        if (counter == 0) {
            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Quantity Should not be 0", new DialogCallBackListner() {
                @Override
                public void setPositiveListner() {
                    //Cliked Ok
                }

                @Override
                public void setNegativeListner() {

                }
            });
        } else {
            if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
                AddToCartBean addToCartBean = new AddToCartBean();
                addToCartBean.menu_id = menuItemBean.id;
                Log.e("menu_id", menuItemBean.id);
                addToCartBean.quantity = String.valueOf(counter);
                addToCartBean.menuadd = "";
                addToCartBean.item_discount = item_discount;
                addToCartBean.choose_sides = "";
                addToCartBean.main_menu = "Y";
                addToCartBean.branch_id = Preference.getString(context, Preference.BRANCH_ID);
                addToCartBean.amount = df2.format(final_total);
                addToCartBean.gruname = Preference.getString(context, Preference.GLOBAL_UNAME);
                addToCartBean.advance_menu = listAdapter.getAdvancedmenu();
                addToCartBean.advance_menu_qty = listAdapter.getAdvancedmenuQuantity();
                addToCartBean.advance_menu_extracharge = listAdapter.getExtraCharge();
                addToCartBean.special_requests = edit_text.getText().toString();
                addToCartBean.is_catering = menuItemBean.is_catering;
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.putExtra("from", "Order_Detail");
                intent.putExtra("addToCartBean", addToCartBean);
                startActivity(intent);
                finish();
            } else {
                AddToCartAPI();
            }


        }
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

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void updateCounter() {

        if (counter <= 0) {
            ll_add.setVisibility(View.VISIBLE);
            ll_show_minus.setVisibility(View.GONE);
        } else {
            ll_add.setVisibility(View.GONE);
            ll_show_minus.setVisibility(View.VISIBLE);
            tv_counter.setText(String.valueOf(counter));
        }

    }


    /*
     * Preparing the list data
     */
    /*private void prepareListData() {


        // Adding child data
        listDataHeader.add(new OrderTitleBean("Entree Side Cheese"));
        listDataHeader.add(new OrderTitleBean("Sauce"));
        listDataHeader.add(new OrderTitleBean("Sizzling Powder"));

        // Adding child data
        List<OrderModifierBean> top250 = new ArrayList<OrderModifierBean>();
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());
        top250.add(new OrderModifierBean());



        List<OrderModifierBean> nowShowing = new ArrayList<OrderModifierBean>();
        nowShowing.add(new OrderModifierBean());
        nowShowing.add(new OrderModifierBean());
        nowShowing.add(new OrderModifierBean());
        nowShowing.add(new OrderModifierBean());
        nowShowing.add(new OrderModifierBean());
        nowShowing.add(new OrderModifierBean());
        nowShowing.add(new OrderModifierBean());
        nowShowing.add(new OrderModifierBean());



        List<OrderModifierBean> comingSoon = new ArrayList<OrderModifierBean>();
        comingSoon.add(new OrderModifierBean());
        comingSoon.add(new OrderModifierBean());
        comingSoon.add(new OrderModifierBean());
        comingSoon.add(new OrderModifierBean());
        comingSoon.add(new OrderModifierBean());
        comingSoon.add(new OrderModifierBean());
        comingSoon.add(new OrderModifierBean());
        comingSoon.add(new OrderModifierBean());

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }*/

    //Get Menu Detail API
    public void MenuDetailAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("menu_id", menuItemBean.id);
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        ApiCall.volleyPostApi(context, AppConstants.menu_details, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Utility.log(String.valueOf(jsonArray.length()));
                    if (jsonArray != null) {
                        Gson gson = new Gson();
                        //Utility.log_api_response("Before => " + menuItemBean.toString());
                        menuItemBean = gson.fromJson(jsonArray.getJSONObject(0).toString(), MenuItemBean.class);
                        //Utility.log_api_response("After => " + menuItemBean.toString());
                        tv_item_name.setText((Html.fromHtml(Html.fromHtml( (menuItemBean.menu_title)).toString())));
                        tv_item_price.setText("$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(menuItemBean.menu_price)));
                        tv_total.setText("$" + Validation.roundOffTo2DecPlaces(Double.parseDouble(menuItemBean.menu_price)));
                        total = Double.parseDouble(menuItemBean.menu_price);
                        actual_total = Double.parseDouble(menuItemBean.menu_price);
                        actual_quantity_total = Double.parseDouble(menuItemBean.menu_price);

                        if (!menuItemBean.menu_description.equalsIgnoreCase("")) {
                            tv_description.setVisibility(View.VISIBLE);
                            tv_description.setText(android.text.Html.fromHtml(menuItemBean.menu_description).toString());

                        }

                        if (menuItemBean.actual_price.equalsIgnoreCase(menuItemBean.menu_price)) {
                            tv_item_price.setText("$" + menuItemBean.menu_price);
                            tv_item_discount.setVisibility(View.GONE);
                            item_discount = "0";
                        } else {
                            tv_item_discount.setVisibility(View.VISIBLE);
                            tv_item_discount.setText("$" + menuItemBean.actual_price);
                            tv_item_price.setText("$" + menuItemBean.menu_price);
                            tv_item_discount.setPaintFlags(tv_item_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            item_discount = "1";

                        }

                        if(menuItemBean.soldstatus.equalsIgnoreCase("1")){
                            tv_soldout.setVisibility(View.VISIBLE);
                            btn_add_to_cart.setVisibility(View.GONE);
                        }else {
                            tv_soldout.setVisibility(View.GONE);
                            btn_add_to_cart.setVisibility(View.VISIBLE);
                        }



                        if (jsonArray.length() > 1) {
                            advancedmenuItemBean = gson.fromJson(jsonArray.getJSONObject(1).toString(), MenuItemBean.class);
                            listDataHeader = advancedmenuItemBean.advance_menu.Menue.menu_options;
                            listDataChild = new HashMap<menu_options, List<options>>();

                            for (int i = 0; i < listDataHeader.size(); i++) {
                                List<options> options = new ArrayList<options>();
                                options = listDataHeader.get(i).options;
                                listDataChild.put(listDataHeader.get(i), options); // Header, Child data
                            }

                            listAdapter.updatelist(listDataHeader, listDataChild);
                        } else {
                            listDataHeader.clear();
                            listDataChild.clear();
                            listAdapter.updatelist(listDataHeader, listDataChild);
                        }


                        if (!menuItemBean.menu_image.equalsIgnoreCase("")) {
                            img_order.setVisibility(View.VISIBLE);
                            ImageManipulation.loadImage(context, menuItemBean.menu_image, img_order);
                        } else {
                            img_order.setVisibility(View.INVISIBLE);
                        }

                        modifierDefaultUpdate(listDataHeader, listDataChild);

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

    //Add to Cart API
    public void AddToCartAPI() {
        /*Utility.log_api_failure(listAdapter.getAdvancedmenu());
        Utility.log_api_failure(listAdapter.getAdvancedmenuQuantity());
        Utility.log_api_failure(listAdapter.getExtraCharge());*/

        Utility.createProgressDialoge(context, "Loading");
        final Map<String, String> map = new HashMap<>();
        map.put("menu_id", menuItemBean.id);
        map.put("user_id", Preference.getString(context, Preference.USER_ID));
        map.put("quantity", String.valueOf(counter));
        map.put("item_discount", item_discount);
        map.put("menuadd", "");
        map.put("choose_sides", "");
        map.put("main_menu", "Y");
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        map.put("amount", df2.format(final_total));
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("advance_menu", listAdapter.getAdvancedmenu());
        map.put("advance_menu_qty", listAdapter.getAdvancedmenuQuantity());
        map.put("advance_menu_extracharge", listAdapter.getExtraCharge());
        map.put("special_requests", edit_text.getText().toString());
        ApiCall.volleyPostApi(context, AppConstants.add_to_cart, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                if (menuItemBean.is_catering.equalsIgnoreCase("0")) {
                    Preference.setString(context, Preference.CATERING_STATUS, "0");
                    Log.e("STORAGE_YOUR_BAG_ID", "STORAGE_YOUR_BAG_ID");

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
                                    finish();
                                } else {
                                    Intent intent = new Intent(activity, MenuActivity.class);
                                    intent.putExtra("from", "Menu");
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                        Utility.dissmisProgress();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.dissmisProgress();
                    }

                } else {
                    Log.e("CATERING_CART_ID", "CATERING_CART_ID");
                    Preference.setString(context, Preference.CATERING_STATUS, "1");

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Utility.log_api_request(String.valueOf(map));
                        Utility.log(response);
                        if (jsonArray != null) {
                            String order_id = jsonArray.getJSONObject(0).getString("order_id");
                            String order_ids = Preference.getString(context, Preference.CATERING_CART_ID);

                            if (!order_id.equalsIgnoreCase("")) {
                                if (!order_ids.equalsIgnoreCase("")) {
                                    JSONArray jsonArray1 = new JSONArray(order_ids);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("order_id", order_id);
                                    jsonArray1.put(jsonObject);
                                    Preference.setString(context, Preference.CATERING_CART_ID, jsonArray1.toString());
                                } else {
                                    JSONArray jsonArray1 = new JSONArray();
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("order_id", order_id);
                                    jsonArray1.put(jsonObject);
                                    Preference.setString(context, Preference.CATERING_CART_ID, jsonArray1.toString());
                                }

                                if (Preference.getString(context, Preference.USER_ID).equalsIgnoreCase("")) {
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    intent.putExtra("from", "Order_Detail");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(activity, MenuActivity.class);
                                    intent.putExtra("from", "Menu");
                                    startActivity(intent);
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

    public void updateTotal() {
        actual_quantity_total = actual_total * counter;
        final_total = actual_quantity_total + modifier_total;
        tv_total.setText("$" + Validation.roundOffTo2DecPlaces(final_total));
    }

    public String modifierUpdate(List<menu_options> _listDataHeader, int groupPosition, options options, HashMap<menu_options, List<options>> _listDataChild, int childPosition) {
        if (!options.price.equalsIgnoreCase("")) {
            double price = Double.parseDouble(options.price);
            int quantiy = options.counter;
            if (quantiy != 0) {
                price = price * quantiy;
            } else {
                price = 0;
            }

            int current_option_allow = 0;


            if (actual_total != 0) {
                double total = 0;
                for (int i = 0; i < _listDataHeader.size(); i++) {
                    for (int j = 0; j < _listDataChild.get(_listDataHeader.get(i)).size(); j++) {
                        if (listDataChild.get(_listDataHeader.get(i)).get(j).isChecked) {

                            if (groupPosition == i) {
                                current_option_allow++;
                            }

                            if (groupPosition == i && childPosition == j) {
                                total = total + price;
                            } else {
                                if (!listDataChild.get(listDataHeader.get(i)).get(j).price.equalsIgnoreCase("")) {
                                    double single_item_price = Double.parseDouble(listDataChild.get(listDataHeader.get(i)).get(j).price);
                                    int single_item_quantiy = listDataChild.get(listDataHeader.get(i)).get(j).counter;
                                    if (single_item_quantiy != 0) {
                                        single_item_price = single_item_price * single_item_quantiy;
                                    } else {
                                        single_item_price = 0;
                                    }
                                    total = total + single_item_price;
                                } else {

                                }
                            }

                        } else {

                        }

                    }
                }


                Utility.log("Option Allow => " + listDataHeader.get(groupPosition).optionallow);
                Utility.log("Current Option Allow => " + listDataHeader.get(groupPosition).currentoptionallow);
                Utility.log("Current Option Allow New => " + current_option_allow);

                modifier_total = total;
                updateTotal();
            }
            return String.valueOf(current_option_allow);
        } else {
            int current_option_allow = 0;

            for (int i = 0; i < _listDataHeader.size(); i++) {
                for (int j = 0; j < _listDataChild.get(_listDataHeader.get(i)).size(); j++) {
                    if (listDataChild.get(_listDataHeader.get(i)).get(j).isChecked) {

                        if (groupPosition == i) {
                            current_option_allow++;
                        }
                    } else {

                    }

                }
            }
            return String.valueOf(current_option_allow);
        }

    }

    public String modifierDefaultUpdate(List<menu_options> _listDataHeader, HashMap<menu_options, List<options>> _listDataChild) {
        double total = 0;
        for (int i = 0; i < _listDataHeader.size(); i++) {
            for (int j = 0; j < _listDataChild.get(_listDataHeader.get(i)).size(); j++) {

                if (_listDataHeader.get(i).default_.equalsIgnoreCase("Y")) {
                    _listDataChild.get(_listDataHeader.get(i)).get(j).setChecked(true);
                    _listDataChild.get(_listDataHeader.get(i)).get(j).setCounter(1);
                }

                if (listDataChild.get(_listDataHeader.get(i)).get(j).isChecked) {
                    if (!listDataChild.get(listDataHeader.get(i)).get(j).price.equalsIgnoreCase("")) {
                        double single_item_price = Double.parseDouble(listDataChild.get(listDataHeader.get(i)).get(j).price);
                        int single_item_quantiy = listDataChild.get(listDataHeader.get(i)).get(j).counter;
                        if (single_item_quantiy != 0) {
                            single_item_price = single_item_price * single_item_quantiy;
                        } else {
                            single_item_price = 0;
                        }
                        total = total + single_item_price;
                    } else {

                    }

                }
            }
            modifier_total = total;
        }
        updateTotal();
        return String.valueOf("");
    }

}

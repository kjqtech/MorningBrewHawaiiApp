package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

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
import java.util.Optional;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.adapter.YourBagItemAdapter;
import com.trec2go.MorningBrewHawaii.bean.YourBagBean;
import com.trec2go.MorningBrewHawaii.commonapicall.GetHomeApi;
import com.trec2go.MorningBrewHawaii.commonapicall.RemoveItemFromCart;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Config;
import com.trec2go.MorningBrewHawaii.utility.GooglePay.GooglePay;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReviewOrderActivity extends AppCompatActivity {

    Activity activity;
    Context context;

    Spinner spn_schedule_order;
    ImageView img_back_arrow;
    Button btn_continue;

    //Specific Time
    LinearLayout ll_add_time, ll_add_date;
    EditText edt_time, edt_date;

    LinearLayout ll_add_item;
    RelativeLayout rl_cashPrograme;

    RecyclerView rv_list;
    ArrayList<YourBagBean> ar_list; //array list
    public static YourBagItemAdapter ad_list; //Adapter


    int hour;
    int minute;
    public static String catering_time = "";
    public static String catering_date = "";


    //String item_total, tax, surcharge, order_type, delivery = "", tips_total = "", total, taxPercentage, special_discount_heading, special_discount_amount, sdType, sdAmount, totalsd, globalPromocodeDiscountType, globalPromocodeDiscount, globalPromocode, promocode_discount, promocode_heading, first_discount_heading, first_discount_ammont, firstordertype, discountvalue;
    TextView tv_item_total, tv_sales_tax, tv_sales_tax_title, tv_surcharge_amount, tv_delivery_charge, tv_tip, tv_total, tv_special_iscount_heading, tv_special_discount_amount, tv_promocode_discount, tv_promocode_heading, tv_first_discount_heading, tv_first_discount_ammont, tv_delivery_address, tv_specific_time_lable;
    LinearLayout ll_delivery, ll_delivery_address;
    AlertDialog dialog;
    EditText edt_special_note;
    View dialogLayout;
    RelativeLayout rl_surcharge_amount, rl_special_discount, rl_promocode, rl_first_discount, rl_sales_tax, rl_tip, rl_delivery_charge, rl_catering_time;
    final int reviwe_index = 0;

    //String is_special_applied, is_first_discount_applied;

    boolean is_asap = true;


    /* Google Pay =========================================================================================================

         /**
       * A client for interacting with the Google Pay API
       *
       * @see <a
       *     href="https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient">PaymentsClient</a>
       */
    private PaymentsClient mPaymentsClient;

    /**
     * A Google Pay payment button presented to the viewer for interaction
     *
     * @see <a href="https://developers.google.com/pay/api/android/guides/brand-guidelines">Google Pay
     * payment button brand guidelines</a>
     */
    private View mGooglePayButton;

    /* A constant integer you define to track a request for payment data activity */


    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 42;

    /*============================================================================================================================================== */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);

        activity = this;
        context = this;
        GetHomeApi.GetHome(context);
        faxCheckApi();


        //Bundle bundle = getIntent().getExtras();
        /*item_total = bundle.getString("item_total");
        surcharge = bundle.getString("surcharge");
        special_discount_amount = bundle.getString("special_discount_amount");
        special_discount_heading = bundle.getString("special_discount_heading");
        sdAmount = bundle.getString("sdAmount");
        sdType = bundle.getString("sdType");
        totalsd = bundle.getString("totlesd");

        is_first_discount_applied = bundle.getString("is_first_discount_applied");
        first_discount_heading = bundle.getString("first_discount_heading");
        first_discount_ammont = bundle.getString("first_discount_ammont");
        firstordertype = bundle.getString("firstordertype");
        discountvalue = bundle.getString("discountvalue");


        globalPromocodeDiscountType = bundle.getString("globalPromocodeDiscountType");

        globalPromocodeDiscount = bundle.getString("globalPromocodeDiscount");

        globalPromocode = bundle.getString("globalPromocode");


        promocode_discount = bundle.getString("promocode_discount");
        promocode_heading = bundle.getString("promocode_heading");

        is_special_applied = bundle.getString("is_special_applied");

        tax = bundle.getString("tax");       //$$$ add tax and tax percentage
        taxPercentage = bundle.getString("taxpercentage");
        order_type = bundle.getString("order_type");


        total = bundle.getString("total");
        if (order_type.equalsIgnoreCase("1")) {
            //delivery
            delivery = bundle.getString("delivery");
            tips_total = bundle.getString("tips_total");
        } else if (order_type.equalsIgnoreCase("2")) {
            //pickup

        ar_list = bundle.getParcelableArrayList("list");
        }*/
        ar_list = YourBagActivity.ad_list.getList();

        init();

        mPaymentsClient =
                Wallet.getPaymentsClient(
                        activity,
                        new Wallet.WalletOptions.Builder()
                                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void init() {
        Double totle = Double.parseDouble(YourBagActivity.review_list.get(0).getDelivery().substring(1)) +  Double.parseDouble(YourBagActivity.review_list.get(0).getTotal().substring(1));
        YourBagActivity.review_list.get(0).setTotal("$" + Validation.roundOffTo2DecPlaces(totle));

        img_back_arrow = findViewById(R.id.img_back_arrow);
        btn_continue = findViewById(R.id.btn_continue);

        edt_special_note = findViewById(R.id.edt_special_note);
        tv_item_total = findViewById(R.id.tv_item_total);
        tv_sales_tax = findViewById(R.id.tv_sales_tax);
        tv_sales_tax_title = findViewById(R.id.tv_sales_tax_title);
        rl_surcharge_amount = findViewById(R.id.rl_surcharge_amount);
        tv_surcharge_amount = findViewById(R.id.tv_surcharge_amount);
        rl_special_discount = findViewById(R.id.rl_special_discount);
        tv_special_discount_amount = findViewById(R.id.tv_special_discount_ammont);
        tv_special_iscount_heading = findViewById(R.id.tv_special_discount_heading);

        rl_cashPrograme = findViewById(R.id.rl_cashPrograme);
        List<String> payment_method_list = Arrays.asList(Preference.getString(context, Preference.PAYMENT_METHOD).split("\\s*,\\s*"));
        for (int i = 0; i < payment_method_list.size(); i++) {
            if (payment_method_list.get(i).equalsIgnoreCase("cash_on")) {
                //btn_payment_pickup.setVisibility(View.VISIBLE);
            } else if (payment_method_list.get(i).equalsIgnoreCase("authorize_net")) {
                //btn_debit_credit.setVisibility(View.VISIBLE);
            } else if (payment_method_list.get(i).equalsIgnoreCase("cashprogram")) {
                rl_cashPrograme.setVisibility(View.VISIBLE);
                //btn_debit_credit.setVisibility(View.VISIBLE);
            }
        }


        rl_first_discount = findViewById(R.id.rl_first_discount);
        tv_first_discount_heading = findViewById(R.id.tv_first_discount_heading);
        tv_first_discount_ammont = findViewById(R.id.tv_first_discount_ammont);


        rl_promocode = findViewById(R.id.rl_promocode);
        tv_promocode_discount = findViewById(R.id.tv_promocode_discount);
        tv_promocode_heading = findViewById(R.id.tv_promocode_heading);

        tv_delivery_charge = findViewById(R.id.tv_delivery_charge);
        tv_tip = findViewById(R.id.tv_tip);
        rl_tip = findViewById(R.id.rl_tip);
        tv_total = findViewById(R.id.tv_total);
        ll_delivery = findViewById(R.id.ll_delivery);

        ll_delivery_address = findViewById(R.id.ll_delivery_address);
        tv_delivery_address = findViewById(R.id.tv_delivery_address);

        rl_sales_tax = findViewById(R.id.rl_sales_tax);
        rl_delivery_charge = findViewById(R.id.rl_delivery_charge);

        tv_specific_time_lable = findViewById(R.id.tv_specific_time_lable);
        rl_catering_time = findViewById(R.id.rl_catering_time);


        tv_item_total.setText(YourBagActivity.review_list.get(reviwe_index).getItem_totle());

        if (YourBagActivity.review_list.get(reviwe_index).getText_percentage().equalsIgnoreCase("$0.0")) {
            rl_sales_tax.setVisibility(View.GONE);
        } else {
            rl_sales_tax.setVisibility(View.VISIBLE);
            tv_sales_tax_title.setText("Sales Tax(" + YourBagActivity.review_list.get(reviwe_index).getTax() + "%)");
            tv_sales_tax.setText(YourBagActivity.review_list.get(reviwe_index).getText_percentage());
        }

        //$$$set tax to texpercentage
        if (YourBagActivity.review_list.get(reviwe_index).getSurcharge().equalsIgnoreCase("$0.00")) {
            rl_surcharge_amount.setVisibility(View.GONE);
        } else {
            rl_surcharge_amount.setVisibility(View.VISIBLE);
            tv_surcharge_amount.setText(YourBagActivity.review_list.get(reviwe_index).getSurcharge());
        }

        if (YourBagActivity.review_list.get(reviwe_index).getIs_special_applied().equalsIgnoreCase(Config.TAG_FAILURE)) {
            rl_special_discount.setVisibility(View.GONE);
        } else {
            rl_special_discount.setVisibility(View.VISIBLE);
            tv_special_iscount_heading.setText(YourBagActivity.review_list.get(reviwe_index).getSpecial_discount_heading());
            tv_special_discount_amount.setText(YourBagActivity.review_list.get(reviwe_index).getSpecial_discount_amount());
        }
        if (YourBagActivity.review_list.get(reviwe_index).getIs_first_discount_applied().equalsIgnoreCase("0")) {
            rl_first_discount.setVisibility(View.GONE);
        } else {
            rl_first_discount.setVisibility(View.VISIBLE);
            tv_first_discount_heading.setText(YourBagActivity.review_list.get(reviwe_index).getFirst_discount_heading());
            tv_first_discount_ammont.setText(YourBagActivity.review_list.get(reviwe_index).getFirst_discount_ammont());
        }
        if (!YourBagActivity.review_list.get(reviwe_index).getPromocode_discount().equalsIgnoreCase("") || !YourBagActivity.review_list.get(reviwe_index).getPromocode_heading().equalsIgnoreCase("")) {
            rl_promocode.setVisibility(View.VISIBLE);
            tv_promocode_heading.setText(YourBagActivity.review_list.get(reviwe_index).getPromocode_heading());
            tv_promocode_discount.setText(YourBagActivity.review_list.get(reviwe_index).getPromocode_discount());
        } else {
            rl_promocode.setVisibility(View.GONE);
        }
        if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
            ll_delivery_address.setVisibility(View.VISIBLE);
            tv_delivery_address.setVisibility(View.VISIBLE);
            tv_delivery_address.setText(YourBagActivity.review_list.get(reviwe_index).getDeliveryApartment() + " , " +YourBagActivity.review_list.get(reviwe_index).getDeliveryStreetAddress() + " , " + YourBagActivity.review_list.get(reviwe_index).getDeliverycity_state_zip());
            if (YourBagActivity.review_list.get(reviwe_index).getDelivery().equalsIgnoreCase("$0.00")) {
                rl_delivery_charge.setVisibility(View.GONE);
            } else {
                rl_delivery_charge.setVisibility(View.VISIBLE);
                tv_delivery_charge.setText(YourBagActivity.review_list.get(reviwe_index).getDelivery());
            }

            if (YourBagActivity.review_list.get(reviwe_index).getTips_totle().equalsIgnoreCase("$0.00")) {
                rl_tip.setVisibility(View.GONE);
            } else {
                rl_tip.setVisibility(View.VISIBLE);
                tv_tip.setText(YourBagActivity.review_list.get(reviwe_index).getTips_totle());
            }

        } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
            ll_delivery.setVisibility(View.GONE);
            ll_delivery_address.setVisibility(View.GONE);

        }
        tv_total.setText(YourBagActivity.review_list.get(reviwe_index).getTotal());


        // Get Current time
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        ll_add_item = findViewById(R.id.ll_add_item);

        ll_add_time = findViewById(R.id.ll_add_time);
        ll_add_date = findViewById(R.id.ll_add_date);
        edt_time = findViewById(R.id.edt_time);
        edt_date = findViewById(R.id.edt_date);
        ll_add_time.setVisibility(View.GONE);


        spn_schedule_order = findViewById(R.id.spn_schedule_order);
        ArrayList<String> schedule = new ArrayList<>();
        schedule.add("ASAP");
        schedule.add("Specific Time");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_textview, schedule);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")) {

            spn_schedule_order.setEnabled(false);
            spn_schedule_order.setClickable(false);
            spn_schedule_order.setAdapter(adapter);
            spn_schedule_order.setSelection(1);
            ll_add_date.setVisibility(View.VISIBLE);
            tv_specific_time_lable.setText("Select Time");
            edt_time.setVisibility(View.GONE);
            rl_catering_time.setVisibility(View.VISIBLE);
            is_asap = false;

        } else {
            spn_schedule_order.setEnabled(true);
            spn_schedule_order.setClickable(true);
            spn_schedule_order.setAdapter(adapter);
            ll_add_date.setVisibility(View.GONE);
            edt_time.setVisibility(View.VISIBLE);
            rl_catering_time.setVisibility(View.GONE);
            is_asap = true;
            tv_specific_time_lable.setText("Specific Time");

        }

        CheckReustaurantStatusAPI();
        setAdapter();

    }


    public void setAdapter() {
        rv_list = findViewById(R.id.rv_list);
        //ar_list = new ArrayList<YourBagBean>();
        /* ar_list = getItemList();*/
        ad_list = new YourBagItemAdapter(context, activity, ar_list, "review", new YourBagItemAdapter.YourBagClickListner() {
            @Override
            public void OnRemoveAllClickListner() {

            }

            @Override
            public void OnListReadyListner(ArrayList<YourBagBean> list) {

            }

            @Override
            public void OnRemoveClickListner(ArrayList<YourBagBean> list, int position) {

            }
        });
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(activity);
        rv_list.setLayoutManager(layoutManager2);
        rv_list.setAdapter(ad_list);

        listners();
    }

    public void listners() {

        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(ReviewOrderActivity.this, MenuActivity.class);
                intent.putExtra("from","review");
                startActivity(intent);
                finish();*/

                Intent intent = new Intent(ReviewOrderActivity.this, MenuActivity.class);
                intent.putExtra("from", "menu");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, R.style.TimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                boolean isPM = (hourOfDay >= 12);
                                edt_time.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                                Log.e("TIME===>",edt_time.getText().toString());

                                //edt_time.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                Calendar cal = Calendar.getInstance();

                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                //Min date setting part
                cal.set(Calendar.MONTH, mm);
                cal.set(Calendar.DAY_OF_MONTH, dd + 2);
                cal.set(Calendar.YEAR, yy);

                //Maximum date setting part
                Calendar calen = Calendar.getInstance();
                calen.set(Calendar.MONTH, mm);
                calen.set(Calendar.DAY_OF_MONTH, dd + 16);
                calen.set(Calendar.YEAR, yy);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.TimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edt_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                catering_date = edt_date.getText().toString();
                                GetCateringTimeAPI(catering_date);
                                //date[0] = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                            }
                        }, yy, mm, dd);

                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(calen.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        spn_schedule_order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    ll_add_time.setVisibility(View.VISIBLE);
                    is_asap = false;
                } else {
                    ll_add_time.setVisibility(View.GONE);
                    edt_time.setText("");
                    is_asap = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void openbottomPopup() {

        //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.fullwidthdialog));
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.fullwidthdialog);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.review_order_bottom_popup, null);

        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
      /*  WindowManager.LayoutParams wlmp = dialog.getWindow()
                .getAttributes();
        wlmp.gravity = Gravity.BOTTOM;*/

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initGooglePay();
        }
*/
        Button btn_payment_pickup = dialogLayout.findViewById(R.id.btn_payment_pickup);
        //  Button btn_google_pay = dialogLayout.findViewById(R.id.btn_google_pay);
        Button btn_debit_credit = dialogLayout.findViewById(R.id.btn_debit_credit);
        Button btn_continue = dialogLayout.findViewById(R.id.btn_continue);
        if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1"))        //$$$ exicute if condition
        {
            btn_payment_pickup.setText("payment on Delivery (" + YourBagActivity.review_list.get(reviwe_index).getTotal() + ")");

        } else {
            btn_payment_pickup.setText("payment on pickup (" + YourBagActivity.review_list.get(reviwe_index).getTotal() + ")");
        }
        btn_debit_credit.setText("pay by credit/debit card (" + YourBagActivity.review_list.get(reviwe_index).getTotal() + ")");
        btn_payment_pickup.setVisibility(View.GONE);
        btn_debit_credit.setVisibility(View.GONE);
        List<String> payment_method_list = Arrays.asList(Preference.getString(context, Preference.PAYMENT_METHOD).split("\\s*,\\s*"));
        for (int i = 0; i < payment_method_list.size(); i++) {
            if (payment_method_list.get(i).equalsIgnoreCase("cash_on")) {
                btn_payment_pickup.setVisibility(View.VISIBLE);
            } else if (payment_method_list.get(i).equalsIgnoreCase("authorize_net")) {
                btn_debit_credit.setVisibility(View.VISIBLE);
            } else if (payment_method_list.get(i).equalsIgnoreCase("cashprogram")) {
                //rl_cashPrograme.setVisibility(View.VISIBLE);
                btn_debit_credit.setVisibility(View.VISIBLE);
            }
        }
        TextView tv_label_1 = dialogLayout.findViewById(R.id.tv_label_1);
        tv_label_1.setText("Ready to place your order from " + Preference.getString(context, Preference.BRANCH_NAME) + " ?");

        TextView tv_terms_of_use = dialogLayout.findViewById(R.id.tv_terms_of_use);
        tv_terms_of_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, TermsAndUseActivity.class));
            }
        });

        TextView tv_privacy_agreement = dialogLayout.findViewById(R.id.tv_privacy_agreement);
        tv_privacy_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, PrivacyPolicyActivity.class));
            }
        });

        btn_debit_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(rl_cashPrograme.getVisibility() == View.VISIBLE){
                    CreditcardPopup();
                }else {
                    CheckCardStatusAPI();
                }
                //CheckCardStatusAPI();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Preference.setString(context, Preference.REORDER_STATUS, "0");
                Intent intent = new Intent(ReviewOrderActivity.this, MenuActivity.class);
                intent.putExtra("from", "menu");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        btn_payment_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isvalidate()) {
                    if (Preference.getString(context, Preference.FAX_STATUS).equalsIgnoreCase("1")) {
                        saveOrderCashFax();

                    } else {
                        if (Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")) {
                            CateringPayOnpickypAPI();
                        } else {
                            PayOnpickypAPI();

                        }

                    }
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);


        builder.setView(dialogLayout);

        dialog.show();
    }

    public void CreditcardPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.fullwidthdialog);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.review_order_creditcart_popup, null);

        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView tv_item_total = dialogLayout.findViewById(R.id.tv_item_total);
        TextView tv_sales_tax = dialogLayout.findViewById(R.id.tv_sales_tax);
        TextView tv_sales_tax_title = dialogLayout.findViewById(R.id.tv_sales_tax_title);
        RelativeLayout rl_surcharge_amount = dialogLayout.findViewById(R.id.rl_surcharge_amount);
        TextView tv_surcharge_amount = dialogLayout.findViewById(R.id.tv_surcharge_amount);
        RelativeLayout rl_special_discount = dialogLayout.findViewById(R.id.rl_special_discount);
        TextView tv_special_discount_amount = dialogLayout.findViewById(R.id.tv_special_discount_ammont);
        TextView tv_special_iscount_heading = dialogLayout.findViewById(R.id.tv_special_discount_heading);


        RelativeLayout rl_first_discount = dialogLayout.findViewById(R.id.rl_first_discount);
        TextView tv_first_discount_heading = dialogLayout.findViewById(R.id.tv_first_discount_heading);
        TextView tv_first_discount_ammont = dialogLayout.findViewById(R.id.tv_first_discount_ammont);

        RelativeLayout rl_svg = dialogLayout.findViewById(R.id.rl_svg);
        TextView tv_svg_heading = dialogLayout.findViewById(R.id.tv_svg_heading);
        TextView tv_svg_ammont = dialogLayout.findViewById(R.id.tv_svg_ammont);
        RelativeLayout rl_promocode = dialogLayout.findViewById(R.id.rl_promocode);
        TextView tv_promocode_discount = dialogLayout.findViewById(R.id.tv_promocode_discount);
        TextView tv_promocode_heading = dialogLayout.findViewById(R.id.tv_promocode_heading);

        TextView tv_delivery_charge = dialogLayout.findViewById(R.id.tv_delivery_charge);
        TextView tv_tip = dialogLayout.findViewById(R.id.tv_tip);
        TextView tv_total = dialogLayout.findViewById(R.id.tv_total);
        LinearLayout ll_delivery = dialogLayout.findViewById(R.id.ll_delivery);

        Button btn_ok = dialogLayout.findViewById(R.id.btn_ok);
        Button btn_cancel = dialogLayout.findViewById(R.id.btn_cancel);

        RelativeLayout rl_sales_tax = dialogLayout.findViewById(R.id.rl_sales_tax);
        RelativeLayout rl_delivery_charge = dialogLayout.findViewById(R.id.rl_delivery_charge);
        RelativeLayout rl_tip = dialogLayout.findViewById(R.id.rl_tip);

        if(rl_cashPrograme.getVisibility() == View.VISIBLE){
            rl_svg.setVisibility(View.VISIBLE);
            tv_svg_heading.setText("SVC(3.99%)");
            Double svg_amount = (Double.parseDouble(YourBagActivity.review_list.get(reviwe_index).getItem_totle().substring(1)) * 3.99) / 100;
            tv_svg_ammont.setText("$" + Validation.roundOffTo2DecPlaces(svg_amount));
            double totle = Double.parseDouble(YourBagActivity.review_list.get(reviwe_index).getTotal().substring(1)) + svg_amount;
            tv_total.setText("$" + Validation.roundOffTo2DecPlaces(totle));

        }else {
            rl_svg.setVisibility(View.GONE);

        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckCardStatusAPI();
            }
        });

        tv_item_total.setText(YourBagActivity.review_list.get(reviwe_index).getItem_totle());

        if (YourBagActivity.review_list.get(reviwe_index).getText_percentage().equalsIgnoreCase("$0.0")) {
            rl_sales_tax.setVisibility(View.GONE);
        } else {
            rl_sales_tax.setVisibility(View.VISIBLE);
            tv_sales_tax_title.setText("Sales Tax(" + YourBagActivity.review_list.get(reviwe_index).getTax() + "%)");
            tv_sales_tax.setText(YourBagActivity.review_list.get(reviwe_index).getText_percentage());
        }
        //$$$set tax to texpercentage
        if (YourBagActivity.review_list.get(reviwe_index).getSurcharge().equalsIgnoreCase("$0.00")) {
            rl_surcharge_amount.setVisibility(View.GONE);
        } else {
            rl_surcharge_amount.setVisibility(View.VISIBLE);
            tv_surcharge_amount.setText(YourBagActivity.review_list.get(reviwe_index).getSurcharge());
        }

        if (YourBagActivity.review_list.get(reviwe_index).getIs_special_applied().equalsIgnoreCase(Config.TAG_FAILURE)) {
            rl_special_discount.setVisibility(View.GONE);
        } else {
            rl_special_discount.setVisibility(View.VISIBLE);
            tv_special_iscount_heading.setText(YourBagActivity.review_list.get(reviwe_index).getSpecial_discount_heading());
            tv_special_discount_amount.setText(YourBagActivity.review_list.get(reviwe_index).getSpecial_discount_amount());
        }
        if (YourBagActivity.review_list.get(reviwe_index).getIs_first_discount_applied().equalsIgnoreCase("0")) {
            rl_first_discount.setVisibility(View.GONE);
        } else {
            rl_first_discount.setVisibility(View.VISIBLE);
            tv_first_discount_heading.setText(YourBagActivity.review_list.get(reviwe_index).getFirst_discount_heading());
            tv_first_discount_ammont.setText(YourBagActivity.review_list.get(reviwe_index).getFirst_discount_ammont());
        }
        if (!YourBagActivity.review_list.get(reviwe_index).getPromocode_discount().equalsIgnoreCase("") || !YourBagActivity.review_list.get(reviwe_index).getPromocode_heading().equalsIgnoreCase("")) {
            rl_promocode.setVisibility(View.VISIBLE);
            tv_promocode_heading.setText(YourBagActivity.review_list.get(reviwe_index).getPromocode_heading());
            tv_promocode_discount.setText(YourBagActivity.review_list.get(reviwe_index).getPromocode_discount());
        } else {
            rl_promocode.setVisibility(View.GONE);
        }
        if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
            ll_delivery_address.setVisibility(View.VISIBLE);
            tv_delivery_address.setVisibility(View.VISIBLE);
            tv_delivery_address.setText(YourBagActivity.review_list.get(reviwe_index).getDeliveryStreetAddress() + " , " + YourBagActivity.review_list.get(reviwe_index).getDeliverycity_state_zip());
            if (YourBagActivity.review_list.get(reviwe_index).getDelivery().equalsIgnoreCase("$0.00")) {
                rl_delivery_charge.setVisibility(View.GONE);
            } else {
                rl_delivery_charge.setVisibility(View.VISIBLE);
                tv_delivery_charge.setText(YourBagActivity.review_list.get(reviwe_index).getDelivery());
            }

            if (YourBagActivity.review_list.get(reviwe_index).getTips_totle().equalsIgnoreCase("$0.00")) {
                rl_tip.setVisibility(View.GONE);
            } else {
                rl_tip.setVisibility(View.VISIBLE);
                tv_tip.setText(YourBagActivity.review_list.get(reviwe_index).getTips_totle());
            }

        } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
            ll_delivery.setVisibility(View.GONE);
            ll_delivery_address.setVisibility(View.GONE);

        }



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);


        builder.setView(dialogLayout);

        dialog.show();
    }

    public void openMessagePopup(String msg) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.fullwidthdialog));
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.fullwidthdialog);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.message_bottom_popup, null);

        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView tv_label_1 = dialogLayout.findViewById(R.id.tv_label_1);
        tv_label_1.setText(msg);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);


        builder.setView(dialogLayout);

        dialog.show();
    }

    //Validation
    public boolean isvalidate() {

        if (Preference.getString(context, Preference.CATERING_STATUS).equalsIgnoreCase("1")) {
            if (catering_date.equalsIgnoreCase("")) {
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Please Select Date", new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {
                        //Cliked Ok
                    }

                    @Override
                    public void setNegativeListner() {

                    }
                });
                return false;
            } else if (catering_time.equalsIgnoreCase("")) {
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Please Select  Time", new DialogCallBackListner() {
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
        } else {
            if (!is_asap) {
                if (edt_time.getText().toString().trim().equalsIgnoreCase("")) {
                    com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, "Please Select Schedule Time", new DialogCallBackListner() {
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
            } else {
                return true;
            }
        }


    }

   /* //Api calling
    public void PayOnpickypAPI() {
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
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            GetHomeApi.GetHome(context);
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }
        map.put("global_street_address", Preference.getString(context, Preference.USER_STREET_ADDRESS));
        map.put("global_apt_suite_floor", Preference.getString(context, Preference.USER_SUITE_FLOOR));
        map.put("global_city_state_zip", Preference.getString(context, Preference.USER_CITY_STATE_ZIP));
        map.put("globalCartItems", oid.substring(0, oid.length() - 1));
        map.put("cart_ids", oid.substring(0, oid.length() - 1));
        map.put("globalLoggedIn", Preference.getString(context, Preference.USER_ID));
        //map.put("globalTax",tv_sales_tax.getText().toString().substring(1));
        map.put("globalTax", tax);
        map.put("globalTips", tv_tip.getText().toString().substring(1));
        if (!globalPromocode.equalsIgnoreCase("0") && !globalPromocodeDiscountType.equalsIgnoreCase("0")) {
            map.put("globalPromocode", globalPromocode);
            map.put("globalPromocodeDiscountType", globalPromocodeDiscountType);
            map.put("globalPromocodeDiscount", globalPromocodeDiscount);

        } else {
            map.put("globalPromocode", "");
            map.put("globalPromocodeDiscountType", "");
            map.put("globalPromocodeDiscount", " ");

        }


        map.put("globalGrandTotal", tv_total.getText().toString().substring(1));

        if (is_asap) {
            map.put("is_asap", "1");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", "");
        } else {
            map.put("is_asap", "0");
            if (order_type.equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");
                map.put("global_delivery_time", edt_time.getText().toString());
                map.put("global_pickup_time", "");
            } else if (order_type.equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");
                map.put("global_delivery_time", "");
                map.put("global_pickup_time", edt_time.getText().toString());
            }
        }
        map.put("global_advance_order", "");
        map.put("global_advance_order_date", "0000-00-00");
        map.put("global_special_note", edt_special_note.getText().toString().trim());
        map.put("global_delivery_charge", tv_delivery_charge.getText().toString().substring(1));

        map.put("order_type", "cash");
        map.put("discountType", "");
        if (rl_special_discount.getVisibility() == View.GONE) {
            map.put("SpecialDiscountType", "");
            map.put("SpecialDiscountAmount", "");
            map.put("TotalSpecialAmount", "");

        } else {
            map.put("SpecialDiscountType", sdType);
            map.put("SpecialDiscountAmount", sdAmount);
            map.put("TotalSpecialAmount", totalsd);

        }
        map.put("CardTotalAmount", tv_total.getText().toString().substring(1));
        map.put("OnlineOrderSurcharge", tv_surcharge_amount.getText().toString().substring(1));

        ApiCall.volleyPostApi(context, AppConstants.save_order_cash_auto_email, map, new ApiCallBackListner() {
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

                            com.trec2go.GregsPizza.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, "Your order has been processed.A confirmation email and Order #" + branch_order_id + " will be sent to your email address. Please check your email for this confirmation.", new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {  // $$$ chenge msg and get order_id
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
                                                    Intent intent = new Intent(ReviewOrderActivity.this, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
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
                                            Intent intent = new Intent(ReviewOrderActivity.this, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
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

    //Api calling

    private void GetCateringTimeAPI(String catering_date) {
        final Spinner spn_time = findViewById(R.id.spn_time);
        Utility.createProgressDialoge(context, "Loading");
        final ArrayList<String> time = new ArrayList<>();
        final ArrayAdapter[] adapter = new ArrayAdapter[1];

        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        map.put("catering_date", catering_date);
        if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1"))        //$$$ exicute if condition
        {
            map.put("order_type", "D");

            //btn_payment_pickup.setText("payment on Delivery (" + YourBagActivity.review_list.get(reviwe_index).getTotal() + ")");

        } else {
            map.put("order_type", "P");

            //btn_payment_pickup.setText("payment on pickup (" + YourBagActivity.review_list.get(reviwe_index).getTotal() + ")");
        }
        ApiCall.volleyPostApi(context, AppConstants.get_catering_time, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            time.add(jsonArray.getString(i));
                        }


                    } else {
                        AlertDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.fullwidthdialog);
                        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                        View dialogLayout = inflater.inflate(R.layout.message_bottom_popup, null);

                        dialog = builder.create();
                        dialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog.setView(dialogLayout, 0, 0, 0, 0);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);

                        TextView tv_label_1 = dialogLayout.findViewById(R.id.tv_label_1);
                        tv_label_1.setText(msg);

                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        Window window = dialog.getWindow();
                        lp.copyFrom(window.getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.gravity = Gravity.BOTTOM;
                        window.setAttributes(lp);


                        builder.setView(dialogLayout);

                        dialog.show();

                    }


                    adapter[0] = new ArrayAdapter(activity, R.layout.spinner_textview, time);
                    adapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_time.setAdapter(adapter[0]);
                    spn_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                               activity.tv_time_error.setVisibility(View.GONE);
                            catering_time = time.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    Utility.dissmisProgress();
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
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, cn.pedant.SweetAlert.SweetAlertDialog.ERROR_TYPE, activity.getResources().getString(R.string.response_error), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {

                    }

                    @Override
                    public void setNegativeListner() {

                    }


                });
            }

            @Override
            public void noInternetConnection() {
                Utility.dissmisProgress();
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, activity.getResources().getString(R.string.no_internet_connection), new DialogCallBackListner() {
                    @Override
                    public void setPositiveListner() {

                    }

                    @Override
                    public void setNegativeListner() {

                    }


                });
            }
        });
    }

    public void PayOnpickypAPI() {
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
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            GetHomeApi.GetHome(context);
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }
        map.put("global_street_address", Preference.getString(context, Preference.USER_STREET_ADDRESS));
        map.put("global_city_state_zip", Preference.getString(context, Preference.USER_CITY_STATE_ZIP));

        map.put("global_apt_suite_floor", Preference.getString(context, Preference.USER_SUITE_FLOOR));
        map.put("globalCartItems", oid.substring(0, oid.length() - 1));
        map.put("cart_ids", oid.substring(0, oid.length() - 1));


        map.put("globalLoggedIn", Preference.getString(context, Preference.USER_ID));
        map.put("globalTax", YourBagActivity.review_list.get(reviwe_index).getTax());
        map.put("globalTips", tv_tip.getText().toString().substring(1));
        if (!YourBagActivity.review_list.get(reviwe_index).getGloblePromocode().equalsIgnoreCase("0") && !YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscountType().equalsIgnoreCase("0")) {
            map.put("globalPromocode", YourBagActivity.review_list.get(reviwe_index).getGloblePromocode());
            map.put("globalPromocodeDiscountType", YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscountType());
            map.put("globalPromocodeDiscount", YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscount());

        } else {
            map.put("globalPromocode", "");
            map.put("globalPromocodeDiscountType", "");
            map.put("globalPromocodeDiscount", "");

        }


        map.put("globalGrandTotal", tv_total.getText().toString().substring(1));

        if (is_asap) {
            map.put("is_asap", "1");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", "");

            if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");

            } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");

            }

        } else {
            map.put("is_asap", "0");
            if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");
                map.put("global_delivery_time", edt_time.getText().toString());
                map.put("global_pickup_time", "");
            } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");
                map.put("global_delivery_time", "");
                map.put("global_pickup_time", edt_time.getText().toString());
            }
        }
        map.put("global_advance_order", "");
        map.put("global_advance_order_date", "0000-00-00");
        map.put("global_special_note", edt_special_note.getText().toString().trim());
        map.put("global_delivery_charge", tv_delivery_charge.getText().toString().substring(1));

        map.put("order_type", "cash");
        map.put("discountType", "");
        if (rl_special_discount.getVisibility() == View.GONE) {
            map.put("SpecialDiscountType", "");
            map.put("SpecialDiscountAmount", "");
            map.put("TotalSpecialAmount", "");

        } else {
            map.put("SpecialDiscountType", YourBagActivity.review_list.get(reviwe_index).getSdType());
            map.put("SpecialDiscountAmount", YourBagActivity.review_list.get(reviwe_index).getSdAmount());
            map.put("TotalSpecialAmount", YourBagActivity.review_list.get(reviwe_index).getTotlesdt());

        }
        if (YourBagActivity.review_list.get(reviwe_index).getIs_first_discount_applied().equalsIgnoreCase("1")) {
            map.put("firstordertype", YourBagActivity.review_list.get(reviwe_index).getFirstordertype());
            map.put("discountvalue", YourBagActivity.review_list.get(reviwe_index).getDiscountvalue());
        } else {
            map.put("firstordertype", "0");
            map.put("discountvalue", "");

        }

        map.put("subtotal", tv_item_total.getText().toString().substring(1));
        map.put("CardTotalAmount", tv_total.getText().toString().substring(1));
        map.put("OnlineOrderSurcharge", tv_surcharge_amount.getText().toString().substring(1));

        ApiCall.volleyPostApi(context, AppConstants.save_order_cash_first_discount, map, new ApiCallBackListner() {
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
                        String order_id = jsonObject.getString("order_id");
                        String branch_order_id = jsonObject.getString("branch_order_id");


                        Utility.dissmisProgress();
                        if (status.equalsIgnoreCase("1")) {

                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, "Your order has been processed.A confirmation email and Order #" + branch_order_id + " will be sent to your email address. Please check your email for this confirmation.", new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {  // $$$ chenge msg and get order_id
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
                                                    Intent intent = new Intent(ReviewOrderActivity.this, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
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
                                            Intent intent = new Intent(ReviewOrderActivity.this, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
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

    public void CateringPayOnpickypAPI() {
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
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            GetHomeApi.GetHome(context);
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }
        map.put("global_street_address", Preference.getString(context, Preference.USER_STREET_ADDRESS));
        map.put("global_city_state_zip", Preference.getString(context, Preference.USER_CITY_STATE_ZIP));

        map.put("global_apt_suite_floor", Preference.getString(context, Preference.USER_SUITE_FLOOR));
        map.put("globalCartItems", oid.substring(0, oid.length() - 1));
        map.put("cart_ids", oid.substring(0, oid.length() - 1));


        map.put("globalLoggedIn", Preference.getString(context, Preference.USER_ID));
        map.put("globalTax", YourBagActivity.review_list.get(reviwe_index).getTax());
        map.put("globalTips", tv_tip.getText().toString().substring(1));
        if (!YourBagActivity.review_list.get(reviwe_index).getGloblePromocode().equalsIgnoreCase("0") && !YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscountType().equalsIgnoreCase("0")) {
            map.put("globalPromocode", YourBagActivity.review_list.get(reviwe_index).getGloblePromocode());
            map.put("globalPromocodeDiscountType", YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscountType());
            map.put("globalPromocodeDiscount", YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscount());

        } else {
            map.put("globalPromocode", "");
            map.put("globalPromocodeDiscountType", "");
            map.put("globalPromocodeDiscount", "");

        }


        map.put("globalGrandTotal", tv_total.getText().toString().substring(1));
        Log.e("grandtotle", "=>" + tv_total.getText().toString().substring(1));


      /*  if (is_asap) {
            map.put("is_asap", "1");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", "");
        } else {*/
        map.put("is_asap", "0");
        if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
            //delivery
            map.put("globalType_LoggedIn", "D");
            map.put("global_delivery_time", edt_time.getText().toString());
            map.put("global_pickup_time", "");
        } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
            //pickup
            map.put("globalType_LoggedIn", "P");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", edt_time.getText().toString());
        }
        /* }*/
        map.put("global_advance_order", "");
        map.put("global_advance_order_date", "0000-00-00");
        map.put("global_special_note", edt_special_note.getText().toString().trim());
        map.put("global_delivery_charge", tv_delivery_charge.getText().toString().substring(1));

        map.put("order_type", "cash");
        map.put("discountType", "");

        if (rl_special_discount.getVisibility() == View.GONE) {
            map.put("SpecialDiscountType", "");
            map.put("SpecialDiscountAmount", "");
            map.put("TotalSpecialAmount", "");

        } else {
            map.put("SpecialDiscountType", YourBagActivity.review_list.get(reviwe_index).getSdType());
            map.put("SpecialDiscountAmount", YourBagActivity.review_list.get(reviwe_index).getSdAmount());
            map.put("TotalSpecialAmount", YourBagActivity.review_list.get(reviwe_index).getTotlesdt());

        }
        if (YourBagActivity.review_list.get(reviwe_index).getIs_first_discount_applied().equalsIgnoreCase("1")) {
            map.put("firstordertype", YourBagActivity.review_list.get(reviwe_index).getFirstordertype());
            map.put("discountvalue", YourBagActivity.review_list.get(reviwe_index).getDiscountvalue());
        } else {
            map.put("firstordertype", "0");
            map.put("discountvalue", "");

        }

        map.put("subtotal", tv_item_total.getText().toString().substring(1));
        map.put("CardTotalAmount", tv_total.getText().toString().substring(1));
        map.put("OnlineOrderSurcharge", tv_surcharge_amount.getText().toString().substring(1));

        map.put("global_advance_order_date", catering_date);
        map.put("global_advance_order_time", catering_time);

        ApiCall.volleyPostApi(context, AppConstants.save_order_cash_first_discount_catering, map, new ApiCallBackListner() {
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
                        String order_id = jsonObject.getString("order_id");
                        String branch_order_id = jsonObject.getString("branch_order_id");


                        Utility.dissmisProgress();
                        if (status.equalsIgnoreCase("1")) {

                            catering_date = "";
                            catering_time = "";
                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, "Your order has been processed.A confirmation email and Order #" + branch_order_id + " will be sent to your email address. Please check your email for this confirmation.", new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {


                                    // $$$ chenge msg and get order_id
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
                                                    Intent intent = new Intent(ReviewOrderActivity.this, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
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
                                            Intent intent = new Intent(ReviewOrderActivity.this, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
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

    public void saveOrderCashFax() {
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
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            GetHomeApi.GetHome(context);
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }
        map.put("global_street_address", Preference.getString(context, Preference.USER_STREET_ADDRESS));
        map.put("global_city_state_zip", Preference.getString(context, Preference.USER_CITY_STATE_ZIP));

        map.put("global_apt_suite_floor", Preference.getString(context, Preference.USER_SUITE_FLOOR));
        map.put("globalCartItems", oid.substring(0, oid.length() - 1));
        map.put("cart_ids", oid.substring(0, oid.length() - 1));


        map.put("globalLoggedIn", Preference.getString(context, Preference.USER_ID));
        map.put("globalTax", YourBagActivity.review_list.get(reviwe_index).getTax());
        map.put("globalTips", tv_tip.getText().toString().substring(1));
        if (!YourBagActivity.review_list.get(reviwe_index).getGloblePromocode().equalsIgnoreCase("0") && !YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscountType().equalsIgnoreCase("0")) {
            map.put("globalPromocode", YourBagActivity.review_list.get(reviwe_index).getGloblePromocode());
            map.put("globalPromocodeDiscountType", YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscountType());
            map.put("globalPromocodeDiscount", YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscount());

        } else {
            map.put("globalPromocode", "");
            map.put("globalPromocodeDiscountType", "");
            map.put("globalPromocodeDiscount", "");

        }


        map.put("globalGrandTotal", tv_total.getText().toString().substring(1));

        if (is_asap) {
            map.put("is_asap", "1");
            map.put("global_delivery_time", "");
            map.put("global_pickup_time", "");

            if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");

            } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");

            }
        } else {
            map.put("is_asap", "0");
            if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
                //delivery
                map.put("globalType_LoggedIn", "D");
                map.put("global_delivery_time", edt_time.getText().toString());
                map.put("global_pickup_time", "");
            } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
                //pickup
                map.put("globalType_LoggedIn", "P");
                map.put("global_delivery_time", "");
                map.put("global_pickup_time", edt_time.getText().toString());
            }
        }
        map.put("global_advance_order", "");
        map.put("global_advance_order_date", "0000-00-00");
        map.put("global_special_note", edt_special_note.getText().toString().trim());
        map.put("global_delivery_charge", tv_delivery_charge.getText().toString().substring(1));

        map.put("order_type", "cash");
        map.put("discountType", "");
        if (rl_special_discount.getVisibility() == View.GONE) {
            map.put("SpecialDiscountType", "");
            map.put("SpecialDiscountAmount", "");
            map.put("TotalSpecialAmount", "");

        } else {
            map.put("SpecialDiscountType", YourBagActivity.review_list.get(reviwe_index).getSdType());
            map.put("SpecialDiscountAmount", YourBagActivity.review_list.get(reviwe_index).getSdAmount());
            map.put("TotalSpecialAmount", YourBagActivity.review_list.get(reviwe_index).getTotlesdt());

        }
        if (YourBagActivity.review_list.get(reviwe_index).getIs_first_discount_applied().equalsIgnoreCase("1")) {
            map.put("firstordertype", YourBagActivity.review_list.get(reviwe_index).getFirstordertype());
            map.put("discountvalue", YourBagActivity.review_list.get(reviwe_index).getDiscountvalue());
        } else {
            map.put("firstordertype", "0");
            map.put("discountvalue", "");

        }

        map.put("subtotal", tv_item_total.getText().toString().substring(1));
        map.put("CardTotalAmount", tv_total.getText().toString().substring(1));
        map.put("OnlineOrderSurcharge", tv_surcharge_amount.getText().toString().substring(1));

        ApiCall.volleyPostApi(context, AppConstants.save_order_cash_fax, map, new ApiCallBackListner() {
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
                        String order_id = jsonObject.getString("order_id");
                        String branch_order_id = jsonObject.getString("branch_order_id");


                        Utility.dissmisProgress();
                        if (status.equalsIgnoreCase("1")) {

                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, "Your order has been processed.A confirmation email and Order #" + branch_order_id + " will be sent to your email address. Please check your email for this confirmation.", new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {  // $$$ chenge msg and get order_id
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
                                                    Intent intent = new Intent(ReviewOrderActivity.this, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
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
                                            Intent intent = new Intent(ReviewOrderActivity.this, HomeActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
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

    public void CheckReustaurantStatusAPI() {

        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));
            GetHomeApi.GetHome(context);

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }
        if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
            //delivery
            map.put("order_type", "D");
        } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
            //pickup
            map.put("order_type", "P");
        }

        ApiCall.volleyPostApi(context, AppConstants.check_res_status, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) json;
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {

                            btn_continue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    StoreStatus();
//                                    openbottomPopup();
                                }
                            });

                        } else {
                            final String msg = jsonObject.optString("msg");
                            btn_continue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openMessagePopup(msg);
                                }
                            });
                        }
                    } else if (json instanceof JSONArray) {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //For redirect to login
                /*onBackPressed();*/
            }

            @Override
            public void onError(String error) {
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

    public void CheckCardStatusAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("user_id", Preference.getString(context, Preference.USER_ID));

        ApiCall.volleyPostApi(context, AppConstants.get_card_details, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject) {

                    } else if (json instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) json;
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String card_no = jsonObject.getString("card_no");
                        String is_save_card = jsonObject.getString("is_save_card");
                        if (isvalidate()) {
                            Intent intent;
                            if (is_save_card.equalsIgnoreCase("0")) {
                                intent = new Intent(context, AddNewCardActivity.class);
                            } else {
                                intent = new Intent(context, SelectCardActivity.class);
                                intent.putExtra("card_no", card_no);
                            }

                            intent.putExtra("globalTax", YourBagActivity.review_list.get(reviwe_index).getTax());
                            intent.putExtra("globalTips", tv_tip.getText().toString().substring(1));
                            if (!YourBagActivity.review_list.get(reviwe_index).getGloblePromocode().equalsIgnoreCase("0") && !YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscountType().equalsIgnoreCase("0")) {
                                intent.putExtra("globalPromocode", YourBagActivity.review_list.get(reviwe_index).getGloblePromocode());
                                intent.putExtra("globalPromocodeDiscountType", YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscountType());
                                intent.putExtra("globalPromocodeDiscount", YourBagActivity.review_list.get(reviwe_index).getGloblePromocodeDiscount());

                            } else {
                                intent.putExtra("globalPromocode", "");
                                intent.putExtra("globalPromocodeDiscountType", "");
                                intent.putExtra("globalPromocodeDiscount", "");

                            }

                            intent.putExtra("globalGrandTotal", tv_total.getText().toString().substring(1));

                            if (is_asap) {
                                intent.putExtra("is_asap", "1");
                                intent.putExtra("global_delivery_time", "");
                                intent.putExtra("global_pickup_time", "");

                                if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
                                    //delivery
                                    intent.putExtra("globalType_LoggedIn", "D");

                                } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
                                    //pickup
                                    intent.putExtra("globalType_LoggedIn", "P");

                                }
                            } else {
                                intent.putExtra("is_asap", "0");

                                if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("1")) {
                                    //delivery
                                    intent.putExtra("globalType_LoggedIn", "D");
                                    intent.putExtra("global_delivery_time", edt_time.getText().toString());
                                    intent.putExtra("global_pickup_time", "");
                                } else if (YourBagActivity.review_list.get(reviwe_index).getOrder_type().equalsIgnoreCase("2")) {
                                    //pickup
                                    intent.putExtra("globalType_LoggedIn", "P");
                                    intent.putExtra("global_delivery_time", "");
                                    intent.putExtra("global_pickup_time", edt_time.getText().toString());
                                }
                            }
                            intent.putExtra("order_type", YourBagActivity.review_list.get(reviwe_index).getOrder_type());

                            intent.putExtra("global_advance_order", "");
                            intent.putExtra("global_advance_order_date", "0000-00-00");
                            intent.putExtra("global_special_note", edt_special_note.getText().toString().trim());
                            intent.putExtra("global_delivery_charge", tv_delivery_charge.getText().toString().substring(1));

                            //intent.putExtra("order_type","cash");
                            intent.putExtra("discountType", "");
                            if (rl_special_discount.getVisibility() == View.GONE) {
                                intent.putExtra("SpecialDiscountType", "");
                                intent.putExtra("SpecialDiscountAmount", "");
                                intent.putExtra("TotalSpecialAmount", "");

                            } else {
                                intent.putExtra("SpecialDiscountType", YourBagActivity.review_list.get(reviwe_index).getSdType());
                                intent.putExtra("SpecialDiscountAmount", YourBagActivity.review_list.get(reviwe_index).getSdAmount());
                                intent.putExtra("TotalSpecialAmount", YourBagActivity.review_list.get(reviwe_index).getSdAmount());

                            }
                            if (YourBagActivity.review_list.get(reviwe_index).getIs_first_discount_applied().equalsIgnoreCase("1")) {
                                intent.putExtra("is_first_discount_applied", YourBagActivity.review_list.get(reviwe_index).getIs_first_discount_applied());
                                intent.putExtra("firstordertype", YourBagActivity.review_list.get(reviwe_index).getFirstordertype());
                                intent.putExtra("discountvalue", YourBagActivity.review_list.get(reviwe_index).getDiscountvalue());
                            } else {
                                intent.putExtra("is_first_discount_applied", YourBagActivity.review_list.get(reviwe_index).getIs_first_discount_applied());
                                intent.putExtra("firstordertype", "0");
                                intent.putExtra("discountvalue", "");

                            }
                            intent.putExtra("item_total", tv_item_total.getText().toString().substring(1));
                            intent.putExtra("CardTotalAmount", tv_total.getText().toString().substring(1));
                            intent.putExtra("OnlineOrderSurcharge", tv_surcharge_amount.getText().toString().substring(1));
                            Utility.dissmisProgress();

                            startActivity(intent);
                        }
                        Utility.dissmisProgress();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //For redirect to login
                /*onBackPressed();*/
            }

            @Override
            public void onError(String error) {
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


    //Google Pay ==================================================================================================================

/*    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initGooglePay(){
        // initialize a Google Pay API client for an environment suitable for testing
        dialog.dismiss();
        possiblyShowGooglePayButton();
    }*/

    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button
     *
     * @see <a
     *     href="https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient#isReadyToPay(com.google.android.gms.wallet.IsReadyToPayRequest)">PaymentsClient#IsReadyToPay</a>
     */
    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private void possiblyShowGooglePayButton() {
        final Optional<JSONObject> isReadyToPayJson = GooglePay.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        if (request == null) {
            return;
        }
        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(ApiException.class);
                            if (result) {
                                // show Google as a payment option
                                mGooglePayButton = dialogLayout.findViewById(R.id.ll_google_pay);
                                mGooglePayButton.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                requestPayment(view);
                                            }
                                        });
                                mGooglePayButton.setVisibility(View.VISIBLE);
                            }
                        } catch (ApiException exception) {
                            // handle developer errors
                        }
                    }
                });
    }*/

    /**
     * Display the Google Pay payment sheet after interaction with the Google Pay payment button
     *
     * @param view optionally uniquely identify the interactive element prompting for payment
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void requestPayment(View view) {
        Optional<JSONObject> paymentDataRequestJson = GooglePay.getPaymentDataRequest();
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }
        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());
        Utility.log_api_failure(paymentDataRequestJson.get().toString());
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    mPaymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet
     *
     * @param requestCode the request code originally supplied to AutoResolveHelper in
     *                    requestPayment()
     * @param resultCode  the result code returned by the Google Pay API
     * @param data        an Intent from the Google Pay API containing payment or error data
     * @see <a href="https://developer.android.com/training/basics/intents/result">Getting a result
     * from an Activity</a>
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        String json = paymentData.toJson();
                        // if using gateway tokenization, pass this token without modification
                        JSONObject paymentMethodData = null;
                        try {
                            paymentMethodData = new JSONObject(json).getJSONObject("paymentMethodData");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String paymentToken = paymentMethodData.getJSONObject("tokenizationData").getString("token");
                            Utility.log_api_failure(paymentMethodData.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Utility.log_api_failure("token received");
                        break;
                    case Activity.RESULT_CANCELED:
                        Utility.log_api_failure("request canceled");
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        Utility.log_api_failure(String.valueOf(status.getStatusCode()));
                        // Log the status for debugging.
                        // Generally, there is no need to show an error to the user.
                        // The Google Pay payment sheet will present any account errors.
                        break;
                    default:
                        // Do nothing.
                }
                break;
            default:
                // Do nothing.
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Double totle = Double.parseDouble(YourBagActivity.review_list.get(0).getTotal().substring(1)) - Double.parseDouble(YourBagActivity.review_list.get(0).getDelivery().substring(1)) ;
        YourBagActivity.review_list.get(0).setTotal("$" + Validation.roundOffTo2DecPlaces(totle));
        Log.e("Review",  "OnBackPressed" + Double.parseDouble(YourBagActivity.review_list.get(0).getTotal().substring(1)));
    }

    public void StoreStatus(){
        Utility.createProgressDialoge(context,"Loading");
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        map.put("branch_id", Preference.getString(context,Preference.BRANCH_ID));
        ApiCall.volleyPostApi(context, AppConstants.store_status,map,new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    int status = object.getInt("status");
                    if(status == 1){

                        openbottomPopup();
                    }else {
                        String msg = object.getString("msg");
                        com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, cn.pedant.SweetAlert.SweetAlertDialog.ERROR_TYPE, msg, new DialogCallBackListner() {
                            @Override
                            public void setPositiveListner() {
                                //Cliked Ok
                            }

                            @Override
                            public void setNegativeListner() {

                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utility.dissmisProgress();
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




    public void faxCheckApi() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();

        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        if (Preference.getString(context, Preference.REORDER_STATUS).equalsIgnoreCase("1")) {
            map.put("branch_id", Preference.getString(context, Preference.REORDER_BRANCH_ID));

        } else {
            map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        }

        ApiCall.volleyPostApi(context, AppConstants.fax_check, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {
                Utility.dissmisProgress();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String status = object.getString("status");
                        //String messsage = object.getString("messsage");

                        Preference.setString(context, Preference.FAX_STATUS, status);

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
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, activity.getResources().getString(R.string.response_error), new DialogCallBackListner() {
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
                com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.ERROR_TYPE, activity.getResources().getString(R.string.no_internet_connection), new DialogCallBackListner() {
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
    }    //=============================================================================================================================
}

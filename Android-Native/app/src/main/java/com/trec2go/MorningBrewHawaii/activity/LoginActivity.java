package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.bean.AddToCartBean;
import com.trec2go.MorningBrewHawaii.commonapicall.AddToCartApi;
import com.trec2go.MorningBrewHawaii.commonapicall.GetImageListAPI;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.ImageManipulation;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {


    TextView tv_create_account, tv_forget_password, tv_skip, tv_privacy_policy, tv_terms;
    Button btn_signin, btn_guest_checkout;
    Activity activity;
    Context context;
    String from = "";


    TextInputLayout til_email, til_password;
    AppCompatEditText aedt_email, aedt_password;
    ImageView img_bk;
    CheckBox chk_remember;

    AddToCartBean addToCartBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;
        context = this;

        //Firebase Token
        Utility.log(Preference.getString(context, Preference.FCM_token));

        init();
        Intent intent = getIntent();
        from = intent.getStringExtra("from");

        if(from.equalsIgnoreCase("Order_Detail"))
        {
            addToCartBean = intent.getParcelableExtra("addToCartBean");
            if(Preference.getString(context, Preference.USER_ID).equalsIgnoreCase(""))
            {
                btn_guest_checkout.setVisibility(View.VISIBLE);
            }
        }else if(from.equalsIgnoreCase("menu")){
            if(Preference.getString(context, Preference.USER_ID).equalsIgnoreCase(""))
            {
                btn_guest_checkout.setVisibility(View.VISIBLE);
            }
        }
        else {
            btn_guest_checkout.setVisibility(View.GONE);
        }



        if(Preference.getString(context, Preference.REMEMBER_ME_STATUS).equalsIgnoreCase("1")){
            chk_remember.setChecked(true);
            if(!Preference.getString(context, Preference.USER_EMAIL).equalsIgnoreCase(""))
            {
                aedt_email.setText(Preference.getString(context, Preference.USER_EMAIL));
                aedt_password.setText(Preference.getString(context, Preference.USER_PASSWORD));
            }
        }
        else {
            chk_remember.setChecked(false);

        }


    }


    public void init() {
        img_bk = findViewById(R.id.img_bk);
        ImageManipulation.loadImage(context, Preference.getString(context, Preference.BACKGROUND_IMAGE), img_bk);

        tv_skip = findViewById(R.id.tv_skip);
        tv_privacy_policy = findViewById(R.id.tv_privacy_policy);
        tv_terms = findViewById(R.id.tv_terms);
        tv_create_account = findViewById(R.id.tv_create_account);
        tv_forget_password = findViewById(R.id.tv_forget_password);
        btn_signin = findViewById(R.id.btn_signin);
        btn_guest_checkout = findViewById(R.id.btn_guest_checkout);

        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        aedt_email = findViewById(R.id.aedt_email);
        aedt_password = findViewById(R.id.aedt_password);
        chk_remember = findViewById(R.id.chk_remember);


        tv_skip.setPaintFlags(tv_skip.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        listner();
    }

    public void listner() {
        tv_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isvalidate()) {

                    String email = aedt_email.getText().toString();
                    String password = aedt_password.getText().toString();


                    LoginAPI(email, password);
                }
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(Preference.getString(context,Preference.BRANCH_ID).equalsIgnoreCase("")){
                    Intent intent = new Intent(activity,SelectStoreLocationActivity.class);
                    intent.putExtra("from","skip");
                    startActivity(intent);
                    finish();
                }else {*/
                Intent intent = new Intent(activity, SelectStoreLocationActivity.class);  //$$$ change HomeActivity to SelectStoreLocationActivity
                intent.putExtra("from", "skip");
                startActivity(intent);
                finish();
                /*  }*/   // $$$ comment code
            }
        });

        GetImageListAPI.GetImageList(context, new GetImageListAPI.GetImageAPIListner() {
            @Override
            public void OnChange() {
                ImageManipulation.loadImage(context, Preference.getString(context, Preference.BACKGROUND_IMAGE), img_bk);
            }
        });
        btn_guest_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PersonalDetail.class);
                intent.putExtra("from", "login");
                intent.putExtra("addToCartBean",addToCartBean);
                startActivity(intent);
                finish();
            }
        });

        chk_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chk_remember.isChecked())
                {
                    Preference.setString(context, Preference.REMEMBER_ME_STATUS, "1");
                }
                else {
                    Preference.setString(context, Preference.REMEMBER_ME_STATUS, "0");

                }
            }
        });

        tv_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PrivacyPolicyActivity.class);  //$$$ change HomeActivity to SelectStoreLocationActivity
                startActivity(intent);
            }
        });

        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TermsAndUseActivity.class);  //$$$ change HomeActivity to SelectStoreLocationActivity
                startActivity(intent);
            }
        });
    }

    //Validation
    public boolean isvalidate() {
        til_email.setError(null);
        til_password.setError(null);

        if (aedt_email.getText().toString().trim().isEmpty()) {
            til_email.setError("Please enter Email Address");
            return false;
        } else if (!Validation.isValidEmail(aedt_email.getText().toString().trim())) {
            til_email.setError("Please enter Valid Email Address");
            return false;
        } else if (aedt_password.getText().toString().trim().isEmpty()) {
            til_password.setError("Please Enter Password");
            return false;
        } else {
            return true;
        }
    }


    public void LoginAPI(String email, String password) {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("fcm_id", Preference.getString(context, Preference.FCM_token));
        map.put("device_type", "android");

        Utility.log_api_failure(AppConstants.login);
        ApiCall.volleyPostApi(context, AppConstants.login, map, new ApiCallBackListner() {
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
                            String phone_no = jsonObject.optString("phone_no");
                            String f_name = jsonObject.optString("f_name");
                            String l_name = jsonObject.optString("l_name");
                            String street_address = jsonObject.optString("street_address");
                            String apt_suite_floor = jsonObject.optString("apt_suite_floor");
                            String city_state_zip = jsonObject.optString("city_state_zip");
                            Preference.setString(context, Preference.USER_ID, user_id);
                            Preference.setString(context, Preference.USER_EMAIL, email);
                            Preference.setString(context, Preference.USER_PASSWORD, password);
                            Preference.setString(context, Preference.USER_FNAME, f_name);
                            Preference.setString(context, Preference.USER_LNAME, l_name);
                            Preference.setString(context, Preference.USER_STREET_ADDRESS, street_address);
                            Preference.setString(context, Preference.USER_CITY_STATE_ZIP, city_state_zip);
                            Preference.setString(context, Preference.USER_SUITE_FLOOR, apt_suite_floor);
                            Preference.setString(context, Preference.USER_PHONE, phone_no);
                            Preference.setString(context, Preference.GUEST_STATUS, "0");

                            if(from.equalsIgnoreCase("Order_Detail")){
                                AddToCartApi.addToCartAPI(context, activity, addToCartBean);
                            }else {
                                Intent intent = new Intent(activity, SelectStoreLocationActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                                intent.putExtra("from","login");
                                startActivity(intent);
                                finish();
                            }

                            /*if(Preference.getString(context,Preference.BRANCH_ID).equalsIgnoreCase("")){
                                Intent intent = new Intent(activity,SelectStoreLocationActivity.class);
                                intent.putExtra("from","login");
                                startActivity(intent);
                                finish();
                            }else {   $$$ comment if*/

//                            }

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

                    } else {
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





    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

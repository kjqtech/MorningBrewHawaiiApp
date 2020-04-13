package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//Packages name to be changed

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView tv_back_login;

    TextInputLayout til_email;
    AppCompatEditText aedt_email;

    Button btn_send_password;

    Activity activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        activity = this;
        context = this;

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void init() {
        tv_back_login = findViewById(R.id.tv_back_login);
        btn_send_password = findViewById(R.id.btn_send_password);

        til_email = findViewById(R.id.til_email);
        aedt_email = findViewById(R.id.aedt_email);

        listner();
    }

    public void listner() {
        tv_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_send_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordAPI();
                //onBackPressed();
                /*
                if(isvalidate()){
                    String email = aedt_email.getText().toString();
                    ForgotPasswordAPI(email);
                }
*/
            }
        });
    }

    //Validation
    public boolean isvalidate() {

        til_email.setError(null);

        if (aedt_email.getText().toString().isEmpty()) {
            til_email.setError("Please enter Email Address");
            return false;
        } else if (!Validation.isValidEmail(aedt_email.getText())) {
            til_email.setError("Please enter Valid Email Address");
            return false;
        } else {
            return true;
        }
    }

    public void ForgotPasswordAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("email", aedt_email.getText().toString());

        Utility.log_api_failure(AppConstants.forgot_password);
        ApiCall.volleyPostApi(context, AppConstants.forgot_password, map, new ApiCallBackListner() {
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


                            Intent intent = new Intent(activity, LoginActivity.class); // $$$ HomeActivity to SelectStoreLocationActivity
                            intent.putExtra("from", "forget_password");
                            startActivity(intent);
                            finish();


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

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
import android.widget.ImageView;
import android.widget.TextView;


import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.commonapicall.GetImageListAPI;
import com.trec2go.MorningBrewHawaii.utility.ImageManipulation;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    TextView tv_back_login;

    Activity activity;
    Context context;

    TextInputLayout til_fname,til_lname,til_email,til_password,til_confirm_password,til_phone_number;
    AppCompatEditText aedt_fname,aedt_lname,aedt_email,aedt_password,aedt_confirm_password,aedt_phone_number;
    Button btn_signin;
    ImageView img_bk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        activity = this;
        context = this;

        init();

    }

    public void init(){

        img_bk = findViewById(R.id.img_bk);
        ImageManipulation.loadImage(context, Preference.getString(context,Preference.BACKGROUND_IMAGE),img_bk);

        tv_back_login = findViewById(R.id.tv_back_login);
        btn_signin = findViewById(R.id.btn_signin);

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
        listner();
    }

    public void listner(){
        tv_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isvalidate()){
                    String email = aedt_email.getText().toString();
                    String fname = aedt_fname.getText().toString();
                    String lname = aedt_lname.getText().toString();
                    String password = aedt_password.getText().toString();
                    String phone_number = aedt_phone_number.getText().toString();
                    Intent intent = new Intent(activity, RegisterAddressActivity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("fname",fname);
                    intent.putExtra("lname",lname);
                    intent.putExtra("password",password);
                    intent.putExtra("phone_number",phone_number);
                    startActivity(intent);
                    finish();
                }


            }
        });

        GetImageListAPI.GetImageList(context, new GetImageListAPI.GetImageAPIListner() {
            @Override
            public void OnChange() {
                ImageManipulation.loadImage(context,Preference.getString(context,Preference.BACKGROUND_IMAGE),img_bk);
            }
        });
    }

    //Validation
    public boolean isvalidate(){
        til_fname.setError(null);
        til_lname.setError(null);
        til_email.setError(null);
        til_password.setError(null);
        til_confirm_password.setError(null);
        til_phone_number.setError(null);

        if(aedt_fname.getText().toString().trim().isEmpty()){
            til_fname.setError("Please enter First Name");
            return false;
        }else if(aedt_lname.getText().toString().trim().isEmpty()){
            til_lname.setError("Please enter Last Name");
            return false;
        }else if(aedt_email.getText().toString().trim().isEmpty()){
            til_email.setError("Please enter Email Address");
            return false;
        }else if(!Validation.isValidEmail(aedt_email.getText().toString().trim())){
            til_email.setError("Please enter Valid Email Address");
            return false;
        }else if(aedt_password.getText().toString().trim().isEmpty()){
            til_password.setError("Please Enter Password");
            return false;
        }else if(aedt_confirm_password.getText().toString().trim().isEmpty()){
            til_confirm_password.setError("Please Enter Confirm Password");
            return false;
        }else if(!aedt_password.getText().toString().trim().equalsIgnoreCase(aedt_confirm_password.getText().toString())){
            til_confirm_password.setError("Password & Confirm Password must be same");
            return false;
        }else if(aedt_phone_number.getText().toString().trim().isEmpty()){
            til_phone_number.setError("Please Enter Phone Number");
            return false;
        }else if(!Validation.isValidPhoneNumber(aedt_phone_number.getText())){
            til_phone_number.setError("Please enter Valid Phone Number");
            return false;
        }else {
            return true;
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

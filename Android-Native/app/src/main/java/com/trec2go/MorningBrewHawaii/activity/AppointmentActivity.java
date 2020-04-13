package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AppointmentActivity extends AppCompatActivity {

    AppCompatEditText apt_booking_date,apt_booking_time,aedt_name,aedt_phone,aedt_your_comment;
    TextInputLayout til_booking_date,til_booking_time,til_name,til_phone,til_your_comment;
    ImageView img_back_arrow;
    Button btn_appointment_booking;
    TextView tv_time_error;

    Activity actvity;
    Context context;
    final Calendar myCalendar = Calendar.getInstance();

    Spinner spn_time;
    ArrayList<String> time = new ArrayList<>();
    ArrayAdapter adapter;

    String booking_time = "";
    String booking_date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        actvity = this;
        context = this;

        img_back_arrow = findViewById(R.id.img_back_arrow);
        btn_appointment_booking = findViewById(R.id.btn_appointment_booking);
        spn_time = findViewById(R.id.spn_time);
        aedt_name = findViewById(R.id.aedt_name);
        til_name = findViewById(R.id.til_name);
        aedt_phone = findViewById(R.id.aedt_phone);
        til_phone = findViewById(R.id.til_phone);
        aedt_your_comment = findViewById(R.id.aedt_your_comment);
        til_your_comment = findViewById(R.id.til_your_comment);
        tv_time_error = findViewById(R.id.tv_time_error);

        til_booking_date = findViewById(R.id.til_booking_date);
        apt_booking_date = findViewById(R.id.apt_booking_date);
        apt_booking_date.setFocusable(false);
        apt_booking_date.setClickable(true);

        til_booking_time= findViewById(R.id.til_booking_time);
        apt_booking_time = findViewById(R.id.apt_booking_time);
        apt_booking_time.setFocusable(false);
        apt_booking_time.setClickable(true);

        if(!Preference.getString(context, Preference.USER_ID).equalsIgnoreCase(""))
        {
            aedt_name.setText(Preference.getString(context, Preference.USER_FNAME) + " " + Preference.getString(context, Preference.USER_LNAME));
            aedt_phone.setText(Preference.getString(context, Preference.USER_PHONE));
        }

        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get Current time
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);
        apt_booking_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,R.style.TimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {



                                apt_booking_time.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        apt_booking_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.TimePickerTheme,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        GetTimeListAPI(sdf.format(myCalendar.getTime()));

                        updateLabel();
                    }

                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        btn_appointment_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidate()){
                    AppointmentAPI();
                }
            }
        });

        spn_time = findViewById(R.id.spn_time);

        adapter = new ArrayAdapter(this,R.layout.spinner_textview,time);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_time.setAdapter(adapter);
        spn_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_time_error.setVisibility(View.GONE);
                booking_time = "";
                booking_time = time.get(position);
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

    public void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        apt_booking_date.setText(sdf.format(myCalendar.getTime()));
    }

    public boolean isValidate(){

        til_name.setError(null);
        til_phone.setError(null);
        til_booking_date.setError(null);
        tv_time_error.setVisibility(View.GONE);

        if(aedt_name.getText().toString().trim().equalsIgnoreCase("")){
            til_name.setError("Please Enter Name");
            return false;
        }else if(aedt_phone.getText().toString().trim().equalsIgnoreCase("")){
            til_phone.setError("Please Enter Phone number");
            return false;
        }else if(!Validation.isValidPhoneNumber(aedt_phone.getText().toString().trim())){
            til_phone.setError("Please Enter Valid Phone number");
            return false;
        }else if(booking_date.trim().equalsIgnoreCase("")){
            til_booking_date.setError("Please Select Appointment Date");
            return false;
        }else if(booking_time.trim().equalsIgnoreCase("")){
            tv_time_error.setText("Please Select Appointment Time");
            tv_time_error.setVisibility(View.VISIBLE);
            return false;
        }else {
            return true;
        }
    }

    //Api calling
    public void AppointmentAPI(){
        Utility.createProgressDialoge(context,"Loading");
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        map.put("branch_id",Preference.getString(context,Preference.BRANCH_ID));
        map.put("book_name",aedt_name.getText().toString().trim());
        map.put("book_phone_no",aedt_phone.getText().toString().trim());
        map.put("book_date",booking_date);
        map.put("book_time",booking_time);
        map.put("book_comment",aedt_your_comment.getText().toString().trim());


        ApiCall.volleyPostApi(context, AppConstants.save_appointment_details, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONObject){
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
                    }else if (json instanceof JSONArray){
                        JSONArray jsonArray = (JSONArray) json;
                        JSONObject jsonObject = jsonArray.optJSONObject(0);
                        String status = jsonObject.optString("status");
                        if(status.equalsIgnoreCase("1")){
                            Utility.dissmisProgress();
                            String msg = jsonObject.optString("message");
                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE,msg, new DialogCallBackListner() {
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

    public void GetTimeListAPI(String date){
        booking_date = date;
        Utility.createProgressDialoge(context,"Loading");
        Map<String,String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context,Preference.GLOBAL_UNAME));
        map.put("branch_id",Preference.getString(context,Preference.BRANCH_ID));
        map.put("booking_date",date);
        map.put("delivery_type","A");
        ApiCall.volleyPostApi(context, AppConstants.get_appointment_booking_slots, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONArray){
                        time.clear();
                        JSONArray jsonArray = (JSONArray) json;
                        if(jsonArray.length() == 0)
                        {
                            AlertDialog dialog;
                            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.fullwidthdialog);
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            View dialogLayout = inflater.inflate(R.layout.message_bottom_popup, null);

                            dialog = builder.create();
                            dialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            dialog.setView(dialogLayout, 0, 0, 0, 0);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setCancelable(true);

                            TextView tv_label_1 = dialogLayout.findViewById(R.id.tv_label_1);
                            tv_label_1.setText("No Time Available for this date");

                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            Window window = dialog.getWindow();
                            lp.copyFrom(window.getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.gravity= Gravity.BOTTOM;
                            window.setAttributes(lp);


                            builder.setView(dialogLayout);

                            dialog.show();
                        }
                        else {
                            time.clear();
                            for(int i=0;i<jsonArray.length();i++){
                                try {
                                    final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                    final Date dateObj = sdf.parse(jsonArray.getString(i));
                                    System.out.println(dateObj);
                                    System.out.println(new SimpleDateFormat("hh:mm a").format(dateObj));
                                    time.add(new SimpleDateFormat("hh:mm a").format(dateObj));
                                } catch (final ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }

                        adapter = new ArrayAdapter(AppointmentActivity.this,R.layout.spinner_textview,time);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_time.setAdapter(adapter);
                    }
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

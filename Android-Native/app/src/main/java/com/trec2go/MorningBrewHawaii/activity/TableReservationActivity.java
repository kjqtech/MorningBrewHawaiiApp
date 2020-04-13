package com.trec2go.MorningBrewHawaii.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;


import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.commonapicall.GetAppoinmentbookingSloat;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import com.trec2go.MorningBrewHawaii.utility.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TableReservationActivity extends AppCompatActivity {

    AppCompatEditText apt_booking_date, apt_booking_time, aedt_name, aedt_email, aedt_phone, aedt_no_of_persons, aedt_your_comment;
    TextInputLayout til_booking_date, til_booking_time, til_name, til_email, til_phone, til_no_of_persons, til_your_comment;
    ImageView img_back_arrow;
    Button btn_table_reservation;
    TextView tv_time_error;

    Activity actvity;
    Context context;
    final Calendar myCalendar = Calendar.getInstance();

    String booking_date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_reservation);
        actvity = this;
        context = this;

        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        aedt_name = findViewById(R.id.aedt_name);
        aedt_email = findViewById(R.id.aedt_email);
        aedt_phone = findViewById(R.id.aedt_phone);
        aedt_no_of_persons = findViewById(R.id.aedt_no_of_persons);
        aedt_your_comment = findViewById(R.id.aedt_your_comment);
        til_name = findViewById(R.id.til_name);
        til_email = findViewById(R.id.til_email);
        til_phone = findViewById(R.id.til_phone);
        til_no_of_persons = findViewById(R.id.til_no_of_persons);
        til_your_comment = findViewById(R.id.til_your_comment);
        btn_table_reservation = findViewById(R.id.btn_table_reservation);

        til_booking_date = findViewById(R.id.til_booking_date);
        apt_booking_date = findViewById(R.id.apt_booking_date);
        apt_booking_date.setFocusable(false);
        apt_booking_date.setClickable(true);

        if(!Preference.getString(context, Preference.USER_ID).equalsIgnoreCase(""))
        {
            aedt_name.setText(Preference.getString(context, Preference.USER_FNAME) + " " + Preference.getString(context, Preference.USER_LNAME));
            aedt_email.setText(Preference.getString(context, Preference.USER_EMAIL));
            aedt_phone.setText(Preference.getString(context, Preference.USER_PHONE));
        }

        /*til_booking_time = findViewById(R.id.til_booking_time);
        apt_booking_time = findViewById(R.id.apt_booking_time);
        apt_booking_time.setFocusable(false);
        apt_booking_time.setClickable(true);*/

        tv_time_error = findViewById(R.id.tv_time_error);

        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);
        /*apt_booking_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(context,R.style.TimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                Date date = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + date);
                                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
                                String formattedDate = df.format(date);

                                if(apt_booking_date.getText().toString().equalsIgnoreCase(formattedDate))
                                {
                                    Log.e(String.valueOf(hourOfDay), String.valueOf(Calendar.getInstance().get(Calendar.HOUR)));
                                    if( hourOfDay  >= Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                                    {
                                        boolean isPM = (hourOfDay >= 12);
                                        apt_booking_time.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                                    }
                                    else {
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
                                        tv_label_1.setText("This time is already expired.");

                                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                        Window window = dialog.getWindow();
                                        lp.copyFrom(window.getAttributes());
                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                        lp.gravity= Gravity.BOTTOM;
                                        window.setAttributes(lp);


                                        builder.setView(dialogLayout);
                                        apt_booking_time.setText(" ");
                                        dialog.show();
                                    }
                                }
                                else {
                                    boolean isPM = (hourOfDay >= 12);
                                    apt_booking_time.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                                }
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });*/


        apt_booking_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        GetAppoinmentbookingSloat.GetTimeListAPI(context, actvity, sdf.format(myCalendar.getTime()));


                        booking_date = sdf.format(myCalendar.getTime());


                        updateLabel();
                    }

                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        btn_table_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    ReservationAPI();
                }
            }
        });

    }

    public boolean isValidate() {
        til_name.setError(null);
        til_email.setError(null);
        til_phone.setError(null);
        til_no_of_persons.setError(null);
        til_booking_date.setError(null);
        //til_booking_time.setError(null);

        if (aedt_name.getText().toString().trim().equalsIgnoreCase("")) {
            til_name.setError("Please Enter Name");
            return false;
        } else if (aedt_email.getText().toString().trim().equalsIgnoreCase("")) {
            til_email.setError("Please Enter Email Address");
            return false;
        } else if (!Validation.isValidEmail(aedt_email.getText().toString().trim())) {
            til_email.setError("Please Enter Valid Email Address");
            return false;
        } else if (aedt_phone.getText().toString().trim().equalsIgnoreCase("")) {
            til_phone.setError("Please Enter Phone number");
            return false;
        } else if (!Validation.isValidPhoneNumber(aedt_phone.getText().toString().trim())) {
            til_phone.setError("Please Enter Valid Phone number");
            return false;
        } else if (aedt_no_of_persons.getText().toString().trim().equalsIgnoreCase("")) {
            til_no_of_persons.setError("Please Enter No. of Persons");
            return false;
        } else if (apt_booking_date.getText().toString().trim().equalsIgnoreCase("")) {
            til_booking_date.setError("Please Select Reservation Date");
            return false;
        } else if (GetAppoinmentbookingSloat.booking_time.equalsIgnoreCase("")) {
            tv_time_error.setVisibility(View.VISIBLE);
            tv_time_error.setText("Please Select Reservation Time");
            return false;
        } else {
            return true;
        }
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

    //Api calling
    public void ReservationAPI() {
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        map.put("book_name", aedt_name.getText().toString().trim());
        map.put("book_email", aedt_email.getText().toString().trim());
        map.put("book_phone_no", aedt_phone.getText().toString().trim());
        map.put("book_no_of_person", aedt_no_of_persons.getText().toString().trim());
        map.put("book_date", booking_date);
        map.put("book_time", GetAppoinmentbookingSloat.booking_time);
        map.put("book_comment", aedt_your_comment.getText().toString().trim());


        ApiCall.volleyPostApi(context, AppConstants.reservation, map, new ApiCallBackListner() {
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
                            String msg = jsonObject.optString("msg");
                            com.trec2go.MorningBrewHawaii.utility.SweetAlertDialog.sweetPopup(context, SweetAlertDialog.SUCCESS_TYPE, msg, new DialogCallBackListner() {
                                @Override
                                public void setPositiveListner() {
                                    //Cliked Ok
                                    finish();
                                }

                                @Override
                                public void setNegativeListner() {

                                }
                            });
                        } else {
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
}

package com.trec2go.MorningBrewHawaii.commonapicall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.trec2go.MorningBrewHawaii.Interface.ApiCallBackListner;
import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;
import com.trec2go.MorningBrewHawaii.utility.ApiCall;
import com.trec2go.MorningBrewHawaii.utility.AppConstants;
import com.trec2go.MorningBrewHawaii.utility.Preference;
import com.trec2go.MorningBrewHawaii.utility.Utility;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class GetAppoinmentbookingSloat {


    static Spinner spn_time;
    static ArrayList<String> time = new ArrayList<>();
    static ArrayAdapter adapter;

    public static String booking_time = "";
    String booking_date = "";

    public static void GetTimeListAPI(final Context context, final Activity activity, String date) {
        spn_time = activity.findViewById(R.id.spn_time);
        String booking_date = date;
        Utility.createProgressDialoge(context, "Loading");
        Map<String, String> map = new HashMap<>();
        map.put("gruname", Preference.getString(context, Preference.GLOBAL_UNAME));
        map.put("branch_id", Preference.getString(context, Preference.BRANCH_ID));
        map.put("booking_date", date);
        map.put("delivery_type", "T");
        ApiCall.volleyPostApi(context, AppConstants.get_appointment_booking_slots, map, new ApiCallBackListner() {
            @Override
            public void onResponse(String response) {

                Object json = null;
                try {
                    json = new JSONTokener(response).nextValue();
                    if (json instanceof JSONArray) {
                        time.clear();
                        JSONArray jsonArray = (JSONArray) json;
                        if (jsonArray.length() == 0) {
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
                            tv_label_1.setText("No Time Available for this date");

                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            Window window = dialog.getWindow();
                            lp.copyFrom(window.getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.gravity = Gravity.BOTTOM;
                            window.setAttributes(lp);


                            builder.setView(dialogLayout);

                            dialog.show();
                        } else {
                            time.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
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

                        adapter = new ArrayAdapter(activity, R.layout.spinner_textview, time);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_time.setAdapter(adapter);
                        spn_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                               activity.tv_time_error.setVisibility(View.GONE);
                                booking_time = "";
                                booking_time = time.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
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
}

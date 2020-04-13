package com.trec2go.MorningBrewHawaii.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;
import com.trec2go.MorningBrewHawaii.R;

public class SweetAlertDialog {


    //====================SHOW MESSAGE
        /*SweetAlertDialog.sweetPopup(getContext(), SweetAlertDialog.NORMAL_TYPE, "Hi Jaina ! Welcome to Asferi Collection", new DialogCallBackListner() {
            @Override
            public void setPositiveListner() {
                //Cliked Ok
            }

            @Override
            public void setNegativeListner() {

            }
        });*/

    //====================SHOW SUCCESS TYPE MESSAGE
        /*SweetAlertDialog.sweetPopup(getContext(), SweetAlertDialog.SUCCESS_TYPE, "Login Successfull", new DialogCallBackListner() {
            @Override
            public void setPositiveListner() {
                //Cliked Ok
            }

            @Override
            public void setNegativeListner() {

            }
        });*/

    //======================SHOW ERROR TYPE MESSAGE
        /*SweetAlertDialog.sweetPopup(getContext(), SweetAlertDialog.ERROR_TYPE, "No Response from Server", new DialogCallBackListner() {
            @Override
            public void setPositiveListner() {
                //Cliked Ok
            }

            @Override
            public void setNegativeListner() {

            }
        });*/



    public static void sweetPopup(final Context mContext, Integer sweetAlertType, String title, final DialogCallBackListner dialogCallBackListner){
        cn.pedant.SweetAlert.SweetAlertDialog sweetAlertDialog = new cn.pedant.SweetAlert.SweetAlertDialog(mContext);


/*
        Window window = sweetAlertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
*/


        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.colorAccent));

        sweetAlertDialog.changeAlertType(sweetAlertType);
        sweetAlertDialog.setTitleText("");
        sweetAlertDialog.setContentText(title);
        sweetAlertDialog.setConfirmClickListener(new cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(cn.pedant.SweetAlert.SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                dialogCallBackListner.setPositiveListner();
//                Toast.makeText(mContext,"Click",Toast.LENGTH_LONG).show();
            }
        });

        sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final cn.pedant.SweetAlert.SweetAlertDialog alertDialog = (cn.pedant.SweetAlert.SweetAlertDialog) dialog;

                LinearLayout loading = (LinearLayout) alertDialog.findViewById(R.id.loading);

                TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                text.setTextSize(12);

                TextView contextText = (TextView) alertDialog.findViewById(R.id.content_text);
                contextText.setTextSize(12);
                contextText.setMinLines(2);
                contextText.setMaxLines(5);   //$$$ chage max line 3 to 5

                Button confirmButton = (Button) alertDialog.findViewById(R.id.confirm_button);
                confirmButton.setBackgroundResource(R.drawable.sweet_alert_button);
                confirmButton.setPadding(0,1,0,1);
                // confirmButton.setPadding(mContext.getResources().getInteger(R.integer.size_int_3dp), mContext.getResources().getInteger(R.integer.size_int_3dp), mContext.getResources().getInteger(R.integer.size_int_3dp), mContext.getResources().getInteger(R.integer.size_int_3dp));
                confirmButton.setTextSize(12);
                confirmButton.setWidth(14);



            }
        });
        // sweetAlertDialog.setContentText("Here's a custom image.");
        sweetAlertDialog.show();
    }
    public static void sweetCallingPopup(final Context mContext, Integer sweetAlertType, String title, final DialogCallBackListner dialogCallBackListner){
        cn.pedant.SweetAlert.SweetAlertDialog sweetAlertDialog = new cn.pedant.SweetAlert.SweetAlertDialog(mContext);


/*
        Window window = sweetAlertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
*/


        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.colorAccent));

        sweetAlertDialog.changeAlertType(sweetAlertType);
        sweetAlertDialog.setTitleText("");
        sweetAlertDialog.setContentText(title);
        sweetAlertDialog.setConfirmText("Call");
        sweetAlertDialog.setCancelButton("Cancel", new cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(cn.pedant.SweetAlert.SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                dialogCallBackListner.setNegativeListner();
            }
        });
        sweetAlertDialog.setConfirmClickListener(new cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(cn.pedant.SweetAlert.SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                dialogCallBackListner.setPositiveListner();
//                Toast.makeText(mContext,"Click",Toast.LENGTH_LONG).show();
            }
        });

        sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final cn.pedant.SweetAlert.SweetAlertDialog alertDialog = (cn.pedant.SweetAlert.SweetAlertDialog) dialog;

                LinearLayout loading = (LinearLayout) alertDialog.findViewById(R.id.loading);

                TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                text.setTextSize(12);

                TextView contextText = (TextView) alertDialog.findViewById(R.id.content_text);
                contextText.setTextSize(12);
                contextText.setMinLines(2);
                contextText.setMaxLines(5);

                Button confirmButton = (Button) alertDialog.findViewById(R.id.confirm_button);
                confirmButton.setBackgroundResource(R.drawable.sweet_alert_button);
                confirmButton.setPadding(0,1,0,1);
                // confirmButton.setPadding(mContext.getResources().getInteger(R.integer.size_int_3dp), mContext.getResources().getInteger(R.integer.size_int_3dp), mContext.getResources().getInteger(R.integer.size_int_3dp), mContext.getResources().getInteger(R.integer.size_int_3dp));
                confirmButton.setTextSize(12);
                confirmButton.setWidth(14);

                Button cancelButton = (Button) alertDialog.findViewById(R.id.cancel_button);
                cancelButton.setBackgroundResource(R.drawable.sweet_alert_button);
                cancelButton.setPadding(0,1,0,1);
                // confirmButton.setPadding(mContext.getResources().getInteger(R.integer.size_int_3dp), mContext.getResources().getInteger(R.integer.size_int_3dp), mContext.getResources().getInteger(R.integer.size_int_3dp), mContext.getResources().getInteger(R.integer.size_int_3dp));
                cancelButton.setTextSize(12);
                cancelButton.setWidth(14);

            }
        });
        // sweetAlertDialog.setContentText("Here's a custom image.");
        sweetAlertDialog.show();
    }



}

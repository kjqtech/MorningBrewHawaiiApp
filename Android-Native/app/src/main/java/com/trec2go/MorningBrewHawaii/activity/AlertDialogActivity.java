package com.trec2go.MorningBrewHawaii.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trec2go.MorningBrewHawaii.Interface.DialogCallBackListner;

import com.trec2go.MorningBrewHawaii.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AlertDialogActivity extends Activity {
    Context context ;
    Activity activity ;
    static SweetAlertDialog sweetAlertDialog ;

    TextView tv_notification, tv_notification_title ;
    Button btn_ok;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);

        activity = this;
        context = this;


        String notificationTitle = getIntent().getStringExtra("NOTIFICATION_TITLE");
        String notificationBody = getIntent().getStringExtra("NOTIFICATION_BODY");
        sweetPopup(context, SweetAlertDialog.NORMAL_TYPE, notificationBody, notificationTitle, new DialogCallBackListner() {
            @Override
            public void setPositiveListner() {

            }

            @Override
            public void setNegativeListner() {

            }
        });




    /*    tv_notification_title = findViewById(R.id.tv_notification_title);
        tv_notification = findViewById(R.id.tv_notification);
        btn_ok = findViewById(R.id.btn_ok);

        tv_notification_title.setText(notificationTitle);
        tv_notification.setText(notificationBody);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    public static void sweetPopup(final Context mContext, Integer sweetAlertType, String title ,String body, final DialogCallBackListner dialogCallBackListner){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(mContext);


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
        sweetAlertDialog.setTitleText(body);
        sweetAlertDialog.setContentText(title);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialogCallBackListner.setPositiveListner();
                sweetAlertDialog.dismissWithAnimation();
            }
        });

        sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(final DialogInterface dialog) {
                final SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;

                LinearLayout loading = (LinearLayout) alertDialog.findViewById(R.id.loading);

                TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                text.setTextColor(R.color.black);
                text.setTextSize(18);

                TextView contextText = (TextView) alertDialog.findViewById(R.id.content_text);
                contextText.setTextSize(12);
                contextText.setMinLines(5);
                contextText.setMaxLines(6);

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


}

package com.trec2go.MorningBrewHawaii.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.trec2go.MorningBrewHawaii.R;

public class Utility {

    //ProgressDialog
    private static ProgressDialog progressDialog;

    public static ProgressDialog createProgressDialoge(Context context, String title) {
        if(progressDialog != null){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
        progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.show();
        progressDialog.setMessage("");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static void dissmisProgress() {
        progressDialog.dismiss();
    }

    //Log , Toast & SanackBar
    public static void toast(Activity activity, String message)
    {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void log(String message) {
        Log.v(Config.TAG, " " +  message);
    }

    public static void log_api_request(String message) {
        Log.d(Config.TAG ,Config.API_REQUEST + " " + message);
    }

    public static void log_api_response(String message) {
        Log.d(Config.TAG,Config.API_RESPONSE + " " +message);
    }

    public static void log_api_failure(String message) {
        Log.e(Config.TAG," " +  message);
    }

    public static void open_snack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static boolean edittextIsEmpty(TextInputEditText editText){
        return editText.getText().toString().trim().equalsIgnoreCase("") || editText.getText().toString().trim().equalsIgnoreCase("null");
    }

    public static boolean stringIsEmpty(String s){
        return s.equalsIgnoreCase("") || s.isEmpty();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    /*public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    Log.e("Utility", "app is running");
                    return true;
                }
            }
        }
        Log.e("Utility", "app not running");
        return false;
    }  //$$$ add function isApprunning or not

    */
public static String stringMatipulation(String s)
{
    StringBuilder str = new StringBuilder(s);
    //System.out.println("string = " + str);

    // insert character at offset 8
    str.insert(0, '(');
    str.insert(4, ')');
    str.insert(5, ' ');
    str.insert(9, ' ');
    str.insert(10, '-');
    str.insert(11, ' ');

    String string = new String(str);
    return string;
}




}

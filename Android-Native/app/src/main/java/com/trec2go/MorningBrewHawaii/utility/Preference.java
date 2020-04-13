package com.trec2go.MorningBrewHawaii.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private static SharedPreferences preferences;
    public static String UserPreference = "UserPreference";

    //Preference Name
    public static String FCM_token = "fcm_token";
    public static String BRANCH_ID = "branch_id";
    public static String BRANCH_NAME = "branch_name";
    public static String USER_ID = "user_id";
    public static String USER_EMAIL = "email";
    public static String USER_PASSWORD = "password";
    public static String REMEMBER_ME_STATUS = "remember_me_status";

    public static String USER_FNAME = "fname";
    public static String USER_LNAME = "lname";
    public static String USER_STREET_ADDRESS = "street_address";
    public static String USER_SUITE_FLOOR = "apt_suite_floor";
    public static String USER_CITY_STATE_ZIP = "city_state_zip";
    public static String USER_PHONE = "phone";
    public static String GLOBAL_UNAME = "global uname";
    public static String GUEST_STATUS = "guest_status";

    public static String CUSTOM_ORDER = "custom_order";

    public static String REORDER_BRANCH_ID = "reorder_branch_id";
    public static String REORDER_STATUS = "status";


    public static String LOGO = "logo";
    public static String BACKGROUND_IMAGE = "back_image";
    public static String MENU_BACKGROUND_IMAGE = "menu_back_image";

    public static String PAYMENT_METHOD = "payment method";
    public static String IS_OPENED_FIRST_TIME = "is open first time";
    public static String IS_DONATION_VISIBLE = "is donation";
    public static String IS_PROPERTY_MAP_VISIBLE = "is property map";
    public static String DONATION = "donation link";
    public static String IS_TABLE = "is table";
    public static String IS_APPOINTMENT = "is appointment";
    public static String IS_MENU = "is menu";
    public static String MENU_TEXT = "menu_text";
    public static String PROPERTY_IMAGE = "property_image";
    public static String ABOUT_US = "about_us";
    public static String from = "from";

    public static String DOORDASH_TEXT = "doordash_text";
    public static String DOORDASH_LINK = "doordash_link";

    public static String CATERING_CART_ID = "catering_cart_id";
    public static String CATERING_STATUS = "catering_status";

    //APP STORAGE DATAABOUT_US
   /* public static String STORAGE_CATEGORY = "category";
    public static String STORAGE_SUB_CATEGORY = "sub category";
    public static String STORAGE_YOUR_BAG = "your_bag";*/
    public static String STORAGE_YOUR_BAG_ID = "your_bag_id";
    public static String STORAGE_Delivery = "delivery";
    public static String STORAGE_Pickup = "pickup";

    //Color name
    public static String splash_back = "splash_back";
    public static String FAX_STATUS = "fax_status";




    public static String getString(Context context, String pref_String) {
        preferences = null;
        preferences = context.getSharedPreferences(UserPreference, Context.MODE_PRIVATE);
        String s = preferences.getString(pref_String, "");
        return s;
    }

    public static void setString(Context context,String pref_String,String s) {
        preferences = null;
        preferences = context.getSharedPreferences(UserPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(pref_String, s);
        editor.commit();
    }
    public static int getColor(Context context, String pref_String) {
        preferences = null;
        preferences = context.getSharedPreferences(UserPreference, Context.MODE_PRIVATE);
        int s = preferences.getInt(pref_String, 0);
        return s;
    }

    public static void setColor(Context context,String pref_String,int s) {
        preferences = null;
        preferences = context.getSharedPreferences(UserPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(pref_String, s);
        editor.commit();
    }


}

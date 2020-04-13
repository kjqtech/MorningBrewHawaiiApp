package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AddToCartBean implements Parcelable {

    @SerializedName("menu_id")
    public String menu_id;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("quantity")
    public String quantity;

    @SerializedName("menuadd")
    public String menuadd;

    @SerializedName("item_discount")
    public String item_discount;

    @SerializedName("choose_sides")
    public String choose_sides;

    @SerializedName("main_menu")
    public String main_menu;

    @SerializedName("advance_menu")
    public String advance_menu;

    @SerializedName("advance_menu_qty")
    public String advance_menu_qty;

    @SerializedName("advance_menu_extracharge")
    public String advance_menu_extracharge;

    @SerializedName("branch_id")
    public String branch_id;

    @SerializedName("amount")
    public String amount;

    @SerializedName("gruname")
    public String gruname;

    @SerializedName("special_requests")
    public String special_requests;

    @SerializedName("is_catering")
    public String is_catering = "";


    protected AddToCartBean(Parcel in) {
        menu_id = in.readString();
        user_id = in.readString();
        quantity = in.readString();
        menuadd = in.readString();
        choose_sides = in.readString();
        main_menu = in.readString();
        advance_menu = in.readString();
        advance_menu_qty = in.readString();
        advance_menu_extracharge = in.readString();
        branch_id = in.readString();
        amount = in.readString();
        gruname = in.readString();
        special_requests = in.readString();
        is_catering = in.readString();
        item_discount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(menu_id);
        dest.writeString(user_id);
        dest.writeString(quantity);
        dest.writeString(menuadd);
        dest.writeString(choose_sides);
        dest.writeString(main_menu);
        dest.writeString(advance_menu);
        dest.writeString(advance_menu_qty);
        dest.writeString(advance_menu_extracharge);
        dest.writeString(branch_id);
        dest.writeString(amount);
        dest.writeString(gruname);
        dest.writeString(special_requests);
        dest.writeString(is_catering);
        dest.writeString(item_discount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddToCartBean> CREATOR = new Creator<AddToCartBean>() {
        @Override
        public AddToCartBean createFromParcel(Parcel in) {
            return new AddToCartBean(in);
        }

        @Override
        public AddToCartBean[] newArray(int size) {
            return new AddToCartBean[size];
        }
    };

    public AddToCartBean() {
    }
}



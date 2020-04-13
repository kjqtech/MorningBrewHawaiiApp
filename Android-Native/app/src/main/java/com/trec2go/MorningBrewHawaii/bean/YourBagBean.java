package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class YourBagBean implements Parcelable {


    @SerializedName("id")
    public String id;

    @SerializedName("item_id")
    public String item_id;

    @SerializedName("order_items")
    public String order_items;

    @SerializedName("quantity")
    public String quantity;

    @SerializedName("amount")
    public String amount;

    @SerializedName("item_unit_price")
    public String item_unit_price;

    @SerializedName("addons")
    public String addons;

    @SerializedName("choose_sides")
    public String choose_sides;

    @SerializedName("main_menu")
    public String main_menu;

    @SerializedName("advance_menu")
    public String advance_menu;

    @SerializedName("advance_menu_quantity")
    public String advance_menu_quantity;

    @SerializedName("advance_menu_charge")
    public String advance_menu_charge;

    @SerializedName("special")
    public String special = "0";

    @SerializedName("product_free_item")
    public String product_free_item;

    @SerializedName("product_free_price")
    public String product_free_price;

    @SerializedName("product_name")
    public String product_name;

    @SerializedName("reaming_free_item")
    public String reaming_free_item;

    @SerializedName("reaming_free_price")
    public String reaming_free_price;

    @SerializedName("order_custom_type")
    public String order_custom_type;

    @SerializedName("actual_price")
    public String actual_price;

    @SerializedName("special_discount")
    public String special_discount;

    @SerializedName("special_request")
    public String special_request;

    public double final_price = 0;

    protected YourBagBean(Parcel in) {
        id = in.readString();
        item_id = in.readString();
        order_items = in.readString();
        quantity = in.readString();
        amount = in.readString();
        item_unit_price = in.readString();
        addons = in.readString();
        choose_sides = in.readString();
        main_menu = in.readString();
        advance_menu = in.readString();
        advance_menu_quantity = in.readString();
        advance_menu_charge = in.readString();
        special = in.readString();
        product_free_item = in.readString();
        product_free_price = in.readString();
        product_name = in.readString();
        reaming_free_item = in.readString();
        reaming_free_price = in.readString();
        order_custom_type = in.readString();
        actual_price = in.readString();
        special_discount = in.readString();
        special_request = in.readString();
        final_price = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(item_id);
        dest.writeString(order_items);
        dest.writeString(quantity);
        dest.writeString(amount);
        dest.writeString(item_unit_price);
        dest.writeString(addons);
        dest.writeString(choose_sides);
        dest.writeString(main_menu);
        dest.writeString(advance_menu);
        dest.writeString(advance_menu_quantity);
        dest.writeString(advance_menu_charge);
        dest.writeString(special);
        dest.writeString(product_free_item);
        dest.writeString(product_free_price);
        dest.writeString(product_name);
        dest.writeString(reaming_free_item);
        dest.writeString(reaming_free_price);
        dest.writeString(order_custom_type);
        dest.writeString(actual_price);
        dest.writeString(special_discount);
        dest.writeString(special_request);
        dest.writeDouble(final_price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<YourBagBean> CREATOR = new Creator<YourBagBean>() {
        @Override
        public YourBagBean createFromParcel(Parcel in) {
            return new YourBagBean(in);
        }

        @Override
        public YourBagBean[] newArray(int size) {
            return new YourBagBean[size];
        }
    };

    public double getFinal_price() {
        return final_price;
    }

    public void setFinal_price(double final_price) {
        this.final_price = final_price;
    }

    public YourBagBean() {
    }


}



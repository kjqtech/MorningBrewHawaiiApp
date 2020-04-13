package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MenuItemBean implements Parcelable {



    @SerializedName("id")
    public String id;

    @SerializedName("menu_title")
    public String menu_title;

    @SerializedName("menu_description")
    public String menu_description;

    @SerializedName("menu_price")
    public String menu_price;

    @SerializedName("menulink")
    public String menulink;

    @SerializedName("ctype")
    public String ctype;

    @SerializedName("new_menu_price")
    public String new_menu_price;

    @SerializedName("menu_image")
    public String menu_image;

    @SerializedName("calories")
    public String calories;

    @SerializedName("category_name")
    public String category_name;

    @SerializedName("spcl_instruction_text")
    public String spcl_instruction_text;

    @SerializedName("actual_price")
    public String actual_price;

    @SerializedName("min_option_check")
    public String min_option_check = "";

    @SerializedName("is_catering")
    public String is_catering = "";

    @SerializedName("soldstatus")
    public String soldstatus = "0";


    @SerializedName("advance_menu")
    public com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class.advance_menu advance_menu;

    public MenuItemBean() {
    }

    protected MenuItemBean(Parcel in) {
        id = in.readString();
        menu_title = in.readString();
        menu_description = in.readString();
        menu_price = in.readString();
        menulink = in.readString();
        ctype = in.readString();
        new_menu_price = in.readString();
        menu_image = in.readString();
        calories = in.readString();
        category_name = in.readString();
        spcl_instruction_text = in.readString();
        actual_price = in.readString();
        min_option_check = in.readString();
        is_catering = in.readString();
        soldstatus = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(menu_title);
        dest.writeString(menu_description);
        dest.writeString(menu_price);
        dest.writeString(menulink);
        dest.writeString(ctype);
        dest.writeString(new_menu_price);
        dest.writeString(menu_image);
        dest.writeString(calories);
        dest.writeString(category_name);
        dest.writeString(spcl_instruction_text);
        dest.writeString(actual_price);
        dest.writeString(min_option_check);
        dest.writeString(is_catering);
        dest.writeString(soldstatus);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuItemBean> CREATOR = new Creator<MenuItemBean>() {
        @Override
        public MenuItemBean createFromParcel(Parcel in) {
            return new MenuItemBean(in);
        }

        @Override
        public MenuItemBean[] newArray(int size) {
            return new MenuItemBean[size];
        }
    };

    @Override
    public String toString() {
        return "MenuItemBean{" +
                "id='" + id + '\'' +
                ", menu_title='" + menu_title + '\'' +
                ", menu_description='" + menu_description + '\'' +
                ", menu_price='" + menu_price + '\'' +
                ", menulink='" + menulink + '\'' +
                ", ctype='" + ctype + '\'' +
                ", new_menu_price='" + new_menu_price + '\'' +
                ", menu_image='" + menu_image + '\'' +
                ", calories='" + calories + '\'' +
                ", category_name='" + category_name + '\'' +
                ", spcl_instruction_text='" + spcl_instruction_text + '\'' +
                ", actual_price='" + actual_price + '\'' +
                ", min_option_check='" + min_option_check + '\'' +
                ", is_catering='" + is_catering + '\'' +
                ", soldstatus='" + soldstatus + '\'' +
                ", advance_menu=" + advance_menu +
                '}';
    }
}



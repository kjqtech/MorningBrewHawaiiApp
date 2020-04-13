package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AddressBean implements Parcelable {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("u_id")
    public String u_id;

    @SerializedName("f_name")
    public String f_name;

    @SerializedName("l_name")
    public String l_name;

    @SerializedName("email")
    public String email;

    @SerializedName("phone_no")
    public String phone_no;

    @SerializedName("street_address")
    public String street_address;

    @SerializedName("city_state_zip")
    public String city_state_zip;

    @SerializedName("apt_suite_floor")
    public String apt_suite_floor;

    public AddressBean() {
    }

    protected AddressBean(Parcel in) {
        id = in.readString();
        name = in.readString();
        u_id = in.readString();
        f_name = in.readString();
        l_name = in.readString();
        email = in.readString();
        phone_no = in.readString();
        street_address = in.readString();
        city_state_zip = in.readString();
        apt_suite_floor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(u_id);
        dest.writeString(f_name);
        dest.writeString(l_name);
        dest.writeString(email);
        dest.writeString(phone_no);
        dest.writeString(street_address);
        dest.writeString(city_state_zip);
        dest.writeString(apt_suite_floor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel in) {
            return new AddressBean(in);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };
}

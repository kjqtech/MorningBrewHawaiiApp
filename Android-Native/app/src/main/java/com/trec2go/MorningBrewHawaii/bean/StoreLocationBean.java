package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StoreLocationBean implements Parcelable {

    @SerializedName("id")
    public String id;

    @SerializedName("branch_name")
    public String branch_name;

    @SerializedName("branch_address")
    public String branch_address;

    @SerializedName("branch_zip")
    public String branch_zip;

    @SerializedName("branch_phone")
    public String branch_phone;

    /*@SerializedName("lat")
    public String lat;

    @SerializedName("long")
    public String long_;*/

    @SerializedName("branch_lat")  // $$$ change lat & long key
    public String branch_lat;

    @SerializedName("branch_long")
    public String branch_long;

    @SerializedName("distance")
    public String distance;

    public StoreLocationBean() {
    }


    protected StoreLocationBean(Parcel in) {
        id = in.readString();
        branch_name = in.readString();
        branch_address = in.readString();
        branch_zip = in.readString();
        branch_phone = in.readString();
        /*lat = in.readString();
        long_ = in.readString();*/
        branch_lat = in.readString();
        branch_long = in.readString();
        distance = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(branch_name);
        dest.writeString(branch_address);
        dest.writeString(branch_zip);
        dest.writeString(branch_phone);
        dest.writeString(branch_lat); //$$$
        dest.writeString(branch_long);
        dest.writeString(distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StoreLocationBean> CREATOR = new Creator<StoreLocationBean>() {
        @Override
        public StoreLocationBean createFromParcel(Parcel in) {
            return new StoreLocationBean(in);
        }

        @Override
        public StoreLocationBean[] newArray(int size) {
            return new StoreLocationBean[size];
        }
    };
}

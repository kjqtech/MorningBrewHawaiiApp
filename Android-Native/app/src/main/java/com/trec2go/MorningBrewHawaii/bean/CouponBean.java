package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CouponBean implements Parcelable {

    @SerializedName("promo_code")
    public String promo_code;

    @SerializedName("discount")
    public String discount;

    @SerializedName("discount_type")
    public String discount_type;

    @SerializedName("expiration_date")
    public String expiration_date;

    public CouponBean() {
    }


    protected CouponBean(Parcel in) {
        promo_code = in.readString();
        discount = in.readString();
        discount_type = in.readString();
        expiration_date = in.readString();
    }

    public static final Creator<CouponBean> CREATOR = new Creator<CouponBean>() {
        @Override
        public CouponBean createFromParcel(Parcel in) {
            return new CouponBean(in);
        }

        @Override
        public CouponBean[] newArray(int size) {
            return new CouponBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(promo_code);
        dest.writeString(discount);
        dest.writeString(discount_type);
        dest.writeString(expiration_date);
    }
}

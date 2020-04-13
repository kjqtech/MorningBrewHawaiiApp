package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RecentOrdersBean implements Parcelable {


    @SerializedName("transaction_id")
    public String transaction_id;

    @SerializedName("id")
    public String id;

    @SerializedName("date")
    public String date;

    @SerializedName("amount")
    public String amount;

    @SerializedName("branch_id")
    public String branch_id;

    protected RecentOrdersBean(Parcel in) {
        transaction_id = in.readString();
        id = in.readString();
        date = in.readString();
        amount = in.readString();
        branch_id = in.readString();

    }

    public static final Creator<RecentOrdersBean> CREATOR = new Creator<RecentOrdersBean>() {
        @Override
        public RecentOrdersBean createFromParcel(Parcel in) {
            return new RecentOrdersBean(in);
        }

        @Override
        public RecentOrdersBean[] newArray(int size) {
            return new RecentOrdersBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(transaction_id);
        dest.writeString(id);
        dest.writeString(date);
        dest.writeString(amount);
        dest.writeString(branch_id);

    }
}



package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CitiesBean implements Parcelable {

    @SerializedName("city")
    public String states;

    protected CitiesBean(Parcel in) {
        states = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(states);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CitiesBean> CREATOR = new Creator<CitiesBean>() {
        @Override
        public CitiesBean createFromParcel(Parcel in) {
            return new CitiesBean(in);
        }

        @Override
        public CitiesBean[] newArray(int size) {
            return new CitiesBean[size];
        }
    };
}

package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StatesBean implements Parcelable {

    @SerializedName("states")
    public String states;

    @SerializedName("state_code")
    public String state_code;


    protected StatesBean(Parcel in) {
        states = in.readString();
        state_code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(states);
        dest.writeString(state_code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StatesBean> CREATOR = new Creator<StatesBean>() {
        @Override
        public StatesBean createFromParcel(Parcel in) {
            return new StatesBean(in);
        }

        @Override
        public StatesBean[] newArray(int size) {
            return new StatesBean[size];
        }
    };
}

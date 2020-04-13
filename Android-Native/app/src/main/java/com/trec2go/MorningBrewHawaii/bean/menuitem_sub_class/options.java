package com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class options implements Parcelable {


    public int counter = 0;

    public boolean isChecked = false;


    protected options(Parcel in) {
        counter = in.readInt();
        isChecked = in.readByte() != 0;
        name = in.readString();
        price = in.readString();
        extra_charge = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(counter);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(extra_charge);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<options> CREATOR = new Creator<options>() {
        @Override
        public options createFromParcel(Parcel in) {
            return new options(in);
        }

        @Override
        public options[] newArray(int size) {
            return new options[size];
        }
    };

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @SerializedName("name")
    public String name;

    @SerializedName("price")
    public String price = "0";

    @SerializedName("extra_charge")
    public String extra_charge = "";



    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

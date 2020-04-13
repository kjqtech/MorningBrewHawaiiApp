package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CategoryBean implements Parcelable {

    @SerializedName("id")
    public String id;

    @SerializedName("branch_name")
    public String branch_name;

    @SerializedName("name")
    public String name;

    @SerializedName("menulink")
    public String menulink;

    @SerializedName("has_sub_category")
    public String has_sub_category = "";

    public CategoryBean() {
    }

    protected CategoryBean(Parcel in) {
        id = in.readString();
        branch_name = in.readString();
        name = in.readString();
        menulink = in.readString();
        has_sub_category = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(branch_name);
        dest.writeString(name);
        dest.writeString(menulink);
        dest.writeString(has_sub_category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryBean> CREATOR = new Creator<CategoryBean>() {
        @Override
        public CategoryBean createFromParcel(Parcel in) {
            return new CategoryBean(in);
        }

        @Override
        public CategoryBean[] newArray(int size) {
            return new CategoryBean[size];
        }
    };

    @Override
    public String toString() {
        return "CategoryBean{" +
                "id='" + id + '\'' +
                ", branch_name='" + branch_name + '\'' +
                ", name='" + name + '\'' +
                ", menulink='" + menulink + '\'' +
                ", has_sub_category='" + has_sub_category + '\'' +
                '}';
    }
}

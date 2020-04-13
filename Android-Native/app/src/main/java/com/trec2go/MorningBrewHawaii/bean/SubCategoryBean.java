package com.trec2go.MorningBrewHawaii.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubCategoryBean implements Parcelable
{
    @SerializedName("id")
    public String id;

    @SerializedName("ctype")
    public String ctype;

    @SerializedName("name")
    public String name;

//    @SerializedName("menulink")
//    public String menulink;

    @SerializedName("category_name")
    public String category_name = "";

    public SubCategoryBean() {
    }

    protected SubCategoryBean(Parcel in) {
        id = in.readString();
        name = in.readString();
       // menulink = in.readString();
        ctype = in.readString();
        category_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ctype);
        dest.writeString(name);
      //  dest.writeString(menulink);
        dest.writeString(category_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategoryBean> CREATOR = new Creator<SubCategoryBean>() {
        @Override
        public SubCategoryBean createFromParcel(Parcel in) {
            return new SubCategoryBean(in);
        }

        @Override
        public SubCategoryBean[] newArray(int size) {
            return new SubCategoryBean[size];
        }
    };

    @Override
    public String toString() {
        return "CategoryBean{" +
                "id='" + id + '\'' +
                ", ctype='" + ctype + '\'' +
                ", name='" + name + '\'' +
        //        ", menulink='" + menulink + '\'' +
                ", category_name='" + category_name + '\'' +
                '}';
    }

}

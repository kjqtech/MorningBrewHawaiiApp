package com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class menu_options{
    @SerializedName("topname")
    public String topname;

    @SerializedName("optionallow")
    public String optionallow;

    @SerializedName("default")
    public String default_ = "";

    @SerializedName("compulsory")
    public String compulsory = "";

    public int getModifier_compulsory_count() {
        return modifier_compulsory_count;
    }

    public void setModifier_compulsory_count(int modifier_compulsory_count) {
        this.modifier_compulsory_count = modifier_compulsory_count;
    }

    public int modifier_compulsory_count = 0;

   /* @SerializedName("min_option_check")
    public String min_option_check = "";*/

    public String getCompulsory() {
        return compulsory;
    }

    public void setCompulsory(String compulsory) {
        this.compulsory = compulsory;
    }

    @SerializedName("optioncount")
    public String optioncount;

    public int currentoptionallow = 0;

    @SerializedName("options")
    public ArrayList<com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class.options> options;
}

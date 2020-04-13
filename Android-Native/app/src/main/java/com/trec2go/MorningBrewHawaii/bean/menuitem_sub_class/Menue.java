package com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Menue{

    @SerializedName("selMenuOption")
    public String selMenuOption;

    @SerializedName("menu_options")
    public  ArrayList<com.trec2go.MorningBrewHawaii.bean.menuitem_sub_class.menu_options> menu_options;

}

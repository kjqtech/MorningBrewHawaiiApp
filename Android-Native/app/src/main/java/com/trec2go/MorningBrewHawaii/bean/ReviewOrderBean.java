package com.trec2go.MorningBrewHawaii.bean;

public class ReviewOrderBean {

   /* public ReviewOrderBean(String item_totle, String text_percentage, String tax, String surcharge, String sdType, String sdAmount, String totlesdt, String is_special_applied, String special_discount_heading, String special_discount_amount, String total, String order_type, String delivery, String tips_totle, String globlePromocodeDiscountType, String globlePromocode, String globlePromocodeDiscount, String promocode_heading, String promocode_discount, String is_first_discount_applied, String first_discount_heading, String first_discount_ammont, String firstordertype, String discountvalue) {
        this.item_totle = item_totle;
        this.text_percentage = text_percentage;
        this.tax = tax;
        this.surcharge = surcharge;
        this.sdType = sdType;
        this.sdAmount = sdAmount;
        this.totlesdt = totlesdt;
        this.is_special_applied = is_special_applied;
        this.special_discount_heading = special_discount_heading;
        this.special_discount_amount = special_discount_amount;
        this.total = total;
        this.order_type = order_type;
        this.delivery = delivery;
        this.tips_totle = tips_totle;
        this.globlePromocodeDiscountType = globlePromocodeDiscountType;
        this.globlePromocode = globlePromocode;
        this.globlePromocodeDiscount = globlePromocodeDiscount;
        Promocode_heading = promocode_heading;
        this.promocode_discount = promocode_discount;
        this.is_first_discount_applied = is_first_discount_applied;
        this.first_discount_heading = first_discount_heading;
        this.first_discount_ammont = first_discount_ammont;
        this.firstordertype = firstordertype;
        this.discountvalue = discountvalue;
    }
*/
    public String getItem_totle() {
        return item_totle;
    }

    public void setItem_totle(String item_totle) {
        this.item_totle = item_totle;
    }

    String item_totle;
    String text_percentage;
    String tax;
    String surcharge;

    String sdType;
    String sdAmount;
    String totlesdt;
    String is_special_applied;
    String special_discount_heading;
    String special_discount_amount;
    String total;

    String order_type;
    String delivery = "";
    String tips_totle = "";

    String globlePromocodeDiscountType;
    String globlePromocode;
    String globlePromocodeDiscount = "";

    String Promocode_heading;
    String promocode_discount;

    String is_first_discount_applied = "0";
    String first_discount_heading;
    String first_discount_ammont;
    String firstordertype;
    String discountvalue;

    String deliveryStreetAddress;
    String deliveryApartment;

    String deliverycity_state_zip;

    public String getDeliveryStreetAddress() {
        return deliveryStreetAddress;
    }
    public String getDeliveryApartment() {
        return deliveryApartment;
    }
    public void setDeliveryApartment(String deliveryApartment) {
        this.deliveryApartment = deliveryApartment;
    }
    public void setDeliveryStreetAddress(String deliveryStreetAddress) {
        this.deliveryStreetAddress = deliveryStreetAddress;
    }

    public String getDeliverycity_state_zip() {
        return deliverycity_state_zip;
    }

    public void setDeliverycity_state_zip(String deliverycity_state_zip) {
        this.deliverycity_state_zip = deliverycity_state_zip;
    }

    public String getIs_first_discount_applied() {
        return is_first_discount_applied;
    }

    public void setIs_first_discount_applied(String is_first_discount_applied) {
        this.is_first_discount_applied = is_first_discount_applied;
    }

    public String getFirst_discount_heading() {
        return first_discount_heading;
    }

    public void setFirst_discount_heading(String first_discount_heading) {
        this.first_discount_heading = first_discount_heading;
    }

    public String getFirst_discount_ammont() {
        return first_discount_ammont;
    }

    public void setFirst_discount_ammont(String first_discount_ammont) {
        this.first_discount_ammont = first_discount_ammont;
    }

    public String getFirstordertype() {
        return firstordertype;
    }

    public void setFirstordertype(String firstordertype) {
        this.firstordertype = firstordertype;
    }

    public String getDiscountvalue() {
        return discountvalue;
    }

    public void setDiscountvalue(String discountvalue) {
        this.discountvalue = discountvalue;
    }

    public String getText_percentage() {
        return text_percentage;
    }

    public void setText_percentage(String text_percentage) {
        this.text_percentage = text_percentage;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getSdType() {
        return sdType;
    }

    public void setSdType(String sdType) {
        this.sdType = sdType;
    }

    public String getSdAmount() {
        return sdAmount;
    }

    public void setSdAmount(String sdAmount) {
        this.sdAmount = sdAmount;
    }

    public String getTotlesdt() {
        return totlesdt;
    }

    public void setTotlesdt(String totlesdt) {
        this.totlesdt = totlesdt;
    }

    public String getIs_special_applied() {
        return is_special_applied;
    }

    public void setIs_special_applied(String is_special_applied) {
        this.is_special_applied = is_special_applied;
    }

    public String getSpecial_discount_heading() {
        return special_discount_heading;
    }

    public void setSpecial_discount_heading(String special_discount_heading) {
        this.special_discount_heading = special_discount_heading;
    }

    public String getSpecial_discount_amount() {
        return special_discount_amount;
    }

    public void setSpecial_discount_amount(String special_discount_amount) {
        this.special_discount_amount = special_discount_amount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getTips_totle() {
        return tips_totle;
    }

    public void setTips_totle(String tips_totle) {
        this.tips_totle = tips_totle;
    }

    public String getGloblePromocodeDiscountType() {
        return globlePromocodeDiscountType;
    }

    public void setGloblePromocodeDiscountType(String globlePromocodeDiscountType) {
        this.globlePromocodeDiscountType = globlePromocodeDiscountType;
    }

    public String getGloblePromocode() {
        return globlePromocode;
    }

    public void setGloblePromocode(String globlePromocode) {
        this.globlePromocode = globlePromocode;
    }

    public String getGloblePromocodeDiscount() {
        return globlePromocodeDiscount;
    }

    public void setGloblePromocodeDiscount(String globlePromocodeDiscount) {
        this.globlePromocodeDiscount = globlePromocodeDiscount;
    }

    public String getPromocode_heading() {
        return Promocode_heading;
    }

    public void setPromocode_heading(String promocode_heading) {
        Promocode_heading = promocode_heading;
    }

    public String getPromocode_discount() {
        return promocode_discount;
    }

    public void setPromocode_discount(String promocode_discount) {
        this.promocode_discount = promocode_discount;
    }
}

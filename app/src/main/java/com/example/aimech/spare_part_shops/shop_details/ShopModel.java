package com.example.aimech.spare_part_shops.shop_details;

public class ShopModel {

    String uid, shop_name, shop_address, open_time, close_time, contact_person, contact_no, email, shop_details, shop_located;

    public ShopModel() {
    }

    public ShopModel(String uid, String shop_name, String shop_address, String open_time, String close_time, String contact_person, String contact_no, String email, String shop_details, String shop_located) {
        this.uid = uid;
        this.shop_name = shop_name;
        this.shop_address = shop_address;
        this.open_time = open_time;
        this.close_time = close_time;
        this.contact_person = contact_person;
        this.contact_no = contact_no;
        this.email = email;
        this.shop_details = shop_details;
        this.shop_located = shop_located;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShop_details() {
        return shop_details;
    }

    public void setShop_details(String shop_details) {
        this.shop_details = shop_details;
    }

    public String getShop_located() {
        return shop_located;
    }

    public void setShop_located(String shop_located) {
        this.shop_located = shop_located;
    }
}

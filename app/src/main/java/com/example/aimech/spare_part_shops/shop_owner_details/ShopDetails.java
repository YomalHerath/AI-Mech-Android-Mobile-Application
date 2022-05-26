package com.example.aimech.spare_part_shops.shop_owner_details;

public class ShopDetails {

    int user_type;
    String uid, full_name, username, email, phone_no, password;

    public ShopDetails() {
    }

    public ShopDetails(String uid, String full_name, String username, String email, String phone_no, int user_type) {
        this.uid = uid;
        this.full_name = full_name;
        this.username = username;
        this.email = email;
        this.phone_no = phone_no;
        this.user_type = user_type;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.example.aimech.drivers.create_driver;

public class DriverModel {

    String uid, full_name, address, city, email, mobile_no, birth_year, license_no;

    public DriverModel() {
    }

    public DriverModel(String uid, String full_name, String address, String city, String email, String mobile_no, String birth_year, String license_no) {
        this.uid = uid;
        this.full_name = full_name;
        this.address = address;
        this.city = city;
        this.email = email;
        this.mobile_no = mobile_no;
        this.birth_year = birth_year;
        this.license_no = license_no;
    }

    public DriverModel(String full_name, String address, String city, String email, String mobile_no, String birth_year, String license_no) {
        this.full_name = full_name;
        this.address = address;
        this.city = city;
        this.email = email;
        this.mobile_no = mobile_no;
        this.birth_year = birth_year;
        this.license_no = license_no;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getLicense_no() {
        return license_no;
    }

    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }
}

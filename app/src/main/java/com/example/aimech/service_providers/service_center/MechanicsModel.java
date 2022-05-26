package com.example.aimech.service_providers.service_center;

public class MechanicsModel {

    String uid, service_center_name, mechanic_name, mechanic_address, contact_no, email, service_center_details, located;

    public MechanicsModel() {
    }

    public MechanicsModel(String uid, String service_center_name, String mechanic_name, String mechanic_address, String contact_no, String email, String service_center_details, String located) {
        this.uid = uid;
        this.service_center_name = service_center_name;
        this.mechanic_name = mechanic_name;
        this.mechanic_address = mechanic_address;
        this.contact_no = contact_no;
        this.email = email;
        this.service_center_details = service_center_details;
        this.located = located;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getService_center_name() {
        return service_center_name;
    }

    public void setService_center_name(String service_center_name) {
        this.service_center_name = service_center_name;
    }

    public String getMechanic_name() {
        return mechanic_name;
    }

    public void setMechanic_name(String mechanic_name) {
        this.mechanic_name = mechanic_name;
    }

    public String getMechanic_address() {
        return mechanic_address;
    }

    public void setMechanic_address(String mechanic_address) {
        this.mechanic_address = mechanic_address;
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

    public String getService_center_details() {
        return service_center_details;
    }

    public void setService_center_details(String service_center_details) {
        this.service_center_details = service_center_details;
    }

    public String getLocated() {
        return located;
    }

    public void setLocated(String located) {
        this.located = located;
    }
}

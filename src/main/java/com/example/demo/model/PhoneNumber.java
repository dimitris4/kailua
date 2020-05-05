package com.example.demo.model;

public class PhoneNumber {

    private int phoneId;

    private String phone;

    public PhoneNumber() {}

    public PhoneNumber(int phoneId, String phone) {
        this.phoneId = phoneId;
        this.phone = phone;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}

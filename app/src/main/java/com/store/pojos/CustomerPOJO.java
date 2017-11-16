package com.store.pojos;

import java.io.Serializable;

public class CustomerPOJO implements Serializable{

    private String firstName;
    private String lastName;
    private String mobileNo;
    private String email;
    private String fb;
    private String address;
    private String cityTown;
    private String brgy;
    private String landmark;
    private String note;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityTown() {
        return cityTown;
    }

    public void setCityTown(String cityTown) {
        this.cityTown = cityTown;
    }

    public String getBrgy() {
        return brgy;
    }

    public void setBrgy(String brgy) {
        this.brgy = brgy;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

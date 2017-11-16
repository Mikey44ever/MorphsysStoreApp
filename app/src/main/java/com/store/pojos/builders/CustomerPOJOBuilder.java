package com.store.pojos.builders;

import com.store.pojos.CustomerPOJO;

public class CustomerPOJOBuilder {

    private CustomerPOJO customerPOJO;

    public CustomerPOJOBuilder(){
        customerPOJO = new CustomerPOJO();
    }

    public CustomerPOJOBuilder fName(final String fName){
        customerPOJO.setFirstName(fName);
        return this;
    }

    public CustomerPOJOBuilder lName(final String lName){
        customerPOJO.setLastName(lName);
        return this;
    }

    public CustomerPOJOBuilder mobile(final String mobile){
        customerPOJO.setMobileNo(mobile);
        return this;
    }

    public CustomerPOJOBuilder email(final String email){
        customerPOJO.setEmail(email);
        return this;
    }

    public CustomerPOJOBuilder fb(final String fb){
        customerPOJO.setFb(fb);
        return this;
    }

    public CustomerPOJOBuilder address(final String address){
        customerPOJO.setAddress(address);
        return this;
    }

    public CustomerPOJOBuilder cityTown(final String cityTown){
        customerPOJO.setCityTown(cityTown);
        return this;
    }

    public CustomerPOJOBuilder brgy(final String brgy){
        customerPOJO.setBrgy(brgy);
        return this;
    }

    public CustomerPOJOBuilder landmark(final String landmark){
        customerPOJO.setLandmark(landmark);
        return this;
    }

    public CustomerPOJOBuilder note(final String note){
        customerPOJO.setNote(note);
        return this;
    }

    public CustomerPOJO build(){
        return customerPOJO;
    }
}

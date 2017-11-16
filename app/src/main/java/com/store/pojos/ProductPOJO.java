package com.store.pojos;

import java.io.Serializable;

public class ProductPOJO implements Serializable{

    private String item;
    private String cv;
    private String price;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

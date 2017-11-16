package com.store.pojos.builders;


import com.store.pojos.ProductPOJO;

public class ProductPOJOBuilder {

    private ProductPOJO pPOJO;

    public ProductPOJOBuilder(){
        pPOJO = new ProductPOJO();
    }

    public ProductPOJOBuilder item(final String item){
        pPOJO.setItem(item);
        return this;
    }

    public ProductPOJOBuilder cv(final String cv){
        pPOJO.setCv(cv);
        return this;
    }

    public ProductPOJOBuilder price(final String price){
        pPOJO.setPrice(price);
        return this;
    }

    public ProductPOJO build(){
        return pPOJO;
    }
}

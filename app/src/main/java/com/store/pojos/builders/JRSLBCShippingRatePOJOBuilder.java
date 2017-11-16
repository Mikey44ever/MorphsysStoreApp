package com.store.pojos.builders;


import com.store.pojos.JRSLBCShippingRatesPOJO;

public class JRSLBCShippingRatePOJOBuilder {

    private JRSLBCShippingRatesPOJO ljPOJO;

    public JRSLBCShippingRatePOJOBuilder(){
        ljPOJO = new JRSLBCShippingRatesPOJO();
    }

    public JRSLBCShippingRatePOJOBuilder qty(final String qty){
        ljPOJO.setQty(qty);
        return this;
    }

    public JRSLBCShippingRatePOJOBuilder lbcRate(final String lbcRate){
        ljPOJO.setLbcRate(lbcRate);
        return this;
    }

    public JRSLBCShippingRatePOJOBuilder jrsRate(final String jrsRate){
        ljPOJO.setJrsRate(jrsRate);
        return this;
    }

    public JRSLBCShippingRatesPOJO build(){
        return ljPOJO;
    }
}

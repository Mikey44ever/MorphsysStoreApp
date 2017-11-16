package com.store.pojos.builders;

import com.store.pojos.CODShippingRatesPOJO;

public class CODShippingRatesPOJOBuilder {

    private CODShippingRatesPOJO csrPOJO;

    public CODShippingRatesPOJOBuilder(){
        csrPOJO = new CODShippingRatesPOJO();
    }

    public CODShippingRatesPOJOBuilder qty(final String qty){
        csrPOJO.setQty(qty);
        return this;
    }

    public CODShippingRatesPOJOBuilder rates(final String rates){
        csrPOJO.setRates(rates);
        return this;
    }

    public CODShippingRatesPOJO build(){
        return  csrPOJO;
    }
}

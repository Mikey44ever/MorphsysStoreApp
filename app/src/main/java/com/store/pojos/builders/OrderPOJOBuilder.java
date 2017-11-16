package com.store.pojos.builders;

import com.store.pojos.OrderPOJO;

public class OrderPOJOBuilder {

    private OrderPOJO orderPOJO;

    public OrderPOJOBuilder(){
        orderPOJO = new OrderPOJO();
    }

    public OrderPOJOBuilder poId(final String poId){
        orderPOJO.setPoId(poId);
        return this;
    }

    public OrderPOJOBuilder date(final String date){
        orderPOJO.setDate(date);
        return this;
    }

    public OrderPOJOBuilder name(final String name){
        orderPOJO.setName(name);
        return this;
    }

    public OrderPOJOBuilder qty(final String qty){
        orderPOJO.setQty(qty);
        return this;
    }

    public OrderPOJO build(){
        return orderPOJO;
    }
}

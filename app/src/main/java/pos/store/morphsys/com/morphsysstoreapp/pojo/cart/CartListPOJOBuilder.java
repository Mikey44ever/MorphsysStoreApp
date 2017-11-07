package pos.store.morphsys.com.morphsysstoreapp.pojo.cart;

public class CartListPOJOBuilder {

    private CartListPOJO cartListPOJO;

    public CartListPOJOBuilder(){
        cartListPOJO = new CartListPOJO();
    }

    public CartListPOJO build(){
        return cartListPOJO;
    }

    public CartListPOJOBuilder quantity(final int qty){
        cartListPOJO.setQuantity(qty);
        return this;
    }

    public CartListPOJOBuilder cartId(final String cartId){
        cartListPOJO.setCartId(cartId);
        return this;
    }

    public CartListPOJOBuilder cost(final double cost){
        cartListPOJO.setCost(cost);
        return this;
    }

    public CartListPOJOBuilder date(final String date){
        cartListPOJO.setDate(date);
        return this;
    }


    public CartListPOJOBuilder status(final String status){
        cartListPOJO.setStatus(status);
        return this;
    }
}

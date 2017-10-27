package pos.store.morphsys.com.morphsysstoreapp.pojo.cart;

/**
 * Created by MorphsysLaptop on 26/10/2017.
 */

public class CartPOJOBuilder {

    private CartPOJO cart;

    public CartPOJOBuilder(){
        cart = new CartPOJO();
    }

    public CartPOJO build(){
        return cart;
    }

    public CartPOJOBuilder item(final String item){
        cart.setItemId(item);
        return this;
    }
    public CartPOJOBuilder desc(final String desc){
        cart.setDescription(desc);
        return this;
    }

    public CartPOJOBuilder quantity(final int qty){
        cart.setQuantity(qty);
        return this;
    }

    public CartPOJOBuilder basePrice(final double base){
        cart.setBasePrice(base);
        return this;
    }
}


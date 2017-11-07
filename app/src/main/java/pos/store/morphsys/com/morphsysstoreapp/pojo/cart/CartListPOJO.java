package pos.store.morphsys.com.morphsysstoreapp.pojo.cart;

import java.io.Serializable;

public class CartListPOJO implements Serializable{

    private static final long serialVersionUID = -7060210544600464482L;
    private int quantity;
    private String date;
    private double cost;
    private String cartId;
    private String status;

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

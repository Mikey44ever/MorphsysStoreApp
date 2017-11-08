package pos.store.morphsys.com.morphsysstoreapp.pojo.cart;

import java.io.Serializable;

public class CartPOJO implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;
    private String itemId;
    private String description;
    private double basePrice;
    private int quantity;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return description+"\n"+"       : "+quantity+" * "+basePrice +" = "+(quantity*basePrice);
    }
}

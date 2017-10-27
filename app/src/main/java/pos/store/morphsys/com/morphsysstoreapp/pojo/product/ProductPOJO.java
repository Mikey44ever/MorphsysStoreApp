package pos.store.morphsys.com.morphsysstoreapp.pojo.product;

/**
 * Created by MorphsysLaptop on 26/10/2017.
 */

public class ProductPOJO {

    private String productId;
    private String productName;
    private double productPrice;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return productId + " : "+ productName+" : "+productPrice;
    }
}

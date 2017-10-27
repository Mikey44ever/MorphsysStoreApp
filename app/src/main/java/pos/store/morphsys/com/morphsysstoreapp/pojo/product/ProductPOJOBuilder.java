package pos.store.morphsys.com.morphsysstoreapp.pojo.product;

/**
 * Created by MorphsysLaptop on 26/10/2017.
 */

public class ProductPOJOBuilder {

    private ProductPOJO pPOJO;

    public ProductPOJOBuilder(){
        pPOJO= new ProductPOJO();
    }

    public ProductPOJO build(){
        return pPOJO;
    }

    public ProductPOJOBuilder productId(final String productId){
        pPOJO.setProductId(productId);
        return this;
    }

    public ProductPOJOBuilder productName(final String productName){
        pPOJO.setProductName(productName);
        return this;
    }

    public ProductPOJOBuilder price(final double price){
        pPOJO.setProductPrice(price);
        return this;
    }
}

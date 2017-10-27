package pos.store.morphsys.com.morphsysstoreapp.constants;

/**
 * Created by MorphsysLaptop on 25/10/2017.
 */

public class Constants {

    public static final String REGISTRATION_URL = "http://morphsys.com.ph/store/index.php?route=account/register/onlineregister";
    public static final String PRODUCTS_URL = "http://morphsys.com.ph/store/getitems.php";

    public static final String CART_POJO_SERIAL_KEY="pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO";

    public static final int BARCODE_READER_REQUEST_CODE = 1;
    public static final int DB_CREATE_REQUEST_CODE = 2;
    public static final int REGISTRATION_REQUEST_CODE = 3;
    public static final int PRODUCT_RETRIEVAL_REQUEST_CODE = 4;
    public static final int REGISTRATION_MESSAGE_REQUEST_CODE = 5;
    public static final int LOGIN_REQUEST_CODE = 6;
    public static final int VIEW_CART_REQUEST_CODE=7;
    public static final int MAIN_ACTIVITY_REQUEST_CODE=8;

    public static final String BARCODE="BARCODE";
    public static final String PRODUCT_ID="PRODUCT_ID";
    public static final String PRODUCT_NAME="PRODUCT_NAME";
    public static final String PRICE="PRODUCT_PRICE";

    /*SQL constants*/
    public static final String USERS_TABLE = "USERS";
    public static final String SELECT_SUFFIX= "SELECT * FROM ";
    public static final String WHERE = " WHERE";
    public static final String ORDER_BY= " ORDER BY";
    public static final String DESC = " DESC";

}

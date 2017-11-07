package pos.store.morphsys.com.morphsysstoreapp.constants;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.HashMap;

public class Constants {

    public static final String[] SPINNERS={"Morphsys","CPI","Mapfre"};//{"17","23","24"};

    public static final String REGISTRATION_URL = "http://morphsys.com.ph/store/appregister/";//http://morphsys.com.ph/store/index.php?route=account/register/onlineregister";
    public static final String PRODUCTS_URL = "http://morphsys.com.ph/store/getitemsonline";//"http://morphsys.com.ph/store/getitems.php";
    public static final String LOGIN_URL = "http://morphsys.com.ph/store/login";//http://morphsys.com.ph/store/index.php?route=common/login";
    public static final String CHECKOUT_URL ="http://morphsys.com.ph/store/checkout";//http://morphsys.com.ph/store/index.php?route=account/cart/checkoutcart";
    public static final String CARTS_URL = "http://morphsys.com.ph/store/getcartsonline";
    public static final String CART_URL = "http://morphsys.com.ph/store/getcartdetails";

    public static final String CART_POJO_SERIAL_KEY="pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO";
    public static final String CART_LIST_POJO_SERIAL_KEY = "pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartListPOJO";

    public static final int BARCODE_READER_REQUEST_CODE = 1;
    public static final int DB_CREATE_REQUEST_CODE = 2;
    public static final int REGISTRATION_REQUEST_CODE = 3;
    public static final int PRODUCT_RETRIEVAL_REQUEST_CODE = 4;
    public static final int REGISTRATION_MESSAGE_REQUEST_CODE = 5;
    public static final int LOGIN_REQUEST_CODE = 6;
    public static final int VIEW_CART_REQUEST_CODE=7;
    public static final int MAIN_ACTIVITY_REQUEST_CODE=8;
    public static final int CHECKOUT_REQUEST_CODE = 9;
    public static final int ALL_PRODUCTS_REQUEST_CODE=10;
    public static final int ALL_CARTS_REQUEST_CODE=11;

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

    public static final String NOTICE_MESSAGE = "You're not connected to the internet!";

    public static final HashMap<String,String> getBranches(){//
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("Morphsys","17");
        map.put("CPI","24");
        map.put("Mapfre","23");
        return map;
    }

    public static void showConstantDialog(final Activity a, String t, String m, final Intent intent,
                                             final String status,final boolean isProceedAfterClick){
        AlertDialog.Builder adb = new AlertDialog.Builder(a);
        adb.setTitle(t);
        adb.setMessage(m);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(status.equalsIgnoreCase("SUCCESS") && isProceedAfterClick){
                    a.setResult(CommonStatusCodes.SUCCESS, intent);
                    a.finish();
                }
            }
        });
        adb.show();
    }
}

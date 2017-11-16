package pos.store.morphsys.com.morphsysstoreapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

import com.store.R;
import com.store.activities.BarcodeCaptureActivity;
import com.store.dbs.DBHelper;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJOBuilder;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJOBuilder;

import static com.store.constants.Constants.*;

public class ShopFragment extends Fragment {
    private static final String LOG_TAG = ShopFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DBHelper mydb;
    private Barcode barcode;
    private Intent data;
    private TextView txtProductName, txtProductPrice, txtQty;
    private Button scanBarcodeButton, btnAddToCart, btnClear,btnViewCart;
    private CartPOJOBuilder cBuilder;
    private ProductPOJOBuilder pBuilder;
    private ProductPOJO pPOJO;
    private CartPOJO cart;
    private ArrayList<CartPOJO> cartList = new ArrayList<CartPOJO>();
    private Boolean isFromUpdate = Boolean.FALSE;
    private String cartId,userId;
    private Bundle bundle;


    private OnFragmentInteractionListener mListener;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    setButtonEnabled("scan");
                    this.data=data;
                    getProductDetails();
                }
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else if(requestCode == VIEW_CART_REQUEST_CODE){
            if (data != null){
                cartList = (ArrayList<CartPOJO>)data.getSerializableExtra(CART_POJO_SERIAL_KEY);
                cartId = data.getStringExtra("cartId");
            }

            updateArgumentsForCartList();
            setButtonEnabled("view");
        }else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void setButtonEnabled(String activity){
        if("scan".equalsIgnoreCase(activity)){
            scanBarcodeButton.setEnabled(false);
            scanBarcodeButton.setAlpha(0.2f);
            btnAddToCart.setEnabled(true);
            btnClear.setEnabled(true);
        }else if("add".equalsIgnoreCase(activity)){
            scanBarcodeButton.setEnabled(true);
            scanBarcodeButton.setAlpha(1f);
            btnAddToCart.setEnabled(false);
            btnClear.setEnabled(false);
        }else if("clear".equalsIgnoreCase(activity)){
            scanBarcodeButton.setEnabled(true);
            scanBarcodeButton.setAlpha(1f);
            btnAddToCart.setEnabled(false);
            btnClear.setEnabled(false);
        }else if("view".equalsIgnoreCase(activity)){
            txtQty.setText("");
            txtProductName.setText("");
            txtProductPrice.setText("");
            scanBarcodeButton.setEnabled(true);
            scanBarcodeButton.setAlpha(1f);
            btnAddToCart.setEnabled(false);
            btnClear.setEnabled(false);
        } else if("list".equalsIgnoreCase(activity)){
            txtQty.setText("");
        }
    }

    private void getProductDetails(){
        barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
        Point[] p = barcode.cornerPoints;
        String strBarcode = barcode.displayValue;
        Cursor product=mydb.getSpecificProduct(mydb.PRODUCTS_TABLE_NAME,BARCODE,strBarcode);
        pBuilder = new ProductPOJOBuilder();
        while (product.moveToNext()) {
            String productId=product.getString(product.getColumnIndex(PRODUCT_ID));
            Cursor price=mydb.getSpecificProduct(mydb.PRODUCTS_PRICE_TABLE_NAME,PRODUCT_ID,productId);

            while(price.moveToNext()){
                pPOJO = pBuilder
                        .productId(productId)
                        .productName(product.getString(product.getColumnIndex(PRODUCT_NAME)))
                        .price(price.getDouble(price.getColumnIndex(PRICE)))
                        .build();
            }
        }
        txtProductName.setText(pPOJO.getProductName());
        txtProductPrice.setText(new Double(pPOJO.getProductPrice()).toString());
    }

    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_shop, container, false);
        mydb = new DBHelper(getActivity());
        setListeners(view);

        return view;
    }

    private void setListeners(View view){
        txtProductName = (TextView) view.findViewById(R.id.txtProductName);
        txtProductPrice = (TextView) view.findViewById(R.id.txtProductPrice);
        txtQty = (TextView) view.findViewById(R.id.txtItemNumber);
        scanBarcodeButton = (Button) view.findViewById(R.id.scan_barcode_button);
        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);

        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
                intent.putExtra("userId",bundle.getString("userId"));
                intent.putExtra("cartId",bundle.getString("cartId"));
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(0);
                Log.i(null,cartList.toArray().toString());
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonEnabled("clear");
                clear();
            }
        });

        getCartList();
        updateArgumentsForCartList();
    }

    private void addToCart(int qty){
        try{
            if(pPOJO!=null){
                setButtonEnabled("add");
                if(qty==0)
                    qty=txtQty.getText().toString().equals("") ? 1 : Integer.parseInt(txtQty.getText().toString());

                cBuilder = new CartPOJOBuilder();
                cart = cBuilder
                        .item(pPOJO.getProductId())
                        .desc(pPOJO.getProductName())
                        .quantity(qty)
                        .basePrice(pPOJO.getProductPrice())
                        .build();
                cartList.add(cart);
                updateArgumentsForCartList();
                clear();
            }else{
                showConstantDialog(getActivity(),"SHOP","SCAN PRODUCT FIRST...",getActivity().getIntent(),"SUCCESS",false);
            }
        }catch(NumberFormatException e){
            addToCart(1);
            Log.e(null,e.getMessage());
        }
    }

    private void getCartList(){
        try{
            cartId = CachePot.getInstance().pop(String.class);
            isFromUpdate = CachePot.getInstance().pop(Boolean.class);
            cartList.addAll((ArrayList<CartPOJO>) CachePot.getInstance().pop(ArrayList.class));
            updateArgumentsForCartList();
        }catch (NullPointerException e){
            cartId = "";
            isFromUpdate=Boolean.FALSE;
            Log.i(null,e.getMessage());
        }catch (Exception e){
            Log.i(null,e.getMessage());
        }
    }

    private void updateArgumentsForCartList(){
        try{
            CachePot.getInstance().clear();
            CachePot.getInstance().push(cartId);
            CachePot.getInstance().push(cartList);
        }catch (NullPointerException e){
            Log.i(null,e.getMessage());
        }catch (Exception e){
            Log.i(null,e.getMessage());
        }
    }

    private void clear(){
        cart = null;
        pPOJO=null;
        txtProductPrice.setText("");
        txtProductName.setText("");
        txtQty.setText("");
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(isFromUpdate!=null && isFromUpdate){
            inflater.inflate(R.menu.view_cart, menu);
        }
        MenuItem viewCart = menu.findItem(R.id.action_view);

        if(viewCart!=null){
            viewCart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package pos.store.morphsys.com.morphsysstoreapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.kimkevin.cachepot.CachePot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.store.R;
import pos.store.morphsys.com.morphsysstoreapp.adapters.CartListAdapter;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartListPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartListPOJOBuilder;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJOBuilder;

import static com.store.constants.Constants.CARTS_URL;
import static com.store.constants.Constants.CART_POJO_SERIAL_KEY;
import static com.store.constants.Constants.CART_URL;
import static com.store.constants.Constants.SPECIFIC_CART_ITEMS_REQUEST_CODE;
import static com.store.constants.Constants.TAG_SHOP;

public class CartListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView allCartsView;
    private CartListAdapter customCartAdapter;
    private ArrayList<CartListPOJO> list;
    private ArrayList<CartPOJO> cartItemList;
    private String userId;
    private CartListPOJOBuilder cListBuilder;
    private CartPOJOBuilder cartPOJOBuilder;
    private CartPOJO cartPOJO;
    private CartListPOJO cartList;
    private Bundle bundle;
    private Intent data;
    private ArrayList<CartPOJO> listToUpdate;
    private Handler mHandler;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(SPECIFIC_CART_ITEMS_REQUEST_CODE == requestCode){
            if(data!=null){
                this.data = data;
                mHandler = new Handler();
                listToUpdate = (ArrayList<CartPOJO>) data.getSerializableExtra(CART_POJO_SERIAL_KEY);
                Log.i(null,data.toString());

                updateArgumentsForCartList();

                Runnable mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putString("userId",data.getStringExtra("userId"));

                        Fragment fragment = new ShopFragment();
                        fragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, TAG_SHOP);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };

                if (mPendingRunnable != null)
                    mHandler.post(mPendingRunnable);
            }
        }else
            super.onActivityResult(requestCode, resultCode, data);
    }


    private void updateArgumentsForCartList(){
        try{
            CachePot.getInstance().clear();
            CachePot.getInstance().push(data.getStringExtra("cartId"));
            CachePot.getInstance().push(listToUpdate);
            CachePot.getInstance().push(Boolean.TRUE);
        }catch (NullPointerException e){
            Log.i(null,e.getMessage());
        }catch (Exception e){
            Log.i(null,e.getMessage());
        }
    }

    public static CartListFragment newInstance(String param1, String param2) {
        CartListFragment fragment = new CartListFragment();
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
        View view= inflater.inflate(R.layout.fragment_cart_list, container, false);
        setListeners(view);
        return view;
    }

    private void setListeners(View view){
        userId = getActivity().getIntent().getStringExtra("userId");
        allCartsView = (ListView) view.findViewById(R.id.allCartsView);
        new AllCartsRetrievalAPICall().execute();
    }

    public void setList(String message){
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(message);
        JsonArray cartObj = tradeElement.getAsJsonObject().getAsJsonArray("carts");
        list = new ArrayList<CartListPOJO>();

        for(JsonElement cart:cartObj){
            JsonObject obj = cart.getAsJsonObject();

            cListBuilder = new CartListPOJOBuilder();
            cartList = cListBuilder
                    .cartId(obj.get("cart_id").toString().replaceAll("^\"|\"$", ""))
                    .quantity(Integer.valueOf(obj.get("quantity").toString().replaceAll("^\"|\"$", "")))
                    .date(obj.get("date_added").toString().replaceAll("^\"|\"$", ""))
                    .cost(Double.valueOf(obj.get("total").toString().replaceAll("^\"|\"$", "")))
                    .status(obj.get("status").toString().replaceAll("^\"|\"$", ""))
                    .build();

            list.add(cartList);
        }
        customCartAdapter = new CartListAdapter(getActivity(),R.layout.carts_custom_array_adapter, list);
        customCartAdapter.setActivity(getActivity());
        allCartsView.setAdapter(customCartAdapter);

        allCartsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CartListPOJO cartListPOJO = (CartListPOJO) view.getTag();
                new SpecificCartsRetrievalAPICall().execute(cartListPOJO.getCartId(),
                        cartListPOJO.getStatus(),cartListPOJO.getDate());
            }
        });
    }

    public void getCartDetails(String callBackMessage,String cartId,String status,String date){
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(callBackMessage);
        JsonArray cartObj = tradeElement.getAsJsonObject().getAsJsonArray("carts");
        cartItemList = new ArrayList<CartPOJO>();

        for(JsonElement cart:cartObj){
            JsonObject obj = cart.getAsJsonObject();

            double cost = Double.valueOf(obj.get("cost").toString().replaceAll("^\"|\"$", ""));
            int quantity = Integer.valueOf(obj.get("quantity").toString().replaceAll("^\"|\"$", ""));

            cartPOJOBuilder = new CartPOJOBuilder();
            cartPOJO = cartPOJOBuilder
                    .basePrice(cost/quantity)
                    .desc(obj.get("item").toString().replaceAll("^\"|\"$", ""))
                    .quantity(quantity)
                    .item(obj.get("item_id").toString().replaceAll("^\"|\"$", ""))
                    .build();

            cartItemList.add(cartPOJO);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    public class SpecificCartsRetrievalAPICall extends AsyncTask<String, Void, String> {

        private String cartId;
        private String status;
        private String date;

        @Override
        protected String doInBackground(String... params) {
            try{
                cartId=params[0];
                status=params[1];
                date=params[2];
                JSONObject userObj = new JSONObject();
                userObj.put("cartId",cartId);

                URL url = new URL(CART_URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                String input = userObj.toString();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(input);

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : "+responseCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            getCartDetails(s,cartId,status,date);
        }
    }

    public class AllCartsRetrievalAPICall extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try{
                JSONObject userObj = new JSONObject();
                userObj.put("userId",userId);

                URL url = new URL(CARTS_URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                String input = userObj.toString();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(input);

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : "+responseCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            setList(s);
        }
    }
}

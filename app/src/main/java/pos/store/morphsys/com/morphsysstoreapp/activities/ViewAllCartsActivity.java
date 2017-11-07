package pos.store.morphsys.com.morphsysstoreapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

import pos.store.morphsys.com.morphsysstoreapp.R;
import pos.store.morphsys.com.morphsysstoreapp.activities.processes.SpecificCartView;
import pos.store.morphsys.com.morphsysstoreapp.adapters.CartListAdapter;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartListPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartListPOJOBuilder;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.*;

public class ViewAllCartsActivity extends AppCompatActivity {

    ListView allCartsView;
    CartListAdapter customCartAdapter;
    ArrayList<CartListPOJO> list = new ArrayList<CartListPOJO>();
    String userId;
    CartListPOJOBuilder cListBuilder;
    CartListPOJO cartList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_carts_list);

        userId = getIntent().getStringExtra("userId");

        setListeners();

        new AllCartsRetrievalAPICall().execute();
    }

    private void setListeners() {
        allCartsView = (ListView) findViewById(R.id.allCartsView);
    }

    public void setList(String message){
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(message);
        JsonArray cartObj = tradeElement.getAsJsonObject().getAsJsonArray("carts");
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
        customCartAdapter = new CartListAdapter(this,R.layout.carts_custom_array_adapter, list);
        customCartAdapter.setActivity(ViewAllCartsActivity.this);
        allCartsView.setAdapter(customCartAdapter);

        allCartsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CartListPOJO cartListPOJO = (CartListPOJO) view.getTag();
                AlertDialog.Builder adb = new AlertDialog.Builder(ViewAllCartsActivity.this);
                adb.setTitle("CART ID#"+cartListPOJO.getCartId());
                adb.show();
                new SpecificCartView().execute(cartListPOJO.getCartId());
            }
        });
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

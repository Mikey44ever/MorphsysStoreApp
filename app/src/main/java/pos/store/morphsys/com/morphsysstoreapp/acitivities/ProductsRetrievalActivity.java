package pos.store.morphsys.com.morphsysstoreapp.acitivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.*;

public class ProductsRetrievalActivity  extends AppCompatActivity {

    private JSONArray productJSONArray = new JSONArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new ProductRetrieval().execute();
    }

    public void parseProductJSON(String result){
        try{
            productJSONArray = (new JSONObject(result)).getJSONArray("products");
            Intent intent = new Intent();
            intent.putExtra("products",productJSONArray.toString());
            setResult(CommonStatusCodes.SUCCESS, intent);
            finish();
        }catch(Exception e){
            Log.e("error here", e.getStackTrace().toString());
        }
    }

    public class ProductRetrieval extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(PRODUCTS_URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

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
            } catch (MalformedURLException e) {
                return new String("Exception: " + e.getMessage());
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
        protected void onPostExecute(String result) {
            parseProductJSON(result);
        }
    }
}

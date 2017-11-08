package pos.store.morphsys.com.morphsysstoreapp.util;


import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/********SAMPLE CALL********code sniffet from ViewAllCartsActivity.java******************
 * params should be in proper sequence
 *  -data : first element of the array
 *  -pkgname : second element of the array, e.g. "com.google.YourClass"
 *  -callBackMethod : 3rd element of the array, method to called from "com.google.YourClass"
 *  -URL : 4th element, URL of the API
* JSONObject postDataParams = new JSONObject();
 *   try {
 *      postDataParams.put("cartId", cartListPOJO.getCartId());
 *   } catch (JSONException e) {
 *      e.printStackTrace();
 *   }
 *   String pkgName = getPkg(this.getClass(),"ViewAllCartsActivity");
 *   String params[]={postDataParams.toString(),pkgName,"getCartDetails",CART_URL};
 *   new APICallerUtil().execute(params);
*
********************/
public class APICallerUtil extends AsyncTask<String, Void, String> {

    private String callBackMethod;
    private String callBackClassPkg;
    private String stringifiedJSONParam;
    private String URL;
    private Context context;

    public  APICallerUtil(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            stringifiedJSONParam = params[0];
            callBackClassPkg = params[1];
            callBackMethod = params[2];
            URL = params[3];

            URL url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(stringifiedJSONParam);

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
        try{
            Class<?> c = Class.forName(callBackClassPkg);
            Object gvClass = c.newInstance();
            Class[] argTypes = new Class[] {String.class, Context.class};

            Method main = c.getDeclaredMethod(callBackMethod, argTypes);
            main.invoke(gvClass,new Object[] {s,context});
        }catch(NoSuchMethodException e){
            e.printStackTrace();
        }catch(InvocationTargetException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

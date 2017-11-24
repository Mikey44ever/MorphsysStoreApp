package com.store.pos.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.model.TextAnnotation;
import com.store.pos.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Arrays;


import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

public class OCRTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrtest);

        String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 10);
        }

        new OCRAPI().execute();
    }

    public void callAfterAPI(String text) {
        Log.i(null, text);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public class OCRAPI extends AsyncTask<String, Void, String> {

        private static final String API_KEY = "AIzaSyCXVhbI5WSoSiFG735DLWITHO2oZlHhqcA";
        private static final String targetImage="quote1.jpg";

        private String doAPICall() throws IOException {
            Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(), new AndroidJsonFactory(), null);
            visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer(API_KEY));
            Vision vision = visionBuilder.build();

            File finalFile = null;
            File imagefile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toURI());
            File[] directoryListing = imagefile.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    String name = child.getName();
                    if (name.equalsIgnoreCase(targetImage))
                        finalFile = new File(child.toString());
                }
            }

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(finalFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 10, baos);
            byte[] img = baos.toByteArray();
            //String base64Image= Base64.encodeToString(img , Base64.DEFAULT);

            Image inputImage = new Image();
            inputImage.encodeContent(img);

            Feature desiredFeature = new Feature();
            desiredFeature.setType("TEXT_DETECTION");

            AnnotateImageRequest request = new AnnotateImageRequest();
            request.setImage(inputImage);
            request.setFeatures(Arrays.asList(desiredFeature));

            BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
            batchRequest.setRequests(Arrays.asList(request));

            BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();

            final TextAnnotation text = batchResponse.getResponses().get(0).getFullTextAnnotation();
            Log.i(null,text.toString());
            String finalText=(String)text.get("text");
            return finalText;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String text=doAPICall();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                Log.i(null, Log.getStackTraceString(e));
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
            callAfterAPI(s);
        }
    }
}

package com.navigation.drawer.activity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.http.AndroidHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Pro on 19/02/2017.
 */

public class HttpManager {

    public static String getData(String url) {
        HttpGet httpGet = new HttpGet(url);
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 20000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 20000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpResponse response ;

        try {
            response = httpClient.execute(httpGet);
            return org.apache.http.util.EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDatas(String jsonURL) throws IOException {
        URL url;
        String response = "";
        try {
            url = new URL(jsonURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null)
                    response += line;
            } else
                response = "";

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}

package com.tim_461.androidgeolocationapp;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tim-azul on 3/3/15.
 */
public class Internet {

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    public static String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 5000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


    /**
     * GET method
     *
     * @param url - the url getting from
     * @return the JSON string object return from get request
     */
    public static String getFromURL(String url) {
        try {
            //Log.d(TAG, "GETTING from URL --> " + url);
            HttpParams httpParameters = new BasicHttpParams();
            // joliver: faster than defualt HTTP_1.0, not sure if this is still a problem, but shouldn't hurt
            HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setConnectionTimeout(httpParameters, 3000); // 3s max for connection
            HttpConnectionParams.setSoTimeout(httpParameters, 4000); // 4s max to get data
            // TODO: use HTTPUrlConnection instead
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpGet httpget = new HttpGet(url); // Set the action you want to do
            httpget.setHeader("Content-type", "application/json");
            HttpResponse response;
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) // Read line by line
                sb.append(line + "\n");
            is.close();
            return sb.toString(); // Result is here*/
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }
}

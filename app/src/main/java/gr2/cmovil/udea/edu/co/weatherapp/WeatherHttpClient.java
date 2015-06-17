package gr2.cmovil.udea.edu.co.weatherapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DiegoAlejandro on 17/06/2015.
 */
public class WeatherHttpClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=";
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    public final String TAG = getClass().getSimpleName();

    public String getWeatherData(String location) {
        HttpURLConnection conn = null;
        InputStream is = null;
        URL url = null;

        try {
            url = new URL(BASE_URL + location);
        } catch (MalformedURLException e) {
            Log.d(TAG, "url sintax error");
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            StringBuffer buffer = new StringBuffer();
            is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while ((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }

            is.close();
            conn.disconnect();
            return buffer.toString();

        } catch (Throwable t) {
            Log.e(TAG, "Can't connect to HTTP url");
            Log.e(TAG,url.toString());
            t.printStackTrace();
        } finally {

            try {
                is.close();
            } catch (Throwable t) {
                Log.e(TAG, "Can't close is");
                t.printStackTrace();
            }

            try {
                conn.disconnect();
            } catch (Throwable t) {
                Log.e(TAG, "Can't discconect conn");
                t.printStackTrace();
            }
        }
        return null;
    }

    public byte[] getImage(String code) {
        HttpURLConnection conn = null;
        InputStream is = null;
        URL url = null;

        try {
            url = new URL(IMG_URL + code + ".png");
        } catch (MalformedURLException e) {
            Log.d(TAG, "url sintax error");
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            is = conn.getInputStream();

            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (is.read(buffer) != -1) {
                baos.write(buffer);
            }
            return baos.toByteArray();

        } catch (Throwable t) {
            Log.e(TAG, "Can't connect to HTTP url for image");
            Log.e(TAG,url.toString());
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
                Log.e(TAG, "Can't close is");
                t.printStackTrace();
            }

            try {
                conn.disconnect();
            } catch (Throwable t) {
                Log.e(TAG, "Can't discconect conn");
                t.printStackTrace();
            }
        }
        return null;
    }
}

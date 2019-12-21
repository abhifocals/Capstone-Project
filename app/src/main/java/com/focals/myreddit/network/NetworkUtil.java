package com.focals.myreddit.network;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {

    static String searchUrl = "https://www.reddit.com/r/php/search.json?q=oop";


    public static String getResponseFromUrl(String endPoint) {

        HttpURLConnection httpURLConnection = null;


        try {
            URL url = new URL(searchUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();

            Scanner scanner = new Scanner(httpURLConnection.getInputStream());
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }

        return null;

    }
}

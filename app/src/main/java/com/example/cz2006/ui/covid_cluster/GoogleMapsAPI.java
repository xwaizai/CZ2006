package com.example.cz2006.ui.covid_cluster;

import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleMapsAPI {

    public static void getPlaceInfo(String locationName, String[] fields, List<JSONObject> refList)
    {
        try {
            String requestStr = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="
                    + locationName
                    + "&inputtype=textquery&fields=";
            for (int num = 0; num < fields.length; ++num)
            {
                requestStr += fields[num];
                if (num + 1 < fields.length)
                {
                    requestStr += ",";
                }
            }
            requestStr += "&key=AIzaSyA4U9CFt7dstIfYDSAYEi8uUbiG0wjJOAc";
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url(requestStr).build();
            Response response = client.newCall(request).execute();
            String responseStr = response.body().string();
            JSONObject jObject = new JSONObject(responseStr);
            synchronized (refList)
            {
                // prevent deadlock
                refList.add(jObject);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void getGeocoding(String locationName, List<JSONObject> refList)
    {
        try
        {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url("https://maps.googleapis.com/maps/api/geocode/json?address=" + locationName + "&key=AIzaSyA4U9CFt7dstIfYDSAYEi8uUbiG0wjJOAc").build();
            Response response = client.newCall(request).execute();
            String responseStr = response.body().string();
            JSONObject jsonObject = new JSONObject(responseStr);
            synchronized (refList)
            {
                // prevent deadlock by synchronization
                refList.add(jsonObject);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    // leave it empty for now
    public static void getNavigation(String startLocation, String endLocation, List<JSONObject> refList)
    {
        try
        {

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}

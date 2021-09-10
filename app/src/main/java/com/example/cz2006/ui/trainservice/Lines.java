package com.example.cz2006.ui.trainservice;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Lines {

    private String[] Line;
    private String[] Direction;
    private String[] Stations;
    private String[] FreePublicBus;
    private String[] FreeMRTShuttle;
    private String[] MRTShuttleDirection;

    public Lines(){
        try{
            MyGETRequest();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[] getLine(){
        return Line;
    }

    public String[] getDirection() {
        return Direction;
    }

    public String[] getFreeMRTShuttle() {
        return FreeMRTShuttle;
    }

    public String[] getFreePublicBus() {
        return FreePublicBus;
    }

    public String[] getMRTShuttleDirection() {
        return MRTShuttleDirection;
    }

    public String[] getStations() {
        return Stations;
    }

    private String MyGETRequest() throws IOException, JSONException

    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //URL urlForGetRequest = new URL("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode="+buscode);
        //URL urlForGetRequest = new URL("http://datamall2.mytransport.sg/ltaodataservice/TrafficIncidents");
        URL urlForGetRequest = new URL("http://datamall2.mytransport.sg/ltaodataservice/TrainServiceAlerts");

        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty("AccountKey", "S0+epC+sQv273o46oiCjNQ=="); // set userId its a sample here

        int responseCode = conection.getResponseCode();

        StringBuffer response = new StringBuffer();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            // print result
            JSONObject jObject = new JSONObject(response.toString());
            JSONObject value = new JSONObject(jObject.getString("value"));
            JSONArray jArray = (JSONArray) new JSONTokener(jObject.getString("AffectedSegments")).nextValue();
            Line = new String[jArray.length()];
            Direction = new String[jArray.length()];
            Stations = new String[jArray.length()];
            FreePublicBus = new String[jArray.length()];
            FreeMRTShuttle = new String[jArray.length()];
            MRTShuttleDirection = new String[jArray.length()];

            JSONObject temp;
            for(int n = 0; n < jArray.length(); n++)
            {
                JSONObject object = jArray.getJSONObject(n);
                // do some stuff....
                Log.d("testing json","JSON String Result " + object);

                Line[n] = object.getString("Line");
                Direction[n] = object.getString("Direction");
                Stations[n] = object.getString("Stations");
                FreePublicBus[n] = object.getString("FreePublicBus");
                FreeMRTShuttle[n] = object.getString("FreeMRTShuttle");
                MRTShuttleDirection[n] = object.getString("MRTShuttleDirection");

            }
            //Log.d("testing json","testing here " + jObject.getString("ServiceNo"));
            Log.d("testing json","JSON String Result " + jArray);

            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("GET NOT WORKED");
        }
        return response.toString();
    }
}

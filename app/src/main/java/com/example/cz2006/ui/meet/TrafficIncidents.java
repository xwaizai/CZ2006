package com.example.cz2006.ui.meet;

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

public class TrafficIncidents {

    //variables
    private String[] Type;
    private double[] Latitude;
    private double[] Longitude;
    private String[] Message;
    //private String fullJson;

    //constructor
    public TrafficIncidents(){
        try{
            MyGETRequest();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //methods
    private String MyGETRequest() throws IOException, JSONException

    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //URL urlForGetRequest = new URL("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=27469");
        URL urlForGetRequest = new URL("http://datamall2.mytransport.sg/ltaodataservice/TrafficIncidents");
        //URL urlForGetRequest = new URL("http://datamall2.mytransport.sg/ltaodataservice/TrainServiceAlerts");

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
            JSONArray jArray = (JSONArray) new JSONTokener(jObject.getString("value")).nextValue();
            Type = new String[jArray.length()];
            Latitude = new double[jArray.length()];
            Longitude = new double[jArray.length()];
            Message = new String[jArray.length()];
            for(int n = 0; n < jArray.length(); n++)
            {
                JSONObject object = jArray.getJSONObject(n);
                // do some stuff....
                Log.d("testing json","JSON String Result " + object);
                Log.d("testing json","JSON String Result  Latitude" + object.getDouble("Latitude"));
                Log.d("testing json","JSON String Result  Longitude" + object.getDouble("Longitude"));
                Log.d("testing json","JSON String Result  Message" + object.getString("Message"));
                Type[n] = object.getString("Type");
                Latitude[n] = object.getDouble("Latitude");
                Longitude[n] = object.getDouble("Longitude");
                Message[n] = object.getString("Message");
            }
            Log.d("testing json","testing here " + jObject.getString("value"));
            Log.d("testing json","JSON String Result " + jArray);
            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("GET NOT WORKED");
        }
        return response.toString();
    }
    public String[] getType(){
        return Type;
    }

    public double[] getLatitude(){
        return  Latitude;
    }

    public double[] getLongitude(){
        return Longitude;
    }

    public String[] getMessage(){
        return Message;
    }


}

package com.example.cz2006.ui.meet;

import android.os.StrictMode;
import android.util.Log;

import com.example.cz2006.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MeetMGR {

    public MeetMGR(){

    }
    public boolean checkValidPostal(String postalText , List<String> lat, List<String> lng)
    {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String api = ""; //INSERT API with geocode&places

            URL urlForGetRequest = new URL(
                    "https://maps.googleapis.com/maps/api/geocode/json?address="
                            + postalText +",+SG&key=" + api);

            String readLine = null;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");

            int responseCode = conection.getResponseCode();

            StringBuffer response = new StringBuffer();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                Log.d( "In: ","IN1");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conection.getInputStream()));
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                // print result
                JSONObject jObject = new JSONObject(response.toString());

                Log.d( "jObject String: ", jObject.getString("status"));

                if (jObject.getString("status").equals("OK"))
                {
                    JSONArray result = (JSONArray) new JSONTokener(jObject.getString("results")).nextValue();
                    JSONObject r1 = result.getJSONObject(0);
                    JSONObject geometry = r1.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");

                    //get the postal code lat and lng for future use to calculate the midpoint to meet
                    lat.add(  location.getString("lat"));
                    lng.add(  location.getString("lng"));
                    Log.d( "In: ","IN2");
                    return true;
                }

            }
            return false;
        } catch(Exception e) {
            Log.d( "Error: ",e.getMessage());
            return false;
        }
    }
}


/*
{
        "results" : [
        {
        "address_components" : [
        {
        "long_name" : "640202",
        "short_name" : "640202",
        "types" : [ "postal_code" ]
        },
        {
        "long_name" : "Jurong West",
        "short_name" : "Jurong West",
        "types" : [ "neighborhood", "political" ]
        },
        {
        "long_name" : "Singapore",
        "short_name" : "Singapore",
        "types" : [ "locality", "political" ]
        },
        {
        "long_name" : "Singapore",
        "short_name" : "SG",
        "types" : [ "country", "political" ]
        }
        ],
        "formatted_address" : "Singapore 640202",
        "geometry" : {
        "location" : {
        "lat" : 1.34369,
        "lng" : 103.714978
        },
        "location_type" : "APPROXIMATE",
        "viewport" : {
        "northeast" : {
        "lat" : 1.3548449,
        "lng" : 103.7309854
        },
        "southwest" : {
        "lat" : 1.3325351,
        "lng" : 103.6989706
        }
        }
        },
        "place_id" : "ChIJT9aZV-gP2jERA2rtX0OPYJk",
        "types" : [ "postal_code" ]
        }
        ],
        "status" : "OK"
        } */
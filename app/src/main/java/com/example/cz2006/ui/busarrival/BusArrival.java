package com.example.cz2006.ui.busarrival;

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

public class BusArrival {

    private String[] ServiceNo;
    private String[] NextBus;
    private String[] NextBus2;
    private String[] NextBus3;
    private String[] Feature;
    private String[] Type;
    private String[] Load;

    public BusArrival(){
        try{
            MyGETRequest(27469);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public BusArrival(int query){
        try{
            MyGETRequest(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String[] getServiceNo(){
        return ServiceNo;
    }

    public String[] getNextBus(){
        return NextBus;
    }

    public String[] getNextBus2(){
        return NextBus2;
    }

    public String[] getNextBus3(){
        return NextBus3;
    }

    public String[] getFeature(){
        return Feature;
    }

    public String[] getType(){
        return Type;
    }

    public String[] getLoad(){
        return Load;
    }

    private String MyGETRequest(int buscode) throws IOException, JSONException

    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL urlForGetRequest = new URL("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode="+buscode);
        //URL urlForGetRequest = new URL("http://datamall2.mytransport.sg/ltaodataservice/TrafficIncidents");
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
            JSONArray jArray = (JSONArray) new JSONTokener(jObject.getString("Services")).nextValue();
            ServiceNo = new String[jArray.length()];
            NextBus = new String[jArray.length()];
            NextBus2 = new String[jArray.length()];
            NextBus3 = new String[jArray.length()];
            Feature = new String[jArray.length()];
            Type = new String[jArray.length()];
            Load = new String[jArray.length()];

            JSONObject temp;
            for(int n = 0; n < jArray.length(); n++)
            {
                JSONObject object = jArray.getJSONObject(n);
                // do some stuff....
                Log.d("testing json","JSON String Result " + object);

                ServiceNo[n] = object.getString("ServiceNo");
                temp = object.getJSONObject("NextBus");
                Feature[n] = temp.getString("Feature");
                Type[n] = temp.getString("Type");
                Load[n] = temp.getString("Load");
                NextBus[n] = temp.getString("EstimatedArrival");
                temp = object.getJSONObject("NextBus2");
                NextBus2[n] = temp.getString("EstimatedArrival");
                temp = object.getJSONObject("NextBus3");
                NextBus3[n] = temp.getString("EstimatedArrival");

            }
            //Log.d("testing json","testing here " + jObject.getString("ServiceNo"));
            /*Log.d("testing json","JSON String Result " + jArray);
            Log.d("testing json","JSON String Result " + ServiceNo[0]);
            Log.d("testing json","JSON String Result " + NextBus[0]);
            Log.d("testing json","JSON String Result " + NextBus2[0]);
            Log.d("testing json","JSON String Result " + NextBus3[0]);
            Log.d("testing json","JSON String Result " + Feature[0]);
            Log.d("testing json","JSON String Result " + Type[0]);
            Log.d("testing json","JSON String Result " + Load[0]);*/
            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("GET NOT WORKED");
        }
        return response.toString();
    }
}

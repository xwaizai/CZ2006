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
    private String[] Feature2;
    private String[] Feature3;
    
    private String[] Type;
    private String[] Type2;
    private String[] Type3;
    
    private String[] Load;
    private String[] Load2;
    private String[] Load3;

    /*public BusArrival(){
        try{
            MyGETRequest(27469);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

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

    public String[] getFeature2(){
        return Feature2;
    }

    public String[] getFeature3(){
        return Feature3;
    }

    public String[] getType(){
        return Type;
    }

    public String[] getType2(){
        return Type2;
    }

    public String[] getType3(){
        return Type3;
    }

    public String[] getLoad(){
        return Load;
    }

    public String[] getLoad2(){
        return Load2;
    }
    
    public String[] getLoad3(){
        return Load3;
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
            Feature2 = new String[jArray.length()];
            Feature3 = new String[jArray.length()];
            Type = new String[jArray.length()];
            Type2 = new String[jArray.length()];
            Type3 = new String[jArray.length()];
            Load = new String[jArray.length()];
            Load2 = new String[jArray.length()];
            Load3 = new String[jArray.length()];

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
                Feature2[n] = temp.getString("Feature");
                Type2[n] = temp.getString("Type");
                Load2[n] = temp.getString("Load");
                temp = object.getJSONObject("NextBus3");
                NextBus3[n] = temp.getString("EstimatedArrival");
                Feature3[n] = temp.getString("Feature");
                Type3[n] = temp.getString("Type");
                Load3[n] = temp.getString("Load");

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
            selectionSort(ServiceNo);
        } else {
            System.out.println("GET NOT WORKED");
        }
        return response.toString();
    }

    public void selectionSort (Comparable[] list)
    {
        int min;
        Comparable temp;
        String tmp;
        for (int index = 0; index < list.length-1; index++)
        {
            min = index;
            for (int scan = index+1; scan < list.length; scan++){
                String string1 = (String) list[scan];
                String string2 = (String) list[min];
                string1 = string1.trim().replaceFirst("ABCDEeMT","");
                string2 = string2.trim().replaceFirst("ABCDEeMT","");
                if (Integer.parseInt(string1) < Integer.parseInt(string2)){
                    min = scan;
                    Log.d("selectionSort", Integer.toString(min));
                }
            }


            // Swap the values
            temp = list[min];
            list[min] = list[index];
            list[index] = temp;

            tmp = NextBus[min];
            NextBus[min] = NextBus[index];
            NextBus[index] = tmp;

            tmp = NextBus2[min];
            NextBus2[min] = NextBus2[index];
            NextBus2[index] = tmp;

            tmp = NextBus3[min];
            NextBus3[min] = NextBus3[index];
            NextBus3[index] = tmp;

            tmp = Feature[min];
            Feature[min] = Feature[index];
            Feature[index] = tmp;

            tmp = Feature2[min];
            Feature2[min] = Feature2[index];
            Feature2[index] = tmp;

            tmp = Feature3[min];
            Feature3[min] = Feature3[index];
            Feature3[index] = tmp;

            tmp = Type[min];
            Type[min] = Type[index];
            Type[index] = tmp;

            tmp = Type2[min];
            Type2[min] = Type2[index];
            Type2[index] = tmp;

            tmp = Type3[min];
            Type3[min] = Type3[index];
            Type3[index] = tmp;

            tmp = Load[min];
            Load[min] = Load[index];
            Load[index] = tmp;

            tmp = Load2[min];
            Load2[min] = Load2[index];
            Load2[index] = tmp;

            tmp = Load3[min];
            Load3[min] = Load3[index];
            Load3[index] = tmp;
        }
    }
}

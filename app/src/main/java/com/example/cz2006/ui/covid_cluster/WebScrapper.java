package com.example.cz2006.ui.covid_cluster;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScrapper {
    // to connect to the latest URL of MOH website
    private String m_URL = "https://www.moh.gov.sg/news-highlights/details/update-on-local-covid-19-situation-(28-oct-2021)";
    private List<PlaceInfo> m_ListOfClusterDetails = new ArrayList<>();

    private Document m_MyDoc;
    private boolean m_IsDone = false;

    public boolean getIsDone()
    {
        return m_IsDone;
    }


    public WebScrapper()
    {
        Thread m_WebThread = new Thread(this::scrapeWebsite);
        m_WebThread.start();
    }

    public List<PlaceInfo> getClusterList()
    {
        return m_ListOfClusterDetails;
    }


    private synchronized void scrapeWebsite()
    {
        try {
            m_MyDoc = Jsoup.connect(m_URL).get();
            Elements tableRows = m_MyDoc.getElementsByTag("tr");
            Pattern pattern = Pattern.compile("(Case)|(Cluster)", Pattern.CASE_INSENSITIVE);
            Pattern statusPattern = Pattern.compile("(OK)", Pattern.CASE_INSENSITIVE);
            List<JSONObject> clusterLocations = new ArrayList<>();
            List<Thread> listOfCreatedThreads = new ArrayList<>();
            String[] fields = {"formatted_address", "name", "geometry"};
            for (Element tablerow : tableRows)
            {
                Elements tableDatas = tablerow.getElementsByTag("td");
                // we have to skip anything that has the word case! so that we dont send too many requests
                String dataStr = tableDatas.get(0).text();
                Matcher m = pattern.matcher(dataStr);
                if (m.find() == true)
                {
                    continue;
                }
                Thread googleAPIPlace = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                GoogleMapsAPI.getPlaceInfo(dataStr, fields, clusterLocations);
                            }
                        }
                );
                googleAPIPlace.run();
                listOfCreatedThreads.add(googleAPIPlace);
            }
            // wait for all of the threads to be joined
            for (Thread httpThread : listOfCreatedThreads)
            {
                httpThread.join();
            }
            // then put it into the map
            for (JSONObject placeJSON : clusterLocations)
            {
                JSONObject placeObj = null;
                // only proceeds when it is ok!
                String statusStr = placeJSON.getString("status");
                Matcher m = statusPattern.matcher(statusStr);
                if (m.find() == true) {
                    JSONArray candidate = placeJSON.getJSONArray("candidates");
                    placeObj = (candidate.getJSONObject(0));
                }
                else
                {
                    continue;
                }

                // then we will have to access geometry followed by location
                JSONObject geometryObj = placeObj.getJSONObject("geometry");
                // now it is about location
                JSONObject locationObj = geometryObj.getJSONObject("location");
                // then lat
                double latPos = locationObj.getDouble("lat");
                // then lng
                double lngPos = locationObj.getDouble("lng");
                // get the formatted address!
                String formattedAddress = placeObj.getString("formatted_address");
                // and lastly the name
                String placeName = placeObj.getString("name");
                m_ListOfClusterDetails.add(new PlaceInfo(placeName, latPos, lngPos, formattedAddress));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        m_IsDone = true;
    }
}

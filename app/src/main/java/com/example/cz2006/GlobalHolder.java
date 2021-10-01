package com.example.cz2006;

import com.example.cz2006.ui.covid_cluster.PlaceInfo;
import com.google.android.gms.maps.GoogleMap;

// simple singleton to handle all of the needed variables
public class GlobalHolder {
    // single variable that can never be changed and will exist throughout the lifetime of the program
    private static GlobalHolder m_Instance = new GlobalHolder();
    // destination to go to's address
    private PlaceInfo m_Destination;
    // start point address
    private PlaceInfo m_Start;
    // google map so that other classes can also access it!
    public GoogleMap m_GMap;

    private GlobalHolder()
    {
        // for debugging purpose and initialize the values!
        m_Start = new PlaceInfo("Pfizer", 1.299756,103.636375,"31 Tuas South Ave 6, Singapore 637578");
        m_Destination = new PlaceInfo("NSL OilChem Group of companies",1.306276,103.6998653,"23 Tanjong Kling Rd, Singapore 628049");
    }

    public static GlobalHolder getInstance()
    {
        return m_Instance;
    }

    public void setDestination(PlaceInfo dest)
    {
        m_Destination = dest;
    }

    public void setStart(PlaceInfo start)
    {
        m_Start = start;
    }

    public PlaceInfo getDesination()
    {
        return m_Destination;
    }

    public PlaceInfo getStart()
    {
        return m_Start;
    }
}
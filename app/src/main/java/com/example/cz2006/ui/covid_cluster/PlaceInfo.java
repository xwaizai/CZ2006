package com.example.cz2006.ui.covid_cluster;

public class PlaceInfo {
    public String m_Name = "";

    public double m_Lat;

    public double m_Long;

    public String m_Address = "";

    public PlaceInfo(String _name, double _lat, double _long, String _address)
    {
        m_Name = _name;
        m_Lat = _lat;
        m_Long = _long;
        m_Address = _address;
    }
}

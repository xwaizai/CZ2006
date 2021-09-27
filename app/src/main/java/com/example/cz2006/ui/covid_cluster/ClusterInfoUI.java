package com.example.cz2006.ui.covid_cluster;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cz2006.GlobalHolder;
import com.example.cz2006.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Dictionary;
import java.util.Hashtable;


public class ClusterInfoUI extends Fragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {
    private GoogleMap m_GMap;
    private WebScrapper m_WebScrapper;

    private Dictionary<String, PlaceInfo> m_ID_ClusterInfo = new Hashtable<>();
    private View m_COVIDInfoBox;

    public ClusterInfoUI() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_WebScrapper = new WebScrapper();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cluster_info_u_i, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager myChildManager = getParentFragmentManager();
        Fragment mapFragment = myChildManager.findFragmentById(R.id.map);
        SupportMapFragment spmapFragment = (SupportMapFragment) mapFragment;
        if (spmapFragment != null) {
            spmapFragment.getMapAsync(this::onMapReady);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // finally managed to get google map
        m_GMap = googleMap;
        m_GMap.setInfoWindowAdapter(this);
        GlobalHolder.getInstance().m_GMap = googleMap;
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity myActivity = getActivity();
        m_COVIDInfoBox = getLayoutInflater().inflate(R.layout.covid_infobox, null);
        new Thread() {
            public void run()
            {
                while (!m_WebScrapper.getIsDone() || m_GMap == null);
                myActivity.runOnUiThread(() -> {
                    setMarkers();
                });
            }
        }.start();
    }

    private void setMarkers() {
        // to set up the markers and bitmaps
        Context context = getContext();
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_outline_brightness_1_24);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        // set up the canvas so that they can draw the bitmap
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);

        // have to put all faith that m_GMap exists!
        for (PlaceInfo clusterPlace : m_WebScrapper.getClusterList())
        {
            LatLng placeLatLng = new LatLng(clusterPlace.m_Lat, clusterPlace.m_Long);
            Marker cluserMarker = m_GMap.addMarker(
                    new MarkerOptions().position(placeLatLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .title("Cluster Location")
                            .snippet(clusterPlace.m_Name)
            );
            // store it as key then can set the custom window
            m_ID_ClusterInfo.put(cluserMarker.getId(), clusterPlace);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        PlaceInfo clusterInfo = m_ID_ClusterInfo.get(marker.getId());
        if (clusterInfo != null)
        {
            TextView locationText = (TextView) m_COVIDInfoBox.findViewById(R.id.locationId);
            locationText.setText(clusterInfo.m_Name);
            TextView addressText = (TextView) m_COVIDInfoBox.findViewById(R.id.addressId);
            addressText.setText(clusterInfo.m_Address);
            return m_COVIDInfoBox;
        }
        return null;
    }
}
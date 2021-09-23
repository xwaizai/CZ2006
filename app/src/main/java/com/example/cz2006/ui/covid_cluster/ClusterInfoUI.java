package com.example.cz2006.ui.covid_cluster;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cz2006.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class ClusterInfoUI extends Fragment implements OnMapReadyCallback {
    private GoogleMap m_GMap;
    private ArrayList<Marker> m_ListOfCOVIDMarkers = new ArrayList<>();
    private WebScrapper m_WebScrapper;

    public ClusterInfoUI() {
        // Required empty public constructor
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
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity myActivity = getActivity();
        new Thread() {
            public void run()
            {
                myActivity.runOnUiThread(() -> {
                    while (!m_WebScrapper.getIsDone());
                    SetMarkers();
                });
            }
        }.start();
    }

    public void SetMarkers() {
        // have to put all faith that m_GMap exists!
        for (PlaceInfo clusterPlace : m_WebScrapper.getClusterList())
        {
            LatLng placeLatLng = new LatLng(clusterPlace.m_Lat, clusterPlace.m_Long);
            m_GMap.addMarker(new MarkerOptions().position(placeLatLng).title(clusterPlace.m_Name));
        }
    }
}
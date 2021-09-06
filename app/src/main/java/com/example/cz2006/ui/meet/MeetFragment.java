package com.example.cz2006.ui.meet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.cz2006.R;
import com.example.cz2006.databinding.FragmentMeetBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.cz2006.ui.meet.TrafficIncidents;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MeetFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{
    private GoogleMap mMap;
    private ArrayList<Marker> trafficincidentsmarkers = new ArrayList<>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {



        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng singapore = new LatLng(1.3521, 103.8198);

            //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        ToggleButton togglebutton =view.findViewById(R.id.trafficincidentstoggle);
        /*eventView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                         }
                                     }
        );*/
        togglebutton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        switch(buttonView.getId()){
            case R.id.trafficincidentstoggle:
                if(isChecked){
                    TrafficIncidents trafficIncidents = new TrafficIncidents();
                    String[] Type = trafficIncidents.getType();
                    double[] Latitude = trafficIncidents.getLatitude();
                    double[] Longitude = trafficIncidents.getLongitude();
                    String[] Message = trafficIncidents.getMessage();

                    for(int i = 0; i < Latitude.length ;i++){
                        LatLng temp = new LatLng(Latitude[i], Longitude[i]);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(temp).title(Message[i]));
                        trafficincidentsmarkers.add(marker);
                    }
                }
                else {
                    for(Marker m: trafficincidentsmarkers){
                        m.remove();
                    }
                }
                break;
        }

    }
}
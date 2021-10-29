package com.example.cz2006.ui.meet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cz2006.R;
import com.example.cz2006.ui.meet.bottomsheets.BottomFragment_Postal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MeetFragment extends Fragment {
    private GoogleMap mMap;
    private ArrayList<Marker> trafficincidentsmarkers = new ArrayList<>();
    private View rootView;

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
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        // Bottom Sheet
        LinearLayout mBottomSheet = rootView.findViewById(R.id.bottom_sheet);
        //Bottom Sheet
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        // Expanded by default
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.bottom_fragment_container, new BottomFragment_Postal());
        fragmentTransaction.commit();

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.trafficincidentstoggle);
        fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_warning_amber_24));

        fab.setOnClickListener(new View.OnClickListener() {
            boolean flag = true;

            public void onClick(View v) {
                if(flag){
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
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_warning_24));
                    flag = false;
                }
                else{
                    for(Marker m: trafficincidentsmarkers){
                        m.remove();
                    }
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_warning_amber_24));
                    flag = true;
                }

            }
        });
    }
}
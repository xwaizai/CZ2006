package com.example.cz2006.ui.meet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cz2006.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.BufferedReader;



import java.io.InputStreamReader;


import java.net.HttpURLConnection;

import java.net.URL;

import java.util.ArrayList;

public class MeetFragment extends Fragment implements View.OnClickListener {
    private GoogleMap mMap;
    private ArrayList<Marker> trafficincidentsmarkers = new ArrayList<>();
    private ArrayList<String> postcode = new ArrayList<>();
    private ArrayList<String> travelType = new ArrayList<>();
    private ArrayList<String> lat = new ArrayList<>();
    private ArrayList<String> lng= new ArrayList<>();
    private ArrayList<Integer> travelTime = new ArrayList<>();

    private View rootView;
    private ChipGroup chipGroup;

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

        // Postal Code bubble
        chipGroup = rootView.findViewById(R.id.chipGroup);

        // Initialise Add/Go Button
        MaterialButton addBtn = rootView.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        MaterialButton goBtn = rootView.findViewById(R.id.goBtn);
        goBtn.setOnClickListener(this);

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

    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.addBtn: {
                // do something for add Button
                retrieveInputs();
                break;
            }

            case R.id.goBtn: {
                // Do something for go Button
                Toast.makeText(getContext(), "Go", Toast.LENGTH_SHORT).show();
                retrieveInputs();

                // Send info to another view
                // Go to another view

                break;
            }

            //.... etc
        }
    }
    private boolean checkValid(String postalText) //check whether the postal code is valid using geocode
    {
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL urlForGetRequest = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="
                    +postalText+",+SG&key=AIzaSyDbPQ0ByGoYskLBVO0EvmEV_t1gGYNeZvw");


            String readLine = null;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");

            int responseCode = conection.getResponseCode();

            StringBuffer response = new StringBuffer();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conection.getInputStream()));
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                // print result
                JSONObject jObject = new JSONObject(response.toString());

                if (jObject.getString("status").equals("OK"))
                {
                    JSONArray result = (JSONArray) new JSONTokener(jObject.getString("results")).nextValue();
                    JSONObject r1 = result.getJSONObject(0);
                    JSONObject geometry = r1.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    this.lat.add(  location.getString("lat"));  //get the postal code lat and lng for future use to calculate the midpoint to meet
                    this.lng.add(  location.getString("lng"));
                    Log.d( "In: ","IN");
                    return true;
                }

            }
            return false;
        }

        catch(Exception e)
        {

            // TODO Auto-generated catch block
            Toast.makeText(getContext(), "Enter valid postal code", Toast.LENGTH_SHORT).show();
            Log.d( "Error: ",e.getMessage());
            return false;

        }
    }


    private void retrieveInputs() {
        TextInputEditText postalTextLayout = rootView.findViewById(R.id.postalCodeInput);
        MaterialButtonToggleGroup transportBtns = rootView.findViewById(R.id.transport_group);
        Slider timeSlider = rootView.findViewById(R.id.timeSlider);

        // Check which transport button pressed
        int buttonId = transportBtns.getCheckedButtonId();
        MaterialButton button = transportBtns.findViewById(buttonId);
        String trans_selected = "car";

        // Scan text input from postal code input
        String postalText = postalTextLayout.getText().toString();

        // Scan slider input
        int time_selected = (int) timeSlider.getValue();

        // Check which button id is match to which
        if (button.getId() == R.id.trainOption) {
            trans_selected = "train";
        } else if (button.getId() == R.id.busOption) {
            trans_selected = "bus";
        } else if (button.getId() == R.id.carOption) {
            trans_selected = "car";
        }


        if(checkValid(postalText))
        {
            Toast.makeText(getContext(), "Add", Toast.LENGTH_SHORT).show();

            addNewChip(postalText, chipGroup, trans_selected);

            // save info to memory
            saveChipsToMem(postalText, trans_selected, time_selected);
        } else {
            Toast.makeText(getContext(), "Please enter a valid postal code!", Toast.LENGTH_SHORT).show();
        }
    }


    private void addNewChip(String text, ChipGroup chipGroup, String type) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.postal_code, chipGroup, false);
        Drawable trans_type = getResources().getDrawable(R.drawable.ic_baseline_directions_car);
        switch(type) {
            case "train":
                trans_type = getResources().getDrawable(R.drawable.ic_baseline_train_24);
                break;
            case "bus":
                trans_type = getResources().getDrawable(R.drawable.ic_baseline_directions_bus_24);
                break;
        }
        chip.setChipIcon(trans_type);
        chip.setText(text);
        chipGroup.addView(chip, chipGroup.getChildCount() );
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String removedpostal = (String) chip.getText(); //get the postal code being removed
                int i;
                for(i=0; i<postcode.size(); i++)
                {
                    if(postcode.get(i).equals(removedpostal)) //find the index of the postalcode removed
                    {
                        break;
                    }
                }
                postcode.remove(i); //remove the postal code from all the arrays
                travelType.remove(i);
                lat.remove(i);
                lng.remove(i);
                travelTime.remove(i);

                /*String show="";
                int j;
                for(j=0; j<postcode.size(); j++)
                {
                    Log.d( "onClick: ",postcode.get(j)+ "," + travelType.get(j) + "," +
                    lat.get(j)+ "," + lng.get(j) + "," + travelTime.get(j));

                }
                */

                chipGroup.removeView(chip);
            }
        });
    }

    private void saveChipsToMem(String postal, String trans, int time) {  //to store the necessary information in the respective array for future calculation
        // Store Data to Array? Singleton?
        this.postcode.add(postal);
        this.travelType.add(trans);
        this.travelTime.add(time);
    }
}
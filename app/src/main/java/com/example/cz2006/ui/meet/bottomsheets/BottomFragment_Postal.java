package com.example.cz2006.ui.meet.bottomsheets;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cz2006.GlobalHolder;
import com.example.cz2006.R;
import com.example.cz2006.ui.meet.MeetMGR;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class BottomFragment_Postal extends Fragment implements View.OnClickListener {

    private ArrayList<String> postcode = new ArrayList<>();
    private ArrayList<String> travelType = new ArrayList<>();
    private ArrayList<String> lat = new ArrayList<>();
    private ArrayList<String> lng= new ArrayList<>();
    private ArrayList<Integer> travelTime = new ArrayList<>();
    private List<Pair<Location, Float>> geofenceList = new ArrayList<>();

    private float r;

    private View postalView;
    private ChipGroup chipGroup;
    private GeofenceHelper geofenceHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        postalView = inflater.inflate(R.layout.bottom_sheet_postal, container, false);

        // Initialise Add/Go Button
        MaterialButton addBtn = postalView.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        MaterialButton goBtn = postalView.findViewById(R.id.goBtn);
        goBtn.setOnClickListener(this);

        geofenceHelper = new GeofenceHelper(getContext());
        return postalView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Postal Code chip
        chipGroup = postalView.getRootView().findViewById(R.id.chipGroup);

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
                if(postcode.size() > 1) {
                    Toast.makeText(getContext(), "Go", Toast.LENGTH_SHORT).show();

                    chipGroup.setVisibility(View.GONE);

                    // Go to next fragment(Search places)
                    Pair midpoint;
                    BottomFragment_Place nextFrag = new BottomFragment_Place();
                    midpoint = findMidpoint(lat, lng);
                    Bundle bundle = new Bundle();
                    String latTemp = midpoint.first.toString();
                    String lngTemp = midpoint.second.toString();
                    bundle.putString("lat", latTemp);
                    bundle.putString("lng", lngTemp);
                    nextFrag.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.bottom_fragment_container, nextFrag);
                    fragmentTransaction.addToBackStack("postal");
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "Please add at least 2 entries", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            //.... etc
        }
    }

    private void retrieveInputs() {

        TextInputEditText postalTextLayout = postalView.findViewById(R.id.postalCodeInput);
        MaterialButtonToggleGroup transportBtns = postalView.findViewById(R.id.transport_group);
        Slider timeSlider = postalView.findViewById(R.id.timeSlider);

        MeetMGR meetMGR = new MeetMGR(this);

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
            r = time_selected*300;
        } else if (button.getId() == R.id.busOption) {
            trans_selected = "bus";
            r = (float) (time_selected*225);
        } else if (button.getId() == R.id.carOption) {
            trans_selected = "car";
            //r = trap0 velTime.get(lat.size()-1)*40;
            r = time_selected*600;
        }

        // setting validPostal to false;
        // Make sure the postal code is 6 digit
        if(postalText.length() == 6 && meetMGR.checkValidPostal(postalText,this.lat,this.lng))
        {
            //Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();

            addNewChip(postalText, chipGroup, trans_selected);

            // save info to memory
            saveChipsToMem(postalText, trans_selected, time_selected);
            Log.d( "postalinfo ",lat.get(lat.size()-1));
            Log.d( "postalinfo ",lng.get(lat.size()-1));
            Log.d( "postalinfo ",postcode.get(lat.size()-1));
            Log.d( "postalinfo ",Integer.toString(travelTime.get(lat.size()-1)));
            Log.d( "postalinfo ",travelType.get(lat.size()-1));

            GlobalHolder.getInstance().postalLat = lat;
            GlobalHolder.getInstance().postalLng = lng;
            GlobalHolder.getInstance().postalTravelType = travelType;

            // Create a geofence
            geofenceHelper.createGeo(Double.parseDouble(String.valueOf(lat.get(lat.size()-1))),Double.parseDouble(String.valueOf(lng.get(lng.size()-1))),r);
            // Add to geofence list
            Location temp = new Location("");

        } else {
            Toast.makeText(getContext(), "Please enter a valid postal code!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewChip(String text, ChipGroup chipGroup, String type) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.postal_code, chipGroup, false);
        Drawable trans_type = ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_baseline_directions_car, null);
        switch(type) {
            case "train":
                trans_type = ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_baseline_train_24, null);
                break;
            case "bus":
                trans_type = ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_baseline_directions_bus_24, null);
                break;
        }
        chip.setChipIcon(trans_type);
        chip.setText(text);
        chipGroup.addView(chip, chipGroup.getChildCount() );
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the postal code being removed
                String postalRemoved = (String) chip.getText();
                int i;
                for(i=0; i<postcode.size(); i++)
                {
                    //find the index of the postalcode removed
                    if(postcode.get(i).equals(postalRemoved))
                    {
                        //remove the postal code from all the arrays
                        postcode.remove(i);
                        travelType.remove(i);
                        lat.remove(i);
                        lng.remove(i);
                        travelTime.remove(i);
                        GlobalHolder.getInstance().m_GMap.clear();
                        break;
                    }
                }
                for(i=0; i<postcode.size(); i++){

                    if (travelType.get(i).equals("car")) {
                        r = travelTime.get(i)*600; //1250m/
                    } else if (travelType.get(i).equals("train")) {
                        r = travelTime.get(i)*300;
                    } else if (travelType.get(i).equals("bus")) {
                        r = (float) (travelTime.get(i)*225);
                    }
                    geofenceHelper.createGeo(Double.parseDouble(String.valueOf(lat.get(i))), Double.parseDouble(String.valueOf(lng.get(i))), r);
                }
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

    // calculate avg of lat and lng return pair of lat and lng
    private Pair findMidpoint(ArrayList<String> lat, ArrayList<String> lng) {
        double total = 0;
        double latAvg, lngAvg = 0;
        for (String l : lat) {
            total = total + Double.parseDouble(l);
        }
        latAvg = total / lat.size();

        total = 0;

        for (String l : lng) {
            total = total + Double.parseDouble(l);
        }
        lngAvg = total / lng.size();

        String latAvgStr = String.valueOf(latAvg);
        String lngAvgStr = String.valueOf(lngAvg);

        Pair p = new Pair(latAvgStr, lngAvgStr);
        return p;
    }

}
package com.example.cz2006.ui.meet.bottomsheets;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cz2006.R;
import com.example.cz2006.ui.meet.MeetMGR;
import com.example.cz2006.ui.meet.TrafficIncidents;
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

public class BottomFragment_Postal extends Fragment implements View.OnClickListener {

    private ArrayList<String> postcode = new ArrayList<>();
    private ArrayList<String> travelType = new ArrayList<>();
    private ArrayList<String> lat = new ArrayList<>();
    private ArrayList<String> lng= new ArrayList<>();
    private ArrayList<Integer> travelTime = new ArrayList<>();

    private View postalView;
    private ChipGroup chipGroup;
    MeetMGR meetMGR = new MeetMGR();

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
                if(postcode.size() > 0) {
                    Toast.makeText(getContext(), "Go", Toast.LENGTH_SHORT).show();
                    retrieveInputs();

                    chipGroup.setVisibility(View.GONE);
                    // Go to next fragment(Search places)
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.bottom_fragment_container, new BottomFragment_Place());
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

    //check whether the postal code is valid using geocode
    /*private boolean checkValid(String postalText)
    {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String api = getResources().getString(R.string.google_maps_key);

            URL urlForGetRequest = new URL(
                    "https://maps.googleapis.com/maps/api/geocode/json?address="
                            + postalText +",+SG&key=" + api);

            String readLine = null;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");

            int responseCode = conection.getResponseCode();

            StringBuffer response = new StringBuffer();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                Log.d( "In: ","IN1");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conection.getInputStream()));
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                // print result
                JSONObject jObject = new JSONObject(response.toString());

                Log.d( "jObject String: ", jObject.getString("status"));

                if (jObject.getString("status").equals("OK"))
                {
                    JSONArray result = (JSONArray) new JSONTokener(jObject.getString("results")).nextValue();
                    JSONObject r1 = result.getJSONObject(0);
                    JSONObject geometry = r1.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");

                    //get the postal code lat and lng for future use to calculate the midpoint to meet
                    this.lat.add(  location.getString("lat"));
                    this.lng.add(  location.getString("lng"));
                    Log.d( "In: ","IN2");
                    return true;
                }

            }
            return false;
        } catch(Exception e) {
            Log.d( "Error: ",e.getMessage());
            return false;
        }
    }*/


    private void retrieveInputs() {
        TextInputEditText postalTextLayout = postalView.findViewById(R.id.postalCodeInput);
        MaterialButtonToggleGroup transportBtns = postalView.findViewById(R.id.transport_group);
        Slider timeSlider = postalView.findViewById(R.id.timeSlider);

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
                        break;
                    }
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
}
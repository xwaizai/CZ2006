package com.example.cz2006.ui.meet.bottomsheets;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cz2006.GlobalHolder;
import com.example.cz2006.R;
import com.example.cz2006.ui.covid_cluster.PlaceInfo;
import com.example.cz2006.ui.meet.PlaceMGR;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class BottomFragment_Place extends Fragment implements View.OnClickListener{

    private View placeView;
    private PlaceRecyclerViewAdapter adapter;

    private ArrayList<String> placeName = new ArrayList<>();
    private ArrayList<String> placeAdd = new ArrayList<>();

    private ArrayList<String> sPlaces =  new ArrayList<>();
    private ArrayList<String> sLat = new ArrayList<>();
    private ArrayList<String> sLng = new ArrayList<>();
    private ArrayList<String> vicinity = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // If using add in BottomFragment_postal, need to removeAllViews
        // container.removeAllViews();
        placeView = inflater.inflate(R.layout.bottom_sheet_place, container, false);

        // Back and Next Button
        MaterialButton backBtn = placeView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        MaterialButton nextBtn = placeView.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);

        return placeView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        String lat = bundle.getString("lat");
        String lng = bundle.getString("lng");
        //Logic for finding locations should be here

        PlaceMGR placeManager = new PlaceMGR(this);
        String nextpageToken = placeManager.suggestPlace(lat, lng, sPlaces, sLat, sLng, vicinity);
        for(int j=0; j<2; j++) {
            nextpageToken = placeManager.placeNextPage(lat, lng, sPlaces, sLat, sLng, vicinity, nextpageToken);
        }

        Log.d("size of sPlaces", Integer.toString(sPlaces.size()));
        for (int i=0; i<sPlaces.size() ; i++) {
            placeName.add(sPlaces.get(i));
            placeAdd.add(vicinity.get(i));
            Log.d("checking recycle view", sPlaces.get(i));
        }

        initRecyclerView();

        // Search Bar
        TextInputEditText search = placeView.findViewById(R.id.placesInput);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

            public void filter(String text){
                ArrayList<String> tempN = new ArrayList<>();
                ArrayList<String> tempA = new ArrayList<>();
                Log.d("filter: ", Integer.toString(placeName.size()));
                for(int i=0; i<placeName.size(); i++){
                    Log.d("filter: string: ", placeName.get(i));
                    if(placeName.get(i).toLowerCase().contains(text.toLowerCase()) ||
                            placeAdd.get(i).toLowerCase().contains(text.toLowerCase())){
                        tempN.add(placeName.get(i));
                        tempA.add(placeAdd.get(i));
                    }
                }
                //update recyclerview
                adapter.updateList(tempN, tempA);
            }
        });

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                ChipGroup chipGroup = placeView.getRootView().findViewById(R.id.chipGroup);
                chipGroup.setVisibility(View.VISIBLE);
                getParentFragmentManager().popBackStackImmediate();
                break;
            case R.id.nextBtn:
                // Save selection to globalHolder
                int pos = adapter.getPosition();
                if(pos != -1) {
                    double lat = Double.parseDouble(sLat.get(pos));
                    double lng = Double.parseDouble(sLng.get(pos));
                    PlaceInfo placeInfo = new PlaceInfo(sPlaces.get(pos), lat, lng, "");
                    GlobalHolder.getInstance().setDestination(placeInfo);
                    Log.d("sPlace: ", GlobalHolder.getInstance().getDesination().m_Name);

                    // Add navigation to next
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.bottom_fragment_container, new BottomFragment_Start());
                    fragmentTransaction.addToBackStack("places");
                    fragmentTransaction.commit();
                }
                else
                    Toast.makeText(getContext(), "Please select a starting location", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = placeView.findViewById(R.id.placeRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new PlaceRecyclerViewAdapter(placeName, placeAdd,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

}
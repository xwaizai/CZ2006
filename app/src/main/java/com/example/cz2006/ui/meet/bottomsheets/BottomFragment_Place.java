package com.example.cz2006.ui.meet.bottomsheets;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cz2006.R;
import com.example.cz2006.ui.busarrival.BusRecyclerViewAdapter;
import com.example.cz2006.ui.meet.PlaceMGR;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Locale;

public class BottomFragment_Place extends Fragment implements View.OnClickListener{

    private View placeView;
    private PlaceRecyclerViewAdapter adapter;

    private ArrayList<String> placeName = new ArrayList<>();
    private ArrayList<String> placeAdd = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // If using add in BottomFragment_postal, need to removeallviews
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

        ArrayList<String> sPlaces =  new ArrayList<>();
        ArrayList<String> sLat = new ArrayList<>();
        ArrayList<String> sLng = new ArrayList<>();
        ArrayList<String> vicinity = new ArrayList<>();

        PlaceMGR placeManager = new PlaceMGR();
        placeManager.suggestPlace(lat, lng, sPlaces, sLat, sLng, vicinity);

        //Example how to insert to recyclerView
        /*
        placeName.add("NTU");
        placeAdd.add("50 aksdas 12312");
        placeName.add("NTU2");
        placeAdd.add("51 aksdas 12313");
        placeName.add("NTU3");
        placeAdd.add("52 aksdas 12314");
        placeName.add("NTU4");
        placeAdd.add("53 aksdas 12315");
        */

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

                //Add navigation to next
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
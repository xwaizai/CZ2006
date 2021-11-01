package com.example.cz2006.ui.meet.bottomsheets;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.cz2006.ui.bottom_UI.TransitUI;
import com.example.cz2006.ui.car_navi.CarUI;
import com.example.cz2006.ui.covid_cluster.PlaceInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class BottomFragment_Start extends Fragment implements View.OnClickListener{

    private View startView;
    private StartRecyclerViewAdapter adapter;

    private ArrayList<String> postalCode = new ArrayList<>();
    private ArrayList<Drawable> chipIcon = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if(!postalCode.isEmpty()){
            Log.d("onViewCreated: ", "clear");
            postalCode.clear();
            adapter.notifyDataSetChanged();
        }

        startView = inflater.inflate(R.layout.bottom_sheet_start, container, false);

        // Initialise Add/Go Button
        MaterialButton addBtn = startView.findViewById(R.id.startBackBtn);
        addBtn.setOnClickListener(this);

        MaterialButton goBtn = startView.findViewById(R.id.startNextBtn);
        goBtn.setOnClickListener(this);

        return startView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChipGroup chipGroup = startView.getRootView().findViewById(R.id.chipGroup);
        for(int i=0; i<chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            String checkDupString = chip.getText().toString();
            if(!postalCode.contains(checkDupString))
                postalCode.add(chip.getText().toString());
                chipIcon.add(chip.getChipIcon());
        }
        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.startBackBtn:
                getParentFragmentManager().popBackStackImmediate();
                break;
            case R.id.startNextBtn:
                //Add navigation to next
                int pos = adapter.getPosition();
                if(pos != -1) {
                    double lng = Double.parseDouble(GlobalHolder.getInstance().postalLng.get(pos));
                    double lat = Double.parseDouble(GlobalHolder.getInstance().postalLat.get(pos));
                    PlaceInfo placeInfo = new PlaceInfo("", lat, lng, "");
                    GlobalHolder.getInstance().setStart(placeInfo);
                    Log.d("Lat: Lat ", Double.toString(GlobalHolder.getInstance().getStart().m_Lat));
                    Log.d("Long: Long ", Double.toString(GlobalHolder.getInstance().getStart().m_Long));

                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    if (GlobalHolder.getInstance().postalTravelType.get(pos).equals("car"))
                        fragmentTransaction.replace(R.id.bottom_fragment_container, new CarUI());
                    else
                        fragmentTransaction.replace(R.id.bottom_fragment_container, new TransitUI());
                    fragmentTransaction.addToBackStack("start");
                    fragmentTransaction.commit();

                }
                else
                    Toast.makeText(getContext(), "Please select a starting point", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = startView.findViewById(R.id.startRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new StartRecyclerViewAdapter(postalCode, chipIcon,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }
}

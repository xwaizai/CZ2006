package com.example.cz2006.ui.bottom_UI;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.cz2006.GlobalHolder;
import com.example.cz2006.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class TransitUI extends BottomSheetDialogFragment {
    private Polyline m_RouteLine;

    public TransitUI()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_transit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // call the google map navigation for the start and end
        GoogleDirection.withServerKey("AIzaSyA4U9CFt7dstIfYDSAYEi8uUbiG0wjJOAc")
                .from(new LatLng(GlobalHolder.getInstance().getStart().m_Lat, GlobalHolder.getInstance().getStart().m_Long))
                .to(new LatLng(GlobalHolder.getInstance().getDesination().m_Lat, GlobalHolder.getInstance().getDesination().m_Long))
                .transportMode(TransportMode.TRANSIT)
                .execute(
                        new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(@Nullable Direction direction) {
                                // then start changing the text!
                                String status = direction.getStatus();
                                if (status.equals(RequestResult.OK))
                                {
                                    // shortest route
                                    Route route = direction.getRouteList().get(0);
                                    Leg leg = route.getLegList().get(0);
                                    Info distanceInfo = leg.getDistance();
                                    Info durationInfo = leg.getDuration();
                                    // start drawing path
                                    ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                    PolylineOptions polylineOptions = DirectionConverter.createPolyline(
                                            getActivity(),directionPositionList,5,getResources().getColor(R.color.blue)
                                    );
                                    m_RouteLine = GlobalHolder.getInstance().m_GMap.addPolyline(polylineOptions);
                                    // then get the step by step instruction!
                                    // TODO: Samuel add steps into recycleview
                                    // Loop through step list
                                    // for each step, check whether it's step list is null
                                    // if it is not null
                                    // loop through step's step list
                                    // populate the recycler view accordingly.....
                                    // most likely, if the transport mode is WALKING, it will have inner step list
                                    // if transport mode is TRANSIT, it will have no step list
                                    List<Step> individualSteps = leg.getStepList();
                                    for (Step indivStep : individualSteps)
                                    {
                                        // it will have it's own html instruction
                                        Log.d("testing transit",indivStep.getHtmlInstruction());
                                        if (indivStep.getStepList() != null) {
                                            for (Step stepOfStep : indivStep.getStepList()) {
                                                Log.d("testing step of step", stepOfStep.getHtmlInstruction());
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    Toast.makeText(context, "Navigation error: " + status, Toast.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void onDirectionFailure(@NonNull Throwable t) {
                                // print the error message
                                System.out.println(t.getMessage());
                            }
                        }
                );
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        m_RouteLine.remove();
        // then remove the polyline!
        super.onDismiss(dialog);
    }
}

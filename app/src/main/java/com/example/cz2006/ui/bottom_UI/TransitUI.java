package com.example.cz2006.ui.bottom_UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransitUI extends Fragment implements View.OnClickListener  {
    private Polyline m_RouteLine;

    private View transitView;

    private ArrayList<String> directions = new ArrayList<>();
    private ArrayList<String> distance = new ArrayList<>();
    private ArrayList<String> turn = new ArrayList<>();
    private TransitRecyclerViewAdapter adapter;
    private GoogleMap gMap;
    private Marker destMarker;

    public TransitUI()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        transitView= inflater.inflate(R.layout.bottom_sheet_transit, container, false);

        MaterialButton goBtn = transitView.findViewById(R.id.transitBackBtn);
        goBtn.setOnClickListener(this);

        gMap = GlobalHolder.getInstance().m_GMap;

        return transitView;
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

                                    //Log.d("onDirectionSuccess: ", route.getLegList().get(0).getViaWaypointList());
                                    TextView totalDist = transitView.findViewById(R.id.totalDist);
                                    TextView totalTime = transitView.findViewById(R.id.totalTime);
                                    TextView dest = transitView.findViewById(R.id.finalDestination);

                                    //If digit, set bold
                                    String durStr = durationInfo.getText();
                                    final SpannableStringBuilder spannable = new SpannableStringBuilder(durStr);

                                    Pattern pattern = Pattern.compile("[0-9]+");
                                    Matcher matcher = pattern.matcher(durStr);
                                    while(matcher.find()) {
                                        spannable.setSpan(
                                                new android.text.style.StyleSpan(android.graphics.Typeface.BOLD)
                                                , matcher.start(), matcher.end(),
                                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    }
                                    totalTime.setText(spannable);
                                    totalDist.setText("("+distanceInfo.getText()+")");
                                    dest.setText(leg.getEndAddress());


                                    LatLng start = leg.getStartLocation().getCoordination();
                                    LatLng end = leg.getEndLocation().getCoordination();

                                    final LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    builder.include(start);
                                    builder.include(end);
                                    LatLngBounds bounds = builder.build();

                                    //Change the padding as per needed
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,200);
                                    gMap.setPadding(0,0,0,900);
                                    gMap.animateCamera(cu);

                                    //Add destination marker
                                    destMarker = gMap.addMarker(new MarkerOptions().position(end).title("Destination"));

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
                                        String transitOrWalk = indivStep.getHtmlInstruction();
                                        Log.d("testing transit", transitOrWalk);
                                        directions.add(transitOrWalk);
                                        distance.add("");
                                        String firstWord = transitOrWalk.split(" ", 2)[0];
                                        if(firstWord.contains("Walk"))
                                            turn.add("walk");
                                        else if(firstWord.contains("Bus"))
                                            turn.add("bus");
                                        else if(firstWord.contains("Subway"))
                                            turn.add("train");
                                        else
                                            turn.add("");

                                        if (indivStep.getStepList() != null) {
                                            for (Step stepOfStep : indivStep.getStepList()) {
                                                String dirText = Jsoup.parse(stepOfStep.getHtmlInstruction()).text();
                                                String disText = stepOfStep.getDistance().getText();

                                                Log.d("transit step of step", dirText);
                                                Log.d("step time: ", disText);
                                                if(dirText.contains("Destination"))
                                                    turn.add("destination");
                                                else
                                                    turn.add("");
                                                directions.add(dirText);
                                                distance.add(disText);
                                            }
                                        }
                                    }
                                    Log.d("size of directions ", Integer.toString(directions.size()));
                                    initRecyclerView();
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

//    @Override
//    public void onDismiss(@NonNull DialogInterface dialog) {
//        m_RouteLine.remove();
//        // then remove the polyline!
//        super.onDismiss(dialog);
//    }



    private void initRecyclerView(){
        RecyclerView recyclerView = transitView.findViewById(R.id.transitRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new TransitRecyclerViewAdapter(directions, distance, turn,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transitBackBtn: {
                // do something for add Button
                getParentFragmentManager().popBackStackImmediate();

                destMarker.remove();
                gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(1.19, 103.812)));
                gMap.setPadding(0,0,0,0);
                gMap.animateCamera(CameraUpdateFactory.zoomTo(10.5f));
                break;
            }
        }
    }

}

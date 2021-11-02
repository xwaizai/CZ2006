package com.example.cz2006.ui.car_navi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.cz2006.GlobalHolder;
import com.example.cz2006.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CarUI extends BottomSheetDialogFragment implements View.OnClickListener {

    private Polyline m_RouteLine;
    private Marker destMarker;
    private GoogleMap gMap;
    private ShimmerFrameLayout shimmerContainer;
    private View carInfo;

    public CarUI()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gMap = GlobalHolder.getInstance().m_GMap;

        View carView = inflater.inflate(R.layout.bottom_sheet_car, container, false);
        shimmerContainer = carView.findViewById(R.id.shimmer_car);
        carInfo = carView.findViewById(R.id.car_info);
        carInfo.setVisibility(View.GONE);
        shimmerContainer.setVisibility(View.VISIBLE);
        shimmerContainer.startShimmer();

        return carView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // then get the button and set the listener
        MaterialButton backBtn = view.findViewById(R.id.carBackBtn);
        backBtn.setOnClickListener(this);

        MaterialButton openMapButton = view.findViewById(R.id.open_google_map_btn);
        openMapButton.setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // call the google map navigation for the start and end
        GoogleDirection.withServerKey("AIzaSyA4U9CFt7dstIfYDSAYEi8uUbiG0wjJOAc")
                .from(new LatLng(GlobalHolder.getInstance().getStart().m_Lat, GlobalHolder.getInstance().getStart().m_Long))
                .to(new LatLng(GlobalHolder.getInstance().getDesination().m_Lat, GlobalHolder.getInstance().getDesination().m_Long))
                .transportMode(TransportMode.DRIVING)
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
                                    // get car time text
                                    TextView myTxt = (TextView) getView().findViewById(R.id.car_time);
                                    myTxt.setText(durationInfo.getText());
                                    // get distance text
                                    myTxt = (TextView) getView().findViewById(R.id.car_dist);
                                    myTxt.setText(distanceInfo.getText());
                                    // start drawing path
                                    ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                    PolylineOptions polylineOptions = DirectionConverter.createPolyline(
                                            getActivity(),directionPositionList,5,getResources().getColor(R.color.blue)
                                    );
                                    m_RouteLine = GlobalHolder.getInstance().m_GMap.addPolyline(polylineOptions);

                                    LatLng start = leg.getStartLocation().getCoordination();
                                    LatLng end = leg.getEndLocation().getCoordination();

                                    final LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    builder.include(start);
                                    builder.include(end);
                                    LatLngBounds bounds = builder.build();

                                    //Change the padding as per needed
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,200);
                                    gMap.setPadding(0,0,0,500);
                                    gMap.animateCamera(cu);

                                    //Add destination marker
                                    destMarker = gMap.addMarker(new MarkerOptions().position(end).title("Destination"));
                                }
                                else
                                {
                                    Toast.makeText(context, "Navigation error: " + status, Toast.LENGTH_SHORT);
                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        shimmerContainer.setVisibility(View.GONE);
                                        carInfo.setVisibility(View.VISIBLE);
                                    }
                                }, 500);
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


    @Override
    public void onClick(View view) {
        // Open google map!
        switch(view.getId()){
            case R.id.open_google_map_btn:
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+
                        GlobalHolder.getInstance().getDesination().m_Address
                );
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
            case R.id.carBackBtn:
                getParentFragmentManager().popBackStackImmediate();

                destMarker.remove();
                m_RouteLine.remove();
                gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(1.19, 103.812)));
                gMap.setPadding(0,0,0,0);
                gMap.animateCamera(CameraUpdateFactory.zoomTo(10.5f));
                break;
        }

    }
}

package com.example.cz2006.ui.meet.bottomsheets;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.cz2006.GlobalHolder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class GeofenceHelper extends ContextWrapper {
    private static final String TAG = "GeofenceHelper";
    //  private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private GeofencingClient geofencingClient;

    PendingIntent pendingIntent;

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    public GeofenceHelper(Context base) {
        super(base);
    }


    public GeofencingRequest getGeofencingRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }

    public Geofence getGeofence(String ID, LatLng latLng, float radius, int transitionTypes) {
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public void createGeo(double latitude, double longitude, float radius) {
        LatLng latLng = new LatLng(latitude, longitude);
        addMarker(latLng);
        addCircle(latLng, radius);
        addGeofence(latLng, radius);
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        GlobalHolder.getInstance().m_GMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.strokeColor(Color.argb(64, 255, 0, 0));
        circleOptions.strokeWidth(4);
        GlobalHolder.getInstance().m_GMap.addCircle(circleOptions);
    }

    private void addGeofence(LatLng latLng, float radius) {
        Geofence geofence = getGeofence(GEOFENCE_ID, latLng, radius, Geofence.
                GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = getGeofencingRequest(geofence);
        PendingIntent pendingIntent = getPendingIntent();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*
            permissions setting */
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = getErrorString(e);
                        Log.d(TAG, "onFailure:" + errorMessage);
                    }
                });
    }

    public PendingIntent getPendingIntent(){
        if (pendingIntent!=null){
            return pendingIntent;
        }
        //Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        Intent intent = new Intent(this,GeofenceBroadcastReceiver.class);
        pendingIntent = pendingIntent.getBroadcast(this, 2607,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public String getErrorString(Exception e){
        if(e instanceof ApiException){
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()){
                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
            }
        }
        return e.getLocalizedMessage();
    }
/*
    public boolean checkIfInsideGeofence(Location, Geofence) {
        // TODO check if a location is inside a geofence
        Location.distanceTo()
        return;
    } */

}

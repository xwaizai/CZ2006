package com.example.cz2006.ui.meet.bottomsheets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "Geofence trigger...", Toast.LENGTH_SHORT).show();

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        // Get the geofences that were triggered
        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        // Get size of geofence list
        /*
        int geofenceListSize = geofenceList.size();
        if (geofenceListSize > 1 ) {
            // addmarker
        }

        String geofenceTransitionDetails = getGeofenceTransitionDetails(
                this,
                geofenceTransition,
                triggeringGeofences
        );
        */
        for(Geofence geofence: geofenceList){
            Log.d(TAG,"OnReceive:" +geofence.getRequestId());
        }
        int transitionType = geofencingEvent.getGeofenceTransition();

        /*
        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER && transitionType == Geofence.GEOFENCE_TRANSITION_DWELL) {
            // add marker
            return;
        }
        */

        switch(transitionType){
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
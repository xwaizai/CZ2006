package com.example.cz2006.ui.busarrival;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cz2006.R;

import org.w3c.dom.Text;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BusResultsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_results,container,false);
        TextView showReceivedData = (TextView) view.findViewById(R.id.busresulttest);
        Bundle bundle = this.getArguments();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String test = bundle.getStringArray("NextBus")[0].replace("T"," ");
        test = test.substring(0,19);

        Timestamp testnext = Timestamp.valueOf(test);
        long milliseconds = testnext.getTime() - timestamp.getTime();
        int seconds = (int) milliseconds / 1000;
        int minutes = (seconds % 3600) / 60;
        if(bundle != null){
            // handle your code here.
            showReceivedData.setText(Integer.toString(minutes));
            //showReceivedData.setText(timestamp.toString());


        }

        return view;
    }
}
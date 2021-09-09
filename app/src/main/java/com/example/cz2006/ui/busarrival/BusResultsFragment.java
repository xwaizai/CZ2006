package com.example.cz2006.ui.busarrival;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.cz2006.MainActivity;
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
        //View view = inflater.inflate(R.layout.fragment_bus_results,container,false);
        View rootView = inflater.inflate(R.layout.fragment_bus_results,
                container, false);
        TableLayout myLayout = (TableLayout) rootView.findViewById(R.id.busresulttable);
        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2=new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
        /*LinearLayout myLayout = (LinearLayout) rootView.findViewById(R.id.busresults);
        LinearLayout.LayoutParams layoutParams;*/
        Bundle bundle = this.getArguments();


        if(bundle != null) {
            // handle your code here.
            int[] ServiceNo = bundle.getIntArray("ServiceNo");
            String[] NextBus = bundle.getStringArray("NextBus");
            String[] NextBus2 = bundle.getStringArray("NextBus2");
            String[] NextBus3 = bundle.getStringArray("NextBus3");
            String[] Feature = bundle.getStringArray("Feature");
            String[] Type = bundle.getStringArray("Type");
            String[] Load = bundle.getStringArray("Load");
            int minutes;
            for (int j = 0; j < ServiceNo.length; j++){
                TableRow row = new TableRow(this.getActivity());
                TextView onerow[] = new TextView[6];
                for (int i = 0; i < 6; i++) {
                    onerow[i] = (TextView) new TextView(this.getActivity());
                    switch (i) {
                        case 0:
                            onerow[i].setText(Integer.toString(ServiceNo[j]));
                            onerow[i].setTextSize(30);
                            row.addView(onerow[i]);
                            break;
                        case 1:
                            onerow[i].setText(Feature[j]);
                            onerow[i].setTextSize(30);
                            onerow[i].setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
                            if(Feature[j].equals("WAB")){
                                ImageView wheelchair = new ImageView(this.getActivity());
                                wheelchair.setImageResource(R.drawable.ic_baseline_wheelchair_pickup_24);
                                row.addView(wheelchair);
                            }
                            break;
                        case 2:
                            if(Type[j].equals("DD")){
                                onerow[i].setText("double decker");
                            }else if (Type[j].equals("SD")){
                                onerow[i].setText("single decker");
                            }else{
                                onerow[i].setText("");
                            }

                            onerow[i].setTextSize(10);
                            onerow[i].setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
                            row.addView(onerow[i]);
                            break;
                        case 3:
                        case 4:
                        case 5:
                            if (i == 3) {
                                minutes = timestamptominutes(bundle.getStringArray("NextBus")[j]);
                                if(Load[j].equals("SEA")) {
                                    onerow[i].setTextColor(Color.parseColor("#84E296"));
                                }else if(Load[j].equals("LSD")){
                                    onerow[i].setTextColor(Color.parseColor("#721817"));
                                }
                            } else {
                                minutes = timestamptominutes(bundle.getStringArray("NextBus" + Integer.toString(i-2))[j]);
                            }
                            onerow[i].setText(Integer.toString(minutes));
                            onerow[i].setTextSize(30);
                            onerow[i].setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
                            row.addView(onerow[i]);
                            break;

                    }
                    onerow[i].setPadding(30,20,5,5);
                    row.setLayoutParams(params2);
                }
                myLayout.addView(row);
        }

        }

        return rootView;
    }

    private int timestamptominutes(String time){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(time.length()==0){
            return -1;
        }
        String test = time.replace("T"," ");
        Log.d("test",test);
        test = test.substring(0,19);

        Timestamp testnext = Timestamp.valueOf(test);
        long milliseconds = testnext.getTime() - timestamp.getTime();
        int seconds = (int) milliseconds / 1000;
        int minutes = (seconds % 3600) / 60;

        return minutes;
    }
}
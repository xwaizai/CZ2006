package com.example.cz2006.ui.busarrival;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cz2006.R;
import com.example.cz2006.databinding.FragmentBusarrivalBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class BusarrivalFragment extends Fragment {

    private static final String TAG = "BusarrivialFragment";

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private BusarrivalViewModel busarrivalViewModel;
    private FragmentBusarrivalBinding binding;

    private ArrayList<String> mServiceNo = new ArrayList<>();
    private ArrayList<String> mBusStopFirst = new ArrayList<>();
    private ArrayList<String> mBusStopSecond = new ArrayList<>();
    private ArrayList<String> mBusStopThird = new ArrayList<>();
    
    private ArrayList<String> mTypeOfBusFirst = new ArrayList<>();
    private ArrayList<String> mTypeOfBusSecond = new ArrayList<>();
    private ArrayList<String> mTypeOfBusThird= new ArrayList<>();

    private ArrayList<Boolean> mFeatureFirst = new ArrayList<>();
    private ArrayList<Boolean> mFeatureSecond = new ArrayList<>();
    private ArrayList<Boolean> mFeatureThird= new ArrayList<>();

    private ArrayList<String> mLoadFirst = new ArrayList<>();
    private ArrayList<String> mLoadSecond = new ArrayList<>();
    private ArrayList<String> mLoadThird= new ArrayList<>();
    private BusarrivalFragment mContext;

    private View root;
    private SwipeRefreshLayout refreshView;

    private RecyclerViewAdapter adapter;

    private BusArrival busArrival;

    private boolean viewPopulated = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        busarrivalViewModel =
                new ViewModelProvider(this).get(BusarrivalViewModel.class);

        binding = FragmentBusarrivalBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        refreshView = root.findViewById(R.id.refreshView);


        TextInputEditText editText = binding.busstopquery;
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Log.d("onQueryTextSubmit", String.valueOf(editText.getEditableText()));

                    // To check if RecyclerView is already populated
                    if(viewPopulated)
                        clearList();
                    else
                        viewPopulated = true;

                    String query = String.valueOf(editText.getEditableText());
                    if (isNumeric(query) && query.length() == 5){
                        busArrival = new BusArrival(Integer.parseInt(query));
                        busResult();

                        // Send all info to recycler view
                        initRecyclerView();

                    }else{
                        Toast.makeText(getContext(), "The bus stop code must be 5 digit numeric!", Toast.LENGTH_LONG).show();
                    }

                    return true;
                }
                return false;
            }
        });

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load data to your RecyclerView
                // clear old list
                clearList();

                // add new list
                String query = String.valueOf(editText.getEditableText());
                if (isNumeric(query) && query.length() == 5) {
                    busArrival = new BusArrival(Integer.parseInt(query));
                    busResult();
                }
                // notify adapter
                refreshView.setRefreshing(false);

            }
            });

        return root;
    }

    public void clearList(){
        mServiceNo.clear();
        mBusStopFirst.clear();
        mBusStopSecond.clear();
        mBusStopThird.clear();
        mTypeOfBusFirst.clear();
        mTypeOfBusSecond.clear();
        mTypeOfBusThird.clear();
        mFeatureFirst.clear();
        mFeatureSecond.clear();
        mFeatureThird.clear();
        mLoadFirst.clear();
        mLoadSecond.clear();
        mLoadThird.clear();
        adapter.notifyDataSetChanged();
    }

    public void busResult(){
        String[] ServiceNo = busArrival.getServiceNo();
        String[] NextBus1 = busArrival.getNextBus();
        String[] NextBus2 = busArrival.getNextBus2();
        String[] NextBus3 = busArrival.getNextBus3();
        String[] Feature1 = busArrival.getFeature();
        String[] Feature2 = busArrival.getFeature2();
        String[] Feature3 = busArrival.getFeature3();
        String[] Type1 = busArrival.getType();
        String[] Type2 = busArrival.getType2();
        String[] Type3 = busArrival.getType3();
        String[] Load1 = busArrival.getLoad();
        String[] Load2 = busArrival.getLoad2();
        String[] Load3 = busArrival.getLoad3();
        if(ServiceNo.length==0){
            Toast.makeText(getContext(), "Sorry! No bus stop found!", Toast.LENGTH_LONG).show();
        }

        for (int j = 0; j < ServiceNo.length; j++) {
            //ServiceNo
            Log.d("buses", Arrays.toString(ServiceNo));
            mServiceNo.add(ServiceNo[j]);

            //Next Buses Minutes
            Log.d("buses", Arrays.toString(NextBus1));
            int minute = timestamptominutes(NextBus1[j]);
            if (minute == -99)
                mBusStopFirst.add("-");
            else if (minute > 0)
                mBusStopFirst.add(Integer.toString(minute));
            else if (minute == 0)
                mBusStopFirst.add("Arr");
            else
                mBusStopFirst.add("Left");

            minute = timestamptominutes(NextBus2[j]);
            if (minute == -99)
                mBusStopSecond.add("-");
            else if (minute > 0)
                mBusStopSecond.add(Integer.toString(minute));
            else if (minute == 0)
                mBusStopSecond.add("Arr");
            else
                mBusStopSecond.add("Left");

            minute = timestamptominutes(NextBus3[j]);
            Log.d("Min,", Integer.toString(minute));
            if (minute == -99)
                mBusStopThird.add("-");
            else if (minute > 0)
                mBusStopThird.add(Integer.toString(minute));
            else if (minute == 0)
                mBusStopThird.add("Arr");
            else
                mBusStopThird.add("Left");

            //Bus Type
            String type = "";
            if (Type1[j].equals("SD")) {
                type = "Single";
            } else if (Type1[j].equals("DD")) {
                type = "Double";
            }
            mTypeOfBusFirst.add(type);

            type = "";
            if (Type2[j].equals("SD")) {
                type = "Single";
            } else if (Type2[j].equals("DD")) {
                type = "Double";
            }
            mTypeOfBusSecond.add(type);

            type = "";
            if (Type3[j].equals("SD")) {
                type = "Single";
            } else if (Type3[j].equals("DD")) {
                type = "Double";
            }
            mTypeOfBusThird.add(type);

            //Feature - Handicap
            boolean handicap = false;
            if(Feature1[j].equals("WAB"))
                handicap = true;
            mFeatureFirst.add(handicap);

            handicap = false;
            if(Feature2[j].equals("WAB"))
                handicap = true;
            mFeatureSecond.add(handicap);

            handicap = false;
            if(Feature3[j].equals("WAB"))
                handicap = true;
            mFeatureThird.add(handicap);

            //Load - Capacity
            String load = "";
            Log.d("buses", Arrays.toString(Load1));
            if (Load1[j].equals("SEA")) {
                load = "low";
            }else if(Load1[j].equals("SDA")) {
                load = "mid";
            }else if(Load1[j].equals("LSD")) {
                load = "high";
            }
            Log.d("After:", load);
            mLoadFirst.add(load);

            load = "";
            if (Load2[j].equals("SEA")) {
                load = "low";
            }else if(Load2[j].equals("SDA")) {
                load = "mid";
            }else if(Load2[j].equals("LSD")) {
                load = "high";
            }
            mLoadSecond.add(load);

            load = "";
            if (Load3[j].equals("SEA")) {
                load = "low";
            }else if(Load3[j].equals("SDA")) {
                load = "mid";
            }else if(Load3[j].equals("LSD")) {
                load = "high";
            }
            mLoadThird.add(load);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }

    }

    private void initRecyclerView(){
        RecyclerView recycleView = root.findViewById(R.id.busRecyclerView);
        adapter = new RecyclerViewAdapter(
                mServiceNo, mBusStopFirst, mBusStopSecond, mBusStopThird,
                mTypeOfBusFirst, mTypeOfBusSecond, mTypeOfBusThird,
                mFeatureFirst, mFeatureSecond, mFeatureThird,
                mLoadFirst, mLoadSecond, mLoadThird,this);
        recycleView.setLayoutManager(new LinearLayoutManager(recycleView.getContext()));
        recycleView.setAdapter(adapter);
    }

    private int timestamptominutes(String time){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(time.equals(""))
            return -99;
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

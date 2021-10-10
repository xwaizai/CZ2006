package com.example.cz2006.ui.busarrival;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cz2006.R;

import java.util.ArrayList;

public class BusRecyclerViewAdapter extends RecyclerView.Adapter<BusRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mServiceNo = new ArrayList<>();
    
    private ArrayList<String> mBusStopFirst = new ArrayList<>();
    private ArrayList<String> mBusStopSecond= new ArrayList<>();
    private ArrayList<String> mBusStopThird = new ArrayList<>();
    
    private ArrayList<String> mTypeOfBusFirst = new ArrayList<>();
    private ArrayList<String> mTypeOfBusSecond = new ArrayList<>();
    private ArrayList<String> mTypeOfBusThird = new ArrayList<>();
    
    private ArrayList<Boolean> mFeatureFirst = new ArrayList<>();
    private ArrayList<Boolean> mFeatureSecond = new ArrayList<>();
    private ArrayList<Boolean> mFeatureThird = new ArrayList<>();

    private ArrayList<String> mLoadFirst = new ArrayList<>();
    private ArrayList<String> mLoadSecond = new ArrayList<>();
    private ArrayList<String> mLoadThird = new ArrayList<>();
    
    private BusarrivalFragment mContext;

    public BusRecyclerViewAdapter(ArrayList<String> ServiceNo, ArrayList<String> BusStopFirst,
                                  ArrayList<String> BusStopSecond, ArrayList<String> BusStopThird,
                                  ArrayList<String> TypeOfBusFirst, ArrayList<String> TypeOfBusSecond,
                                  ArrayList<String> TypeOfBusThird, ArrayList<Boolean> FeatureFirst,
                                  ArrayList<Boolean> FeatureSecond, ArrayList<Boolean> FeatureThird,
                                  ArrayList<String> LoadFirst, ArrayList<String> LoadSecond,
                                  ArrayList<String> LoadThird,
                                  BusarrivalFragment Context) {
        mServiceNo = ServiceNo;

        mBusStopFirst = BusStopFirst;
        mBusStopSecond = BusStopSecond;
        mBusStopThird = BusStopThird;

        mTypeOfBusFirst = TypeOfBusFirst;
        mTypeOfBusSecond = TypeOfBusSecond;
        mTypeOfBusThird = TypeOfBusThird;

        mFeatureFirst = FeatureFirst;
        mFeatureSecond = FeatureSecond;
        mFeatureThird = FeatureThird;

        mLoadFirst = LoadFirst;
        mLoadSecond = LoadSecond;
        mLoadThird = LoadThird;

        mContext = Context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_buslistitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.serviceNo.setText(mServiceNo.get(position));

        holder.busStopFirst.setText(mBusStopFirst.get(position));
        holder.busStopSecond.setText(mBusStopSecond.get(position));
        holder.busStopThird.setText(mBusStopThird.get(position));

        holder.typeOfBusFirst.setText(mTypeOfBusFirst.get(position));
        holder.typeOfBusSecond.setText(mTypeOfBusSecond.get(position));
        holder.typeOfBusThird.setText(mTypeOfBusThird.get(position));

        // Checking capacity of the bus and change textcolor based on capacity
        checkLoad(mLoadFirst, holder.busStopFirst, position);
        checkLoad(mLoadSecond, holder.busStopSecond, position);
        checkLoad(mLoadThird, holder.busStopThird, position);

        // Checking if bus is wheelchair accessible
        checkFeature(mFeatureFirst, holder.busStopFirst, position);
        checkFeature(mFeatureSecond, holder.busStopSecond, position);
        checkFeature(mFeatureThird, holder.busStopThird, position);
    }

    public void checkFeature(ArrayList<Boolean> feature, TextView holder, int position){
        boolean wc = feature.get(position);
        if(wc == true)
            holder.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_wheelchair_pickup_24, 0, 0, 0);
        else
            holder.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    public void checkLoad(ArrayList<String> load, TextView holder,int position){
        String pos =  load.get(position);
        Log.d("checkLoad: ", pos);
        if (pos.equals("low")) {
            holder.setTextColor(Color.GREEN);
        } else if (pos.equals("mid")) {
            holder.setTextColor(Color.parseColor("#FF8000"));
        } else if (pos.equals("high")) {
            holder.setTextColor(Color.RED);
        }
    }


    @Override
    public int getItemCount() {
        return mServiceNo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView serviceNo;

        TextView busStopFirst;
        TextView busStopSecond;
        TextView busStopThird;

        TextView typeOfBusFirst;
        TextView typeOfBusSecond;
        TextView typeOfBusThird;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNo = itemView.findViewById(R.id.serviceno);

            busStopFirst = itemView.findViewById(R.id.firstbus);
            busStopSecond = itemView.findViewById(R.id.secondbus);
            busStopThird = itemView.findViewById(R.id.thirdbus);

            typeOfBusFirst = itemView.findViewById(R.id.bustype1);
            typeOfBusSecond = itemView.findViewById(R.id.bustype2);
            typeOfBusThird = itemView.findViewById(R.id.bustype3);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

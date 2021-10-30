package com.example.cz2006.ui.meet.bottomsheets;

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

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<com.example.cz2006.ui.meet.bottomsheets.PlaceRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mPlaceName = new ArrayList<>();
    private ArrayList<String> mPlaceAdd = new ArrayList<>();

    private BottomFragment_Place mContext;

    private int pos = -1;

    private int selectedPosition = -1;

    public PlaceRecyclerViewAdapter(ArrayList<String> placeName, ArrayList<String> placeAdd,
                                  BottomFragment_Place Context) {
        mPlaceName = placeName;
        mPlaceAdd = placeAdd;

        mContext = Context;
    }

    @NonNull
    @Override
    public com.example.cz2006.ui.meet.bottomsheets.PlaceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_placelistitem, parent, false);
        com.example.cz2006.ui.meet.bottomsheets.PlaceRecyclerViewAdapter.ViewHolder holder =
                new com.example.cz2006.ui.meet.bottomsheets.PlaceRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.cz2006.ui.meet.bottomsheets.PlaceRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.placeName.setText(mPlaceName.get(position));
        holder.placeAdd.setText(mPlaceAdd.get(position));

        if(selectedPosition == position)
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.quantum_grey200));
        else
            holder.itemView.setBackgroundColor(Color.WHITE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                pos = position;

                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + Integer.toString(mPlaceName.size()));
        return mPlaceName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView placeName;
        TextView placeAdd;

        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            placeAdd = itemView.findViewById(R.id.placeAdd);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public void updateList(ArrayList<String> name, ArrayList<String> addr){
        mPlaceName = name;
        mPlaceAdd = addr;
        notifyDataSetChanged();
    }

    public int getPosition() {
        return this.pos;
    }


}


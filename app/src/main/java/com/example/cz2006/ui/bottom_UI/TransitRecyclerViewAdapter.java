package com.example.cz2006.ui.bottom_UI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cz2006.R;

import java.util.ArrayList;


public class TransitRecyclerViewAdapter extends RecyclerView.Adapter<TransitRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mDirections = new ArrayList<>();
    private ArrayList<String> mDistance = new ArrayList<>();
    private ArrayList<String> mTurn = new ArrayList<>();

    private TransitUI mContext;


    public TransitRecyclerViewAdapter(ArrayList<String> directions, ArrayList<String> distance,
                                      ArrayList<String> turn,
                                      TransitUI Context) {
        mDirections = directions;
        mDistance = distance;
        mTurn = turn;
        mContext = Context;
    }

    @NonNull
    @Override
    public TransitRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_transitlistitem, parent, false);
        TransitRecyclerViewAdapter.ViewHolder holder =
                new TransitRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransitRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.directions.setText(mDirections.get(position));
        holder.distance.setText(mDistance.get(position));

        // TODO: Find better vector graphics for slight and sharp
        if(mTurn.get(position).equals("walk"))
            holder.turn.setImageResource(R.drawable.directions_walk_24);
        else if(mTurn.get(position).equals("bus"))
            holder.turn.setImageResource(R.drawable.ic_baseline_directions_bus_24);
        else if(mTurn.get(position).equals("train"))
            holder.turn.setImageResource(R.drawable.ic_baseline_train_24);
        else if(mTurn.get(position).equals("destination"))
            holder.turn.setImageResource(R.drawable.ic_baseline_celebration_24);
        else
            holder.turn.setImageResource(R.drawable.emptyplaceholder);
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + Integer.toString(mDirections.size()));
        return mDirections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView directions;
        TextView distance;
        ImageView turn;

        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            directions = itemView.findViewById(R.id.directions);
            distance = itemView.findViewById(R.id.distance);
            turn = itemView.findViewById(R.id.turn_image);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


}



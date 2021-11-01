package com.example.cz2006.ui.meet.bottomsheets;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
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


public class StartRecyclerViewAdapter extends RecyclerView.Adapter<com.example.cz2006.ui.meet.bottomsheets.StartRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mStartName = new ArrayList<>();
    private ArrayList<Drawable> mIcon = new ArrayList<>();

    private BottomFragment_Start mContext;

    private int selectedPosition = -1;
    private int pos = -1;

    public StartRecyclerViewAdapter(ArrayList<String> startName, ArrayList<Drawable> icon,
                                    BottomFragment_Start Context) {
        mStartName = startName;
        mIcon = icon;
        mContext = Context;
    }

    @NonNull
    @Override
    public com.example.cz2006.ui.meet.bottomsheets.StartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_startlistitem, parent, false);
        com.example.cz2006.ui.meet.bottomsheets.StartRecyclerViewAdapter.ViewHolder holder =
                new com.example.cz2006.ui.meet.bottomsheets.StartRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.cz2006.ui.meet.bottomsheets.StartRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.startName.setText(mStartName.get(position));
        holder.transit.setImageDrawable(mIcon.get(position));

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
        Log.d(TAG, "getItemCount: " + Integer.toString(mStartName.size()));
        return mStartName.size();
    }

    public int getPosition() {
        return this.pos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView startName;
        ImageView transit;

        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startName = itemView.findViewById(R.id.startName);
            transit = itemView.findViewById(R.id.transport_image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public void updateList(ArrayList<String> name){
        mStartName = name;
        notifyDataSetChanged();
    }


}



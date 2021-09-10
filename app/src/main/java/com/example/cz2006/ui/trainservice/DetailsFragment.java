package com.example.cz2006.ui.trainservice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.cz2006.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_details,
                container, false);

        Details details = new Details();
        String[] Content;
        String[] CreateDate;

        Content = details.getContent();
        CreateDate = details.getCreateDate();

        TableLayout myLayout = (TableLayout) rootView.findViewById(R.id.detailsresulttable);
        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2=new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.WRAP_CONTENT);

        if(Content!=null && CreateDate!=null){
            for (int j = 0; j < Content.length; j++){
                TableRow row = new TableRow(this.getActivity());
                TextView onerow[] = new TextView[6];
                for (int i = 0; i < 6; i++) {
                    onerow[i] = (TextView) new TextView(this.getActivity());
                    switch (i) {
                        case 0:
                            onerow[i].setText(CreateDate[j]);
                            onerow[i].setTextSize(30);
                            row.addView(onerow[i]);
                            row.setPadding(1,10,1,1);
                            row.setLayoutParams(params2);
                            myLayout.addView(row);
                            break;
                        case 1:
                            onerow[i].setText(Content[j]);
                            onerow[i].setTextSize(10);
                            row.addView(onerow[i]);
                            row.setPadding(1,10,1,1);
                            row.setLayoutParams(params2);
                            myLayout.addView(row);
                            break;
                    }


                }

            }
        }else{
            TableRow row = new TableRow(this.getActivity());
            Log.d("Testing else","Linesfragment else");
            TextView allgood = new TextView(this.getActivity());
            allgood.setText("The Lines are all good!");
            allgood.setGravity(Gravity.CENTER);
            allgood.setTextSize(30);
            row.addView(allgood);
            row.setLayoutParams(params2);
            row.setGravity(Gravity.CENTER);
            row.setPadding(0,50,0,0);
            myLayout.addView(row);
        }

        return rootView;
    }
}
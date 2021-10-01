package com.example.cz2006.ui.busarrival;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.cz2006.MainActivity;
import com.example.cz2006.R;
import com.example.cz2006.databinding.FragmentBusarrivalBinding;
import com.google.android.material.textfield.TextInputEditText;

public class BusarrivalFragment extends Fragment {


    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private BusarrivalViewModel busarrivalViewModel;
    private FragmentBusarrivalBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        busarrivalViewModel =
                new ViewModelProvider(this).get(BusarrivalViewModel.class);

        binding = FragmentBusarrivalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textBusarrival;
        busarrivalViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        }
        );*/

        TextInputEditText editText = binding.busstopquery;
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Log.d("onQueryTextSubmit", String.valueOf(editText.getEditableText()));
                    String query = String.valueOf(editText.getEditableText());
                    if (isNumeric(query) && query.length() == 5){
                        BusArrival busArrival = new BusArrival(Integer.parseInt(query));

                        String[] ServiceNo = busArrival.getServiceNo();
                        String[] NextBus = busArrival.getNextBus();
                        String[] NextBus2 = busArrival.getNextBus2();
                        String[] NextBus3 = busArrival.getNextBus3();
                        String[] Feature = busArrival.getFeature();
                        String[] Type = busArrival.getType();
                        String[] Load = busArrival.getLoad();


                        Bundle bundle = new Bundle();
                        bundle.putStringArray("ServiceNo",ServiceNo);
                        bundle.putStringArray("NextBus",NextBus);
                        bundle.putStringArray("NextBus2",NextBus2);
                        bundle.putStringArray("NextBus3",NextBus3);
                        bundle.putStringArray("Feature",Feature);
                        bundle.putStringArray("Type",Type);
                        bundle.putStringArray("Load",Load);

                        BusResultsFragment busResultsFragment = new BusResultsFragment();
                        busResultsFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, busResultsFragment);
                        fragmentTransaction.commit();
                    }else{
                        Toast.makeText(getContext(), "The bus stop code must be 5 digit numeric!", Toast.LENGTH_LONG).show();
                    }

                    return true;
                }
                return false;
            }
        });
        /*editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


            }
        });*/

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);



        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();

        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("onQueryTextSubmit", query);

                    BusArrival busArrival = new BusArrival();

                    int[] ServiceNo = busArrival.getServiceNo();
                    String[] NextBus = busArrival.getNextBus();
                    String[] NextBus2 = busArrival.getNextBus2();
                    String[] NextBus3 = busArrival.getNextBus3();
                    String[] Feature = busArrival.getFeature();
                    String[] Type = busArrival.getType();
                    String[] Load = busArrival.getLoad();

                    Bundle bundle = new Bundle();
                    bundle.putIntArray("ServiceNo",ServiceNo);
                    bundle.putStringArray("NextBus",NextBus);
                    bundle.putStringArray("NextBus2",NextBus2);
                    bundle.putStringArray("NextBus3",NextBus3);
                    bundle.putStringArray("Feature",Feature);
                    bundle.putStringArray("Type",Type);
                    bundle.putStringArray("Load",Load);

                    BusResultsFragment busResultsFragment = new BusResultsFragment();
                    busResultsFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, busResultsFragment);
                    fragmentTransaction.commit();

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }*/

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
}
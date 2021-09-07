package com.example.cz2006.ui.busarrival;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.cz2006.databinding.FragmentBusarrivalBinding;

public class BusarrivalFragment extends Fragment {

    private BusarrivalViewModel busarrivalViewModel;
    private FragmentBusarrivalBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        busarrivalViewModel =
                new ViewModelProvider(this).get(BusarrivalViewModel.class);

        binding = FragmentBusarrivalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textBusarrival;
        busarrivalViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        BusArrival busArrival = new BusArrival();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
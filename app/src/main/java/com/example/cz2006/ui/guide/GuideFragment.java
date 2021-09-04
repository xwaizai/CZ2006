package com.example.cz2006.ui.guide;

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

import com.example.cz2006.databinding.FragmentGuideBinding;

public class GuideFragment extends Fragment {

    private GuideViewModel guideViewModel;

    private FragmentGuideBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        guideViewModel =
                new ViewModelProvider(this).get(GuideViewModel.class);

        binding = FragmentGuideBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGuide;
        guideViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
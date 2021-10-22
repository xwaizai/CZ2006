package com.example.cz2006.ui.covidsitrep;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.cz2006.R;
import com.example.cz2006.databinding.FragmentCovidsitrepBinding;

public class CovidSitRepFragment extends Fragment {

    private CovidSitRepViewModel spaceoutViewModel;
    private FragmentCovidsitrepBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        spaceoutViewModel =
                new ViewModelProvider(this).get(CovidSitRepViewModel.class);

        binding = FragmentCovidsitrepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        String url = "https://covidsitrep.moh.gov.sg/";
        WebView view = (WebView) root.findViewById(R.id.webView);
        view.setWebViewClient( new WebViewClient());
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(url);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
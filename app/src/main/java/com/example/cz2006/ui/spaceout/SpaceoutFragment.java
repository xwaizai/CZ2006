package com.example.cz2006.ui.spaceout;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.cz2006.R;
import com.example.cz2006.databinding.FragmentSpaceoutBinding;

public class SpaceoutFragment extends Fragment {

    private SpaceoutViewModel spaceoutViewModel;
    private FragmentSpaceoutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        spaceoutViewModel =
                new ViewModelProvider(this).get(SpaceoutViewModel.class);

        binding = FragmentSpaceoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        String url = "https://www.google.com";
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
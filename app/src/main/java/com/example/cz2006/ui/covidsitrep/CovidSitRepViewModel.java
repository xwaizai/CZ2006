package com.example.cz2006.ui.covidsitrep;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CovidSitRepViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CovidSitRepViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Space Out fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
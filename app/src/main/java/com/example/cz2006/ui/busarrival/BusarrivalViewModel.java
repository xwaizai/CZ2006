package com.example.cz2006.ui.busarrival;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusarrivalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BusarrivalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Bus Arrival fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
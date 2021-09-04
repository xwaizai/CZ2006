package com.example.cz2006.ui.trainservice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrainViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TrainViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Train Service Alerts fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.cz2006.ui.spaceout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SpaceoutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SpaceoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Space Out fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
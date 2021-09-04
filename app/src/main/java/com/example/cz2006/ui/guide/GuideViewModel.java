package com.example.cz2006.ui.guide;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GuideViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public GuideViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Guide fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
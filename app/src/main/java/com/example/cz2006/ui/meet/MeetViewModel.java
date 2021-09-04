package com.example.cz2006.ui.meet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeetViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MeetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Meet fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
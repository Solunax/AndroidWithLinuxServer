package com.example.ownserver.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> infoList;

    public LiveData<ArrayList<String>> getViewModelList(){
        if(infoList == null)
            infoList = new MutableLiveData<>();

        return infoList;
    }

    public void setInfoList(ArrayList<String> values){
        infoList.setValue(values);
        Log.d("INFO", infoList.getValue().toString());
    }
}

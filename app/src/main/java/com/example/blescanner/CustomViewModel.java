package com.example.blescanner;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomViewModel extends ViewModel {
    private MutableLiveData<List<CustomScanResult>> results = new MutableLiveData<>();
    public MutableLiveData<List<CustomScanResult>> getResults() {
        return results;
    }

    private MutableLiveData<List<CustomScanResult>> callingResults = new MutableLiveData<>();
    public MutableLiveData<List<CustomScanResult>> getCallingResults() {
        return callingResults;
    }

    private Map<String, Integer> resultsCounter = new HashMap<String, Integer>();

    public void updateResults(List<CustomScanResult> results){
        this.results.postValue(results);
    }
    public void updateCallingResults(List<CustomScanResult> results){
        this.callingResults.postValue(results);
    }

    public Map<String, Integer> getResultsCounter() {
        return resultsCounter;
    }

    public void updateResultsCounter(Map<String, Integer> resultsCounter){
        this.resultsCounter = resultsCounter;
    }
    public int getCounter(String serial){
        return resultsCounter.get(serial);
    }

    public static class ModelFactory extends ViewModelProvider.NewInstanceFactory {
        public ModelFactory() {
            super();
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == CustomViewModel.class) {
                return (T) new CustomViewModel();
            }
            return null;
        }
    }
}

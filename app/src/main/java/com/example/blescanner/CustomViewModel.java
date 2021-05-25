package com.example.blescanner;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class CustomViewModel extends ViewModel {

    private MutableLiveData<List<CustomScanResult>> results = new MutableLiveData<>();

    public MutableLiveData<List<CustomScanResult>> getResults() {
        return results;
    }

    public void updateResults(List<CustomScanResult> results){
        this.results.postValue(results);
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

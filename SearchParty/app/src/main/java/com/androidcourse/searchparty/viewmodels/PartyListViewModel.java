package com.androidcourse.searchparty.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.androidcourse.searchparty.data.Party;

import java.util.List;

public class PartyListViewModel extends ViewModel {
    private LiveData<List<Party>> parties;
    public LiveData<List<Party>> getParties() {
        return null;
    }
}

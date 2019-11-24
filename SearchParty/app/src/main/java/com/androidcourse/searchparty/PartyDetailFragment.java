package com.androidcourse.searchparty;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidcourse.searchparty.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartyDetailFragment extends Fragment {


    public PartyDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_party_detail, container, false);
    }

}

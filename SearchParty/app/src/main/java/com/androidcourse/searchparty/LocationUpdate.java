package com.androidcourse.searchparty;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

public interface LocationUpdate {
    void updateMap(Map<String, ArrayList<LatLng>> mapUpdate);

    Activity getActivity();
}

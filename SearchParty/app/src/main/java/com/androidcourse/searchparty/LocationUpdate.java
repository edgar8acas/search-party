package com.androidcourse.searchparty;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.model.LatLng;

public interface LocationUpdate {
    void updateMap(Location location);

    Activity getActivity();
}

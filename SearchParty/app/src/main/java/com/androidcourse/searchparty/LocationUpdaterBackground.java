package com.androidcourse.searchparty;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

public class LocationUpdaterBackground extends AsyncTask<Long, Void, Location> {

    WeakReference<LocationUpdate> listener;
    Activity activity;

    public LocationUpdaterBackground(LocationUpdate delegate){
        this.listener = new WeakReference(delegate);
        activity = listener.get().getActivity();
    }

    @Override
    protected Location doInBackground(Long... longs) {
        /**/
        return null;
    }

    @Override
    protected void onPostExecute(Location l){
        listener.get().updateMap(l);
    }
}

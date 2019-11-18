package com.androidcourse.searchparty;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocationUpdaterBackground extends AsyncTask<Long, Void, Location> implements OnSuccessListener<Location>{

    private WeakReference<LocationUpdate> listener;
    private Activity activity;
    private FusedLocationProviderClient fusedLocationClient;
    private Location location;
    private Lock lock;
    private Condition locationCondition;

    protected LocationUpdaterBackground(LocationUpdate delegate){
        this.listener = new WeakReference<>(delegate);
        activity = listener.get().getActivity();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(listener.get().getActivity());
        lock = new ReentrantLock();
        locationCondition = lock.newCondition();
    }

    @Override
    protected Location doInBackground(Long... longs) {
        lock.lock();

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, this)
                    .addOnFailureListener(activity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
            while (location == null) {
                locationCondition.await();
            }
            return location;
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Location l){
        listener.get().updateMap(l);
    }

    @Override
    public void onSuccess(Location location) {
        lock.lock();
        try {
            this.location = location;
            locationCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

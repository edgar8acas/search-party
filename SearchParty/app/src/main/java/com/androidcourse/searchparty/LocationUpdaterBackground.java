package com.androidcourse.searchparty;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.androidcourse.searchparty.utils.LocationUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocationUpdaterBackground extends AsyncTask<DocumentReference, Void, Location> implements OnSuccessListener<Location>{

    private WeakReference<LocationUpdate> listener;
    private Activity activity;
    private FusedLocationProviderClient fusedLocationClient;
    private Location location;
    private Lock lock;
    private Condition locationCondition;
    private LocationUtil util;
    private LocationUtil.Location loc;

    protected LocationUpdaterBackground(LocationUpdate delegate){
        this.listener = new WeakReference<>(delegate);
        activity = listener.get().getActivity();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(listener.get().getActivity());
        lock = new ReentrantLock();
        locationCondition = lock.newCondition();
        util = new LocationUtil();
    }

    @Override
    protected Location doInBackground(DocumentReference... docs) {
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
            FirebaseFirestore ff = FirebaseFirestore.getInstance();
            Map<String, Object> doc = new HashMap<>();
            loc = util.generateNewLocation();
            doc.put(new Timestamp(new Date()).toString(),new LatLng(loc.getLat(), loc.getLong()));
            ff.collection("parties")
                    .document(docs[0].getId())
                    .collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .update(doc);
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
        //listener.get().updateMap(l);
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

package com.androidcourse.searchparty;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, LocationUpdate {

    private GoogleMap mMap;
    private Map<String, Polyline> routes;
    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 8;
    private DocumentReference searchParty;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        String ref = i.getStringExtra(PartyDetailActivity.PARTY_ID);
        searchParty = ff.collection("parties").document(ref);
        DocumentReference refUser = ff.collection("parties")
                .document(searchParty.getId())
                .collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot d = task.getResult();
                    if(!d.exists()){
                        HashMap<String, Object> initData = new HashMap<>();
                        initData.put("createdAt", new Timestamp(new Date()));
                        ff.collection("parties")
                                .document(searchParty.getId())
                                .collection("users")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(initData);
                    }
                    else{
                        Log.d("Doc exists with id: ", d.getId());
                    }
                }
            }
        });
        Log.d("Search Party Ref", searchParty.getId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the currentUser will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the currentUser has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ff.collection("parties")
                .document(searchParty.getId())
                .collection("users").addSnapshotListener(MetadataChanges.INCLUDE,new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.d("listen:error", e.toString());
                            return;
                        }
                        for(DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                            switch (dc.getType()){
                                case ADDED:
                                    Log.d("New location added: ", dc.getDocument().getData().toString());
                                    updateMap(prepareMapUpdate(dc));
                                    break;
                                case MODIFIED:
                                    Log.d("New location Modified: ", dc.getDocument().getData().toString());
                                    updateMap(prepareMapUpdate(dc));
                                    break;
                                case REMOVED:
                                    Log.d("New location Removed: ", dc.getDocument().getData().toString());
                                    break;
                            }
                        }

                    }
                });
        final Handler handler = new Handler();
        //Check if there's permission to Access Fine Location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //If permission is not granted, request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Log.d("PERMISSION=>", "GRANTED");
            handler.post(new Runnable(){
                @Override
                public void run() {
                    LocationUpdaterBackground task = getTask();
                    task.execute(searchParty);
                    handler.postDelayed(this, 30000);
                }
            });
        }
    }

    public LocationUpdaterBackground getTask(){
        return new LocationUpdaterBackground(this);
    }

    public void updateMap(Map<String, ArrayList<LatLng>> mapUpdate){
        if(routes == null){routes = new HashMap<>();}
        for(String user : mapUpdate.keySet()){
            ArrayList<LatLng> locations = mapUpdate.get(user);
            for(LatLng location : locations){
                Log.d("LAT=>", ""+location.latitude);
                Log.d("LON=>", ""+location.longitude);
                if(user == FirebaseAuth.getInstance().getCurrentUser().getUid()){
                    if(routes.get(user) == null){
                        routes.put(user,mMap.addPolyline(new PolylineOptions()
                                .clickable(false)
                                .add(location)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    }
                    else{
                        List<LatLng> points = routes.get(user).getPoints();
                        points.add(location);
                        routes.get(user).setPoints(points);
                    }
                }
                else{
                    if(routes.get(user) == null){
                        routes.put(user,mMap.addPolyline(new PolylineOptions()
                                .clickable(false)
                                .add(location)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    }
                    else{
                        List<LatLng> points = routes.get(user).getPoints();
                        points.add(location);
                        routes.get(user).setPoints(points);
                    }
                }
            }
        }
    }

    public Activity getActivity(){
        return this;
    }

    public HashMap<String, ArrayList<LatLng>> prepareMapUpdate(DocumentChange dc){
        Map<String, Object> serverData = dc.getDocument().getData();
        ArrayList<String> keys = new ArrayList(serverData.keySet());
        Collections.sort(keys, Collections.reverseOrder());
        ArrayList<LatLng> userLocations = new ArrayList<>();
        for(String key : keys){
            try{
                Map<String, Object> latLngMap =  (HashMap) serverData.get(key);
                LatLng latLng = new LatLng((double)latLngMap.get("latitude"), (double)latLngMap.get("longitude"));
                userLocations.add(latLng);
            }
            catch(ClassCastException er){
                Log.d("Timestamp reached", er.toString());
            }
        }
        HashMap<String, ArrayList<LatLng>> mapUpdate = new HashMap<>();
        mapUpdate.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),userLocations);
        return mapUpdate;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }
    }
}

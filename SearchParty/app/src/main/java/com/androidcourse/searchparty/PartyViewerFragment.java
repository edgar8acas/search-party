package com.androidcourse.searchparty;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.ListFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class PartyViewerFragment extends ListFragment {

    private ArrayList<String> users;
    private ArrayAdapter<String> listAdapter;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();

    public PartyViewerFragment() {
        users = new ArrayList<>();
    }


    public static PartyViewerFragment newInstance() {
        PartyViewerFragment fragment = new PartyViewerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listAdapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, users);
        setListAdapter(listAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void updateList(String user){
        ff.collection("users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                users.add((String)documentSnapshot.get("name"));
                listAdapter.notifyDataSetChanged();
            }
        });
    }


}

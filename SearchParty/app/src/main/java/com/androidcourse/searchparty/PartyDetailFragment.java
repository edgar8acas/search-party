package com.androidcourse.searchparty;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidcourse.searchparty.adapters.UsersAdapter;
import com.androidcourse.searchparty.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartyDetailFragment extends Fragment {

    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<String> memberUsersList;
    private UsersAdapter usersAdapter;
    public PartyDetailFragment() {
        // Required empty public constructor
    }

    public void setMemberUsersList(List<String> memberUsersList) {
        this.memberUsersList = memberUsersList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_party_detail, container, false);

        firestore = FirebaseFirestore.getInstance();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.users_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        usersAdapter = new UsersAdapter();
        recyclerView.setAdapter(usersAdapter);
        return view;
    }

    public void addUser(User user) {
        usersAdapter.addUser(user);
        usersAdapter.notifyDataSetChanged();
    }
}

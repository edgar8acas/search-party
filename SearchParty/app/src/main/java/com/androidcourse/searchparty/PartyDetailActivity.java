package com.androidcourse.searchparty;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.androidcourse.searchparty.data.Party;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PartyDetailActivity extends AppCompatActivity {

    public static final String PARTY_ID = "party_id";
    private String partyId;
    private FirebaseFirestore firestore;
    private Party currentParty;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_detail);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        partyId = getIntent().getStringExtra(PARTY_ID);
        initialize();

    }

    private void initialize() {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("parties").document(partyId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentParty = documentSnapshot.toObject(Party.class);
                toolbar.setTitle(currentParty.getName());
                toolbar.setSubtitle("Code: " + currentParty.getInvitationCode());
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                PartyDetailFragment detailFragment = new PartyDetailFragment();
                detailFragment.setMemberUsersList(currentParty.getMemberUsers());
                transaction.add(R.id.frag_detail_container, detailFragment);
                transaction.commit();
            }
        });

    }

    public void onClickStartMap(View view) {
    }
}

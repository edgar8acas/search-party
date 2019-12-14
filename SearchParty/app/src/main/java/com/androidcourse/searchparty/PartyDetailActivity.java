package com.androidcourse.searchparty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.androidcourse.searchparty.data.Party;
import com.androidcourse.searchparty.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PartyDetailActivity extends AppCompatActivity {

    public static final String PARTY_ID = "party_id";
    private String partyId;
    private FirebaseFirestore firestore;
    private Party currentParty;
    private Toolbar toolbar;
    private TextView codeView;
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
        codeView = findViewById(R.id.party_code);
        initialize();

    }

    private void initialize() {
        firestore = FirebaseFirestore.getInstance();
        final PartyDetailFragment detailFragment = new PartyDetailFragment();
        firestore.collection("parties").document(partyId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentParty = documentSnapshot.toObject(Party.class);
                toolbar.setTitle(currentParty.getName());
                codeView.setText("Code: " + currentParty.getInvitationCode());
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                for (String uid:
                        currentParty.getMemberUsers()) {
                    DocumentReference docRef = firestore.collection("users").document(uid);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                user.setColor(-1); // No color
                                detailFragment.addUser(user);
                            }
                        }
                    });
                }
                transaction.add(R.id.frag_detail_container, detailFragment);
                transaction.commit();
            }
        });

    }

    private void updateUsersInfo(List<String> memberUsersList) {

    }
    public void onClickStartMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(PARTY_ID, currentParty.getId());
        startActivity(intent);

    }
}

package com.androidcourse.searchparty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidcourse.searchparty.adapters.PartyItemHolder;
import com.androidcourse.searchparty.data.Party;
import com.androidcourse.searchparty.utils.PartyUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PartyListActivity extends AppCompatActivity
                                implements AddPartyDialogFragment.AddPartyDialogListener, JoinPartyDialogFragment.Listener {

    private static final String TAG = "PartyListActivity";
    private FirebaseFirestore firestore;
    FirebaseUser currentUser;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);
        fragmentManager = getSupportFragmentManager();

        initialize();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(currentUser != null) {
            TextView nameView = (TextView)findViewById(R.id.user_info);
            nameView.setText("Current currentUser: " + currentUser.getEmail() + "\n" + currentUser.getUid() + "\n" + currentUser.getDisplayName());
        }
    }

    private void initialize() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setHost("10.0.2.2:7070")
                .setSslEnabled(false)
                .setPersistenceEnabled(false)
                .build();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        //firestore.setFirestoreSettings(settings);
        recyclerView = findViewById(R.id.parties_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(getRecyclerAdapter());
    }

    private void onSignOut() {
        auth.signOut();
        startActivity(new Intent(this, AuthActivity.class));
    }

    private void onJoinParty() {
        JoinPartyDialogFragment dialog = new JoinPartyDialogFragment();
        dialog.show(fragmentManager, "Join party");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                onSignOut();
                return true;
            case R.id.menu_join_party:
                onJoinParty();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private FirestoreRecyclerAdapter getRecyclerAdapter() {
        Query query = firestore
                .collection("parties")
                .whereArrayContains("memberUsers", currentUser.getUid())
                .orderBy("createdAt");
        FirestoreRecyclerOptions<Party> options = new FirestoreRecyclerOptions.Builder<Party>()
                .setQuery(query, Party.class)
                .setLifecycleOwner(this)
                .build();
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Party, PartyItemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PartyItemHolder holder, int position, @NonNull final Party model) {
                holder.nameView.setText(model.getName());
                //TODO: Format creation date and get members of party count;
                holder.creationView.setText(model.getCreatedAt().toString());
                holder.membersView.setText("" + model.getMemberUsers().size());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), PartyDetailActivity.class);
                        intent.putExtra(PartyDetailActivity.PARTY_ID, model.getId());
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public PartyItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_party, parent, false);
                return new PartyItemHolder(view);
            }
        };
        return adapter;
    }

    public void onClickAddParty(View view) {
        AddPartyDialogFragment dialog = new AddPartyDialogFragment();
        dialog.show(fragmentManager, "Add party");
    }

    @Override
    public void onDialogPositiveClick(String name, String code) {
        Party party = new Party(name, code, currentUser.getUid());
        CollectionReference partiesCollection = firestore.collection("parties");
        partiesCollection.add(party);
    }

    @Override
    public void onJoinDialogPositiveClick(final String code) {
        firestore
            .collection("parties")
            .whereEqualTo("invitationCode", code)
            .limit(1)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference()
                                    .update("memberUsers", FieldValue.arrayUnion(currentUser.getUid()));
                        }
                    } else {
                        Log.e(TAG, task.getException().toString());
                    }
                }
            });

    }
}

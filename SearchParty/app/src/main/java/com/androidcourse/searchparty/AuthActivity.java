package com.androidcourse.searchparty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity
                            implements AuthFragment.Listener, AuthNameFragment.Listener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private FragmentManager fragmentManager;

    public static final String TAG = "AuthActivity ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        AuthFragment authFragment = new AuthFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.auth_forms_container, authFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        initialize();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if currentUser is signed in (non-null) and update UI accordingly
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            DocumentReference doc = firestore.collection("users").document(user.getUid());
            doc.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                Intent intent = new Intent(AuthActivity.this, PartyListActivity.class);
                                startActivity(intent);
                            } else {
                                AuthNameFragment authNameFragment = new AuthNameFragment();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.auth_forms_container, authNameFragment);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }
                    }
                });
        }
    }

    private void initialize() {
        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onActionSignIn(String email, String password) {
        signIn(email, password);
    }

    @Override
    public void onActionSignUp(String email, String password) {
        createAccount(email, password);
    }

    @Override
    public void onActionContinue(String name) {
        currentUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        addUserToFirestore(user);
                        updateUI(user);
                    }
                });

    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(AuthActivity.class.toString() + "createAccount()", task.getException().toString());
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signIn() --> " + user.getUid());
                            updateUI(user);
                        } else {
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "signIn() --> " + task.getException().toString());
                            updateUI(null);
                        }
                    }
                });
    }

    private void addUserToFirestore(FirebaseUser fUser) {
        DocumentReference doc = firestore.collection("users").document(fUser.getUid());
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", fUser.getEmail());
        userData.put("name", fUser.getDisplayName());
        doc.set(userData);
    }


}

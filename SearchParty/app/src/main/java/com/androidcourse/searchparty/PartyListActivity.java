package com.androidcourse.searchparty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;



public class PartyListActivity extends AppCompatActivity implements PartyCreator.PartyCreatorListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            TextView nameView = (TextView)findViewById(R.id.user_info);
            nameView.setText("Current user: " + user.getEmail() + "\n" + user.getUid());
        }

    }

    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onGoToMap(View view) {
        PartyCreator newParty = new PartyCreator(this);
        newParty.execute(ff);
    }

    public void joinParty(View view){
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("REF", "t3SmCxTEfT9zwqXDrhNa");
        startActivity(i);
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
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void startParty(String s) {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("REF", s);
        startActivity(i);
    }
}

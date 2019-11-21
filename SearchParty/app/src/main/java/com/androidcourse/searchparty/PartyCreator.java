package com.androidcourse.searchparty;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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
import java.util.concurrent.ExecutionException;

public class PartyCreator extends AsyncTask<FirebaseFirestore, Void, String> {

    WeakReference<PartyCreatorListener> delegate;

    public PartyCreator(PartyCreatorListener listener){
        delegate = new WeakReference<>(listener);
    }

    @Override
    protected String doInBackground(FirebaseFirestore... ff) {
        try{
            Map<String, Object> party = new HashMap<>();
            party.put("created at", new Timestamp(new Date()));
            Task<DocumentReference> searchParty = ff[0].collection("parties").add(party);
            DocumentReference searchPartyRef = Tasks.await(searchParty);
            return searchPartyRef.getId();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        catch(ExecutionException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String ref){
        delegate.get().startParty(ref);
    }

    interface PartyCreatorListener{
        void startParty(String s);
    }
}

package com.androidcourse.searchparty.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcourse.searchparty.R;
import com.androidcourse.searchparty.data.Party;

import java.util.ArrayList;

public class PartyAdapter extends RecyclerView.Adapter<PartyItemHolder> {
    private ArrayList<Party> parties;
    public static final String TAG = "PartyAdapter ";
    public interface PartyAdapterListener {
        void onClick(int position);
    }

    public PartyAdapter(ArrayList<Party> parties) {
        this.parties = parties;
    }

    @NonNull
    @Override
    public PartyItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PartyItemHolder(inflater.inflate(R.layout.item_party, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PartyItemHolder holder, int position) {
        final Party partyAtPosition = parties.get(position);
        holder.nameView.setText(partyAtPosition.getName());
        //TODO: Format creation date and get members of party count;
        holder.creationView.setText(partyAtPosition.getCreatedAt().toString());
        holder.membersView.setText("members");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(TAG, partyAtPosition.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return parties.size();
    }
}

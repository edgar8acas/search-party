package com.androidcourse.searchparty.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androidcourse.searchparty.R;

public class PartyItemHolder extends RecyclerView.ViewHolder {

    public TextView nameView;
    public TextView membersView;
    public TextView creationView;

    public PartyItemHolder(View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.party_item_name);
        membersView = itemView.findViewById(R.id.party_item_members);
        creationView = itemView.findViewById(R.id.party_item_creation_date);
    }
}


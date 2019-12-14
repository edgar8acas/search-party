package com.androidcourse.searchparty.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androidcourse.searchparty.R;

public class UserItemHolder extends RecyclerView.ViewHolder {
    public TextView nameView;

    public UserItemHolder(View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.user_item_name);
    }
}

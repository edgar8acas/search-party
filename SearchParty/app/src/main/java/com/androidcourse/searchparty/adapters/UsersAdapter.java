package com.androidcourse.searchparty.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcourse.searchparty.R;
import com.androidcourse.searchparty.data.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserItemHolder> {

    private List<User> members;

    public UsersAdapter() {
        this.members = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemHolder holder, int position) {
        User user = members.get(position);
        holder.nameView.setText(user.getName());
        if(user.getColor() != -1) {
            holder.nameView.setTextColor(user.getColor());
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void addUser(User user) {
        members.add(user);
    }
}

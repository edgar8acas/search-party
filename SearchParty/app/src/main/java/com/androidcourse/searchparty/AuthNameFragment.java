package com.androidcourse.searchparty;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthNameFragment extends Fragment
                                implements View.OnClickListener {

    private Listener listener;
    private EditText nameView;
    private Button continueView;
    public interface Listener {
        void onActionContinue(String name);
    }

    public AuthNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_name, container, false);
        nameView = view.findViewById(R.id.field_name);
        continueView = view.findViewById(R.id.action_continue);
        continueView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(listener == null) {
            listener = (Listener) context;
        }
    }

    @Override
    public void onClick(View view) {
        String name = nameView.getText().toString();
        if(view.getId() == R.id.action_continue) {
            listener.onActionContinue(name);
        }
    }
}

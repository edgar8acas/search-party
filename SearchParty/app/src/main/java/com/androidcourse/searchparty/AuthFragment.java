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
public class AuthFragment extends Fragment
                            implements View.OnClickListener {
    private Listener listener;
    private Button signInView;
    private Button signUpView;
    private EditText emailView;
    private EditText passwordView;
    public interface Listener {
        void onActionSignIn(String email, String password);
        void onActionSignUp(String email, String password);
    }

    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        signInView = view.findViewById(R.id.action_signin);
        signUpView = view.findViewById(R.id.action_signup);
        emailView = view.findViewById(R.id.field_email);
        passwordView = view.findViewById(R.id.field_password);
        signInView.setOnClickListener(this);
        signUpView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(listener == null) {
            this.listener = (Listener) context;
        }
    }

    @Override
    public void onClick(View view) {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        //TODO: Validate fields
        switch (view.getId()) {
            case R.id.action_signin:
                listener.onActionSignIn(email, password);
                break;
            case R.id.action_signup:
                listener.onActionSignUp(email, password);
                break;

        }
    }
}

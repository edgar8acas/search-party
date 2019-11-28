package com.androidcourse.searchparty;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddPartyDialogFragment extends DialogFragment {

    public interface AddPartyDialogListener {
        public void onDialogPositiveClick(String name, String code);
    }

    AddPartyDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.create_party, null);
        builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = ((EditText) view.findViewById(R.id.name)).getText().toString();
                        String code = ((EditText) view.findViewById(R.id.invitation_code)).getText().toString();
                        listener.onDialogPositiveClick(name, code);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(listener == null) {
            listener = (AddPartyDialogListener) context;
        }
    }
}

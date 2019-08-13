package com.aviharez.tomatapp.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aviharez.tomatapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private LinearLayout bt_change_pass;
    //private Dialog dialog;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        //dialog = new Dialog(getActivity());
        //dialog.setContentView(R.layout.dialog_loading);

        bt_change_pass = v.findViewById(R.id.bt_change_pass);
        bt_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        return v;
    }

    private void showDialog(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);

        //View view  = getActivity().getLayoutInflater().inflate(R.layout.dialog_loading, null);
        dialog.setContentView(R.layout.dialog_change_pass);
        ImageButton bt_close = dialog.findViewById(R.id.bt_close);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    };

}

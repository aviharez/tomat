package com.aviharez.tomatapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aviharez.tomatapp.R;
import com.aviharez.tomatapp.activity.DetailTomatActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TomatFragment extends Fragment {

    private Button bt_search;


    public TomatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tomat, container, false);

        bt_search = v.findViewById(R.id.bt_search_tomat);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DetailTomatActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

}

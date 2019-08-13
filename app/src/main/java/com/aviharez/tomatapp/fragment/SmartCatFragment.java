package com.aviharez.tomatapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aviharez.tomatapp.R;
import com.aviharez.tomatapp.activity.DetailSmartActivity;
import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmartCatFragment extends Fragment {

    private NiceSpinner nice_spinner1, nice_spinner2, nice_spinner3, nice_spinner4, nice_spinner5;
    private Button bt_search;


    public SmartCatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_smart_cat, container, false);

        nice_spinner1 = v.findViewById(R.id.nice_spinner1);
        nice_spinner2 = v.findViewById(R.id.nice_spinner2);
        nice_spinner3 = v.findViewById(R.id.nice_spinner3);
        nice_spinner4 = v.findViewById(R.id.nice_spinner4);
        nice_spinner5 = v.findViewById(R.id.nice_spinner5);

        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));

        nice_spinner1.attachDataSource(dataset);
        nice_spinner2.attachDataSource(dataset);
        nice_spinner3.attachDataSource(dataset);
        nice_spinner4.attachDataSource(dataset);
        nice_spinner5.attachDataSource(dataset);

        bt_search = v.findViewById(R.id.bt_search_smart);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DetailSmartActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

}

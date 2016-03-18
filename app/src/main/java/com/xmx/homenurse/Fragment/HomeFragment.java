package com.xmx.homenurse.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xmx.homenurse.Measure.BloodPressureActivity;
import com.xmx.homenurse.Measure.TemperatureActivity;
import com.xmx.homenurse.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RelativeLayout temperature = (RelativeLayout) view.findViewById(R.id.layout_temperature);
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TemperatureActivity.class);
            }
        });

        RelativeLayout bloodPressure = (RelativeLayout) view.findViewById(R.id.layout_blood_pressure);
        bloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BloodPressureActivity.class);
            }
        });

        return view;
    }

}

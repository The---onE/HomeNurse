package com.xmx.homenurse.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xmx.homenurse.Data.DataManager;
import com.xmx.homenurse.R;
import com.xmx.homenurse.User.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        Button logout = (Button) view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().logout();
                startActivity(LoginActivity.class);
                getActivity().finish();
            }
        });

        return view;
    }

}

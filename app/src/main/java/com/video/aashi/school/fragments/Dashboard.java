package com.video.aashi.school.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.video.aashi.school.Myclass;
import com.video.aashi.school.Performance;
import com.video.aashi.school.R;
import com.video.aashi.school.fragments.payments.Invoice;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment implements View.OnClickListener{

    TextView student_name;
    String username;
    CardView performance,notification,invoice,settings;
    SharedPreferences sharedPreferences;

    String pops;
    public Dashboard()
    {
    }
    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_dashboard, container, false);
        sharedPreferences = getActivity().getSharedPreferences("Login",MODE_PRIVATE);

      //  editor = sharedPreferences.edit();
        student_name=(TextView)view.findViewById(R.id.studentname);
        username = sharedPreferences.getString("S_name","");
        student_name.setText(username);
        settings =(CardView)view.findViewById(R.id.settings) ;
        notification =(CardView)view.findViewById(R.id.notification);
        performance =(CardView)view.findViewById(R.id.performance);
        invoice=(CardView)view.findViewById(R.id.invoice);
        performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),Performance.class);
                startActivity(i);
            }
        });

        notification.setOnClickListener(this);
        invoice.setOnClickListener(this);
        settings.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.notification:
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.container,new NoticeBoard()).addToBackStack(null).commit();
                break;
            case R.id.invoice:
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.container,new Invoice()).addToBackStack(null).commit();
                break;
            case R.id.settings:
                startActivity(new Intent(getActivity(), Myclass.class));
                break;
        }
    }
}

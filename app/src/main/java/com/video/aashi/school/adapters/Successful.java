package com.video.aashi.school.adapters;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toolbar;

import com.video.aashi.school.R;

import java.util.Objects;

public class Successful extends AppCompatActivity {



    android.support.v7.widget.Toolbar toolbar;

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful);
        toolbar =(android.support.v7.widget.Toolbar)findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(R.color.white);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share,menu);
        return  true;
    }
}

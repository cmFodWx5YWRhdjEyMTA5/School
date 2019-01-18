package com.video.aashi.school.pinchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.video.aashi.school.Login;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.PinLogin;
import com.video.aashi.school.R;
import com.video.aashi.school.fragments.HomePage;

public class ChangePin extends AppCompatActivity {

    EditText changePin,phoneNumber;
    CardView change;
    Boolean silent;
    String pin,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        changePin =(EditText)findViewById(R.id.enterPin);
        phoneNumber =(EditText)findViewById(R.id.enterPhone);
        change =(CardView)findViewById(R.id.changePins);

        SharedPreferences settings = getSharedPreferences("com.example.xyz", 0);
        silent   = settings.getBoolean("switchkey", true);
     //   pin = changePin.getText().toString();


        number = phoneNumber.getText().toString();

        if (silent)
        {
           changePin.setText(Navigation.parentPin);
           changePin.setEnabled(false);
        }
        else
        {
            changePin.setEnabled(true);
        }

         change.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 submit();
             }
         });


    }
    public  void  submit()
    {
        if (!changePin.getText().toString().equals(Navigation.parentPin))
        {
            changePin.setError("Pin is invalid");
            changePin.requestFocus();
        }
        else if (changePin.getText().toString().isEmpty()) {
            changePin.setError("Please enter pin");
            changePin.requestFocus();

        }
        else if (phoneNumber.getText().toString().isEmpty()) {
            phoneNumber.setError("Please enter number");
            phoneNumber.requestFocus();
        }
        else if (!phoneNumber.getText().toString().equals(Navigation.parentMob))
        {

            phoneNumber.setError("Invalid number");
            phoneNumber.requestFocus();

        }
        else
        {
            requestForm();
        }

    }

    public  void  requestForm()
    {

         getSupportFragmentManager().beginTransaction().replace(R.id.mylayouts,new ChangePins()).addToBackStack(null).commit();
    }
    public boolean Validate()
    {

        return  true;
    }
}

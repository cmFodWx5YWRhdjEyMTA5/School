package com.video.aashi.school.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.Login;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.Interfaces.SwitchStatus;
import com.video.aashi.school.pinchange.ChangePin;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment
{

    Switch authentication;
    SwitchStatus switchStatus;
    SharedPreferences sharedPreferences;
    boolean status;
    SharedPreferences.Editor editor;
    boolean silent;
    CardView savepin;
    RelativeLayout changePin;
    android.support.v7.widget.Toolbar toolbar;
    SharedPreferences settings;
    TextView getText,yourPin;
    String  myPin,mytext;
    CheckBox cbRememberMe;
    boolean rememberMe;
   boolean checkPreference;
    SharedPreferences.Editor editorss;
    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPreferences = getActivity(). getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
        authentication =(Switch)view.findViewById(R.id.autheentication);
        // SharedPreferences sharedPrefs = getActivity(). getSharedPreferences("com.example.xyle", MODE_PRIVATE);
        savepin =(CardView)view.findViewById(R.id.pinConfig);
        getText =(TextView)view.findViewById(R.id.getText);
        cbRememberMe =(CheckBox)view.findViewById(R.id.checkSave);
        yourPin =(TextView)view.findViewById(R.id.yourPin);
        changePin =(RelativeLayout)view.findViewById(R.id.changePin);
        settings = getActivity(). getSharedPreferences("com.example.xyz", 0);
        silent = settings.getBoolean("switchkey", true);
        myPin = settings.getString("myPin","");
        yourPin.setText(myPin);
        editorss =  getActivity().getPreferences(0).edit();;
        if (silent)
        {
            yourPin.setVisibility(View.VISIBLE);
        }
        else
        {
            yourPin.setVisibility(View.GONE);
        }
        SharedPreferences prefs = getActivity().getPreferences(0);
        rememberMe = prefs.getBoolean("check", true);
        if (rememberMe) {
            cbRememberMe.setChecked(true);
        }
        SharedPreferences changetert = getActivity(). getSharedPreferences("text", 0);
        mytext =changetert.getString("mytext","");
        authentication.setChecked(silent);
        switchStatus =new SwitchStatus();
        getText.setText(mytext);
        toolbar=(android.support.v7.widget.Toolbar)getActivity(). findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");

       changePin.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {


       startActivity(new Intent(getActivity(),ChangePin.class));

        }
      });

       cbRememberMe.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (getText.getText().toString().contains("Save Pin"))
               {
                   showDialogue(getActivity(),"Are you sure you want to save your pin?","Yes","No");
               }
               else if (getText.getText().toString().contains("Remove Pin"))
               {
                   showDialogues(getActivity(),"Are you sure you want to remove your pin?","Yes","No");

               }
           }
       });

        savepin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (getText.getText().toString().contains("Save Pin"))
                {
                    showDialogue(getActivity(),"Are you sure you want to save your pin?","Yes","No");
                }
                else if (getText.getText().toString().contains("Remove Pin"))
                {
                    showDialogues(getActivity(),"Are you sure you want to remove your pin?","Yes","No");

                }
             }
        });
        return  view;
    }
    private  void showDialogue(Context context,String title,String validate,String validates)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setPositiveButton(validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getActivity().getSharedPreferences("com.example.xyz", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("switchkey", true);
                        editor.putString("mytext","");
                        editor.putString("myPin",myPin);
                        yourPin.setVisibility(View.VISIBLE);
                        editor.apply();
                        SharedPreferences changetert =getActivity(). getSharedPreferences("text", 0);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editors = changetert.edit();
                        editors.putString("mytext","Remove Pin");
                        editors.apply();
                        yourPin.setText(myPin);
                        getText.setText("Remove Pin");
                        cbRememberMe.setChecked(true);
                        if(cbRememberMe.isChecked()){
                            checkPreference = true;
                            editorss.putBoolean("check", checkPreference);
                            editorss.commit();
                            Log.i("Remember Me", "checked,preference added");
                        }
                        else{
                            checkPreference = false;
                            editorss.putBoolean("check", checkPreference);
                            editorss.commit();
                            Log.i("Remember Me", "Unchecked, preferences removed");
                        }
                    }
                })
                .setNegativeButton(validates, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cbRememberMe.setChecked(false);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private  void showDialogues(Context context,String title,String validate,String validates)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setPositiveButton(validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SharedPreferences settings = getActivity().getSharedPreferences("com.example.xyz", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("switchkey", false);
                      //  editor.putString("myPin","");
                        editor.putString("mytext","");
                        editor.apply();
                        yourPin.setText("");
                        getText.setText("Save Pin");
                        cbRememberMe.setChecked(false);
                        if(cbRememberMe.isChecked()){
                            checkPreference = true;
                            editorss.putBoolean("check", checkPreference);
                            editorss.commit();
                            Log.i("Remember Me", "checked,preference added");
                        }
                        else{
                            checkPreference = false;
                            editorss.putBoolean("check", checkPreference);
                            editorss.commit();
                            Log.i("Remember Me", "Unchecked, preferences removed");
                        }
                        SharedPreferences changetert =getActivity(). getSharedPreferences("text", 0);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editors = changetert.edit();
                        editors.putString("mytext","Save Pin");
                        editors.apply();
                        Intent intent = new Intent(getActivity(),Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                    }
                })
                .setNegativeButton(validates, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cbRememberMe.setChecked(true);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

package com.video.aashi.school.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import static android.content.Context.MODE_PRIVATE;

public class Expired extends AlertDialog.Builder {

    SharedPreferences sharedPreferences,myshared ;
    SharedPreferences.Editor editor,editors;
    Context context;
    String message;

    public Expired(@NonNull Context context, String message) {

        super(context);
        this.context = context;
        this.message= message;
    }
    public SharedPreferences getSharedPreferences()
    {
        sharedPreferences = context. getSharedPreferences("MySession",MODE_PRIVATE);
        myshared = context.getSharedPreferences("loginstatus",MODE_PRIVATE);
        editors = myshared.edit();
        editor = sharedPreferences.edit();
        editor.putBoolean("isLogins",true);
        editors.apply();
        editor.apply();
        return sharedPreferences ;
    }

    @Override
    public AlertDialog.Builder setTitle(@Nullable CharSequence title) {
        return super.setTitle(title);
    }

    @Override
    public AlertDialog show() {
        return super.show();
    }
    @Override
    public AlertDialog.Builder setCancelable(boolean cancelable) {
        return super.setCancelable(cancelable);
    }
    @Override
    public AlertDialog.Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
        return super.setPositiveButton(textId, listener);
            }
}

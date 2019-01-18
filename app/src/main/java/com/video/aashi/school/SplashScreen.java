package com.video.aashi.school;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.video.aashi.school.adapters.Interfaces.Authentication;
import com.video.aashi.school.adapters.Interfaces.SwitchStatus;
import com.video.aashi.school.adapters.post_class.PinVal;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity implements Authentication {
    int pStatus = 0;
    private Handler handler = new Handler();
    TextView tv;
    boolean send = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Resources res = getResources();

        Drawable drawable = res.getDrawable(R.drawable.circuler);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);
        tv = (TextView) findViewById(R.id.tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus < 100) {
                    pStatus += 1;

                    handler.post(() -> {
                        // TODO Auto-generated method stub
                        mProgress.setProgress(pStatus);
                        tv.setText(pStatus + "%");
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(16); //thread will take approx 3 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                if (pStatus==100)
                {


                        Intent i = new Intent(SplashScreen.this, PinLogin.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SplashScreen.this.finish();
                        startActivity(i);



                }
            }
        }).start();
    }
    @Override
    public void status(boolean b) {
       send = b;
    }
}

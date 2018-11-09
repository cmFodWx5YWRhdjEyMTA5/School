package com.video.aashi.school;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoticeDetails extends AppCompatActivity {


    TextView  header,heading,description,back;
    ImageView back1;
    LinearLayout ll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);


        header = (TextView)findViewById(R.id.header);
        heading = (TextView)findViewById(R.id.heading);
        description = (TextView)findViewById(R.id.description);
        back = (TextView)findViewById(R.id.back1);
        back1 =(ImageView) findViewById(R.id.back);
        ll = (LinearLayout)findViewById(R.id.animat1);




        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation a = AnimationUtils.loadAnimation(NoticeDetails.this, R.anim.fade_out);
                a.reset();
                 ll.startAnimation(a);
                finish();
            }
        });


    }
    @Override
    protected void onPause() {


        super.onPause();
    }
}

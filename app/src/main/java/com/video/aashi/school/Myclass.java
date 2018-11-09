package com.video.aashi.school;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.video.aashi.school.adapters.BarView;


public class Myclass extends AppCompatActivity {

    private TextView mLowLabel, mMidLabel, mHighLabel;
    private BarView mLowBar, mMidBar, mHighBar;


    private final int low = 17;
    private final int mid = 90;
    private final int high = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        mLowBar = (BarView) findViewById(R.id.low_bar);
        mLowLabel = (TextView) findViewById(R.id.low_text);
        mLowBar.set(Color.BLUE, low);
        mLowLabel.setText(getPercentage(low));

    }

    private String getPercentage(int per) {
        return per + "%";
    }


}

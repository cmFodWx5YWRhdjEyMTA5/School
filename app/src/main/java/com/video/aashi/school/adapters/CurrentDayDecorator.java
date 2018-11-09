package com.video.aashi.school.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.video.aashi.school.R;

import java.util.Date;

public class CurrentDayDecorator implements DayViewDecorator {

private Drawable drawable;

private CalendarDay currentDay = CalendarDay.from(new Date());

public CurrentDayDecorator(Activity context) {
    drawable = ContextCompat.getDrawable(context,     R.drawable.button_next_month_selector);

}

@Override
public boolean shouldDecorate(CalendarDay day) {
    return day.equals(currentDay);
}
    @SuppressLint({"NewApi", "ResourceAsColor"})
    private static Color[] PRIME_TABLE ={

            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.red),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.yelow),
            Color.valueOf(R.color.grey),
            Color.valueOf(R.color.yelow),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.red),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.yelow),
            Color.valueOf(R.color.grey),
            Color.valueOf(R.color.yelow),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.red),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.yelow),
            Color.valueOf(R.color.grey),
            Color.valueOf(R.color.yelow),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.red),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.navy),
            Color.valueOf(R.color.yelow),
            Color.valueOf(R.color.grey),


    };
    private static int[] DRAWABLES ={

           R.drawable.bg_circle2,
            R.drawable.bg_circle,
            R.drawable.bg_circle,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle3,
            R.drawable.bg_circle2,
            R.drawable.bg_circle1,
            R.drawable.bg_circle,
            R.drawable.bg_circle,
            R.drawable.bg_circle1,
            R.drawable.bg_circle2,
            R.drawable.bg_circle2,
            R.drawable.bg_circle,
            R.drawable.bg_circle,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle1,
            R.drawable.bg_circle3,
            R.drawable.bg_circle2,
            R.drawable.bg_circle1,
            R.drawable.bg_circle,
            R.drawable.bg_circle,
            R.drawable.bg_circle1
    };
@Override
public void decorate(final DayViewFacade view) {
  view.setBackgroundDrawable(drawable);


}
}
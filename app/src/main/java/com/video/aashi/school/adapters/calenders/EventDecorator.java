package com.video.aashi.school.adapters.calenders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.video.aashi.school.R;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class EventDecorator implements DayViewDecorator {
 
   private int color;
   private HashSet<CalendarDay> dates;
   private   Context context;
   private  String absent;
 
    public EventDecorator(int color, Collection<CalendarDay> dates,Context context,String absent) {
        this.context = context;
        this.color = color; 
        this.dates = new HashSet<>(dates);
        this.absent = absent;
    }
    @Override 
    public boolean shouldDecorate(CalendarDay day) { 
        return dates.contains(day); 
    } 
 
    @Override 
    public void decorate(DayViewFacade view) {
        switch (absent)
        {
            case "absent":
                view.addSpan(new StyleSpan(Typeface.BOLD));
                view.addSpan(new RelativeSizeSpan(1f));
                view.addSpan(new ForegroundColorSpan(Color.RED));
              //  view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context,R.drawable.bg_circle)));
                break;
            case "holiday":
                view.addSpan(new StyleSpan(Typeface.BOLD));
                view.addSpan(new RelativeSizeSpan(1f));
                view.addSpan(new ForegroundColorSpan(Color.parseColor("#FFD2B139")));
            //    view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context,R.drawable.bg_circle2)));
                break;
               case "weekoff":
                view.addSpan(new StyleSpan(Typeface.BOLD));
                view.addSpan(new RelativeSizeSpan(1f));
                view.addSpan(new ForegroundColorSpan(Color.parseColor("#e9e8e1")));
             //   view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context,R.drawable.bg_circle3)));
                break;
            case "afternoon":
                view.addSpan(new StyleSpan(Typeface.BOLD));
                view.addSpan(new RelativeSizeSpan(1f));
                view.addSpan(new ForegroundColorSpan(Color.parseColor("#018786")));
             //   view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context,R.drawable.bg_circle5)));
                break;
                default:
                    break;



        }

      //view.addSpan(new DotSpan(5, color));
    } 
}
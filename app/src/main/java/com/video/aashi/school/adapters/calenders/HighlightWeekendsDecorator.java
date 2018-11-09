package com.video.aashi.school.adapters.calenders;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.video.aashi.school.R;

import java.util.Calendar;

public class HighlightWeekendsDecorator implements DayViewDecorator
{
  private final Drawable highlightDrawable;
  private static final int color = Color.parseColor("#228BC34A");
  private final Calendar calendar = Calendar.getInstance();
  public HighlightWeekendsDecorator(Context context) {
    highlightDrawable = ContextCompat.getDrawable(context,R.drawable.bg_circle1);
  }
  @Override public boolean shouldDecorate(final CalendarDay day) {
    day.copyTo(calendar);
    int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
    Log.e("Tag" ,"Dates"+ weekDay);
    return weekDay == Calendar.SUNDAY || weekDay == Calendar.SATURDAY || weekDay == Calendar.MONDAY
         || weekDay == Calendar.TUESDAY || weekDay == Calendar.WEDNESDAY || weekDay == Calendar.THURSDAY || weekDay == Calendar.FRIDAY;
  }
  @Override public void decorate(final DayViewFacade view) {
    view.addSpan(new RelativeSizeSpan(1.4f));
    view.addSpan(new ForegroundColorSpan(Color.parseColor("#000000")));
  }
}

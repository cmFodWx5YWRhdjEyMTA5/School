package com.video.aashi.school.adapters.calenders;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.video.aashi.school.R;

public class MySelectorDecorator implements DayViewDecorator {


  private final Drawable drawable;
  private final  CalendarDay calendarDayl;

  public MySelectorDecorator(Activity context,CalendarDay day)
  {
    this.calendarDayl =day;
    drawable = context.getResources().getDrawable(R.drawable.badge);
  }

  @Override
  public boolean shouldDecorate(CalendarDay day) {
    return calendarDayl.equals(day);
  }
  @Override
  public void decorate(DayViewFacade view) {
    view.addSpan(new ForegroundColorSpan(Color.WHITE));
    view.setSelectionDrawable(drawable);
  }
}

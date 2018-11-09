package com.video.aashi.school.adapters.calenders;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.video.aashi.school.R;

public class MySelectorDecorator implements DayViewDecorator {


  private final Drawable drawable;

  public MySelectorDecorator(Activity context)
  {

    drawable = context.getResources().getDrawable(R.drawable.bg_circle);
  }

  @Override
  public boolean shouldDecorate(CalendarDay day) {
    return true;
  }

  @Override
  public void decorate(DayViewFacade view) {
    view.setSelectionDrawable(drawable);
  }
}

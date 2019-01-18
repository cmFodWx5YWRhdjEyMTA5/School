package com.video.aashi.school.adapters.calenders;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import java.util.Date;

public class OneDayDecorator implements DayViewDecorator {

  private CalendarDay date;
  String absent;

  public OneDayDecorator(CalendarDay  dates,String absent)
  {

    date = dates;
    this.absent = absent;
  }

  @Override
  public boolean shouldDecorate(CalendarDay day)
  {
    return date != null && day.equals(date);
  }

  @Override
  public void decorate(DayViewFacade view) {

      switch (absent)
      {
          case "present":
              view.addSpan(new StyleSpan(Typeface.BOLD));
              view.addSpan(new RelativeSizeSpan(1f));
              view.addSpan(new ForegroundColorSpan(Color.parseColor("#FF4CD7C9")));
              break;
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
              // view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context,R.drawable.bg_circle2)));
              break;
          case "weekoff":
              view.addSpan(new StyleSpan(Typeface.BOLD));
              view.addSpan(new RelativeSizeSpan(1f));
              view.addSpan(new ForegroundColorSpan(Color.parseColor("#e9e8e1")));
              //  view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context,R.drawable.bg_circle3)));
              break;
          case "afternoon":
              view.addSpan(new StyleSpan(Typeface.BOLD));
              view.addSpan(new RelativeSizeSpan(1f));
              view.addSpan(new ForegroundColorSpan(Color.parseColor("#018786")));
              //  view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context,R.drawable.bg_circle5)));
              break;
          case "forenoon":
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1f));
            view.addSpan(new ForegroundColorSpan(Color.parseColor("#FF4081")));
            // view.setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context,R.drawable.bg_circle5)));
          break;
          default:
          break;
      }
  }
  public void setDate(Date dates)
  {
    this.date = CalendarDay.from(dates);
  }
}

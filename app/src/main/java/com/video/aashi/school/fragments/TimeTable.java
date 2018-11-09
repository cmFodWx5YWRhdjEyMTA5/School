package com.video.aashi.school.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.ApiClient;
import com.video.aashi.school.adapters.Interfaces.MyInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.Month;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTable extends Fragment {

LinearLayout views;
    public TimeTable() {
        // Required empty public constructor
    }
    String locid;
    String acayear;
    String classid;
    Toolbar toolbar;
    MyInterface myInterface;
    Retrofit retrofit;
    String formatedDate;
    TextView orderno,day;
    String dayno,day_name,staffname,classno,subjectname;
    SimpleDateFormat format;
   MaterialCalendarView widget;
   LinearLayout removes;
    ProgressDialog progressDialog;
    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_time_table, container, false);
        widget =(MaterialCalendarView)view.findViewById(R.id.calendarView);
        widget.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        widget.setTileWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        toolbar =(android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Timetable");
        orderno =(TextView)view.findViewById(R.id.orderno);
        day=(TextView)view.findViewById(R.id.day);
        removes =(LinearLayout)view.findViewById(R.id.removes);
        views =(LinearLayout)view.findViewById(R.id.views);
        setHasOptionsMenu(true);
        progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMessage("Loading");
        getActivity().invalidateOptionsMenu();
        locid = Navigation.location_id;
        acayear = Navigation.academicyear;
        classid = Navigation.class_id;
        views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        myInterface= ApiClient.getApiCLient().create(MyInterface.class);
        OkHttpClient defaulthttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException
                            {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Content-Type", "application/json").build();
                                return chain.proceed(request);

                            }
                        }).build();
        retrofit =   new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();

        widget.setOnDateChangedListener(new OnDateSelectedListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                views.removeAllViews();
               // progressDialog.show();

                @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date dates = date.getDate();
                try {
                    dates = (Date)formatter.parse(dates.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println(date);


                Calendar cal = Calendar.getInstance();
                cal.setTime(dates);
                format = new SimpleDateFormat("dd/MM/yyyy");
                formatedDate = format.format(dates);
             //    formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
                new getAttendance().execute(formatedDate);


            }
        });
       CalendarDay currentCal= CalendarDay.today();
        Date dates= currentCal.getDate();
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        try {
            dates = (Date)formatter.parse(dates.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dates);
        format = new SimpleDateFormat("dd/MM/yyyy");
        formatedDate = format.format(dates);
    //    formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +         cal.get(Calendar.YEAR);
        new getAttendance().execute(formatedDate);
        Log.e("Tag","FormatedDate"+ formatedDate+ currentCal);
        return  view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.dashboard,menu);


        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

        }

        return super.onOptionsItemSelected(item);
    }


class getAttendance extends AsyncTask<String, Integer, String>
{


    @Override
    protected void onPreExecute() {

       // progressDialog.setCancelable(false);

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(final String... strings) {
        retrofit2.Call<ResponseBody> responseBodyCall = myInterface.getTimetable(new com.video.aashi.school.adapters.post_class.TimeTable(classid,acayear,locid));

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                  String bodyString = null;
                try {
                    bodyString = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject object = new JSONObject(bodyString);
                    JSONArray list = object.getJSONArray("Student TimeTable Details");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject data = list.getJSONObject(i);
                        JSONObject jsonObject = data.getJSONObject("period1Dtl");
                        String dates = jsonObject.getString("createdDtDisp");

                        String mydate = formatedDate.replace("/","-");
                        if (mydate.equals(dates))
                        {
                            Log.i("Tag", "MyTables" + dates);
                          String  dayno1 = data.getString("dayNo");
                          String dayno = jsonObject.getString("hourNo");
                          String  day_name = data.getString("dayName");
                          String  staffname = data.getString("period1StaffName");
                          String  subjectname = data.getString("period1SubjectName");
                            String  staffname2 = data.getString("period2StaffName");
                            String  subjectname2 = data.getString("period2SubjectName");
                            String  staffname3 = data.getString("period3StaffName");
                            String  subjectname3 = data.getString("period3SubjectName");
                            String  staffname4 = data.getString("period4StaffName");
                            String  subjectname4 = data.getString("period4SubjectName");
                            String  staffname5 = data.getString("period5StaffName");
                            String  subjectname5 = data.getString("period5SubjectName");
                            String  staffname6 = data.getString("period6StaffName");
                            String  subjectname6 = data.getString("period6SubjectName");
                            String  staffname7 = data.getString("period7StaffName");
                            String  subjectname7 = data.getString("period7SubjectName");
                            String  staffname8 = data.getString("period8StaffName");
                            String  subjectname8 = data.getString("period8SubjectName");
                            Log.i("Tag","HourNo"+ dayno);
                            addview("1",subjectname,staffname);
                            addview("2",subjectname2,staffname2);
                            addview("3",subjectname3,staffname3);
                            addview("4",subjectname4,staffname4);
                            addview("5",subjectname5,staffname5);
                            addview("6",subjectname6,staffname6);
                            addview("7",subjectname7,staffname7);
                            addview("8",subjectname8,staffname8);
                            JSONObject pri = data.getJSONObject("period9Dtl");

                            if(!pri.equals("null"))
                            {
                                String  staffname9 = data.getString("period9StaffName");
                                String  subjectname9 = data.getString("period9SubjectName");
                                addview("9",subjectname9,staffname9);
                            }
                            else
                            {



                            }
                          orderno.setText(dayno1);
                          day.setText(day_name);

                        }

                    }

                }
                 catch (JSONException e )
                 {
                     e.printStackTrace();
                 }




                    }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
         });
        return null;
       }
    }
    @SuppressLint("SetTextI18n")
    void  addview(String number, String sub, String staffname)
    {

        LinearLayout linearLayouts= new LinearLayout(getActivity());
        linearLayouts .setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParamss = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamss.weight= 1f;
        linearLayouts.setGravity(Gravity.CENTER);
        linearLayouts.setLayoutParams(layoutParamss);
        LinearLayout LL = new LinearLayout(getActivity());
        LL.setOrientation(LinearLayout.HORIZONTAL);
        LL.setGravity(Gravity.CENTER);
        LL.setPadding(8,5,5,5);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        LL.setWeightSum(20f);
        LL.setLayoutParams(LLParams);
        ImageView ladder = new ImageView(getActivity());
        ladder.setImageResource(R.drawable.arrow_shape);
        LinearLayout.LayoutParams image = new LinearLayout.LayoutParams   (ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        image.weight = 5f;
        TextView textView = new TextView(getActivity());
        textView.setText(number);
        textView.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.arrow_shape));
        textView.setGravity(Gravity.CENTER);
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setLayoutParams(image);
        linearLayout.addView(textView);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams   (ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
               layoutParams.weight = 15f;
        LinearLayout layouts = new LinearLayout(getActivity());
        layouts.setOrientation(LinearLayout.HORIZONTAL);
        layouts.setGravity(Gravity.START);
        TextView textViews = new TextView(getActivity());
        textViews.setText(sub);
        textViews.setGravity(Gravity.START);
        textViews.setTextColor(Color.BLACK);
        textViews.setTextSize(16f);
        textViews.setTypeface(textViews.getTypeface(), Typeface.BOLD);
        TextView textViews1 = new TextView(getActivity());
        textViews1.setText(" (" +staffname+ ")");
        textViews1.setTextColor(Color.BLACK);
        textViews.setTextSize(15f);
        layouts.addView(textViews);
        layouts.addView(textViews1);
        layouts.setGravity(Gravity.START);
        layouts.setLayoutParams(layoutParams);
        LL.addView(linearLayout);
        LL.addView(layouts);
        View view = new View(getActivity());
              LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,5);
        params.height = 3;
        view.setLayoutParams(params);
                view.setBackgroundColor(Color.parseColor("#e9e8e1"));
        linearLayouts.addView(LL);
        linearLayouts.addView(view);
        LinearLayout rl=((LinearLayout)getActivity(). findViewById(R.id.views));
        rl.addView(linearLayouts);

    }

}

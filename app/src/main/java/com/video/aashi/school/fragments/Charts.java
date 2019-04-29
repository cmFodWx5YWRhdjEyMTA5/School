package com.video.aashi.school.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.Performance;
import com.video.aashi.school.PinLogin;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.BarView;
import com.video.aashi.school.adapters.Expired;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.ExamArray;
import com.video.aashi.school.adapters.arrar_adapterd.MarksArray;
import com.video.aashi.school.adapters.post_class.Marks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

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
public class Charts extends Fragment {


    public Charts() {

    }
  public static   String examGroupId;
   public static String locId;
   public static String classId;
   public static   String studentId;
    public static String accademicYearId;

    RecyclerView recyclerView;
    CardView cardView;
     Retrofit retrofit;
    MyInterface myInterface;
    Paidadapters  paidadapters;
    TextView studentame;
    TextView studentid,groupname,termname;
  public  static   String group,term;
  TextView roundoff,total,percent;
  String mypercent;
 int myroundoff;
    List<Integer> myList = new ArrayList<Integer>();
    List<Integer> myLists = new ArrayList<Integer>();
    List<MarksArray> marksArrays= new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts, container, false);
        studentame =(TextView)view.findViewById(R.id.student_name);
        studentame.setText(HomePage.userName);
        recyclerView =(RecyclerView)view.findViewById(R.id.charts_recycle);
        roundoff =(TextView)view.findViewById(R.id.roundoff);
        total =(TextView)view.findViewById(R.id.totalmark);
        examGroupId = Performance.groupid;
        locId = Navigation.location_id;
        classId = Navigation.class_id ;
        studentId    = Navigation.student_id;
        accademicYearId = Navigation.academicyear;
        studentid =(TextView)view.findViewById(R.id.myid);
        groupname =(TextView)view.findViewById(R.id.groups);
        termname =(TextView)view.findViewById(R.id.termname);
        studentid.setText(HomePage.s_class);
        groupname.setText(Performance.groupname);
        termname.setText(Performance.termname);
        percent =(TextView)view.findViewById(R.id.percent);
        cardView =(CardView)view.findViewById(R.id.cardview);
        Log.i("Tag","Myids"+examGroupId+locId+classId+studentId+ accademicYearId);
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
        retrofit =   new Retrofit.Builder().baseUrl(HomePage.url).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        myInterface = retrofit.create(MyInterface.class);
        new getMarks().execute();
        cardView.setVisibility(View.GONE);
       return  view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
    class getMarks extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects) {
            Call<ResponseBody> call = myInterface.getMarks(new Marks(examGroupId,locId,classId,studentId,accademicYearId,Navigation.loginId,Navigation.session));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String bodyString = null;
                    if (response.isSuccessful())
                    {
                        try
                        {
                            bodyString  = response.body().string();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        {
                            try {
                                JSONObject object = new JSONObject(bodyString);
                                String   failure = object.getString("status");
                                String  errorMessage = object.getString("errorMessage");
                                if  (failure.contains("failure"))
                                {
                                    String finalErrorMessage = errorMessage;
                                    Expired expired = new Expired(getActivity(), finalErrorMessage);
                                    expired.setTitle(finalErrorMessage);
                                    expired.setCancelable(false);
                                    expired.setPositiveButton("OK", (dialog1, which) -> {
                                        expired.getSharedPreferences();
                                        Intent i = new Intent(getActivity(), PinLogin.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    });
                                    expired.show();
                                }
                                JSONArray list = object.getJSONArray("Student Performance Chart Data");

                                for (int i = 0; i < list.length(); i++)
                                {
                                    String marks,subject;
                                    JSONObject data = list.getJSONObject(i);
                                    marks = data.getString("studAverage");
                                    subject = data.getString("subjectName");
                                    marksArrays.add(new MarksArray(marks,subject));
                                    paidadapters = new Paidadapters(marksArrays,getActivity());
                                    recyclerView.setAdapter(paidadapters);
                                    Log.i("Tag","MyMarkss"+  marks);
                                    if (!marks.isEmpty())
                                    {
                                     cardView.setVisibility(View.VISIBLE);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else

                    {
                        Toast.makeText(getActivity(),"Something went wrong..!!",Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
            return null;
        }
    }




    class Paidadapters extends RecyclerView.Adapter<Viewholder> {


        List<MarksArray> list;
        Context context;


        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.chart, viewGroup, false);
            return new Viewholder(view);
        }

        public Paidadapters(List<MarksArray> adapters, Context context)
        {
            this.context = context;
            this.list = adapters;
        }

        @SuppressLint({"NewApi", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, int i)
        {
            Random rnd = new Random();
            int color =  Color.TRANSPARENT;
//            int color = Color.argb(255, rnd.nextInt(256),
//                    rnd.nextInt(256), rnd.nextInt(256));
            int toatMarks =Integer.valueOf(Performance.maxmarks);
            int finalMark = 0;
            int average =Integer.parseInt(list.get(i).getStudAverage());



            String totalmark = String.valueOf(list.size() * toatMarks);
            if (toatMarks == 50)
            {
                finalMark = average * 2 ;

            }
            else if (toatMarks ==  75)
            {
                finalMark = (int) (average * 1.32 + 1);
            }
            else if (toatMarks == 100)
            {
                finalMark = average;
            }
            else if (toatMarks == 150)
            {
                finalMark    = average / 2 + 25;
            }
            else if (toatMarks == 200)
            {
                finalMark = average/2;
            }

            if (finalMark  <= 30)
            {
                color = Color.RED;
            }
            else if (finalMark >  31 && finalMark<=39)
            {
                color = Color.parseColor("#FFA500");
            }
            else if (finalMark >  40 && finalMark<=49)
            {
                color  = Color.YELLOW;
            }
            else  if (finalMark >  50 && finalMark<=59)
            {
                color  = Color.parseColor("#FFC0CB");
            }
            else  if (finalMark >=  60 && finalMark<=69)
            {
                color  = Color.BLUE;
            }
            else  if (finalMark >  70 && finalMark<=79)
            {
                color  = Color.parseColor("#800080");
            }
            else  if (finalMark >  80 && finalMark<=100)
            {
                color  = Color.parseColor("#008000");
            }
            viewholder.mLowBar.set(color, finalMark);
            viewholder.textView.setText(list.get(i).getSubjectName());
            viewholder.low_text.setText(getPercentage(finalMark));
            int values= Integer.parseInt(list.get(i).getStudAverage());
            total.setText(totalmark);
            if (toatMarks == 0)
            {
            }

            String taa = null;
            addMember(finalMark);
            addMembers(values);
            taa = String.valueOf(myroundoff);
            String my = String.valueOf(myLists.stream().mapToInt(value -> value).sum());
            Log.i("Tag","MyAdd"+ myList+my);
            percent.setText(myList.stream().mapToInt(value -> value).sum() /list.size()+ "%");
            roundoff.setText(my);

        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    public static class Viewholder extends RecyclerView.ViewHolder
    {
        BarView mLowBar;
        TextView textView,low_text;
        public Viewholder(@NonNull View itemView)
        {
            super(itemView);
            low_text=(TextView)itemView.findViewById(R.id.low_text);
            mLowBar = (BarView)itemView. findViewById(R.id.low_bar);
            textView =(TextView)itemView.findViewById(R.id.textforchart);
        }
    }
    private String getPercentage(int per) {
        return per + "%";
    }
    @SuppressLint("NewApi")
    public void addMember(Integer x) {
        myList.add(x);
    };
    public void addMembers(Integer x) {
        myLists.add(x);
    };
}

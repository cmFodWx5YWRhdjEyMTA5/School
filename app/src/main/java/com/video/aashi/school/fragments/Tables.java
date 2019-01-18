package com.video.aashi.school.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Performance;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.MarksArray;
import com.video.aashi.school.adapters.post_class.Marks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Tables extends Fragment {
    public Tables() {

    }

    TextView studentid,groupname,termname,studentame;
    TextView roundoff,total,percents;
    RecyclerView recyclerView;
    Myadapters myadapters;
    Retrofit retrofit;
    List<MarksArray> marksArrays = new ArrayList<>();
    MyInterface myInterface;
    List<Integer> myList = new ArrayList<>();
    List<Integer> myLists = new ArrayList<>();
    CardView cardView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tables, container, false);
        studentame =(TextView)view.findViewById(R.id.studentname1);
        studentame.setText(HomePage.userName);
        recyclerView =(RecyclerView)view.findViewById(R.id.table_recycle);
        roundoff =(TextView)view.findViewById(R.id.roundoff1);
        total =(TextView)view.findViewById(R.id.total1);
        studentid =(TextView)view.findViewById(R.id.myid1);
        groupname =(TextView)view.findViewById(R.id.groups1);
        termname =(TextView)view.findViewById(R.id.termname1);
        studentid.setText(HomePage.s_class);
        groupname.setText(Performance.groupname);
        termname.setText(Performance.termname);
        percents =(TextView)view.findViewById(R.id.percents);
        cardView =(CardView)view.findViewById(R.id.cardviews);
        cardView.setVisibility(View.GONE);
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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        myInterface = retrofit.create(MyInterface.class);
        new getTotal().execute();
        return  view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    class  getTotal extends AsyncTask
    {

  ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Call<ResponseBody> call = myInterface.getMarks(new Marks(Charts.examGroupId,Charts. locId,Charts. classId,
                   Charts. studentId,Charts. accademicYearId));
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
                        Log.i("Tag","MyTables"+  call.request().url() + bodyString );
                        {
                            try {

                                JSONObject object = new JSONObject(bodyString);
                                JSONArray list = object.getJSONArray("Student Performance Chart Data");
                                for (int i = 0; i < list.length(); i++) {

                                    String marks,subject;
                                    JSONObject data = list.getJSONObject(i);
                                    marks = data.getString("studAverage");
                                    subject = data.getString("subjectName");
                                    marksArrays.add(new MarksArray(marks,subject));
                                    myadapters = new Myadapters(marksArrays,getActivity());
                                    recyclerView.setAdapter(myadapters);
                                    if (!marks.isEmpty()  )
                                    {
                                        cardView.setVisibility(View.VISIBLE);
                                    }
                                   // progressDialog.dismiss();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Something went wrong..!!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


            return null;
        }
    }

   class Myadapters extends RecyclerView.Adapter<Viewholder> {


        List<MarksArray> list;
        Context context;
        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.marktable, viewGroup, false);
            return new Viewholder(view);
        }

        public Myadapters(List<MarksArray> adapters, Context context)
        {
            this.context = context;
            this.list = adapters;
        }

        @SuppressLint({"NewApi", "SetTextI18n"})
        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, final int i)
        {
           viewholder.subject.setText(list.get(i).getSubjectName());
           viewholder.marks.setText(list.get(i).getStudAverage());
           viewholder.total.setText(Performance.maxmarks);
            int toatMarks =Integer.valueOf(Performance.maxmarks);
            String totalmark = String.valueOf(list.size() * toatMarks);
            int finalMark = 0;
            int average =Integer.parseInt(list.get(i).getStudAverage());
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
            int values= Integer.parseInt(list.get(i).getStudAverage());
            total.setText(totalmark);


            String taa = null;
            addMember(finalMark);
            addMembers(values);
            String myroundoff = null;
            @SuppressLint({"NewApi", "LocalSuppress"}) String my = String.valueOf(myLists.stream().mapToInt(value -> value).sum());
            Log.i("Tag","MyAdd"+ myList+my);
            percents.setText(myList.stream().mapToInt(value -> value).sum() /list.size()+ "%");
            roundoff.setText(my);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    public static class Viewholder extends RecyclerView.ViewHolder
    {
       TextView subject,marks,total;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            subject =(TextView)itemView.findViewById(R.id.subject);
            marks =(TextView)itemView.findViewById(R.id.mark);
            total =(TextView)itemView.findViewById(R.id.total);
        }
    }
    @SuppressLint("NewApi")
    public void addMember(Integer x)
    {
        myList.add(x);
    };
    public void addMembers(Integer x)
    {
        myLists.add(x);
    };
}

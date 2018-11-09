package com.video.aashi.school.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.Performance;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.ExamArray;
import com.video.aashi.school.adapters.arrar_adapterd.Invoice_array;
import com.video.aashi.school.adapters.post_class.Combo;
import com.video.aashi.school.fragments.payments.Invoice;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class ExamCombo extends Fragment {
    String examGroupName;
    String locId;
    String startRow;
    String noOfRecords;

    Retrofit retrofit;
    MyInterface myInterface;
    RecyclerView recyclerView;
    Paidadapters paidadapter;
    List<ExamArray> examArrays= new ArrayList<>();
    Toolbar toolbar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_exam_combo, container, false);
       setHasOptionsMenu(true);
        toolbar =(android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle("Performance");
       getActivity().invalidateOptionsMenu();
       examGroupName ="%";
       locId = Navigation.location_id;
       startRow ="1";
       noOfRecords ="1";
       recyclerView =(RecyclerView)view.findViewById(R.id.examcombo);
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
        examArrays.clear();
        new getCombo().execute();


       return  view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.dashboard,menu);

        super.onCreateOptionsMenu(menu, inflater);

    }


    class  getCombo extends AsyncTask
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(true);

            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects) {


            Call<ResponseBody> call = myInterface.getCombo(new Combo(examGroupName,locId,startRow,noOfRecords));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String bodyString = null;
                    try
                    {
                        bodyString  = response.body().string();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                  //  Log.i("Tag","MyExams"+ call.request().url() + bodyString );
                    {
                        try {
                            String academicYearId;
                            String examEndDtDisp;
                            String examGroupName;
                            String examGroupId;
                            String examTermName;
                            String locId,typeid;

                            JSONObject object = new JSONObject(bodyString);
                            JSONArray list = object.getJSONArray("Student Exam Combo");
                            for (int i = 0; i < list.length(); i++)
                            {
                                Log.i("Tag","MyExams"+ call.request().url() + bodyString );
                                JSONObject data = list.getJSONObject(i);

                                academicYearId = data.getString("academicYearId");
                                examGroupName = data.getString("examGroupName");
                                examEndDtDisp = data.getString("examEndDtDisp");
                                examGroupId = data.getString("examGroupId");
                                examTermName = data.getString("examTermName");
                                typeid = data.getString("examTypeId");

                                locId = data.getString("locId");
                                examArrays.add(new ExamArray(academicYearId,examEndDtDisp,examGroupName,
                                        examGroupId,examTermName,locId,typeid ));
                                paidadapter = new Paidadapters(examArrays,getActivity());
                                recyclerView.setAdapter(paidadapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
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


        List<ExamArray> list;
        Context context;


        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.examdesigns, viewGroup, false);
            return new Viewholder(view);
        }

        public Paidadapters(List<ExamArray> adapters, Context context)
        {
            this.context = context;
            this.list = adapters;
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, final int i)
        {
            viewholder.date.setText(list.get(i).getExamEndDtDisp());
            viewholder.exams.setText(list.get(i).getExamTermName());
            viewholder.exams.setText(list.get(i).getExamGroupName());
            Log.i("Tag","MyExam"+ list.get(i).getExamEndDtDisp()+list.get(i).getExamTermName()+ list.get(i).getExamGroupName());
              viewholder.mycard.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v)
                  {

                      Performance performance = new Performance();
                     Bundle bundle= new Bundle();
                     bundle.putString("terms",list.get(i).getExamTermName());
                     bundle.putString("groupid",list.get(i).getExamGroupId() );
                     bundle.putString("examtype",list.get(i).getExamGroupName());
                     performance.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.mycontainer,performance).addToBackStack(null).commit();

                  }
              });
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
        TextView date;
        TextView exams,mainexam;
        LinearLayout paidlayout,unpaidlayout;
        CardView mycard;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mycard =(CardView)itemView .findViewById(R.id.cadview);
            date = (TextView)itemView.findViewById(R.id.examdat);
            exams = (TextView)itemView.findViewById(R.id.exams);
            mainexam = (TextView)itemView.findViewById(R.id.mainexam);


        }
    }
}

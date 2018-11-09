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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.Performance;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.ExamArray;
import com.video.aashi.school.adapters.post_class.Memo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Memos extends Fragment {


    Toolbar toolbar;
    PopupWindow popUp;
    LinearLayout layout,layouts;
    TextView tv;
    Button but;
    LinearLayout.LayoutParams params;
    LinearLayout mainLayout;
    Button memo;
    Retrofit retrofit;
    MyInterface myInterface;
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    String classId;
    String studentId;
    String locId;
    String classGeneralId;
    String date;
    String validdate;
    String inference;
    String remarks;
   public static String issues;
    public  static   String memoname;
    Paidadapters paidadapters;
    RecyclerView recyclerView;
    TextView validupto,infer,remark,issue,memo_topic,nomemos;
    List<com.video.aashi.school.adapters.arrar_adapterd.Memos> lists= new ArrayList<>();
  boolean click = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_memos, container, false);
        toolbar =(android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        layout =(LinearLayout)view.findViewById(R.id.popupwindow);
        layouts =(LinearLayout)view.findViewById(R.id.alpha);
        tv = new TextView(getActivity());
        but = new Button(getActivity());
        classId = Navigation.class_id;
        studentId = Navigation.student_id;
        locId = Navigation.location_id;
        classGeneralId = MainActivity.general_id;
        recyclerView =(RecyclerView)view.findViewById(R.id.mymemos);
         nomemos =(TextView)view.findViewById(R.id.nomemo);
        lists.clear();
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        retrofit =   new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(client)
                .build();
        layouts.setAlpha(1);
        myInterface = retrofit.create(MyInterface.class);
        memo =(Button)view.findViewById(R.id.viewmemo);
        memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click)
                {
                    layouts.setAlpha((float) 0.4);
                    click = false;
                }
                else
                {
                    click = true;
                }



                     //   popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
                 //   popUp.update(50, 50, 300, 80);
             }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        new getMemos().execute();
        toolbar.setTitle("Memoboard");
        return  view;
    }


    class  getMemos extends AsyncTask
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Call<ResponseBody> call = myInterface.getMemos(new Memo(classId,studentId,locId,classGeneralId));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String bodyString = null;
                    try {
                        bodyString  = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Tag", "MyMemos"+ bodyString);
                        try {
                            JSONObject object = new JSONObject(bodyString);
                            JSONArray list = object.getJSONArray("Memo Board Details");

                            if (list.length() != 0)
                            {
                                nomemos.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < list.length(); i++) {


                                Log.i("Tag", "MyExams" + call.request().url() + bodyString);
                                JSONObject jsonObject = list.getJSONObject(i);
                                date = jsonObject.getString("createdDtDisp");
                                validdate = jsonObject.getString("validuntilDt");
                                memoname = jsonObject.getString("memoTypeName");
                                issues = jsonObject.getString("issues");
                                inference = jsonObject.getString("inference");
                                remarks = jsonObject.getString("remarks");
                                Log.i("Tag","MyMems"+date+validdate+memoname+issues+inference+remarks);
                                lists.add(new com.video.aashi.school.adapters.arrar_adapterd.Memos(date,validdate,inference,
                                        remarks,issues,memoname));
                                paidadapters = new Paidadapters(lists,getActivity());
                                recyclerView.setAdapter(paidadapters);
                                progressDialog.dismiss();
                                }

                            }
                            else
                            {
                               nomemos.setVisibility(View.VISIBLE);
                               recyclerView.setVisibility(View.GONE);
                            }


                                progressDialog.dismiss();





                        } catch (JSONException e) {
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


    class Paidadapters extends RecyclerView.Adapter<Viewholder> {


        List<com.video.aashi.school.adapters.arrar_adapterd.Memos> list;
        Context context;


        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.memmodesign, viewGroup, false);
            return new Viewholder(view);
        }

        public Paidadapters(List<com.video.aashi.school.adapters.arrar_adapterd.Memos> adapters, Context context)
        {
            this.context = context;
            this.list = adapters;
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, final int i)
        {
            viewholder.date.setText(list.get(i).getDate());
            viewholder.issue.setText(list.get(i).getIssues());
            viewholder.main.setText(list.get(i).getMemoname());

            viewholder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                initiatePopupWindow();
                    validupto.setText(list.get(i).getValiddate());
                    remark.setText(list.get(i).getRemarks() );
                    issue.setText(list.get(i).getIssues());
                    infer.setText(list.get(i).getInference());
                    memo_topic.setText(list.get(i).getMemoname());
                    layouts.setAlpha((float) 0.3);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    public static class Viewholder extends RecyclerView.ViewHolder
    {
        TextView date;
        TextView issue,main;
        LinearLayout paidlayout,unpaidlayout;
        CardView mycard;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mycard =(CardView)itemView .findViewById(R.id.cadviews);
            date = (TextView)itemView.findViewById(R.id.dates);
            issue = (TextView)itemView.findViewById(R.id.issuememo);
            main = (TextView)itemView.findViewById(R.id.memoheading);


        }
    }


    private void initiatePopupWindow()
    {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity

            View popupView =  LayoutInflater.from(getActivity()).inflate(R.layout.memoview,
              (ViewGroup)getView(). findViewById(R.id.
                    popupwindow));
            final PopupWindow popupWindow = new PopupWindow(popupView,
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            validupto =(TextView)popupView.findViewById(R.id.valid);
            infer =(TextView)popupView.findViewById(R.id.inference);
            remark =(TextView)popupView.findViewById(R.id.remarks);
            issue =(TextView)popupView.findViewById(R.id.issue);
            memo_topic =(TextView)popupView.findViewById(R.id.memo_topic);
            CardView cardView;
            cardView =(CardView) popupView.findViewById(R.id.dismiss);
            cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                layouts.setAlpha(1);
             }
            });
            popupWindow.showAtLocation(popupView,Gravity.CENTER,0,0);
            }
             catch (Exception e)
             {
            e.printStackTrace();
            }
         }

}

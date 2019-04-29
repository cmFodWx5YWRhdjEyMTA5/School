package com.video.aashi.school.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.PinLogin;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Expired;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.Holiday_adapter;
import com.video.aashi.school.adapters.arrar_adapterd.Homeworks;
import com.video.aashi.school.adapters.post_class.Howork;
import com.video.aashi.school.fragments.payments.Weekoffs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
public class Homework extends Fragment
{



   android.support.v7.widget.Toolbar toolbar;
   Retrofit retrofit;
   MyInterface myInterfacee;
   List<Homeworks> homeworks=new ArrayList<>();
   RecyclerView recyclerView;
   String classid ;
   String studentid ;
   String locid ;
   String viewmore = "0";
   HomeWorkAdapter homeWorkAdapter;
   TextView nohome;
   String check ="0";

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homework, container, false);
        classid = Navigation.class_id;
        studentid =Navigation.student_id;
        locid =  Navigation.location_id;
        toolbar =(android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Homework");
        Bundle bundle = getArguments();
        if (bundle != null) {
            check =     bundle.getString("check");
        }
        recyclerView =(RecyclerView)view.findViewById(R.id.recycle_home);
        nohome =(TextView)view.findViewById(R.id.nohome);
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
        OkHttpClient defaulthttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Content-Type", "application/json").build();
                                return chain.proceed(request);

                            }
                        }).build();
        retrofit = new Retrofit.Builder().baseUrl(HomePage.url).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        myInterfacee = retrofit.create(MyInterface.class);
        new getHomework().execute();
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
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    if (check.equals("1"))
                    {
                        getFragmentManager().beginTransaction().replace(R.id.mycontainer,new HomePage()).commit();
                    }
                    else
                    {
                        getFragmentManager().beginTransaction().replace(R.id.mycontainer,new HomePage()).commit();
                    }

                    return true;
                }
                return false;
            }
        });
    }
    class  getHomework extends AsyncTask
    {

       ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading");
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Call<ResponseBody> call = myInterfacee.getWorks(new Howork(classid,studentid,locid,viewmore,Navigation.loginId,Navigation.session));


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    homeworks = new ArrayList<>();



                    String bodyString = null;

                    if (response.isSuccessful())
                    {
                        try {
                            bodyString = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Log.i("Tag", "MyHomework" +classid+studentid+locid+viewmore+  call.request().url()+ bodyString);

                        {
                            try {
                                JSONObject object = new JSONObject(bodyString);
                                String   failure = object.getString("status");
                                String  errorMessage = object.getString("errorMessage");
                                if  (failure.contains("failure")) {
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
                                JSONArray list = object.getJSONArray("Home Work Details");
                                if (list.length() != 0)
                                {
                                    nohome.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < list.length(); i++)
                                    {
                                        JSONObject data = list.getJSONObject(i);
                                        String description;
                                        String date1,date2;
                                        String chapter,subject;
                                        description = data.getString("homeWorkDesc");
                                        date1 = data.getString("giveDtDisp");
                                        date2 = data.getString("submitedDtDisp");
                                        chapter = data.getString("chapter");
                                        subject = data.getString("subject");
                                        homeworks.add(new Homeworks(date1,date2,description,chapter,subject));
                                        homeWorkAdapter = new HomeWorkAdapter(homeworks);
                                        recyclerView.setAdapter(homeWorkAdapter);
                                        progressDialog.dismiss();

                                    }
                                }
                                else
                                {
                                    nohome.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                }
                            }
                            catch (JSONException e)
                            {
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

    class HomeWorkAdapter extends RecyclerView.Adapter<Homework.Viewholder> {


        List<Homeworks> holiday_adapters;


        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.homeworks, viewGroup, false);
            return new Viewholder(view);
        }

        public HomeWorkAdapter(List<Homeworks> adapters) {
            this.holiday_adapters = adapters;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {


            viewholder.subject.setText(holiday_adapters.get(i).getSubject());
            viewholder.chapter.setText("("+holiday_adapters.get(i).getChapter() +")");
            viewholder.description.setText(holiday_adapters.get(i).getDescriptio());
            viewholder.submit.setText(holiday_adapters.get(i).getSubmitdate());
            viewholder.given.setText( holiday_adapters.get(i).getGivendate() );



        }

        @Override
        public int getItemCount() {

            return holiday_adapters.size();

        }
    }

    public static class Viewholder extends RecyclerView.ViewHolder

    {
        TextView submit;
        TextView given;
        TextView description,chapter,subject;
        LinearLayout visibility;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            given =(TextView)itemView.findViewById(R.id.givendate);
            submit =(TextView)itemView.findViewById(R.id.submitdate);
            description=(TextView)itemView.findViewById(R.id.description);
            chapter=(TextView)itemView.findViewById(R.id.chapter);
            subject =(TextView)itemView.findViewById(R.id.subjects);


        }
    }
}

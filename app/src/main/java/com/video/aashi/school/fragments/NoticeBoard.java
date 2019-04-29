package com.video.aashi.school.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.PinLogin;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Expired;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.NoticeBoards;
import com.video.aashi.school.adapters.post_class.Notify;
import com.video.aashi.school.adapters.viewAdspters.Notification_designs;
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
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.Context.MODE_PRIVATE;
public class NoticeBoard extends Fragment
{
    String title,createdat,time,activedis,noticemessage;
    RecyclerView noticeboard;
    MyInterface loginInterface;
    Retrofit retrofit;
    List<NoticeBoards> noticeBoards= new ArrayList<>();
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    SharedPreferences sharedPreferences;
    String  studentid,genid,classid,locaid;
    Notification_designs notification_designs;
    Toolbar toolbar;
    TextView nonotifi;
    String check ="0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_notice_board, container, false);
       sharedPreferences = getActivity().getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
       noticeboard = view.findViewById(R.id.notification_recylerview);
       studentid = sharedPreferences.getString("StudentId","");
       genid = sharedPreferences.getString("gen_id","");
       classid = sharedPreferences.getString("class_id","");
       locaid = sharedPreferences.getString("location_id","");
       nonotifi =(TextView)view.findViewById(R.id.nonotify);
       toolbar =(android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle("Noticeboard");
        Bundle bundle = getArguments();
        if (bundle != null) {
            check =     bundle.getString("check");
        }
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
        noticeboard.setLayoutManager(layoutManager);
        loginInterface = retrofit.create(MyInterface.class);
       new GetNotification().execute();
       return view;
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
    class GetNotification extends AsyncTask
    {
    ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Object o) {

            super.onPostExecute(o);
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            Call<ResponseBody> call = loginInterface.GetNotification(new
                    Notify(classid,studentid,locaid,genid,Navigation.loginId,Navigation.session));
            call.enqueue(new Callback<ResponseBody>()
            {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    noticeBoards = new ArrayList<>();
                    Log.i("Tag","MyNotification"+ call.request().url()+ classid+studentid+locaid+genid+Navigation.loginId+Navigation.session);
                    if (!response.isSuccessful())
                    {
                        Toast.makeText(getActivity(),"Something went wrong..!!!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {


                    String bodyString = null;
                    try {
                        bodyString  = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
                            JSONArray list = object.getJSONArray("Notice Board Details");
                            if (list.length() != 0)
                            {
                                nonotifi.setVisibility(View.GONE);
                                noticeboard.setVisibility(View.VISIBLE);


                            for(int i=0;i<list.length();i++)
                            {
                                Log.i("Tag","MyNotification"+  list.length());
                                JSONObject data = list.getJSONObject(i);
                                title = data.getString("noticeBoardTitle");
                                activedis = data.getString("activeDisp");
                                noticemessage = data.getString("noticeMessage");
                                createdat = data.getString("noticeCreatedDtDisp");
                                time = data.getString("noticeCreatedDtDisp");
                                Log.i("Tag","MyList"+  list.length());
                                noticeBoards.add(new NoticeBoards(activedis,title,createdat,time,noticemessage));
                                notification_designs = new Notification_designs(getActivity(),noticeBoards);
                                noticeboard.setAdapter(notification_designs);
                                progressDialog.dismiss();
                                Log.i("Tag","MyDebug"+  time);
                            }
                            }
                            else
                            {
                                nonotifi.setVisibility(View.VISIBLE);
                                noticeboard.setVisibility(View.GONE);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.dashboard,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}

package com.video.aashi.school.fragments.payments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.Holiday_adapter;
import com.video.aashi.school.adapters.post_class.Hols;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
public class Weekoffs extends Fragment {


    public Weekoffs() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    HolidayAdapter holidayAdapter;
    List<Holiday_adapter> holiday_list;
    Retrofit retrofit;
    MyInterface loginInterface;
    String general_id, year_id, loc_id;
    String day_name, date, day_des;
    String mytime;
    TextView noholiday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekoffs, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.weekoffs_recycle);
        noholiday =(TextView)view.findViewById(R.id.noweekoff);
        general_id = Navigation.general_id;
        year_id = Navigation.academicyear;
        loc_id = Navigation.location_id;
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
        retrofit = new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        loginInterface = retrofit.create(MyInterface.class);
        new getHolidays().execute();
        return view;

    }

    class getHolidays extends AsyncTask
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
        protected Object doInBackground(Object[] objects) {

            Call<ResponseBody> call = loginInterface.getHolidays(new Hols(year_id, general_id, loc_id));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    holiday_list = new ArrayList<>();
                    String bodyString = null;
                    try {
                        bodyString = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("Tag", "MyHolidays" + year_id + general_id + loc_id + call.request().url() + bodyString);
                    {
                        try {
                            JSONObject object = new JSONObject(bodyString);
                            JSONArray list = object.getJSONArray("Student Holiday Details");
                            if (list.length() !=0 ) {
                                noholiday.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                Log.i("Tag", "MyHolidayss" + list.length());
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject data = list.getJSONObject(i);
                                    if (data.length() != 0) {
                                        day_name = data.getString("dayName");
                                        date = data.getString("holidayDateDisp");
                                        day_des = data.getString("holidayCategoryDesc");

                                        holiday_list.add(new Holiday_adapter(date, day_name, day_des));

                                        holidayAdapter = new HolidayAdapter(holiday_list);

                                        recyclerView.setAdapter(holidayAdapter);
                                        progressDialog.dismiss();


                                    } else {
                                        progressDialog.dismiss();
                                    }
                                }
                            }
                            else
                            {
                                progressDialog.dismiss();
                                recyclerView.setVisibility(View.GONE);
                                noholiday.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    progressDialog.dismiss();


                }
            });


            return null;
        }
    }

    class HolidayAdapter extends RecyclerView.Adapter<Weekoffs.Viewholder> {


        List<Holiday_adapter> holiday_adapters;


        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.holsday, viewGroup, false);
            return new Viewholder(view);
        }

        public HolidayAdapter(List<Holiday_adapter> adapters) {
            this.holiday_adapters = adapters;
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

            viewholder.day.setText(holiday_adapters.get(i).getTime().substring(0,3));
            viewholder.time.setText(holiday_adapters.get(i).getDate());
            viewholder.description.setText(holiday_adapters.get(i).getDescription());
            viewholder.visibility.setVisibility(View.GONE);
        }
        @Override
        public int getItemCount() {
            return holiday_adapters.size();
        }
    }

    public static class Viewholder extends RecyclerView.ViewHolder

    {
        TextView day;
        TextView time;
        TextView description;
        LinearLayout visibility;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.hlidayday1);
            time = (TextView) itemView.findViewById(R.id.holidays_date1);
            description = (TextView) itemView.findViewById(R.id.weekoff);
            visibility = (LinearLayout) itemView.findViewById(R.id.visibility1);
        }
    }
}

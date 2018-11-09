package com.video.aashi.school.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.ForgetPass;
import com.video.aashi.school.Login;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.post_class.OtpUpdate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
public class Validate extends Fragment {


    public Validate() {
        // Required empty public constructor
    }

    String userId;
    String newPassword;
    String otp;
    Retrofit retrofit;
    MyInterface myInterface;
    EditText otps,enter,renter;
   CardView sendcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_validate, container, false);
        userId = ForgetPass.userId;
        otps=(EditText)view.findViewById(R.id.otp);
        enter =(EditText)view.findViewById(R.id.enter);
        renter =(EditText)view.findViewById(R.id.reenter);
        sendcode    =(CardView)view.findViewById(R.id.sendcode);
        OkHttpClient defaulthttpClient = new OkHttpClient.Builder()
               .addInterceptor(
                        chain -> {
                            Request request = chain.request().newBuilder()
                                    .addHeader("Content-Type", "application/json").build();
                            return chain.proceed(request);
                        }).build();
        retrofit =   new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        myInterface = retrofit.create(MyInterface.class);

         sendcode.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (otps.getText().toString().isEmpty())
                 {
                     otps.setError("Enter otp");
                 }
                 else if (enter.getText().toString().isEmpty())
                 {
                     enter.setError("Field can't be empty");
                 }
                 else if (renter.getText().toString().isEmpty())
                 {
                     renter.setError("Field can't be empty");
                 }
                 else if (!enter.getText().toString().equals(renter.getText().toString()))
                 {
                     Toast.makeText(getActivity(),"passwords doesn't match",Toast.LENGTH_LONG).show();
                 }
                 else
                 {
                     newPassword = renter.getText().toString();
                     otp =otps.getText().toString();
                     new updatePass().execute();
                 }
             }
         });









        return  view;
    }

    class updatePass extends AsyncTask

    {

        @Override
        protected Object doInBackground(Object[] objects) {
            Call<ResponseBody> call = myInterface.otpUpdate(new OtpUpdate(userId,newPassword,otp));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {

                    String bodyString = null;
                    try
                    {
                        bodyString  = response.body().string();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    String success,fails;

                    try {
                        JSONObject jsonArray = new JSONObject(bodyString);
                        success = jsonArray.getString("result");
                        fails = jsonArray.getString("message");
                        if (success.contains("success"))

                        {
                            Toast.makeText(getActivity(),fails,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(),Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                        else if (success.contains("failure"))
                        {
                            Toast.makeText(getActivity(),fails,Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Something went wrong please try again",Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e)
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

}

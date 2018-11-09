package com.video.aashi.school;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.post_class.OtpGen;
import com.video.aashi.school.fragments.Validate;

import org.json.JSONArray;
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

public class ForgetPass extends AppCompatActivity {

    CardView sendcode;
    Retrofit retrofit;
    MyInterface myInterface;
   public static String userId;
    String emailId,users;
    TextView email,userids;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        email =(TextView)findViewById(R.id.username1);
        userids =(TextView)findViewById(R.id.userid);


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

        //userId = Navigation.emailids;
        emailId = email.getText().toString().trim();
        retrofit =   new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        myInterface = retrofit.create(MyInterface.class);
        sendcode=(CardView)findViewById(R.id.sendcode);

        sendcode.setOnClickListener(v -> {
                emailId = email.getText().toString();
               if (userids.getText().toString().isEmpty())
                {
                 userids.setError("Field can't be empty");
                }
                else if (email.getText().toString().isEmpty() )
                {
                    email.setError("Field can't be empty");

                }
                else if (!emailId.matches(emailPattern))
                {
                    email.setError("Enter valid email");
                }
                else
                {
                    emailId = email.getText().toString();
                    userId = userids.getText().toString();
                    new sendCode().execute();
                    //   getSupportFragmentManager().beginTransaction().replace(R.id.mylayout,new Validate()).commit();
                }
             }
             );
    }
    class sendCode extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects) {
            Call<ResponseBody> call=myInterface.getOtp(new OtpGen(userId,emailId));
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
                    try
                    {

                        JSONObject jsonArray = new JSONObject(bodyString);
                        success = jsonArray.getString("result");
                        fails = jsonArray.getString("message");
                        if (success.contains("success"))
                        {
                            Toast.makeText(getApplicationContext(),fails,Toast.LENGTH_LONG).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("userid",userId);
                            getSupportFragmentManager().beginTransaction().replace(R.id.mylayout,new Validate()).commit();
                        }
                        else if (success.contains("failure"))
                        {
                            Toast.makeText(getApplicationContext(),fails,Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Something went wrong please try again",Toast.LENGTH_LONG).show();
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
              }
            );
            return null;
        }
    }
}

package com.video.aashi.school;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.post_class.Combo;
import com.video.aashi.school.adapters.post_class.PinVal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PinLogin extends AppCompatActivity {

    EditText userId,userPin,userMobile;
    CardView validate;
    ProgressDialog progressDialog;
    String u_pin,u_id,mobile;
    Retrofit retrofit,retrofits;
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public  static final   String NAME_PREF = "pinValidate";
    MyInterface loginInterface,loginInterfaces;
    boolean silent = true;
    String loginId,passwords;
    OkHttpClient clients;
    SharedPreferences sharedPreferencess;
    SharedPreferences.Editor editor2;
    public static final String PREF_NAME = "loginstatus";
    HttpLoggingInterceptor loggings = new HttpLoggingInterceptor();
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_pin_login);
     userId =(EditText)findViewById(R.id.userId);
     userPin =(EditText)findViewById(R.id.userPin);
     userMobile=(EditText)findViewById(R.id.mobileNum);
     validate=(CardView)findViewById(R.id.validate);
     sharedPreferencess = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE); // 0 - for private mode
        editor2 = sharedPreferencess.edit();

        sharedPreferences = getApplicationContext().getSharedPreferences(NAME_PREF, MODE_PRIVATE);
        editor =  sharedPreferences.edit();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        retrofit =   new Retrofit.Builder().baseUrl(APIUrl.PIN_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(client)
                .build();
        SharedPreferences settings = getSharedPreferences("com.example.xyz", 0);
        silent   = settings.getBoolean("switchkey", true);
        // Log.i("Tag","PinValidate"+  silent);
        loginInterface = retrofit.create(MyInterface.class);
        if (getSharedPreferences(NAME_PREF,0).getBoolean("isLogin",false)) {
            if (silent)
            {
                Intent i = new Intent(PinLogin.this, Navigation.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            else
            {
                Log.i("Tag","PinValidate"+silent);
                Intent intent= new Intent(PinLogin.this,Login.class);
                intent.putExtra("loginKey","0");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        }
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.getText().toString().isEmpty())
                {
                    userId.setError("Please enter userId");
                    userId.requestFocus();
                }
                else if (userPin.getText().toString().isEmpty())
                {
                    userPin.setError("Please enter pin");
                    userPin.requestFocus();
                }
                else if (userMobile.getText().toString().isEmpty())
                {
                    userMobile.setError("Please enter mobile number");
                    userMobile.requestFocus();
                }
                else
                {
                    new pinLogin().execute(this);
                }
            }
        });
    }



    public class pinLogin extends AsyncTask
    {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PinLogin.this);
            progressDialog.setMessage("Validating ......");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
       }
        @Override
        protected Object doInBackground(Object[] objects) {
            u_pin = userPin.getText().toString();
            mobile = userMobile.getText().toString();
            u_id =  userId.getText().toString();
            Call<ResponseBody> call = loginInterface.getPin(new PinVal(u_id,u_pin,mobile));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String bodyString = null;
                    try
                    {
                        bodyString  = response.body().string();
                        JSONObject object = new JSONObject(bodyString);

                        String validationUrl,organizationName,parentLoginId,parentName,errorMessage,
                                studentName,parentPin,parentMobile,userId;
                        errorMessage = object.getString("errorMessage");
                        if (errorMessage.equals("null"))
                        {
                            parentLoginId = object.getString("parentLoginId");
                            validationUrl = object.getString("validationUrl");
                            organizationName = object.getString("organizationName");
                            parentName = object.getString("parentName");
                            studentName = object.getString("studentName");
                            parentPin = object.getString("parentPin");
                            parentMobile = object.getString("parentMobile");
                            userId =object.getString("appUserId");
                            editor.putBoolean("isLogin",true);
                            editor.putString("loginId",parentLoginId);
                            editor.putString("url",validationUrl);
                            editor.putString("oName",organizationName);
                            editor.putString("parentName",parentName);
                            editor.putString("stuName",studentName);
                            editor.putString("parentPin",parentPin);
                            editor.putString("mobiles",parentMobile);
                            editor.putString("userId",userId);
                            editor.apply();
                            SharedPreferences settings =getSharedPreferences("com.example.xyz", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("switchkey", true);
                            editor.putString("myPin",parentPin);
                            SharedPreferences changetert =getSharedPreferences("text", 0);
                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editors = changetert.edit();
                            editors.putString("mytext","Remove Pin");
                            editors.apply();
                            editor.commit();
                            loginId = parentLoginId;
                            passwords =parentPin;
                            loggings.setLevel(HttpLoggingInterceptor.Level.BODY);
                            clients = new OkHttpClient.Builder().addInterceptor(logging).build();
                            retrofits =   new Retrofit.Builder().baseUrl(validationUrl +"rest/ParentLoginRestWS/").addConverterFactory
                                    (GsonConverterFactory.create())
                                    .client(clients)
                                    .build();
                            loginInterfaces = retrofits.create(MyInterface.class);
                            new LoginTask().execute();
                           // progressDialog.dismiss();
                           // Intent i=new Intent(PinLogin.this,Login.class);
                         //   i.putExtra("loginKey","0");
                           // i.putExtra("myPin",parentPin);
                           // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
                           // progressDialog.dismiss();
                        }
                    }
                    catch (IOException | JSONException e)
                    {
                        e.printStackTrace();
                    }

            }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
              //      Log.i("Tag","Failresponse"+ t.toString());
                }
            });

            return null;
        }
    }
    public class  LoginTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //  usernames = username1.getText().toString();


         //   Log.i("Tag","LogCredit"+passwords);
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mylogin", MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor1 = sharedPreferences.edit();

            editor1.apply();
            Call<ResponseBody> call = loginInterfaces.getLogin
                    (new com.video.aashi.school.adapters.post_class.Login(loginId,passwords));
            call.enqueue(new Callback<ResponseBody>()
            {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               //     Log.d("Tag", "Mybody"+ call.request().url() + loginId + passwords)  ;
                    String bodyString = null;
                    try {
                        bodyString  = response.body().string();
                        if(!bodyString.equals("{}") )
                        {
                            String  sstudentId,name,locationid,genid,classid,studentpic,acyearid,examid,classname,mobile;
                            String studentPhotoPath,passwor,users,fatherEmailId;
                            try
                            {
                                JSONObject object=new JSONObject(bodyString);
                                JSONObject jsonObject = object.getJSONObject("Student Data");
                                sstudentId = jsonObject.getString("studentId");
                                name = jsonObject.getString("studentFirstName");
                                locationid = jsonObject.getString("locationId");
                                genid = jsonObject.getString("currentClassGenId");
                                classid = jsonObject.getString("currentClassId");
                                studentpic = jsonObject.getString("studentPhotoPath");
                                acyearid = jsonObject.getString("currentAcademicYrId");
                                examid = jsonObject.getString("examTermGroupId");
                                classname =  jsonObject.getString("currentClassCd");
                                mobile = jsonObject.getString("mobileNo");
                                studentPhotoPath = jsonObject.getString("studentPhotoPath");
                                passwor = jsonObject.getString("parentPasswordDisp");
                                users = jsonObject.getString("studParentCode");
                                fatherEmailId = jsonObject.getString("fatherEmailId");
                                Log.i("Tag","LoginId"+acyearid);
                                editor2.putString("StudentId",sstudentId);
                                editor2.putString("S_name",name);
                                editor2.putString("location_id",locationid);
                                editor2.putString("gen_id",genid);
                                editor2.putString("class_id",classid);
                                editor2.putString("image",studentpic);
                                editor2.putString("academic",acyearid);
                                editor2.putBoolean("isLoginKey",true);
                                editor2.putString("examid",examid);
                                editor2.putString("classname",classname);
                                editor2.putString("mobile",mobile);
                                editor2.putString("photo",studentPhotoPath);
                                editor2.putString("fathers",fatherEmailId);
                                editor2.commit();
                                Intent i=new Intent(PinLogin.this,Navigation.class);
                                SharedPreferences settings =getSharedPreferences("com.example.xyz", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("myPin",passwords);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                              progressDialog.dismiss();
                              Toast.makeText(getApplicationContext(),"Enter valid pin..!!!",Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();

                    }

                    ResponseBody responseBody =  response.body();
                  //  Log.i("Tag","Respose" + call.request().url());
                    assert bodyString != null;

                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {


                }
            });
            return null;
        }
    }
}

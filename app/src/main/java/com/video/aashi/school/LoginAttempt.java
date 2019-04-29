package com.video.aashi.school;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.post_class.Imei;
import com.video.aashi.school.adapters.post_class.OtpGeneration;
import com.video.aashi.school.fragments.studentlist.StudentList;

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

import static android.os.Build.SERIAL;

public class LoginAttempt extends AppCompatActivity {
    SharedPreferences sharedPreferences, mysgared;
    SharedPreferences.Editor editor;
    public static String usernames, passwords;
    SharedPreferences loginCredit;
    ProgressDialog progressDialog;
    public static final String PREF_NAME = "loginstatus";
    public static String loginId, url, oName, parentName, studentName, parentPin;
    OkHttpClient client;
    String loginkey, pin, validationUrl,userId,mobileNo;
    String NAMEPREF = "pinValidate";
    OkHttpClient clients;
    EditText pinLogin;
    TextView loginText;
    CardView login;
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    Retrofit retrofits;
    MyInterface loginInterfaces;
    SharedPreferences.Editor editor2;
    TextView errorText;
    int count = 6;
    String imeis ;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    private TelephonyManager mTelephonyManager;
    Retrofit s;
    MyInterface myInterface;
    @Override
    protected void onDestroy() {
        progressDialog.dismiss();
        super.onDestroy();
    }

    @SuppressLint("CommitPrefEdits")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_attempt);
        sharedPreferences = getApplicationContext().getSharedPreferences(NAMEPREF, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        progressDialog = new ProgressDialog(LoginAttempt.this);
        pinLogin = (EditText) findViewById(R.id.validatePin);
        loginText = (TextView) findViewById(R.id.validateText);
        login = (CardView) findViewById(R.id.validateLogin);
        errorText = (TextView) findViewById(R.id.errorText);
        mysgared = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE); // 0 - for private mode
        editor2 = mysgared.edit();
        loginkey = sharedPreferences.getString("loginId", "");
        validationUrl = sharedPreferences.getString("url", "");
        userId =  sharedPreferences.getString("userId","");
        mobileNo = sharedPreferences.getString("mobiles","");
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        clients = new OkHttpClient.Builder().addInterceptor(logging).build();
        retrofits = new Retrofit.Builder().baseUrl(validationUrl + "rest/ParentLoginRestWS/").
                addConverterFactory
                        (GsonConverterFactory.create())
                .client(clients)
                .build();
        loginInterfaces = retrofits.create(MyInterface.class);
      //  new RequestOtp().execute();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginText.getText().toString(). equals("Request OTP"))
                {
                    s = new Retrofit.Builder().baseUrl(APIUrl.PIN_URL).
                            addConverterFactory
                                    (GsonConverterFactory.create())
                            .client(clients)

                            .build();
                    myInterface = s.create(MyInterface.class);
                    new RequestOtp().execute();

                }
                else
                {
                    if (pinLogin.getText().toString().isEmpty()) {

                        pinLogin.setError("Field is empty");

                    } else if (pinLogin.getText().toString().length() == 6) {
                        new LoginTask().execute();
                    } else {

                        pinLogin.setError("Enter valid pin");
                    }
                }


            }
        });
    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Please wait..");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

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
                    (new com.video.aashi.school.adapters.post_class.Login(loginkey, pinLogin.getText().toString()));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    String bodyString = null;
                    try {
                        bodyString = response.body().string();

                        String sstudentId, name, locationid, genid, classid, studentpic, acyearid, examid, classname, mobile;
                        String studentPhotoPath, passwor, users, fatherEmailId, session, multipleLogin;
                        String errorMessage;
                        try {
                            JSONObject object = new JSONObject(bodyString);
                            JSONObject jsonObject = object.getJSONObject("Student Data");
                            multipleLogin = jsonObject.getString("isMultipleStudent");
                            session = jsonObject.getString("mobSession");
                            errorMessage = jsonObject.getString("errorMessage");


                            if (errorMessage.contains("Invalid pin")) {
                                count--;
                                Log.d("Tag", "Mybody"+ call.request().url() +  errorMessage)  ;
                                errorText.setVisibility(View.VISIBLE    );
                                errorText.setText("Your entered pin is not valid..." + "\n" + " please try again... " + "\n" + " you have  " + String.valueOf(count) + "  attempts left ");

                                progressDialog.dismiss();
                                if (count == 0) {
                                    DeviceImei();
                                    progressDialog.dismiss();
                                }
                            }
                            else
                            {

                                if (multipleLogin.contains("Y"))
                                {
                                    Intent intent = new Intent(LoginAttempt.this, StudentList.class);
                                    editor.putBoolean("isLogin", true);
                                    editor2.putString("session", session);
                                    editor.putBoolean("MyStudents", true);
                                    SharedPreferences sharedPreferences ;
                                    sharedPreferences = getApplicationContext(). getSharedPreferences(
                                            "MySession",MODE_PRIVATE);
                                    SharedPreferences.Editor editors;
                                    editors = sharedPreferences.edit();
                                    editors.putBoolean("isLogins",false);
                                    editors.apply();
                                    editor.putString("parentPin",pinLogin.getText().toString());
                                    editor.apply();
                                    SharedPreferences settings = getSharedPreferences("com.example.xyz", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                   ///  editor.putBoolean("switchkey", true);
                                    editor.putString("myPin",pinLogin.getText().toString());
                                    editor.apply();
                                    Log.i("Tag","LoginAttemps" + session );
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                     progressDialog.dismiss();
                                     editor2.apply();
                                    startActivity(intent);
                                    finish();



                                }

                                else

                                {
                                    sstudentId = jsonObject.getString("studentId");
                                    name = jsonObject.getString("studentFirstName");
                                    locationid = jsonObject.getString("locationId");
                                    genid = jsonObject.getString("currentClassGenId");
                                    classid = jsonObject.getString("currentClassId");
                                    studentpic = jsonObject.getString("studentPhotoPath");
                                    acyearid = jsonObject.getString("currentAcademicYrId");
                                    examid = jsonObject.getString("examTermGroupId");
                                    classname = jsonObject.getString("currentClassCd");
                                    mobile = jsonObject.getString("mobileNo");
                                    studentPhotoPath = jsonObject.getString("studentPhotoPath");
                                    passwor = jsonObject.getString("parentPasswordDisp");
                                    String dob = jsonObject.getString("studentDobDisp");
                                    users = jsonObject.getString("studParentCode");
                                    fatherEmailId = jsonObject.getString("fatherEmailId");
                                    session = jsonObject.getString("mobSession");
                                    String s_name = jsonObject.getString("studentFirstName");
                                    String s_class = jsonObject.getString("currentClassCd");
                                    String academicyear = jsonObject.getString("currentAcademicYr");
                                    String city = jsonObject.getString("studentPlace");
                                    String gendre = jsonObject.getString("genderDisp");
                                    String middlename = jsonObject.getString("studentMiddleName");
                                    String lastnmame = jsonObject.getString("studentLastName");
                                    String fathersnme = jsonObject.getString("fatherName");
                                    String mobleno = jsonObject.getString("mobileNo");
                                    String pincode = jsonObject.getString("pincode");
                                    String state = jsonObject.getString("state");
                                    String age = jsonObject.getString("studentAge");
                                    String studentNationality =  jsonObject.getString("studentNationality");
                                    String motherName = jsonObject.getString("motherName");
                                    String registration = jsonObject.getString("registrationNo");
                                    String joineddate = jsonObject.getString("joiningDtDisp");
                                    String joinedyear = jsonObject.getString("joiningAcademicYr");
                                    String rollno = jsonObject.getString("rollNo");
                                    String bloodgroups = jsonObject.getString("studentBloodGroup");
                                    String schoolName = jsonObject.getString("locationName");
                                    Log.i("Tag", "LoginId" + acyearid);
                                    editor2.putString("StudentId", sstudentId);
                                    editor2.putString("S_name", name);
                                    editor2.putString("location_id", locationid);
                                    editor2.putString("gen_id", genid);
                                    editor2.putString("class_id", classid);
                                    editor2.putString("image", studentpic);
                                    editor2.putString("academic", acyearid);
                                    editor2.putBoolean("isLoginKey", true);
                                    editor2.putString("examid", examid);
                                    editor2.putString("classname", classname);
                                    editor2.putString("mobile", mobile);
                                    editor2.putString("photo", studentPhotoPath);
                                    editor2.putString("fathers", fatherEmailId);
                                    editor2.putString("session", session);
                                    editor2.putString("studentFirstName",s_name);
                                    editor2.putString("currentClassCd",s_class);
                                    editor2.putString("studentMiddleName",middlename);
                                    editor2.putString("studentLastName",lastnmame);
                                    editor2.putString("studentPlace",city);
                                    editor2.putString("state",state);
                                    editor2.putString("pincode",pincode);
                                    editor2.putString("mobileNo",mobleno);
                                    editor2.putString("motherName",motherName);
                                    editor2.putString("currentAcademicYr",academicyear);
                                    editor2.putString("genderDisp",gendre);
                                    editor2.putString("studentNationality",studentNationality);
                                    editor2.putString("fatherEmailId",fatherEmailId);
                                    editor2.putString("fatherName",fathersnme);
                                    editor2.putString("studentAge",age);
                                    editor2.putString("registrationNo",registration);
                                    editor2.putString("joiningDtDisp",joineddate);
                                    editor2.putString("joiningAcademicYr",joinedyear);
                                    editor2.putString("studentDobDisp",dob);
                                    editor2.putString("rollNo",rollno);
                                    editor2.putString("studentBloodGroup",bloodgroups);
                                    editor2.putString("schoolName",schoolName);
                                    editor.putBoolean("isLogin", true);
                                    editor.apply();
                                    editor2.apply();
                                    SharedPreferences sharedPreferences1;
                                    sharedPreferences1 = getApplicationContext(). getSharedPreferences("MySession",MODE_PRIVATE);
                                    SharedPreferences.Editor editors;
                                    editors = sharedPreferences1.edit();
                                    editors.putBoolean("isLogins",false);
                                    editors.apply();
                                    Intent i = new Intent(LoginAttempt.this, Navigation.class);
                                    SharedPreferences settings = getSharedPreferences("com.example.xyz", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                  // editor.putBoolean("switchkey", true);
                                    editor.putString("myPin", pinLogin.getText().toString());
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    progressDialog.dismiss();
                                    startActivity(i);

                                    finish();
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ResponseBody responseBody = response.body();
                    assert bodyString != null;
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Something went wronh..!!",Toast.LENGTH_SHORT).show();

                }
            });
            return null;
        }

        @SuppressLint("NewApi")
        void DeviceImei()
        {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
           new ImeiValidate().execute();
        }

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            new ImeiValidate().execute();
        }
    }


        class ImeiValidate extends AsyncTask {

            @SuppressLint({"HardwareIds", "MissingPermission"})
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onPreExecute() {
                mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                imeis = mTelephonyManager.getDeviceId();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {


                Call<ResponseBody> call  =   loginInterfaces.getImei(new Imei(loginkey,imeis));

                Log.d("msg", "DeviceImei " + imeis + loginkey);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            String bodyString = null;
                            try {
                                bodyString = response.body().string();
                                JSONObject object = new JSONObject(bodyString);
                                Log.i("Tag","LoginAttemps"+bodyString+loginkey);
                                String errorMessage,string;
                                string = object.getString("status");
                                errorMessage = object.getString("errorMessage");


                                if (string.contains("success"))
                                {
                                    errorText.setText(errorMessage);
                                    loginText.setText("Request OTP");
                                  //  login.setClickable(true);
                                }

                            } catch (IOException | JSONException   e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Semething went wrong..!! please try again..!!",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

                return null;

        }





    }
    class RequestOtp extends AsyncTask
    {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            Log.i("TAG","MyRequest"+loginkey+userId+mobileNo);
            Call<ResponseBody> respons = myInterface.otpGeneration(new OtpGeneration(loginkey,userId,mobileNo));

           respons.enqueue(new Callback<ResponseBody>() {
               @Override
               public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                   String bodyString = null;
                   try {
                       bodyString = response.body().string();
                       JSONObject object = new JSONObject(bodyString);
                       String errorMessage,status;
                       errorMessage = object.getString("errorMessage");
                       status = object.getString("status");
                       Log.i("TAG","MyRequests"+ errorMessage +loginkey+userId+mobileNo);

                       if (status.contains("success"))
                       {
                           loginText.setText("Login");
                           errorText.setVisibility(View.GONE);
                           Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
                           count = 6 ;
                        //   login.setClickable(true);
                       }
                   }
                   catch (JSONException | IOException  e ) {
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

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.post_class.Imei;
import com.video.aashi.school.adapters.post_class.OtpGeneration;
import com.video.aashi.school.fragments.Settings;
import com.video.aashi.school.fragments.studentlist.StudentList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    TextInputLayout username,password;
    EditText username1,password1;
    CardView signin;
    TextView signup;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor,getEditor;
    boolean error = false;
    MyInterface loginInterface;
    Retrofit retrofit;
    public  static String usernames,passwords;
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    TextView forgetpass;
    SharedPreferences loginCredit;
    android.support.v7.widget.Toolbar toolbar;
    EditText pName,orName,cName;
    ProgressDialog progressDialog;
    public static final String PREF_NAME = "loginstatus";
    public static String loginId,url,oName,parentName,studentName,parentPin;
    OkHttpClient client;
    String loginkey;
    TextView errorText;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    String imeis;
    private TelephonyManager mTelephonyManager;
    Retrofit s;
    MyInterface myInterface;
    int count = 6;
    TextView loginText;
    String userId,mobileNo;
    Retrofit retrofits;
    MyInterface loginInterfaces;
    @Override
    protected void onDestroy() {
        progressDialog.dismiss();
        super.onDestroy();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(Login.this);
        username = (TextInputLayout) findViewById(R.id.username);
        password =(TextInputLayout) findViewById(R.id.password);
        //username1 =(EditText) findViewById(R.id.username1);
        password1 = (EditText) findViewById(R.id.password1);
        signin =(CardView)findViewById(R.id.signin);
        loginText =(TextView)findViewById(R.id.logintexts);
        signup =(TextView)findViewById(R.id.signup);
        forgetpass=(TextView)findViewById(R.id.forgetpass);
        toolbar =(android.support.v7.widget.Toolbar)findViewById(R.id.loginTool);
        pName =(EditText)findViewById(R.id.parentName);
        orName =(EditText)findViewById(R.id.schoolName);
        cName=(EditText)findViewById(R.id.childName);
        sharedPreferences = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE); // 0 - for private mode
        editor = sharedPreferences.edit();
        errorText = (TextView) findViewById(R.id.errorMessage);
        loginCredit = getSharedPreferences("pinValidate",MODE_PRIVATE);
        getEditor  = loginCredit.edit();
        loginId = loginCredit.getString("loginId","");
        url = loginCredit.getString(  "url","");
        oName =  loginCredit.getString("oName","");
        parentName = loginCredit.getString("parentName","");
        studentName = loginCredit.getString("stuName","");
        parentPin = loginCredit.getString("parentPin","");
        userId =  loginCredit.getString("userId","");
        mobileNo = loginCredit.getString("mobiles","");
        toolbar.setTitle(oName);
        pName.setText(parentName);
        orName.setText(oName);
        cName.setText(studentName);
        cName.setEnabled(false);
        pName.setEnabled(false);
        orName.setEnabled(false);

       loginkey = getIntent().getStringExtra("loginKey");

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
         client = new OkHttpClient.Builder().addInterceptor(logging).build();
        retrofit =   new Retrofit.Builder().baseUrl(url+"rest/ParentLoginRestWS/").addConverterFactory
                (GsonConverterFactory.create())
                .client(client)
                .build();
        loginInterface = retrofit.create(MyInterface.class);
        loginInterfaces = retrofit.create(MyInterface.class);
        forgetpass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Login.this,ForgetPass.class));
            }
        });
        if (getSharedPreferences("com.example.xyz",0).getBoolean("switchkey",true)) {

          //  if (loginkey.equals("0"))
           // {
           //     passwords = parentPin;
           //     new LoginTask().execute();
          //  }

        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginText.getText().toString(). equals("Request OTP"))
                {
                    s = new Retrofit.Builder().baseUrl(APIUrl.PIN_URL).
                            addConverterFactory
                                    (GsonConverterFactory.create())
                            .build();
                    myInterface = s.create(MyInterface.class);
                    new RequestOtp().execute();

                }
                else
                {
                    submitForm();
                }

            }
        });
    }

    private void submitForm() {
        if (!validatePassword()) {
            return;
        }

        new LoginTask().execute();
    }
    private boolean validateEmail() {
        String email = username1.getText().toString().trim();
        if (email.isEmpty() ){
            username1.setError(getString(R.string.err_msg_email));
            username1.requestFocus();
            return false;
        } else {
            username.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validatePassword() {
        if (password1.getText().toString().trim().isEmpty()) {
            password1.requestFocus();
            password1.setError("Please enter password");
            return false;
        }
        else
        {
            password.setErrorEnabled(false);
        }

        return true;
    }

  public class  LoginTask extends AsyncTask<Void,Void,Void>
  {

      @Override
      protected void onPreExecute()
      {
          progressDialog.setMessage("Loading");
          progressDialog.show();
          progressDialog.setCancelable(false);
          super.onPreExecute();
      }
      @Override
      protected void onPostExecute(Void aVoid) {
          progressDialog.dismiss();
          super.onPostExecute(aVoid);
      }
      @Override
      protected Void doInBackground(Void... voids) {
          passwords = password1.getText().toString().trim();
          Log.i("Tag","LogCredit"+passwords+loginId);
          SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mylogin", MODE_PRIVATE);
          @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor1 = sharedPreferences.edit();
          editor1.apply();
          Call<ResponseBody> call = loginInterface.getLogin
          (new com.video.aashi.school.adapters.post_class.Login(loginId,passwords));
          call.enqueue(new Callback<ResponseBody>()
          {
              @Override
              public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                  String bodyString = null;
                  try {
                      bodyString  = response.body().string();

                         String  sstudentId,name,locationid,genid,classid,studentpic,acyearid,examid,classname,mobile;
                          String studentPhotoPath,passwor,users,fatherEmailId,active;
                          String errorMessage;
                          try
                          {
                              JSONObject object=new JSONObject(bodyString);
                              JSONObject jsonObject = object.getJSONObject("Student Data");


                              String  multipleLogin = jsonObject.getString("isMultipleStudent");
                             String  session = jsonObject.getString("mobSession");
                              errorMessage = jsonObject.getString("errorMessage");
                              Log.d("Tag", "Mybody"+ call.request().url()+ errorMessage )  ;


                              if (errorMessage.contains("Invalid pin")) {
                                  count--;
                                  errorText.setVisibility(View.VISIBLE    );
                                  errorText.setText("Your entered pin is not valid..." + "\n" + " please try again... " + "\n" + " you have  " + String.valueOf(count) + "  attempts left ");
                                  progressDialog.dismiss();
//                                  Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();

                                  if (count == 0) {
                                      DeviceImei();
                                      progressDialog.dismiss();
                                  }
                              }
                              else {
                                  if (multipleLogin.contains("Y")) {
                                      Intent intent = new Intent(Login.this, StudentList.class);
                                      editor.putString("session", session);
                                      SharedPreferences settings = getSharedPreferences("com.example.xyz", 0);
                                      SharedPreferences.Editor editors = settings.edit();
                                      editors.putString("myPin", passwords);
                                      editor.apply();
                                      editors.apply();
                                      startActivity(intent);
                                  } else {
                                      sstudentId = jsonObject.getString("studentId");
                                      active = jsonObject.getString("active");
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
                                      users = jsonObject.getString("studParentCode");
                                      fatherEmailId = jsonObject.getString("fatherEmailId");
                                      Log.i("Tag", "LoginId" + acyearid);
                                      editor.putString("StudentId", sstudentId);
                                      editor.putString("S_name", name);
                                      editor.putString("location_id", locationid);
                                      editor.putString("gen_id", genid);
                                      editor.putString("class_id", classid);
                                      editor.putString("image", studentpic);
                                      editor.putString("academic", acyearid);
                                      editor.putBoolean("isLoginKey", true);
                                      editor.putString("examid", examid);
                                      editor.putString("classname", classname);
                                      editor.putString("mobile", mobile);
                                      editor.putString("photo", studentPhotoPath);
                                      editor.putString("fathers", fatherEmailId);

                                      editor.putString("session", session);
                                      editor.apply();
                                     // editor.apply();
                                      Intent i = new Intent(Login.this, Navigation.class);
                                      SharedPreferences settings = getSharedPreferences("com.example.xyz", 0);
                                      SharedPreferences.Editor editors = settings.edit();
                                      editors.putString("myPin", passwords);
                                      editors.apply();
                                      Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();

                                      i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                      startActivity(i);
                                      finish();
                                  }
                              }



                          }
                          catch (Exception e)
                          {

                              e.printStackTrace();
                          }

                  } catch (IOException e) {
                      e.printStackTrace();

                  }

                  ResponseBody responseBody =  response.body();
                  Log.i("Tag","Resposes" + call.request().url());
                  assert bodyString != null;

              }
              @Override
              public void onFailure(Call<ResponseBody> call, Throwable t) {


              }
          });
          return null;
      }
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
class RequestOtp extends AsyncTask {
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
//        Log.i("TAG","MyRequest"+loginkey+userId+mobileNo);
        Call<ResponseBody> respons = myInterface.otpGeneration(new OtpGeneration(loginkey, userId, mobileNo));

        respons.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String bodyString = null;
                try {
                    bodyString = response.body().string();
                    JSONObject object = new JSONObject(bodyString);
                    String errorMessage, status;
                    errorMessage = object.getString("errorMessage");
                    status = object.getString("status");
                    // Log.i("TAG","MyRequests"+ errorMessage +loginkey+userId+mobileNo);

                    if (status.contains("success")) {
                        loginText.setText("Login");
                        errorText.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        count = 6;
                        //   login.setClickable(true);
                    }
                } catch (JSONException | IOException e) {
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

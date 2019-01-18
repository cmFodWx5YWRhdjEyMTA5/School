package com.video.aashi.school;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.video.aashi.school.adapters.Interfaces.MyInterface;

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
    SharedPreferences.Editor editor;
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
        signup =(TextView)findViewById(R.id.signup);
        forgetpass=(TextView)findViewById(R.id.forgetpass);
        toolbar =(android.support.v7.widget.Toolbar)findViewById(R.id.loginTool);
        pName =(EditText)findViewById(R.id.parentName);
        orName =(EditText)findViewById(R.id.schoolName);
        cName=(EditText)findViewById(R.id.childName);
        sharedPreferences = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE); // 0 - for private mode
        editor = sharedPreferences.edit();
        loginCredit = getSharedPreferences("pinValidate",MODE_PRIVATE);
        loginId = loginCredit.getString("loginId","");
        url = loginCredit.getString(  "url","");
        oName =  loginCredit.getString("oName","");
        parentName = loginCredit.getString("parentName","");
        studentName = loginCredit.getString("stuName","");
        parentPin = loginCredit.getString("parentPin","");
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
                submitForm();
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
                  Log.d("Tag", "Mybody"+ call.request().url())  ;
                  String bodyString = null;
                  try {
                      bodyString  = response.body().string();
                      if(!bodyString.equals("{}") )
                      {
                          String  sstudentId,name,locationid,genid,classid,studentpic,acyearid,examid,classname,mobile;
                          String studentPhotoPath,passwor,users,fatherEmailId,active;
                          try
                          {
                              JSONObject object=new JSONObject(bodyString);
                              JSONObject jsonObject = object.getJSONObject("Student Data");
                              sstudentId = jsonObject.getString("studentId");
                              active = jsonObject.getString("active");
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
                              editor.putString("StudentId",sstudentId);
                              editor.putString("S_name",name);
                              editor.putString("location_id",locationid);
                              editor.putString("gen_id",genid);
                              editor.putString("class_id",classid);
                              editor.putString("image",studentpic);
                              editor.putString("academic",acyearid);
                              editor.putBoolean("isLoginKey",true);
                              editor.putString("examid",examid);
                              editor.putString("classname",classname);
                              editor.putString("mobile",mobile);
                              editor.putString("photo",studentPhotoPath);
                              editor.putString("fathers",fatherEmailId);

                              if (!active.equals(""))
                              {
                                  editor.apply();
                                  Intent i=new Intent(Login.this,Navigation.class);
                                  SharedPreferences settings =getSharedPreferences("com.example.xyz", 0);
                                  SharedPreferences.Editor editor = settings.edit();
                                  editor.putString("myPin",passwords);
                                  editor.apply();
                                  Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                                  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                  startActivity(i);
                                  finish();
                              }
                              else
                              {
                                  Toast.makeText(getApplicationContext(),"Enter valid pin..!!!",Toast.LENGTH_LONG).show();
                              }


                          }
                          catch (Exception e)
                          {

                              e.printStackTrace();
                          }
                      }
                      else
                      {

                          Toast.makeText(getApplicationContext(),"Enter valid pin..!!!",Toast.LENGTH_LONG).show();
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
}

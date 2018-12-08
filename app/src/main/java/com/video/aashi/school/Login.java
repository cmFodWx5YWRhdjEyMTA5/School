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

import com.video.aashi.school.adapters.Interfaces.MyInterface;

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

    public static final String PREF_NAME = "loginstatus";;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //loginInterface = ApiClient.getApiCLient().create(MyInterface.class);
        username = (TextInputLayout) findViewById(R.id.username);
        password =(TextInputLayout) findViewById(R.id.password);
        username1 =(EditText) findViewById(R.id.username1);
        password1 = (EditText) findViewById(R.id.password1);
        signin =(CardView)findViewById(R.id.signin);
        signup =(TextView)findViewById(R.id.signup);
        forgetpass=(TextView)findViewById(R.id.forgetpass);
        sharedPreferences = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE); // 0 - for private mode
        editor = sharedPreferences.edit();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        retrofit =   new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(client)

                .build();
        loginInterface = retrofit.create(MyInterface.class);
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgetPass.class));
            }
        });


        if (getSharedPreferences(PREF_NAME,0).getBoolean("isLoginKey",false)) {
            Intent intent= new Intent(Login.this,Navigation.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                submitForm();

            }
        });
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }

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
        } else {
            password.setErrorEnabled(false);
        }

        return true;
    }

  public class  LoginTask extends AsyncTask<Void,Void,Void>
  {

      ProgressDialog progressDialog;


      @Override
      protected void onPreExecute() {


          progressDialog = new ProgressDialog(Login.this);
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


         usernames = username1.getText().toString();
         passwords = password1.getText().toString().trim();

         SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mylogin", MODE_PRIVATE);
         @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor1 = sharedPreferences.edit();
         editor1.putString("user",usernames);
         editor1.putString("pass",passwords);
         editor1.apply();

          Call<ResponseBody> call = loginInterface.LoginValidation(usernames,passwords);
          call.enqueue(new Callback<ResponseBody>()
          {
              @Override
              public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                  String bodyString = null;
                  try {
                      bodyString  = response.body().string();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                  Log.d("Tag", "Body"+ bodyString)  ;
                  ResponseBody responseBody =  response.body();
                  Log.i("Tag","Respose" + call.request().url());
                  assert bodyString != null;
                  if(!bodyString.equals("{}"))
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
                          editor.commit();
                          Intent i=new Intent(Login.this,Navigation.class);
                          i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                          startActivity(i);
                          finish();
                          Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                          // progressDialog.dismiss();
                      }

                      catch (Exception e)
                      {
                          e.printStackTrace();
                      }

                  }
                  else
                  {
                      //    progressDialog.dismiss();
                      Toast.makeText(getApplicationContext(),"Username or password Mismatch",Toast.LENGTH_LONG).show();

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

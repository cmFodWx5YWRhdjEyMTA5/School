package com.video.aashi.school;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.video.aashi.school.adapters.Interfaces.CircleTransform;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.Students;
import com.video.aashi.school.adapters.post_class.Login;
import com.video.aashi.school.fragments.HomePage;
import com.video.aashi.school.fragments.studentlist.StudentList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ProfileView extends Fragment {

    Retrofit retrofit;
    ImageView profileimg;
    MyInterface loginInterfaces;
    MyInterface loginInterface;
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
  SharedPreferences sharedPreferences,sharedPreferencess,myPref;
  String username,password,sessions;
  int positions;

  String s_name,s_class,academicyear,city,gendre,middlename,lastnmame,fathersnme,dob,mobleno,pincode,state,
          fatherEmailId,studentNationality,motherName,age;

  String registration,joineddate,joinedyear,rollno;
    ArrayList<Students> students;

   TextView tusername,tClass,tAcademic,tCity,tCitys,tState,tPincode,tMiddle,tLast,tMother,tFather,tFirst,tMobile,tGendre;
   TextView   registation,joinedyears,joineddates ,ages,dobs,rollnumber,bloodgroup;
    android.support.v7.widget.Toolbar toolbar;
    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_profile_view, container, false);
        toolbar=(android.support.v7.widget.Toolbar)getActivity(). findViewById(R.id.toolbar);
        students = new ArrayList<>();
        tFirst =(TextView)view. findViewById(R.id.firstname);
        tusername =(TextView)view.findViewById(R.id.usernames);
        tClass =(TextView)view.findViewById(R.id.classid);
        tAcademic =(TextView)view.findViewById(R.id.academcyear);
        tCitys =(TextView)view.findViewById(R.id.citys);
        tCity =(TextView)view.findViewById(R.id.mycity);
        tGendre =(TextView)view.findViewById(R.id.gendre);
        tState =(TextView)view.findViewById(R.id.state);
        tPincode =(TextView)view.findViewById(R.id.pincode);
        tMiddle =(TextView)view.findViewById(R.id.middlenme);
        tLast =(TextView)view.findViewById(R.id.last_name);
        tMother =(TextView)view.findViewById(R.id.mothersname);
        tFather =(TextView)view.findViewById(R.id.fathersname);
        tMobile =(TextView)view.findViewById(R.id.mobiles);
        bloodgroup =view.findViewById(R.id.bloodgroup);
        ages =(TextView)view.findViewById(R.id.age);
        dobs=(TextView)view.findViewById(R.id.dobs);
        rollnumber =(TextView)view.findViewById(R.id.rollno);
        joineddates=(TextView)view.findViewById( R.id.joineddate);
        joinedyears=(TextView)view.findViewById(R.id.joined);
        registation=(TextView)view.findViewById(R.id.registration);
        profileimg =(ImageView)view.findViewById(R.id.profileImg);
        sharedPreferencess = getActivity().getSharedPreferences(StudentList.PREF_NAME, MODE_PRIVATE);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        myPref = getActivity().getSharedPreferences("pinValidate",MODE_PRIVATE);
        sharedPreferences =getActivity(). getSharedPreferences("mylogin",MODE_PRIVATE);
        username = myPref.getString("loginId","");
        password = myPref.getString("parentPin","") ;
        sessions = myPref.getString("Sessions","");
        positions = sharedPreferencess.getInt("student",0);
        toolbar.setTitle("Profile");
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        retrofit =   new Retrofit.Builder().baseUrl(HomePage.url).addConverterFactory
                (GsonConverterFactory.create())
                .client(client)
                .build();


        s_name = sharedPreferencess.getString("studentFirstName","");
        s_class = sharedPreferencess.getString("currentClassCd","");
        middlename = sharedPreferencess.getString("studentMiddleName","");
        lastnmame = sharedPreferencess.getString("studentLastName","");
        city = sharedPreferencess.getString("studentPlace","");
        state = sharedPreferencess.getString("state","");
        pincode = sharedPreferencess.getString("pincode","");
        mobleno = sharedPreferencess.getString("mobileNo","");
        motherName = sharedPreferencess.getString("motherName","");
        academicyear = sharedPreferencess.getString("currentAcademicYr","");
        gendre = sharedPreferencess.getString("genderDisp","");
        studentNationality =  sharedPreferencess.getString("studentNationality","");
        fatherEmailId = sharedPreferencess.getString("fatherEmailId","");
        fathersnme = sharedPreferencess.getString("fatherName","");
        age = sharedPreferencess.getString("studentAge","");
        registration = sharedPreferencess.getString("registrationNo","");
        joineddate = sharedPreferencess.getString("joiningDtDisp","");
        joinedyear = sharedPreferencess.getString("joiningAcademicYr","");
        dob = sharedPreferencess.getString("studentDobDisp","");
        rollno = sharedPreferencess.getString("rollNo","");
        String bloodgroups = sharedPreferencess.getString("studentBloodGroup","");
        Log.i("Tag", "MyProfiles"+ s_name+motherName+lastnmame+middlename+city);
        tusername.setText(s_name);
        tClass.setText("( " + s_class+" )");
        tAcademic.setText(academicyear);
        tCitys.setText(city);
        tCity.setText(city);
        tState.setText(state + " -");
        tPincode.setText( pincode);
        tMiddle.setText(middlename);
        tLast.setText(lastnmame);
        tMother.setText(motherName);
        tFather.setText(fathersnme);
        tGendre.setText(gendre);
        tMobile.setText(mobleno);
        tFirst.setText(s_name);
        joineddates.setText(joineddate);
        joinedyears.setText(joinedyear);
        registation.setText(registration);
        ages.setText(age+" Yrs");
        dobs.setText(dob);
        rollnumber.setText(rollno);
        bloodgroup.setText(bloodgroups);
        Picasso.get()
                .load(HomePage.urls+ HomePage.image)
                .error(R.drawable.badge )
                .into(profileimg);
        loginInterface = retrofit.create(MyInterface.class);
           // Log.i("Tag","Mylogin"+username+password);
//         new LoadProfile().execute();
        return view ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        return super.onOptionsItemSelected(item);
    }

    class LoadProfile extends AsyncTask
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            retrofit2.Call<ResponseBody> call = loginInterface.getLogin(new Login(username,password));
            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    String bodyString = null;
                    try {
                        bodyString  = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Tag", "MyProfile"+ bodyString);
                    if(!bodyString.equals("{}"))
                    {
                        try {

                            JSONObject object = new JSONObject(bodyString);
                            JSONObject jsonObject = object.getJSONObject("Student Data");
                            String isMultipleStudent = jsonObject.getString("isMultipleStudent");

                            if (isMultipleStudent.contains("Y"))
                            {
                                new LoadStudent().execute();
                                progressDialog.dismiss();

                            }
                            else
                            {


                            s_name = jsonObject.getString("studentFirstName");
                            s_class = jsonObject.getString("currentClassCd");
                            middlename = jsonObject.getString("studentMiddleName");
                            lastnmame = jsonObject.getString("studentLastName");
                            city = jsonObject.getString("studentPlace");
                            state = jsonObject.getString("state");
                            pincode = jsonObject.getString("pincode");
                            mobleno = jsonObject.getString("mobileNo");
                            motherName = jsonObject.getString("motherName");
                            academicyear = jsonObject.getString("currentAcademicYr");
                            gendre = jsonObject.getString("genderDisp");
                            studentNationality =  jsonObject.getString("studentNationality");
                            fatherEmailId = jsonObject.getString("fatherEmailId");
                            fathersnme = jsonObject.getString("fatherName");
                            age = jsonObject.getString("studentAge");
                            registration = jsonObject.getString("registrationNo");
                            joineddate = jsonObject.getString("joiningDtDisp");
                            joinedyear = jsonObject.getString("joiningAcademicYr");
                            dob = jsonObject.getString("studentDobDisp");
                            rollno = jsonObject.getString("rollNo");
                            String bloodgroups = jsonObject.getString("studentBloodGroup");
                            Log.i("Tag", "MyProfiles"+ s_name+motherName+lastnmame+middlename+city);
                            tusername.setText(s_name);
                            tClass.setText("( " + s_class+" )");
                            tAcademic.setText(academicyear);
                            tCitys.setText(city);
                            tCity.setText(city);
                            tState.setText(state + " -");
                            tPincode.setText( pincode);
                            tMiddle.setText(middlename);
                            tLast.setText(lastnmame);
                            tMother.setText(motherName);
                            tFather.setText(fathersnme);
                            tGendre.setText(gendre);
                            tMobile.setText(mobleno);
                            tFirst.setText(s_name);
                            joineddates.setText(joineddate);
                            joinedyears.setText(joinedyear);
                            registation.setText(registration);
                            ages.setText(age+" Yrs");
                            dobs.setText(dob);
                            rollnumber.setText(rollno);
                            bloodgroup.setText(bloodgroups);
                            progressDialog.dismiss();
                        }

                        }
                        catch (JSONException e)

                        {
                            e.printStackTrace();
                        }


                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Something went wrong!!!",Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

                }
            });
            return null;
        }
    }

    class LoadStudent extends AsyncTask
    {

        MyInterface myInterface;
        @Override
        protected void onPreExecute() {

            Retrofit retrofit1;

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

            retrofit1 = new Retrofit.Builder().baseUrl(HomePage.url).addConverterFactory
                    (GsonConverterFactory.create())
                    .client(defaulthttpClient)
                    .build();
            myInterface = retrofit1.create(MyInterface.class);
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            retrofit2.Call<ResponseBody> call =
                    myInterface.getStudentList(new com.video.aashi.school.adapters.post_class.StudentList(
                    username,password,sessions));


            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i("Tag","Mylogin "+ Navigation.session +  "Session 2 :" + sessions  );
                    if (response.isSuccessful()) {
                        try {
                            String bodyString = null;
                            bodyString = response.body().string();
                            Log.i("Tag","MyStudentListss"+bodyString);
                            JSONObject object = new JSONObject(bodyString);
                            JSONArray list = object.getJSONArray("Student List");
                            for (int i = 0;i<list.length();i++)
                            {
                                if (list.length() ==0)
                                {

                                }
                                else
                                {


                                }
                            }
                        }
                        catch (IOException  | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Something went wrong..!",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

                }
            });
            return null;
        }
    }


}

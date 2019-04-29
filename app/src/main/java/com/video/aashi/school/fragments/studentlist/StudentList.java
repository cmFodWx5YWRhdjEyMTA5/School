package com.video.aashi.school.fragments.studentlist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.video.aashi.school.APIUrl;
import com.video.aashi.school.LoginAttempt;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.PinLogin;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.CircleTransform;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.Students;
import com.video.aashi.school.adapters.post_class.Howork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentList extends AppCompatActivity {

    SharedPreferences loginCredit,sessions,mySessions;
    Retrofit retrofit;
    MyInterface myInterfacee;
    GridView gridView;
    SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "loginstatus";
    String session,parentPin,loginId;
    public static  String url;
    ArrayList<Students> students;
    SharedPreferences.Editor editor2;
    SharedPreferences.Editor editor,ses;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
    //    sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
        loginCredit = getSharedPreferences("pinValidate",MODE_PRIVATE);
        sessions = getSharedPreferences(PinLogin.PREF_NAME,MODE_PRIVATE);
        editor =  loginCredit.edit();
        mySessions = getSharedPreferences("MySession",MODE_PRIVATE);
        ses =  mySessions.edit();
        parentPin = loginCredit.getString("parentPin","");
        loginId = loginCredit.getString("loginId","");
        url = loginCredit.getString(  "url","");
        session = sessions.getString("session","");
        sharedPreferences = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE); // 0 - for private mode
        editor2 = sharedPreferences.edit();
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

        retrofit = new Retrofit.Builder().baseUrl(url+"rest/ParentLoginRestWS/").addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        myInterfacee = retrofit.create(MyInterface.class);
        gridView=(GridView)findViewById(R.id.studentList);
        new getStudent().execute();
        Log.i("Tag","MyStudentList"+loginId+ parentPin+ session+url);
     }
    public  class  getStudent extends AsyncTask
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(StudentList.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Plese wait..!");
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            Call<ResponseBody> call = myInterfacee.getStudentList(new com.video.aashi.school.adapters.post_class.StudentList(
                   loginId,parentPin,session));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    students = new ArrayList<>();
                    if (response.isSuccessful()) {
                        try {
                            String  sstudentId,locationid,genid,classid,studentpic,acyearid,examid,classname,mobile;
                            String studentPhotoPath,passwor,users,fatherEmailId,active,sessionss;
                            String age,name,image,dob,classes,rolno;
                            String bodyString = null;
                            bodyString = response.body().string();
                            Log.i("Tag","MyStudentLists"+loginId+parentPin+session+bodyString);
                            JSONObject object = new JSONObject(bodyString);
                           String failure = object.getString("status");
                            String errorMessage = object.getString("errorMessage");
                            if  (failure.contains("failure"))
                            {

                                AlertDialog.Builder builder = new AlertDialog.Builder(StudentList.this );
                                builder.setMessage(errorMessage)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                editor2.clear();
                                                ses.putBoolean("isLogins",true);
                                                editor2.apply();
                                                ses.apply();
                                                Intent i = new Intent(StudentList.this,LoginAttempt.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                            }
                                        });


                                AlertDialog alert = builder.create();
                                alert.setIcon(R.drawable.ic_error_outline_red_500_24dp);
                                alert.setCancelable(false);
                                alert.show();
                            //    Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();

                            }
                            else
                            {


                            JSONArray list = object.getJSONArray("Student List");
                            for (int i = 0;i<list.length();i++)
                            {
                                   if (list.length() ==0)
                                   {
                                       progressDialog.dismiss();
                                       Toast.makeText(getApplicationContext(),"No students available",Toast.LENGTH_SHORT).show();
                                   }
                                   else
                                   {
                                       JSONObject jsonObject = list.getJSONObject(i);
                                       age = jsonObject.getString("studentAge");
                                       name = jsonObject.getString("studentFirstName");
                                       image = jsonObject.getString("studentPhotoPath");
                                       dob = jsonObject.getString("studentDobDisp");
                                       classes = jsonObject.getString("currentClassGenCd");
                                       rolno = jsonObject.getString("rollNo");
                                       sstudentId = jsonObject.getString("studentId");
                                       locationid = jsonObject.getString("locationId");
                                       genid = jsonObject.getString("currentClassGenId");
                                       classid = jsonObject.getString("currentClassId");
                                       acyearid = jsonObject.getString("currentAcademicYrId");
                                       examid = jsonObject.getString("examTermGroupId");
                                       classname =  jsonObject.getString("currentClassCd");
                                       mobile = jsonObject.getString("mobileNo");
                                       passwor = jsonObject.getString("parentPasswordDisp");
                                       users = jsonObject.getString("studParentCode");
                                       fatherEmailId = jsonObject.getString("fatherEmailId");
                                       sessionss = jsonObject.getString("mobSession");
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
                                       String studentNationality =  jsonObject.getString("studentNationality");
                                       String motherName = jsonObject.getString("motherName");
                                       String registration = jsonObject.getString("registrationNo");
                                       String joineddate = jsonObject.getString("joiningDtDisp");
                                       String joinedyear = jsonObject.getString("joiningAcademicYr");
                                       String rollno = jsonObject.getString("rollNo");
                                       String bloodgroups = jsonObject.getString("studentBloodGroup");
                                       String sc_name = jsonObject.getString("locationName");
                                       students .add(new Students(name,image,rolno,age,classes,dob,sstudentId,locationid,genid,classid,acyearid
                                             ,examid,classname,mobile,passwor,users,fatherEmailId,sessionss,s_name,s_class,academicyear,city,gendre,
                                             middlename,lastnmame,fathersnme,mobleno,pincode,state,fatherEmailId, studentNationality,motherName,registration,joineddate,joinedyear
                                       ,rollno,bloodgroups ,sc_name));
                                       gridView.setAdapter(new StudentAdaper(getApplicationContext(),students));
                                       progressDialog.dismiss();
                                   }
                               }
                            }
                          }
                            catch (IOException  | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Something went wrong..!",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Something went wrong..!",Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            super.onPostExecute(o);
        }
    }
    public class StudentAdaper extends BaseAdapter
    {
        Context context;
        ArrayList<Students> students;
        private   StudentAdaper(Context context,ArrayList<Students> students)
        {
            this.context = context;
            this.students =students;
        }


        @Override
        public int getCount() {
            return students.size();
        }


        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.O_MR1)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;
            if (row == null) {
                LayoutInflater inflater  =getLayoutInflater();
                row = inflater.inflate(R.layout.studentdesign, parent, false);
                holder = new ViewHolder();
                holder.studentname =(TextView)row.findViewById(R.id.stuName);
                holder.classes =(TextView)row.findViewById(R.id.stuClass);
                holder.rollno =(TextView)row.findViewById(R.id.stuRoll);
                holder.age =(TextView)row.findViewById(R.id.stuAge);
                holder.dob =(TextView)row.findViewById(R.id.studob);
                holder.image =(ImageView)row.findViewById(R.id.stuImg);
                holder.mystudent =(CardView)row.findViewById(R.id.myStudents);
                row.setTag(holder);
            } else

                {
                holder = (ViewHolder) row.getTag();
                 }
                 Students studentss = students.get(position);

                holder.studentname.setText(students.get(position).getName());
                holder.dob.setText(students.get(position).getDob());
                holder.age.setText(students.get(position).getAge());
                if (!students.get(position).getRollno().equals(""))
                {
                    holder.rollno.setText(students.get(position).getBlood_group());
                }
                holder.classes.setText(students.get(    position).getClassses());
             //   students.get(position).getImage().equals("");
                Picasso.get()
                        .load( url + students.get(position).getImage())
                        .error(R.drawable.download )
                        .transform(new CircleTransform())
                        .into(holder.image);

                holder.mystudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor2.putString("StudentId", studentss.getStudentId());
                        editor2.putString("S_name",studentss.getName());
                        editor2.putString("location_id",studentss.getLocationId());
                        editor2.putString("gen_id",studentss.getGenId());
                        editor2.putString("class_id",studentss.getClassId());
                        editor2.putString("image", studentss.getImage());
                        editor2.putString("academic",studentss.getAcayesr());
                        editor2.putString("examid",studentss.getExamId());
                        editor2.putString("classname"  , studentss.getClassname());
                        editor2.putString("mobile", studentss.getMobile());
                        editor2.putString("photo",studentss.getImage());
                        editor2.putString("fathers", studentss.getFatherId());
                        editor2.putString("session",session);
                        editor2.putInt("student",position);
                        editor2.putString("studentFirstName",studentss.getName());
                        editor2.putString("currentClassCd",studentss.getS_class());
                        editor2.putString("studentMiddleName",studentss.getMiddlename());
                        editor2.putString("studentLastName",studentss.getLastnmame());
                        editor2.putString("studentPlace",studentss.getCity());
                        editor2.putString("state",studentss.getState());
                        editor2.putString("pincode",studentss.getPincode());
                        editor2.putString("mobileNo",studentss.getMobleno());
                        editor2.putString("motherName",studentss.getMotherName());
                        editor2.putString("currentAcademicYr",studentss.getAcademicyear());
                        editor2.putString("genderDisp",studentss.getGendre());
                        editor2.putString("studentNationality",studentss.getStudentNationality());
                        editor2.putString("fatherEmailId",studentss.getFatherEmailId());
                        editor2.putString("fatherName",studentss.getFathersnme());
                        editor2.putString("studentAge",studentss.getAge());
                        editor2.putString("registrationNo",studentss.getRegistration());
                        editor2.putString("joiningDtDisp",studentss.getJoinedDate());
                        editor2.putString("joiningAcademicYr",studentss.getYearJoined());
                        editor2.putString("studentDobDisp",studentss.getDob());
                        editor2.putString("rollNo",studentss.getRollNo());
                        editor2.putString("schoolName",studentss.getSchoolName());
                        editor2.putString("studentBloodGroup",studentss.getBlood_group());
                        editor2.putInt("visible",1);
                        editor2.apply();
                        editor.putString("url",url);
                        editor.apply();
                        Intent i=new Intent(StudentList.this,Navigation.class);

                        SharedPreferences settings =getSharedPreferences("com.example.xyz", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("myPin",parentPin);
                        editor.apply();
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });

            return  row;
        }

    }
    class ViewHolder
    {
        TextView studentname,classes,rollno,age,dob;
         ImageView image;
         CardView mystudent;
    }

    void  showMain(ArrayList students)
    {

    }
}

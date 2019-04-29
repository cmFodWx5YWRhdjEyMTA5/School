package com.video.aashi.school.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.mtp.MtpDeviceInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.video.aashi.school.APIUrl;
import com.video.aashi.school.Login;
import com.video.aashi.school.LoginAttempt;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.PinLogin;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.CircleTransform;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.Interfaces.SwitchStatus;
import com.video.aashi.school.adapters.arrar_adapterd.Students;
import com.video.aashi.school.fragments.studentlist.StudentList;
import com.video.aashi.school.pinchange.ChangePin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment
{

    Switch authentication;
    SwitchStatus switchStatus;
    SharedPreferences sharedPreferences;
    boolean status;
    SharedPreferences.Editor editor;
    boolean silent;
    CardView savepin;
    RelativeLayout changePin;
    android.support.v7.widget.Toolbar toolbar;
    SharedPreferences settings;
    TextView getText,yourPin;
    String  myPin,mytext;
    CheckBox cbRememberMe;
    boolean rememberMe;
    boolean checkPreference;
    SharedPreferences.Editor editorss;
   public static   LinearLayout switchStudent;
    PopupWindow editPop;
    MyInterface myInterfacee;
    RecyclerView recyclerView;
    ArrayList<Students> students;
    SharedPreferences.Editor editor2;
    SharedPreferences preferences;
      View mView;
    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPreferences = getActivity(). getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
        authentication =(Switch)view.findViewById(R.id.autheentication);
        // SharedPreferences sharedPrefs = getActivity(). getSharedPreferences("com.example.xyle", MODE_PRIVATE);
        savepin =(CardView)view.findViewById(R.id.pinConfig);
        getText =(TextView)view.findViewById(R.id.getText);
        cbRememberMe =(CheckBox)view.findViewById(R.id.checkSave);
        yourPin =(TextView)view.findViewById(R.id.yourPin);
        changePin =(RelativeLayout)view.findViewById(R.id.changePin);
        switchStudent =(LinearLayout)view.findViewById(R.id.switchStudent);
        switchStudent.setVisibility(View.GONE);
        settings = getActivity(). getSharedPreferences("com.example.xyz", 0);
        silent = settings.getBoolean("switchkey", true);
        myPin = settings.getString("myPin","");
        preferences = getActivity().getSharedPreferences (StudentList. PREF_NAME, MODE_PRIVATE); // 0 - for private mode
        editor2 = preferences.edit();
        yourPin.setText(myPin);
        editorss =  getActivity().getPreferences(0).edit();;
        if (silent)
        {
            yourPin.setVisibility(View.VISIBLE);
        }
        else
        {
            yourPin.setVisibility(View.GONE);
        }
        SharedPreferences prefs = getActivity().getPreferences(0);
        rememberMe = prefs.getBoolean("check", true);
        if (rememberMe) {
            cbRememberMe.setChecked(true);
        }
        SharedPreferences changetert = getActivity(). getSharedPreferences("text", 0);
        mytext =changetert.getString("mytext","");
        authentication.setChecked(silent);
        switchStatus =new SwitchStatus();
        getText.setText(mytext);
        toolbar=(android.support.v7.widget.Toolbar)getActivity(). findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        changePin.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View v) {
         startActivity(new Intent(getActivity(),ChangePin.class));

        }
      });
        switchStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPop();

            }
        });

       cbRememberMe.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (getText.getText().toString().contains("Save Pin"))
               {
                   showDialogue(getActivity(),"Are you sure you want to save your pin?","Yes","No");
               }
               else if (getText.getText().toString().contains("Remove Pin"))
               {
                   showDialogues(getActivity(),"Are you sure you want to remove your pin?","Yes","No");

               }
           }
       });

        savepin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (getText.getText().toString().contains("Save Pin"))
                {
                    showDialogue(getActivity(),"Are you sure you want to save your pin?","Yes","No");
                }
                else if (getText.getText().toString().contains("Remove Pin"))
                {
                    showDialogues(getActivity(),"Are you sure you want to remove your pin?","Yes","No");

                }
             }
        });
        return  view;
    }
    private  void showDialogue(Context context,String title,String validate,String validates)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setPositiveButton(validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getActivity().getSharedPreferences("com.example.xyz", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("switchkey", true);
                        editor.putString("mytext","");
                        editor.putString("myPin",myPin);
                        yourPin.setVisibility(View.VISIBLE);
                        editor.apply();
                        SharedPreferences changetert =getActivity(). getSharedPreferences("text", 0);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editors = changetert.edit();
                        editors.putString("mytext","Remove Pin");
                        editors.apply();
                        yourPin.setText(myPin);
                        getText.setText("Remove Pin");
                        cbRememberMe.setChecked(true);
                        if(cbRememberMe.isChecked()){
                            checkPreference = true;
                            editorss.putBoolean("check", checkPreference);
                            editorss.commit();
                            Log.i("Remember Me", "checked,preference added");
                        }
                        else{
                            checkPreference = false;
                            editorss.putBoolean("check", checkPreference);
                            editorss.commit();
                            Log.i("Remember Me", "Unchecked, preferences removed");
                        }
                    }
                })
                .setNegativeButton(validates, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cbRememberMe.setChecked(false);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private  void showDialogues(Context context,String title,String validate,String validates)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setPositiveButton(validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SharedPreferences settings = getActivity().getSharedPreferences("com.example.xyz", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("switchkey", false);
                      //  editor.putString("myPin","");
                        editor.putString("mytext","");
                        editor.apply();
                        yourPin.setText("");
                        getText.setText("Save Pin");
                        cbRememberMe.setChecked(false);
                        if(cbRememberMe.isChecked()){
                            checkPreference = true;
                            editorss.putBoolean("check", checkPreference);
                            editorss.commit();
                            Log.i("Remember Me", "checked,preference added");
                        }
                        else{
                            checkPreference = false;
                            editorss.putBoolean("check", checkPreference);
                            editorss.commit();
                            Log.i("Remember Me", "Unchecked, preferences removed");
                        }
                        SharedPreferences changetert =getActivity(). getSharedPreferences("text", 0);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editors = changetert.edit();
                        editors.putString("mytext","Save Pin");
                        editors.apply();
                        Intent intent = new Intent(getActivity(),LoginAttempt.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }
                })
                .setNegativeButton(validates, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cbRememberMe.setChecked(true);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    void editPop()
    {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.switch_layout, null);

        recyclerView = mView.findViewById(R.id.studentsRecycle);



        editPop = new PopupWindow(
                mView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        new getList().execute();

    }
    public void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            }
            else
            {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);
    }

     class getList extends AsyncTask
     {
         @Override
         protected void onPreExecute() {
             Retrofit retrofit;


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

             retrofit = new Retrofit.Builder().baseUrl(HomePage.url).addConverterFactory
                     (GsonConverterFactory.create())
                     .client(defaulthttpClient)
                     .build();
             myInterfacee = retrofit.create(MyInterface.class);
             final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
             layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
             recyclerView.setLayoutManager(layoutManager);
             super.onPreExecute();
         }

         @Override
         protected Object doInBackground(Object[] objects) {
             Call<ResponseBody> call = myInterfacee.getStudentList(new com.video.aashi.school.adapters.post_class.StudentList(
                    Navigation.loginId  , Navigation.parentPin ,Navigation.session ));
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
                             Log.i("Tag","MyStudents"+ Navigation.loginId  + Navigation.parentPin+Navigation.session);
                             JSONObject object = new JSONObject(bodyString);
                             JSONArray list = object.getJSONArray("Student List");
                             for (int i = 0;i<list.length();i++)
                             {
                                 if (list.length() ==0)
                                 {

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

                                     recyclerView.setAdapter(new StudentAdpter(getActivity(),students));

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
                 public void onFailure(Call<ResponseBody> call, Throwable t) {

                 }
             });
             return null;
         }

         @Override
         protected void onPostExecute(Object o) {
             editPop.setFocusable(true);
             editPop.setAnimationStyle(R.style.popupanimation);
             editPop.showAtLocation(mView, Gravity.BOTTOM|Gravity.END, 0, 0);
             dimBehind(editPop);
             super.onPostExecute(o);
         }
     }
     class StudentAdpter extends RecyclerView.Adapter<ViewHoler>
     {
          Context context;
         ArrayList<Students> students;

         public  StudentAdpter (Context context,ArrayList<Students> students)
         {
             this.context = context;
             this.students = students;

         }

         @NonNull
         @Override
         public ViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
             View view = LayoutInflater.from(getActivity()).inflate(R.layout.students, viewGroup, false);
             return new ViewHoler(view);
         }

         @Override
         public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
             Students studentss = students.get(position);
             holder.name.setText(students.get(position).getName() );
             holder.age.setText(students.get(position).getAge() );
             holder.classes.setText(students.get(position).getClassses() );
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                 Picasso.get()
                         .load( StudentList.url+ students.get(position).getImage())
                         .error(R.drawable.download )
                         .transform(new CircleTransform())
                         .into(holder.imageView);
             }

             holder.studentCard.setOnClickListener(new View.OnClickListener() {
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
                     editor2.putString("session",Navigation.session);
                     editor2.putInt("student",position);
                     editor2.apply();
                     Intent i=new Intent(getActivity(),Navigation.class);
                     i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(i);
                 }
             });

         }

         @Override
         public int getItemCount() {
             return students.size();
         }
     }
     public  static class  ViewHoler extends RecyclerView.ViewHolder
     {

         ImageView imageView;
         TextView classes,age,name;
         CardView studentCard;
         public ViewHoler(@NonNull View itemView) {
             super(itemView);
             imageView =(ImageView)itemView.findViewById(R.id.myImage);
             classes =(TextView)itemView.findViewById(R.id.myClass);
             age =(TextView)itemView.findViewById(R.id.myAge);
             studentCard =(CardView)itemView.findViewById(R.id.studentCard);
             name =(TextView)itemView.findViewById(R.id.myName);
         }
     }
}

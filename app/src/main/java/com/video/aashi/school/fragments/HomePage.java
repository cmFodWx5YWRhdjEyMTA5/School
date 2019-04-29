package com.video.aashi.school.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.video.aashi.school.APIUrl;
import com.video.aashi.school.BarScanner;
import com.video.aashi.school.Login;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Myclass;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.Performance;
import com.video.aashi.school.PinLogin;
import com.video.aashi.school.ProfileView;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Expired;
import com.video.aashi.school.adapters.Interfaces.CircleTransform;
import com.video.aashi.school.adapters.Interfaces.DatabaseHelper;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.Name;
import com.video.aashi.school.adapters.arrar_adapterd.Students;
import com.video.aashi.school.adapters.drawer.BottomNavigationViewHelper;
import com.video.aashi.school.adapters.local.DateLocal;
import com.video.aashi.school.adapters.post_class.Attend;
import com.video.aashi.school.adapters.post_class.Home;
import com.video.aashi.school.fragments.studentlist.StudentList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment implements View.OnClickListener {
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    String titles,messages;
    TextView usenmae,classname,mobileno;
    NestedScrollView mypops;
    PopupWindow popupWindow;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TabLayout tabLayout;
    NestedScrollView nestedScrollView;
    CardView cardView;
    BottomNavigationView bnav ,bnav1;
    TextView user_name;
    ImageView imageViews;
   android.support.v7.widget.Toolbar toolbar;
    TextView textCartItemCount;
    int mCartItemCount = 10;
    ImageView profile;
    String pops;
     public static String userName,s_class,mobile,image;
    FrameLayout performance;
     TextView getClassname;
    SharedPreferences sharedPreferences,loginCredit;
    FrameLayout timetable;
    ImageView perfor,time;
    String classId;
    String  studentId;
    String locId;
    String  classGeneralId;
    String viewMore;
    String academicYearId;
    Retrofit retrofit;
    MyInterface myInterface;
    TextView homework,home_ds,hols,hols_des,memo,mome_des,notice,notice_des;
    TextView view_hols,view_memo,view_notice,view_home;
    SharedPreferences sharedPreferencess,shared;
    SharedPreferences.Editor editors;
    ImageView findstudent;
    TextView topics,descrip;
    String showpop;
    String aca_year;
    String monthName,schoolname;
    DateLocal db;
    public  static  String url;
     public  static   String urls;
     SharedPreferences.Editor editor,editor2;
    static boolean doubleBackToExitPressedOnce = false;
    LinearLayout myHomework,mememos,myHoidays,myNotice;
    RecyclerView recyclerView;
    PopupWindow editPop;
    View mView;
    SharedPreferences mysession;
    SharedPreferences.Editor sess;
    TextView schoolName;
    int visible = 0;
    @SuppressLint({"SetTextI18n", "NewApi", "CommitPrefEdits"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        sharedPreferences = getActivity().getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
        sharedPreferencess = getActivity().getSharedPreferences("popup",MODE_PRIVATE);
        loginCredit = getActivity(). getSharedPreferences("pinValidate",MODE_PRIVATE);
        mysession = getActivity(). getSharedPreferences("MySession",MODE_PRIVATE);
        sess = mysession.edit();
        shared = getActivity().getSharedPreferences("loginstatus",MODE_PRIVATE);
        editors = sharedPreferencess.edit();
        editor = loginCredit.edit();
        editor2 = shared.edit();
        url = loginCredit.getString(  "url","");
        urls = loginCredit.getString(  "url","");
        url = url +"rest/ParentLoginRestWS/";
        getActivity().setTitle("EaziEd");
        showpop = sharedPreferencess.getString("mykey","");
        myHomework =(LinearLayout)view.findViewById(R.id.myHomeworks);
        mememos =(LinearLayout)view.findViewById(R.id.myMemoss);
        myHoidays =(LinearLayout)view.findViewById(R.id.myHolodays);
        myNotice =(LinearLayout)view.findViewById(R.id.myNoticeboard);
        findstudent =(ImageView)view.findViewById(R.id.findstudent);
        user_name =(TextView)view.findViewById(R.id.user_name);
        performance=(FrameLayout)view.findViewById(R.id.performance);
        getClassname =(TextView)view.findViewById(R.id.s_class);
        mobileno =(TextView)view.findViewById(R.id.mobileno);
        profile =(ImageView) view.findViewById(R.id.profile_image);
        schoolName  =(TextView)view.findViewById(R.id.scholNames);
        db = new DateLocal(getActivity());
        userName =  sharedPreferences.getString("S_name","");
        s_class = sharedPreferences.getString("classname","");
        mobile = sharedPreferences.getString("mobile","");
        image = sharedPreferences.getString("photo","");
        schoolname =  sharedPreferences.getString("schoolName","");
        if (schoolname != null)
        {
            schoolName.setText(schoolname);
        }

        visible = shared.getInt("visible",0);
      //  Navigation.userImage = image;
        perfor =(ImageView)view.findViewById(R.id.performance1);
        time=(ImageView)view.findViewById(R.id.timetable1);
        homework =(TextView)view.findViewById(R.id.homework_heaf);
        home_ds =(TextView)view.findViewById(R.id.homework_des);
        hols =(TextView)view.findViewById(R.id.hols_head);
        hols_des =(TextView)view.findViewById(R.id.hols_des);
        memo =(TextView)view.findViewById(R.id.memo_head);
        mome_des=(TextView)view.findViewById(R.id.memo_des);
        notice=(TextView)view.findViewById(R.id.notice_head);
        notice_des =(TextView)view.findViewById(R.id.notice_des);
        classId = Navigation.class_id;
        studentId = Navigation.student_id;
        locId = Navigation.location_id;
        classGeneralId = Navigation.general_id;
        aca_year = Navigation.academicyear;
        viewMore = "0";
        mypops =(NestedScrollView)view.findViewById(R.id.mypops);
        academicYearId = Navigation.academicyear;
        view_hols =(TextView)view.findViewById(R.id.view_hols);
        view_memo =(TextView)view.findViewById(R.id.view_memo);
        view_notice =(TextView)view.findViewById(R.id.view_notice);
        view_home =(TextView)view.findViewById(R.id.view_homework);
        view_hols.setOnClickListener(this);
        view_memo.setOnClickListener(this);
        view_notice.setOnClickListener(this);
        view_home.setOnClickListener(this);
        myHomework.setOnClickListener(this);
        mememos.setOnClickListener(this);
        myHoidays.setOnClickListener(this);
        myNotice.setOnClickListener(this);
        Log.i("Tag","UserNames"+ s_class + image+url);
        int navDefaultTextColor = Color.parseColor("#202020");
       // bnav =(BottomNavigationView)view.findViewById(R.id.bottomnav);
        //bnav1=(BottomNavigationView)view.findViewById(R.id.bottomnav1);
       // BottomNavigationViewHelper.disableShiftMode(bnav);
       // BottomNavigationViewHelper.disableShiftMode(bnav1);
        timetable =(FrameLayout)view.findViewById(R.id.timetable);
        user_name.setText(userName  );
        getClassname.setText("Class : "+ s_class);
        mobileno.setText(mobile);
        collapsingToolbarLayout =(CollapsingToolbarLayout)view.findViewById(R.id.colapse);
        AppBarLayout appBarLayout = (AppBarLayout)view. findViewById(R.id.app_bar);
        toolbar =(android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
        OkHttpClient defaulthttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor()
                        {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException
                            {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Content-Type", "application/json").build();
                                return chain.proceed(request);
                            }
                        }).build();
        retrofit =   new Retrofit.Builder().baseUrl(url ).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        myInterface = retrofit.create(MyInterface.class);
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("combo","1");
                bundle.putString("check","1");
                TimeTable performance = new TimeTable();
                performance.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.mycontainer,performance).
                        addToBackStack(null).commit();
            }
        });
        findstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  viewpopup();

            }
        });

        performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExamCombo performances = new ExamCombo();
                Bundle bundle= new Bundle();
                bundle.putString("check","1");
                performances.setArguments(bundle);
                bundle.putString("combo","1");
                getFragmentManager().beginTransaction().replace(R.id.mycontainer,performances).
                        addToBackStack(null).commit();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("combo","1");
                bundle.putString("check","1");
                TimeTable performance = new TimeTable();
                performance.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.mycontainer,performance).
                        addToBackStack(null).commit();
            }
        });
        perfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putString("combo","1");
                bundle.putString("check","1");
                ExamCombo performance = new ExamCombo();
                performance.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.mycontainer,performance).
                        addToBackStack(null).commit();
            }
        });
       Picasso.get()
                .load(urls+ image)
                .error(R.drawable.badge )
                .transform(new CircleTransform())
                .into(profile);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {

                }
                else
                {

               }

            }

      });
        new getHomepage().execute();
           profile.setOnClickListener(new View.OnClickListener()

      {
      @Override
      public void onClick(View v) {



      }
  });

    return  view;
    }

    void  showFragments(Fragment fragment)
    {
        Bundle bundle= new Bundle();
        bundle  .putString("chech","1");
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.mycontainer,fragment).
                addToBackStack(null).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

         menu.clear();
        inflater.inflate(R.menu.multiply,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.history2:
                ApplicationInfo packageinfo = null;
                try {
                    packageinfo = getActivity().getPackageManager().getApplicationInfo("com.video.aashi.school", 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                File file = new File(packageinfo.publicSourceDir);
                shareApplication(file);
                break;
            case R.id.qrcode:
                launchActivity(BarScanner.class);
                break;
            case R.id.switchs:
                editPop();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem register = menu.findItem(R.id.switchs);
        if (register != null)
        {
            if(visible == 1)
            {
                register.setVisible(true);
            }
            else
            {
                register.setVisible(false);
            }
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.view_hols:
                Holidays holidays = new Holidays();
                showFragment(holidays);
                break;
            case R.id.myHolodays:
                Holidays holidayss = new Holidays();
                showFragment(holidayss);
                break;
            case R.id.view_homework:
                Homework homework = new Homework();
                showFragment(homework);
                break;
            case R.id.myHomeworks:
                Homework homeworks = new Homework();
                showFragment(homeworks);
                break;
            case R.id.view_notice:
                NoticeBoard noticeBoard=new NoticeBoard();
                showFragment(noticeBoard);

                break;
            case R.id.myNoticeboard:
                NoticeBoard noticeBoards=new NoticeBoard();
                showFragment(noticeBoards);

                break;
            case R.id.view_memo:
                Memos memos=new Memos();
                showFragment(memos);

                break;
            case R.id.myMemoss:
                Memos memoss=new Memos();
                showFragment(memoss);

                break;
                default:
                    break;
        }

    }

    private void showFragment(android.support.v4.app.Fragment fragment) {
        Bundle bundle= new Bundle();
        bundle  .putString("check","1");
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.mycontainer, fragment)
                .addToBackStack(null)
                .commit();

    }

    class getHomepage extends AsyncTask
    {

        @Override
        protected void onPreExecute() {

         //ProgressDialog progressDialog=  new ProgressDialog();

            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            retrofit2.Call<ResponseBody> call = myInterface.getHome(new Home
                    (classId,studentId,locId,classGeneralId,viewMore,academicYearId,
                            Navigation.loginId,Navigation.session));
            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    String bodyString = null;
                    Log.i("Tag","MyHomePage"+call.request().url() + classId + studentId+locId+
                            classGeneralId+viewMore+academicYearId);
                    String errorMessage;
                    try
                    {
                        bodyString  = response.body().string();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    try
                    {
                       JSONObject objects =  new JSONObject(bodyString);
                       errorMessage = objects.getString("errorMessage");
                       String   failure = objects.getString("status");
                        errorMessage = objects.getString("errorMessage");
                        if  (failure.contains("failure"))
                        {
                            String finalErrorMessage = errorMessage;
                            Expired expired = new Expired( getActivity(),  finalErrorMessage);
                            expired.setTitle(finalErrorMessage);
                            expired.setCancelable(false);

                            expired.setPositiveButton("OK", (dialog1, which) -> {
                                expired.getSharedPreferences();
                                Intent i = new Intent(getActivity(),PinLogin.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            });
                            expired.show();
//                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity() );
//
//                            builder.setMessage(errorMessage)
//                                    .setCancelable(false)
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//
//
//                                            editors.clear();
//                                            sess.putBoolean("isLogins",true);
//
//                                            editors.apply();
//                                            sess.apply();
//
//                                        }
//                                    });
//
//
//                            AlertDialog alert = builder.create();
//                            alert.setIcon(R.drawable.ic_error_outline_red_500_24dp);
//                            alert.setCancelable(false);
//                            alert.show();
                            //    Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            JSONArray object1 = objects.getJSONArray("DashBoard HomeWork Data");
                            for (int i = 0; i < object1.length(); i++) {
                                JSONObject object = object1.getJSONObject(i);
                                String home_hea = object.getString("chapter");
                                String ho_des = object.getString("homeWorkDesc");
                                String sub = object.getString("subject");
                                if (home_hea.length() > 35) {
                                    homework.setText(home_hea.subSequence(0, 35) + "..." + "  (" + sub + ")");
                                } else {
                                    homework.setText(home_hea + "..." + "  (" + sub + ")");
                                }
                                if (ho_des.length() > 35) {
                                    home_ds.setText(ho_des.subSequence(0, 35) + "...");

                                } else {
                                    home_ds.setText(ho_des);

                                }


                            }
                            JSONArray list = objects.getJSONArray("DashBoard NoticeBoard Data");
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject object = list.getJSONObject(i);

                                titles = object.getString("noticeBoardTitle");
                                messages = object.getString("noticeMessage");

                                if (titles.length() > 35) {
                                    notice.setText(titles.subSequence(0, 35) + "...");
                                } else {
                                    notice.setText(titles);
                                }
                                if (messages.length() > 35)

                                {
                                    notice_des.setText(messages.subSequence(0, 35) + "...");
                                } else {
                                    notice_des.setText(messages);
                                }
                                String noticeid;
                                noticeid = object.getString("noticeBoardId");
                                Log.i("Tag", "MyNavi" + list);
                                if (!noticeid.equals(showpop)) {
                                    editors.putString("mykey", noticeid);
                                    editors.apply();
                                    viewpopup();
                                }
                            }
                            JSONArray lists = objects.getJSONArray("DashBoard MemoBoard Data");
                            for (int i = 0; i < lists.length(); i++) {
                                JSONObject object = lists.getJSONObject(i);
                                String title = object.getString("memoTypeName");
                                String message = object.getString("remarks");
                                Log.i("TAG", "MyHolidays" + title + message);
                                if (title.length() > 35) {
                                    memo.setText(title.subSequence(0, 35) + "...");
                                } else {
                                    memo.setText(title);
                                }
                                if (message.length() > 35) {
                                    mome_des.setText(message.subSequence(0, 35) + "...");
                                } else {
                                    mome_des.setText(message);
                                }
                            }
                            JSONArray lists1 = objects.getJSONArray("DashBoard Upcoming Holiday Data");
                            for (int i = 0; i < lists1.length(); i++) {
                                JSONObject object = lists1.getJSONObject(i);
                                String title = object.getString("holidayCategoryName");
                                String day = object.getString("dayName");
                                String message = object.getString("holidayCategoryDesc");
                                if (title.length() > 35) {
                                    hols.setText(title.subSequence(0, 35) + "..." + " (" + day + ")");
                                } else {
                                    hols.setText(title + "  (" + day + ")");
                                }
                                if (message.length() > 35) {
                                    hols_des.setText(message.subSequence(0, 35) + "...");
                                } else {
                                    hols_des.setText(message);
                                }
                            }
                        }
                       // viewpopup();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

                }
            });


            return null;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                     doubleBackToExitPressedOnce = true;
                    //Toast.makeText(getActivity(), "Please click BACK", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getView().setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    if (doubleBackToExitPressedOnce) {
                                        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                                            Intent intent = new Intent(Intent.ACTION_MAIN);
                                            intent.addCategory(Intent.CATEGORY_HOME);
                                            startActivity(intent);
                                            doubleBackToExitPressedOnce = false;
                                            return true;
                                        }
                                    }
                                    return false;
                                }
                            });
                        }
                   }, 500);
                    return true;
                }
                return false;
            }
        });
    }
    void viewpopup()
    {
        try
        {
            View popupView =  LayoutInflater.from(getActivity()).inflate(R.layout.mypopup,
                    (ViewGroup)getView(). findViewById(R.id.
                            mycontainer));
            popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,false);
            topics =(TextView)popupView.findViewById(R.id.topics);
            descrip =(TextView)popupView.findViewById(R.id.descrip);
            topics.setText(titles);
            descrip.setText(messages);
            imageViews= (ImageView)popupView.findViewById(R.id.enddd);
            popupWindow.showAtLocation(popupView,Gravity.CENTER,0,0);
            imageViews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            dimBehind(popupWindow);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void shareApplication(File file) {
        try {
            //Make new directory in new location
            File tempFile = new File(  Objects.requireNonNull(getActivity()).getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;

            tempFile = new File(tempFile.getPath() + "/" + "appname" + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }

            InputStream in = new FileInputStream(file);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri uri = Uri.parse(file.getPath());
            sharingIntent.setType("*/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Click to link and download the apk. https://drive.google.com/open?id=10Nc5BoYn4NZ_O8ae32UVQwyzdCzFxNy");
            startActivity(Intent.createChooser(sharingIntent, "Share app using"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
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
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container, p);
    }
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(getActivity(), clss);
            startActivity(intent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(getActivity(), mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
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
    class getList extends AsyncTask
    {
        MyInterface myInterfacee;
        ArrayList<Students> students;
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
            retrofit2.Call<ResponseBody> call = myInterfacee.getStudentList(new com.video.aashi.school.adapters.post_class.StudentList(
                    Navigation.loginId  , Navigation.parentPin ,Navigation.session ));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
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
                                    String schoolName = jsonObject.getString("locationName");
                                    students .add(new Students(name,image,rolno,age,classes,dob,sstudentId,locationid,genid,classid,acyearid
                                            ,examid,classname,mobile,passwor,users,fatherEmailId,sessionss,s_name,s_class,academicyear,city,gendre,
                                            middlename,lastnmame,fathersnme,mobleno,pincode,state,fatherEmailId, studentNationality,motherName,registration,joineddate,joinedyear
                                            ,rollno,bloodgroups,schoolName ));
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
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
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
    class StudentAdpter extends RecyclerView.Adapter<Settings.ViewHoler>
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
        public Settings.ViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.students, viewGroup, false);
            return new Settings.ViewHoler(view);
        }
        @Override
        public void onBindViewHolder(@NonNull Settings.ViewHoler holder, int position) {
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
            holder.studentCard.setOnClickListener(new View.OnClickListener()
            {
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
                    editor2.putString("studentBloodGroup",studentss.getBlood_group());
                    editor2.putString("schoolName",studentss.getSchoolName());
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
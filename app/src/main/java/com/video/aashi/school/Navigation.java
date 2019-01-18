package com.video.aashi.school;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.video.aashi.school.adapters.Interfaces.CircleTransform;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.post_class.Home;
import com.video.aashi.school.adapters.viewAdspters.NavigationAdapter;
import com.video.aashi.school.fragments.Attendace;
import com.video.aashi.school.fragments.ExamCombo;
import com.video.aashi.school.fragments.ExamTables;
import com.video.aashi.school.fragments.Holidays;
import com.video.aashi.school.fragments.HomePage;
import com.video.aashi.school.fragments.Homework;
import com.video.aashi.school.fragments.MainInvoice;
import com.video.aashi.school.fragments.Memos;
import com.video.aashi.school.fragments.NoticeBoard;
import com.video.aashi.school.fragments.PtaMee;
import com.video.aashi.school.fragments.Settings;
import com.video.aashi.school.fragments.TimeTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Navigation extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    SharedPreferences sharedPreferencess;
    SharedPreferences.Editor editors;
    RecyclerView recyclerView;
    NavigationAdapter navigationAdapter;
    DrawerLayout drawerLayout;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ActionBarDrawerToggle drawerToggle;
    boolean doubleBackToExitPressedOnce = false;
    Retrofit retrofit;
    String titles,messages,showpop;
    String noticeid;
    ImageView imageViews;
    MyInterface myInterface;
    int pos = 0;
    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_TIMETABLE = 3;
    private static final int POS_EXAM = 4;
    private static final int POS_HOLIDAYS = 5;
    private static final int POS_MEMOS = 6;
    private static final int POS_MEEETING = 7;
    private static final int POS_CART = 8;
    private static final int POS_INVOICE = 9;
    private static final int POS_PERFO = 10;
    private static final int POS_PROFILE = 11;
    private static  final int POS_SETTINGS =12;
    private static final int POS_LOGOUT = 13;
    public  static  String academicyear,student_id,location_id,general_id,class_id,examid,emailids;
    ImageView imageView;
    TextView users,ids;
    String username;
    String user_image,s_class;
    int position;
    PopupWindow popupWindow;
    TextView topics,descrip;
    public static String loginId,url,oName,parentName,studentName,parentPin,parentMob,userid;
    SharedPreferences loginCredit;
    private int currentMenuItem;
    private Fragment fragmentCurrent;
    boolean silent= true;
    @SuppressLint("CommitPrefEdits")
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        loadHomeFragment();
        sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
        loginCredit = getSharedPreferences("pinValidate",MODE_PRIVATE);
        loginId = loginCredit.getString("loginId","");
        url = loginCredit.getString(  "url","");
        oName =  loginCredit.getString("oName","");
        parentName = loginCredit.getString("parentName","");
        studentName = loginCredit.getString("stuName","");
        parentMob = loginCredit.getString("mobiles","");
        userid = loginCredit.getString("userId","");
        sharedPreferencess =  getSharedPreferences("popup",MODE_PRIVATE);
        editors = sharedPreferencess.edit();
        username = sharedPreferences.getString("S_name","");
        user_image = sharedPreferences.getString("image","");
        student_id = sharedPreferences.getString("StudentId","");
        academicyear = sharedPreferences.getString("academic","");
        location_id = sharedPreferences.getString("location_id","");
        general_id = sharedPreferences.getString("gen_id","");
        class_id = sharedPreferences.getString("class_id","");
        examid = sharedPreferences.getString("examid","");
        emailids = sharedPreferences.getString("fathers","");
        s_class = sharedPreferences.getString("classname","");
        Log.i("Tag","MyImages"+ academicyear+ student_id + url +parentMob);
        users =(TextView)findViewById(R.id.userna);
        ids=(TextView)findViewById(R.id.myids);
        users.setText(username);
        ids .setText(s_class);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");
        imageView =(ImageView)findViewById(R.id.userimage);
        showpop = sharedPreferencess.getString("myke","");
        SharedPreferences settings = getSharedPreferences("com.example.xyz", 0);
        silent   = settings.getBoolean("switchkey", false);
        parentPin = settings.getString("myPin","");
       final GestureDetector mGestureDetector = new GestureDetector(
                Navigation.this, new GestureDetector.SimpleOnGestureListener()
        {
            @Override public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }

        });
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
        retrofit =   new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        myInterface = retrofit.create(MyInterface.class);
              Picasso.get()
                .load( APIUrl.IMAGE_URl+ user_image)
                .error(R.drawable.badge )
                .transform(new CircleTransform())
                .into(imageView);
              Log.i("Tag","UserImages"+ APIUrl.IMAGE_URl+ user_image);
        new getHomepage().execute();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        drawerLayout=(DrawerLayout)findViewById(R.id.mydrawer);
        recyclerView = (RecyclerView)findViewById(R.id.navi_recycle);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
           recyclerView.setHasFixedSize(true);
           String[] strings = getResources().getStringArray(R.array.ld_activityScreenTitles);
           for (String string : strings)
           {
               stringArrayList.add(string);
           }
           TypedArray imgs = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
           navigationAdapter = new NavigationAdapter(stringArrayList,imgs,Navigation.this);
           recyclerView.setAdapter(navigationAdapter);
           drawerToggle =  new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);





           drawerLayout.setDrawerListener(drawerToggle);
           recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
               @Override
               public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                   View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
                   if(child!=null && mGestureDetector.onTouchEvent(motionEvent)) {
                       drawerLayout.closeDrawers();
                      // Toast.makeText(getApplicationContext(), "The Item Clicked is: " +
                              // recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                     position = recyclerView.getChildPosition(child);
                       if (position == POS_LOGOUT) {
                               showDialogue(Navigation.this);

                       }
                       if (position == POS_DASHBOARD)
                       {
                           HomePage dashboard = new HomePage();
                           showFragment(dashboard);
                           fragmentCurrent = dashboard;
                       }
                       if(position == POS_HOLIDAYS)
                       {
                           Holidays holidays = new Holidays();
                           showFragment(holidays);
                       }
                       if (position == POS_ACCOUNT)
                       {
                           Attendace attendace= new Attendace();
                           showFragment(attendace);
                         //  viewpopup();

                       }
                       if(position == POS_TIMETABLE)
                       {

                           TimeTable timeTable= new TimeTable();
                           showFragment(timeTable);
                       }
                       if(position == POS_CART)
                       {
                           Homework homework= new Homework();

                           showFragment(homework);

                       }
                       if (position == POS_MESSAGES)
                       {
                           NoticeBoard noticeBoard= new NoticeBoard();
                           showFragment(noticeBoard);
                       }

                       if(position == POS_MEMOS)
                       {
                           Memos memos= new Memos();

                           showFragment(memos);
                       }
                       if(position == POS_EXAM)
                       {
                           Bundle bundle= new Bundle();
                           bundle.putString("combo","0");
                           bundle.putString("check","1");
                           ExamCombo examTables = new ExamCombo();
                           examTables.setArguments(bundle);
                           showFragment(examTables);
                       }
                       if (position == POS_PERFO)
                       {
                           Bundle bundle= new Bundle();
                           bundle.putString("combo","1");
                           bundle.putString("check","1");
                           ExamCombo performance = new ExamCombo();
                           performance.setArguments(bundle);
                           showFragment(performance);
                       }
                       if(position == POS_INVOICE)
                       {
                           MainInvoice invoicec = new MainInvoice();
                           showFragment(invoicec);
                       }

                       if (position == POS_PROFILE)
                       {
                          ProfileView profileView= new ProfileView();
                          showFragment(profileView);
                       }
                       if (position == POS_MEEETING)
                       {
                           PtaMee ptaMee = new PtaMee();
                           showFragment(ptaMee);
                       }

                       if (position == POS_SETTINGS)
                       {
                          showFragment(new Settings());
                       }
                       return true;
                   }
                   return false;
               }

               @Override
               public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

               }

               @Override
               public void onRequestDisallowInterceptTouchEvent(boolean b) {

               }
           });
    }
    private void showFragment(android.support.v4.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mycontainer, fragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard,menu);
        return  true;
    }
    @Override
    public void onBackPressed() {

        getSupportFragmentManager().beginTransaction().add(R.id.mycontainer,new HomePage()).commit();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.myhome:
                HomePage homePage= new HomePage();
                showFragment(homePage);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void loadHomeFragment()
    {

        HomePage dashboard= new HomePage();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.mycontainer, dashboard);
        fragmentTransaction.commitAllowingStateLoss();
        invalidateOptionsMenu();

    }
    class getHomepage extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects) {
            retrofit2.Call<ResponseBody> call = myInterface.getHome(new
                    Home(class_id,student_id,location_id,general_id,"0",academicyear));
            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    String bodyString = null;
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
                       JSONArray list= objects.getJSONArray("DashBoard NoticeBoard Data");
                       for (int i=0;i<list.length(); i++)
                        {
                            JSONObject object = list.getJSONObject(i);
                            titles = object.getString("noticeBoardTitle");
                            messages = object.getString("noticeMessage");
                            noticeid    = object.getString("noticeBoardId");
                            Log.i("Tag","MyNavi"+ list);
                            if (!noticeid.equals(showpop))
                            {

                               // viewpopup();
                                editors.putString("myke",  noticeid);
                                editors.apply();

                            }
                        }
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
    void viewpopup()
    {
        try
        {
            View popupView =  LayoutInflater.from(getApplicationContext()).inflate(R.layout.mypopup,
                   (ViewGroup) findViewById(R.id.
                            mycontainer));
            popupWindow = new PopupWindow(popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,false);
            topics =(TextView)popupView.findViewById(R.id.topics);
            descrip =(TextView)popupView.findViewById(R.id.descrip);
            topics.setText(titles);
            descrip.setText(messages);
            imageViews= (ImageView)popupView.findViewById(R.id.enddd);
            popupWindow.showAtLocation(popupView,Gravity.CENTER,0,0);
            dimBehind(popupWindow);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
           }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void dimBehind(PopupWindow popupWindow)
    {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            }
            else
            {
                container = popupWindow.getContentView();
            }
            }
            else
            {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                container = (View) popupWindow.getContentView().getParent().getParent();
            }
            else
            {
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
    private  void showDialogue(Context context)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
         }
    private  void showDialogues(Context context)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}

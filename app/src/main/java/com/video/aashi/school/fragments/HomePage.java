package com.video.aashi.school.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.telecom.Call;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
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
import com.video.aashi.school.ProfileView;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.CircleTransform;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.drawer.BottomNavigationViewHelper;
import com.video.aashi.school.adapters.post_class.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    SharedPreferences sharedPreferences;
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
    SharedPreferences sharedPreferencess;
    SharedPreferences.Editor editors;
    ImageView findstudent;
    TextView topics,descrip;
    String showpop;
    @SuppressLint({"SetTextI18n", "NewApi", "CommitPrefEdits"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
        sharedPreferencess = getActivity().getSharedPreferences("popup",MODE_PRIVATE);
        editors = sharedPreferencess.edit();
        showpop = sharedPreferencess.getString("mykey","");
        findstudent =(ImageView)view.findViewById(R.id.findstudent);
        user_name =(TextView)view.findViewById(R.id.user_name);
        performance=(FrameLayout)view.findViewById(R.id.performance);
        getClassname =(TextView)view.findViewById(R.id.s_class);
        mobileno =(TextView)view.findViewById(R.id.mobileno);
        profile =(ImageView) view.findViewById(R.id.profile_image);
        userName =  sharedPreferences.getString("S_name","");
        s_class = sharedPreferences.getString("classname","");
        mobile = sharedPreferences.getString("mobile","");
        image = sharedPreferences.getString("photo","");
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
        Log.i("Tag","UserNames"+ s_class + image);
        int navDefaultTextColor = Color.parseColor("#202020");
       // bnav =(BottomNavigationView)view.findViewById(R.id.bottomnav);
        //bnav1=(BottomNavigationView)view.findViewById(R.id.bottomnav1);
       // BottomNavigationViewHelper.disableShiftMode(bnav);
       // BottomNavigationViewHelper.disableShiftMode(bnav1);
        timetable =(FrameLayout)view.findViewById(R.id.timetable);
        user_name.setText(userName  );
        getClassname.setText("class "+ s_class);
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
        retrofit =   new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        myInterface = retrofit.create(MyInterface.class);
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mycontainer,new TimeTable()).
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

               getFragmentManager().beginTransaction().replace(R.id.mycontainer,new ExamCombo()).
                       addToBackStack(null).commit();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mycontainer,new TimeTable()).
                        addToBackStack(null).commit();
            }
        });
        perfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mycontainer,new ExamCombo()).
                        addToBackStack(null).commit();
            }
        });
      Picasso.get()
                .load(APIUrl.IMAGE_URl+ image)
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
          Intent intent = new Intent(getActivity() , ProfileView.class);
          Pair[] pairs= new Pair[3];
          pairs[0] = new Pair<View , String> (profile ,"profile");
          pairs[1] = new Pair<View , String> (usenmae ,"name");
          pairs[2] = new Pair<View , String> (classname ,"classname");
          ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation(getActivity());
          startActivity(intent,activityOptions.toBundle());

      }
  });

    return  view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

         menu.clear();
        inflater.inflate(R.menu.multiply,menu);

      //  textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

      /* setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        */
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
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {


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
            case R.id.view_homework:
                Homework homework = new Homework();
                showFragment(homework);
                break;
            case R.id.view_notice:
                NoticeBoard noticeBoard=new NoticeBoard();
                showFragment(noticeBoard);

                break;
            case R.id.view_memo:
                Memos memos=new Memos();
                showFragment(memos);

                break;
                default:
                    break;
        }

    }

    private void showFragment(android.support.v4.app.Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.mycontainer, fragment)
                .addToBackStack(null)
                .commit();
    }
    class getHomepage extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects) {

            retrofit2.Call<ResponseBody> call = myInterface.getHome(new Home(classId,studentId,locId,classGeneralId,viewMore,academicYearId));

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
                        JSONArray object1 =  objects.getJSONArray("DashBoard HomeWork Data");
                        for (int i = 0; i < object1.length(); i++)
                        {
                            JSONObject object = object1.getJSONObject(i);
                            String  home_hea = object.   getString  ("chapter");
                            String ho_des = object.getString("homeWorkDesc");
                            String sub = object.getString("subject");
                            if(home_hea.length() >35)
                            {
                                homework.setText(home_hea.subSequence(0,35)+"..."   + "  ("+ sub+")" );
                            }
                            else
                            {
                                homework.setText(home_hea+"..."   + "  ("+ sub+")" );
                            }
                            if (ho_des.length() >35)
                            {
                                home_ds.setText(ho_des.subSequence(0,35)+"...");

                            }
                            else
                            {
                                home_ds.setText(ho_des);

                            }


                        }
                        JSONArray list= objects.getJSONArray("DashBoard NoticeBoard Data");
                        for (int i=0;i<list.length(); i++)
                        {
                            JSONObject object = list.getJSONObject(i);

                            titles = object.getString("noticeBoardTitle");
                           messages = object.getString("noticeMessage");

                            if (titles.length() >35)
                            {
                                notice.setText(titles.subSequence(0,35)+"...");
                            }
                            else
                            {
                                notice.setText(titles);
                            }
                            if (messages.length()>35)

                            {
                                notice_des.setText(messages.subSequence(0,35)+"...");
                            }
                            else
                            {
                                notice_des.setText(messages);
                            }
                            String noticeid;

                            noticeid    = object.getString("noticeBoardId");
                            Log.i("Tag","MyNavi"+ list);
                            if (!noticeid.equals(showpop))
                            {

                                editors.putString("mykey",  noticeid);
                                editors.apply();
                                viewpopup();
                            }

                        }

                        JSONArray lists= objects.getJSONArray("DashBoard MemoBoard Data");
                        for (int i=0;i<lists.length(); i++)
                        {
                            JSONObject object = lists.getJSONObject(i);

                            String title = object.getString("memoTypeName");
                            String message = object.getString("remarks");


                            if (title.length() >35)
                            {
                                memo.setText(title.subSequence(0,35)+"...");
                            }
                            else
                            {
                                memo.setText(title);
                            }
                            if (message.length()>35)

                            {
                                mome_des.setText(message.subSequence(0,35)+"...");
                            }
                            else
                            {
                                mome_des.setText(message);
                            }

                        }



                        JSONArray lists1= objects.getJSONArray("DashBoard Upcoming Holiday Data");
                        Log.i("TAG","MyHolidays"+ lists1);
                        for (int i=0;i<lists1.length(); i++)
                        {
                            JSONObject object = lists1.getJSONObject(i);

                            String title = object.getString("holidayCategoryName");
                            String day = object.getString("dayName");
                            String message = object.getString("holidayCategoryDesc");
                            if (title.length() >35)
                            {
                                hols.setText(title.subSequence(0,35)+"..." +" ("+ day +")");
                            }
                            else
                            {
                                hols.setText(title+"  ("+ day +")");
                            }
                            if (message.length()>35)
                            {
                                hols_des.setText(message.subSequence(0,35)+"...");
                            }
                            else
                            {
                                hols_des.setText(message);
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
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + "appname" + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
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
}

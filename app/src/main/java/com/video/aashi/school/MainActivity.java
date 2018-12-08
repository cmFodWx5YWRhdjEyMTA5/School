package com.video.aashi.school;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.video.aashi.school.adapters.animate.SlidingRootNav;
import com.video.aashi.school.adapters.animate.SlidingRootNavBuilder;
import com.video.aashi.school.adapters.drawer.DrawerAdapter;
import com.video.aashi.school.adapters.drawer.DrawerItem;
import com.video.aashi.school.adapters.drawer.SimpleItem;
import com.video.aashi.school.fragments.Attendace;
import com.video.aashi.school.fragments.ExamCombo;
import com.video.aashi.school.fragments.ExamTables;
import com.video.aashi.school.fragments.Holidays;
import com.video.aashi.school.fragments.HomePage;
import com.video.aashi.school.fragments.Homework;
import com.video.aashi.school.fragments.MainInvoice;
import com.video.aashi.school.fragments.Memos;
import com.video.aashi.school.fragments.payments.Invoice;
import com.video.aashi.school.fragments.NoticeBoard;
import com.video.aashi.school.fragments.TimeTable;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    String username,user_image;
    SharedPreferences sharedPreferences;
    DrawerAdapter adapter;
    public static final String PREF_NAME = "loginstatus";
    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_TIMETABLE = 3;
    private static final int POS_EXAM = 4;
    private static final int POS_HOLIDAYS = 5;
    private static final int POS_MEMOS = 6;
    private static final int POS_CART = 7;
    private static final int POS_INVOICE = 8;
    private static final int POS_PERFO = 9;
    private static final int POS_PROFILE = 10;
    private static final int POS_LOGOUT = 11;
    public  static  String academicyear,student_id,location_id,general_id,class_id,examid;
    TextView studentname;
    RecyclerView list;
    Homework homework= new Homework();
    SharedPreferences.Editor editor;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
    public static String CURRENT_TAG = String.valueOf(POS_DASHBOARD);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("dashboard");

        sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
        username = sharedPreferences.getString("S_name","");
        user_image = sharedPreferences.getString("image","");
        student_id = sharedPreferences.getString("StudentId","");
        academicyear = sharedPreferences.getString("academic","");
        location_id = sharedPreferences.getString("location_id","");
        general_id = sharedPreferences.getString("gen_id","");
        class_id = sharedPreferences.getString("class_id","");
        examid = sharedPreferences.getString("examid","");
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                 .inject();
         loadHomeFragment();

          if (savedInstanceState == null) {
            CURRENT_TAG = String.valueOf(POS_DASHBOARD);
          }
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
      adapter  = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_TIMETABLE),
                createItemFor(POS_EXAM),
                createItemFor(POS_HOLIDAYS),
                createItemFor(POS_MEMOS),
                createItemFor(POS_CART),
                createItemFor(POS_INVOICE),
                createItemFor(POS_PERFO),
                createItemFor(POS_PROFILE),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);
       list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_DASHBOARD);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //    getMenuInflater().inflate(R.menu.multiply,menu);

        return true;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {

            editor = sharedPreferences.edit();
            editor.remove("isLoginKey");
            editor.clear();
            editor.apply();
            Intent intent=new Intent(MainActivity.this,Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (position == POS_DASHBOARD)
        {
            HomePage dashboard = new HomePage();

            showFragment(dashboard);

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

        }
        if(position == POS_TIMETABLE)
        {
            CURRENT_TAG = String.valueOf(POS_TIMETABLE);
            TimeTable timeTable= new TimeTable();
            showFragment(timeTable);
        }
        if(position == POS_CART)
        {

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
            ExamTables examTables = new ExamTables();
            showFragment(examTables);

        }
        if (position == POS_PERFO)
        {
            ExamCombo performance = new ExamCombo();
            showFragment(performance);
        }
        if(position == POS_INVOICE)
        {
            MainInvoice invoicec = new MainInvoice();
            showFragment(invoicec);
        }

        if (position == POS_PROFILE)
        {
            startActivity(new Intent(MainActivity.this,ProfileView.class));
        }
        slidingRootNav.closeMenu();

    }

    private void showFragment(android.support.v4.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.black1))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.flutter_blue))
                .withSelectedTextTint(color(R.color.flutter_blue));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    public void loadHomeFragment()
    {
        HomePage dashboard= new HomePage();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, dashboard);
        fragmentTransaction.commitAllowingStateLoss();
        invalidateOptionsMenu();
    }

    boolean crateMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.share,menu);
        return true;
    }
}

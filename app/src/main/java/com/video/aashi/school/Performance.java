package com.video.aashi.school;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.video.aashi.school.fragments.Charts;
import com.video.aashi.school.fragments.ExamCombo;
import com.video.aashi.school.fragments.Tables;

import java.util.ArrayList;
import java.util.List;

public class Performance extends Fragment {
    android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
   public static String groupid,termname,groupname,maxmarks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_performance, container, false);
        toolbar = (android.support.v7.widget.Toolbar)getActivity(). findViewById(R.id.toolbar);
        toolbar.setTitle("Performance");
        viewPager = (ViewPager)view. findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout)view. findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#20A4E8")));
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            groupname =bundle.getString("examtype");
            termname = bundle.getString("terms");
            groupid = bundle.getString("groupid");
            maxmarks = bundle.getString("maxmarks");

        }
        return  view;

    }

    private void setupViewPager(ViewPager viewPager)
    {


        String marks = "Marks in charts";
        String table = "Marks in Table";
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Charts(), marks);
        adapter.addFragment(new Tables(), table);
        viewPager.setAdapter(adapter);
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
                    Bundle bundle= new Bundle();
                    bundle.putString("combo","1");
                    bundle.putString("check","1");
                    ExamCombo examTables = new ExamCombo();
                    examTables.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.mycontainer,examTables).commit();
                   return true;
                                }
                return false;
            }
        });
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        //menu.findItem(R.id.history).setVisible(false);
        inflater.inflate(R.menu.multiply,menu);
        if (menu.findItem(R.id.history) != null)
            menu.findItem(R.id.history).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

package com.video.aashi.school.fragments;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.video.aashi.school.R;
import com.video.aashi.school.fragments.payments.Others;
import com.video.aashi.school.fragments.payments.Weekoffs;
import java.util.ArrayList;
import java.util.List;

public class Holidays extends Fragment {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager pager;
    public static String POSITION = "POSITION";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_holidays, container, false);
        toolbar =(android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle("Holidays");
        getActivity().invalidateOptionsMenu();
        tabLayout =(TabLayout)view.findViewById(R.id.tabs_holidays);
        pager =(ViewPager)view.findViewById(R.id.viewpager_holidays);
        setupViewPager(pager);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#20A4E8")));
        return  view;
    }
    private void setupViewPager(ViewPager viewPager)
    {

        String marks = "All";
        String table = "Festivels";
       ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Weekoffs(), marks);
        adapter.addFragment(new Others(), table);
        viewPager.setAdapter(adapter);

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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.dashboard,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}

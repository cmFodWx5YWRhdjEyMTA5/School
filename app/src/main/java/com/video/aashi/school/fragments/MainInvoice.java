package com.video.aashi.school.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.video.aashi.school.Performance;
import com.video.aashi.school.R;
import com.video.aashi.school.fragments.payments.Invoice;
import com.video.aashi.school.fragments.payments.Paid;
import com.video.aashi.school.fragments.payments.UnPaid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainInvoice extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    //private BadgeView badge;
    Toolbar toolbar;
    public MainInvoice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_invoice, container, false);
        viewPager = (ViewPager)view. findViewById(R.id.viewpager_invoice);
        setupViewPager(viewPager);
        tabLayout = (TabLayout)view. findViewById(R.id.tabs_invoice);
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#20A4E8")));

        toolbar =(android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Invoice");
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        TabLayout.Tab tab = new TabLayout.Tab();
        if(tab.getCustomView() != null) {
            TextView b = (TextView) tab.getCustomView().findViewById(R.id.badge);
            if(b != null) {
                b.setText("hello");
            }
            View v = tab.getCustomView().findViewById(R.id.badgeCotainer);
            if(v != null) {
                v.setVisibility(View.VISIBLE);
            }
        }
        return  view;
    }
    private void setupViewPager(ViewPager viewPager)
    {

        String marks = "All";
        String table = "Un paid";
        String paid ="Paid";
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Invoice(), marks);
        adapter.addFragment(new UnPaid(), table);
        adapter.addFragment(new Paid(),paid);
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

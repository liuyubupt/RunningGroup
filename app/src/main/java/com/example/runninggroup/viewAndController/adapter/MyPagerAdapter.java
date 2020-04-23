package com.example.runninggroup.viewAndController.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mStringArrayList;

    public MyPagerAdapter(@NonNull FragmentManager fm,ArrayList<Fragment> fragments ,ArrayList<String> strings) {
        super(fm);
        this.mFragments = fragments;
        this.mStringArrayList = strings;
    }


//    public MyPagerAdapter(@NonNull FragmentManager fm, int behavior ,ArrayList<Fragment> fragments ,ArrayList<String> strings) {
//        super(fm, behavior);
//        this.mFragments = fragments;
//        this.mStringArrayList = strings;
//    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mStringArrayList.get(position);
    }
}

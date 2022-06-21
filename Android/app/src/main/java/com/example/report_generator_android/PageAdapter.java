package com.example.report_generator_android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    int count;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        count=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return new DashboardFragment();
        else if(position==1)
            return new ReportsFragment();
        return new EmailFragment();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        if(position==0)
            return "Dashboard";
        else if(position==1)
            return "Reports";
        return "Email";
    }

    @Override
    public int getCount() {
        return 3;
    }
}

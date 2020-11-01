package com.example.el_wi.classforum;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1:
                ScheduleFragment scheduleFragment= new ScheduleFragment();
                return scheduleFragment;
            case 2:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

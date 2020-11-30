package com.brannik.system.ui.main;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.brannik.system.MainActivity;
import com.brannik.system.R;
import com.brannik.system.admin;
import com.brannik.system.extra;
import com.brannik.system.myAccount;
import com.brannik.system.shifts;
import com.brannik.system.sumary;
import com.brannik.system.sundays;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3,
            R.string.tab_text_4,
            R.string.tab_text_5,
            R.string.tab_text_6};

    @DrawableRes
    private static final int[] TAB_ICONS = new int[]{

    };

    private final Context mContext;
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment=null;
        switch(position){
            case 0:
                fragment = sumary.newInstance(MainActivity.devId,"sumary");
                break;
            case 1:
                fragment = shifts.newInstance(MainActivity.devId,"shifts");
                break;
            case 2:
                fragment = sundays.newInstance(MainActivity.devId,"sundays");
                break;
            case 3:
                fragment = extra.newInstance(MainActivity.devId,"extra");
                break;
            case 5:
                fragment = admin.newInstance(MainActivity.devId,"admin");
                break;
            case 4:
                fragment = myAccount.newInstance(MainActivity.devId,"ACC");
                break;
        }
        return fragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        if(MainActivity.getUserRank() > 1){
            return 6;
        }else{
            return 5;
        }

    }
}
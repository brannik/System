package com.brannik.system.ui.main;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.brannik.system.GlobalVariables;
import com.brannik.system.MainActivity;
import com.brannik.system.R;
import com.brannik.system.AdminCP;
import com.brannik.system.ExtraDHMain;
import com.brannik.system.SettingsMainFrame;
import com.brannik.system.ShiftsMainFrame;
import com.brannik.system.HomeScreenMainFrame;
import com.brannik.system.DocumentsMainFrame;
/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    GlobalVariables GLOBE = new GlobalVariables(MainActivity.getAppContext());
    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3,
            R.string.tab_text_4,
            R.string.tab_text_5,
            R.string.tab_text_7,
        };

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
                fragment = HomeScreenMainFrame.newInstance(GlobalVariables.getDevId(),"username","names","rank");
                break;
            case 1:
                fragment = ShiftsMainFrame.newInstance(GlobalVariables.getDevId(),"shifts");
                break;
            case 2:
                fragment = DocumentsMainFrame.newInstance(GlobalVariables.getDevId(),"sundays");
                break;
            case 3:
                fragment = ExtraDHMain.newInstance(GlobalVariables.getDevId(),"extra");
                break;
            case 4:
                fragment = SettingsMainFrame.newInstance(GlobalVariables.getDevId(),"ACC");
                break;
            case 5:
                fragment = AdminCP.newInstance(GlobalVariables.getDevId(),"admin");
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
        if(GLOBE.userRank() < 2){
            return 5;
        }else{
            return 6;
        }

    }
}
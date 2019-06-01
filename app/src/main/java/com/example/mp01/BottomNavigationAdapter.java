package com.example.mp01;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mp01.Fragment.Menu1Fragment;
import com.example.mp01.Fragment.Menu2Fragment;
import com.example.mp01.Fragment.Menu3Fragment;

public class BottomNavigationAdapter extends FragmentStatePagerAdapter {

    BottomNavigationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Menu1Fragment();
            case 1:
                return new Menu2Fragment();
            default:
                return new Menu3Fragment();
        }

    }
    @Override
    public int getCount() {
        return 3;
    }

}

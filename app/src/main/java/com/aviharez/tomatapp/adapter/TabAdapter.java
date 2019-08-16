package com.aviharez.tomatapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.aviharez.tomatapp.fragment.*;

public class TabAdapter extends FragmentStatePagerAdapter {

    int mnooftabs;

    public TabAdapter(FragmentManager fm, int mnooftabs) {
        super(fm);
        this.mnooftabs = mnooftabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                SmartOverviewFragment tab1 = new SmartOverviewFragment();
                return tab1;
            case 1:
                SmartCharFragment tab2 = new SmartCharFragment();
                return tab2;
            case 2:
                SmartInfoFragment tab3 = new SmartInfoFragment();
                return tab3;
            case 3:
                SmartFlagsFragment tab4 = new SmartFlagsFragment();
                return tab4;
            case 4:
                SmartEquipmentFragment tab5 = new SmartEquipmentFragment();
                return tab5;
            case 5:
                SmartPoFragment tab6 = new SmartPoFragment();
                return tab6;
            case 6:
                SmartDocFragment tab7 = new SmartDocFragment();
                return tab7;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 7;
    }
}
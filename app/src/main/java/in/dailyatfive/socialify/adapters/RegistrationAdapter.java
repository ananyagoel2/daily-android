package in.dailyatfive.socialify.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.dailyatfive.socialify.fragments.RegEmailFragment;
import in.dailyatfive.socialify.fragments.RegMobileFragment;
import in.dailyatfive.socialify.fragments.RegProfileFragment;

public class RegistrationAdapter extends FragmentStatePagerAdapter {

    public RegistrationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0 : return new RegProfileFragment();
            case 1 : return new RegEmailFragment();
            case 2 : return new RegMobileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

package in.chefsway.chefsway.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.chefsway.chefsway.fragments.RegEmailFragment;
import in.chefsway.chefsway.fragments.RegMobileFragment;
import in.chefsway.chefsway.fragments.RegProfileFragment;

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

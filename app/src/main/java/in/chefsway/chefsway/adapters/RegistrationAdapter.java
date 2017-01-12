package in.chefsway.chefsway.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.chefsway.chefsway.fragments.RegEmailFragment;
import in.chefsway.chefsway.fragments.RegMobileFragment;
import in.chefsway.chefsway.fragments.RegProfileFragment;
import in.chefsway.chefsway.fragments.RegistrationFragment;

public class RegistrationAdapter extends FragmentStatePagerAdapter {

    public RegistrationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0 : return RegistrationFragment.newInstance("name");
            case 1 : return RegistrationFragment.newInstance("email");
            case 2 : return RegistrationFragment.newInstance("mobile");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

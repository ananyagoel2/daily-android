package in.dailyatfive.socialify.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.dailyatfive.socialify.fragments.RegEmailFragment;
import in.dailyatfive.socialify.fragments.RegMobileFragment;
import in.dailyatfive.socialify.fragments.RegProfileFragment;
import in.dailyatfive.socialify.models.UserModel;

public class RegistrationAdapter extends FragmentStatePagerAdapter {

    private UserModel userModel;

    public RegistrationAdapter(FragmentManager fm, UserModel userModel) {
        super(fm);
        this.userModel = userModel;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0 : return RegProfileFragment.newInstance(userModel);
            case 1 : return RegEmailFragment.newInstance(userModel);
            case 2 : return RegMobileFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

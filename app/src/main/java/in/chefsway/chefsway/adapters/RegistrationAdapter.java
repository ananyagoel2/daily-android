package in.chefsway.chefsway.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Arrays;
import java.util.List;

import in.chefsway.chefsway.fragments.RegistrationFragment;

public class RegistrationAdapter extends FragmentStatePagerAdapter {

    private List<String> RegistrationFields;

    public RegistrationAdapter(FragmentManager fm) {
        super(fm);
        RegistrationFields = Arrays.asList("email","name","mobile");
    }

    @Override
    public Fragment getItem(int position) {
        return RegistrationFragment.newInstance(RegistrationFields.get(position));
    }

    @Override
    public int getCount() {
        return RegistrationFields.size();
    }
}

package in.chefsway.chefsway.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.chefsway.chefsway.fragments.RecipeFragment;
import in.chefsway.chefsway.fragments.ShopFragment;

public class MainAdapter extends FragmentStatePagerAdapter {
    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 : return new RecipeFragment();
            case 1 : return new ShopFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

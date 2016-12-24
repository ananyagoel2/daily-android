package in.dailyatfive.socialify.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.fragments.SplashSlideshowFragment;

public class SplashSlideshowAdapter extends FragmentStatePagerAdapter {

    private int pages;

    public SplashSlideshowAdapter(FragmentManager fm, int pages) {
        super(fm);
        this.pages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return new SplashSlideshowFragment(R.drawable.splash_slide_1);
    }

    @Override
    public int getCount() {
        return pages;
    }
}
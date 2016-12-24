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
        switch (position){
            case 0 : return SplashSlideshowFragment.newInstance(R.drawable.splash_slide_1);
            case 1 : return SplashSlideshowFragment.newInstance(R.drawable.splash_slide_2);
            case 2 : return SplashSlideshowFragment.newInstance(R.drawable.splash_slide_3);
            case 3 : return SplashSlideshowFragment.newInstance(R.drawable.splash_slide_4);
        }
        return SplashSlideshowFragment.newInstance(R.drawable.splash_slide_1);
    }

    @Override
    public int getCount() {
        return pages;
    }
}
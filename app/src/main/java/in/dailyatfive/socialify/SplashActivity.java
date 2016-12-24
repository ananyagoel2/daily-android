package in.dailyatfive.socialify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import in.dailyatfive.socialify.adapters.SplashSlideshowAdapter;
import in.dailyatfive.socialify.models.UserModel;

public class SplashActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager splashPager;
    private FragmentManager fragmentManager;

    private static int NUM_PAGES = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("PREF_FILE", Context.MODE_PRIVATE);
        if(UserModel.isLoggedIn(sharedPreferences)) {
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        fragmentManager = getSupportFragmentManager();

        setContentView(R.layout.activity_splash);

        splashPager = (ViewPager) findViewById(R.id.splash_pager);
        SplashSlideshowAdapter splashSlideshowAdapter = new SplashSlideshowAdapter(getSupportFragmentManager(),NUM_PAGES);
        splashPager.setAdapter(splashSlideshowAdapter);
        splashPager.setCurrentItem(0);

        splashPager.addOnPageChangeListener(this);

        findViewById(R.id.fb_login_button).setVisibility(View.GONE);


    }

    @Override
    public void onBackPressed() {
        if (splashPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            splashPager.setCurrentItem(splashPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == NUM_PAGES-1) {
            findViewById(R.id.fb_login_button).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

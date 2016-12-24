package in.dailyatfive.socialify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import in.dailyatfive.socialify.adapters.SplashSlideshowAdapter;

public class SplashActivity extends BaseActivity {

    private ViewPager splashPager;

    private static int NUM_PAGES = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
//        startActivity(intent);
//        finish();

        setContentView(R.layout.activity_splash);

        splashPager = (ViewPager) findViewById(R.id.splash_pager);
        SplashSlideshowAdapter splashSlideshowAdapter = new SplashSlideshowAdapter(getSupportFragmentManager(),NUM_PAGES);

        splashPager.setAdapter(splashSlideshowAdapter);
        splashPager.setCurrentItem(0);

    }

    @Override
    public void onBackPressed() {
        if (splashPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            splashPager.setCurrentItem(splashPager.getCurrentItem() - 1);
        }
    }
}

package in.chefsway.chefsway;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import in.chefsway.chefsway.adapters.SplashSlideshowAdapter;
import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.ui.LoginActivity;

public class SplashActivity extends BaseActivity {

    private ViewPager splashPager;
    private TabLayout splashPagerIndicator;

    private static int NUM_PAGES = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SessionHelper.isLoggedIn(sharedPreferences)) {

            // Login and refresh token

        } else {

            setContentView(R.layout.activity_splash);

            splashPager = (ViewPager) findViewById(R.id.splash_pager);
            SplashSlideshowAdapter splashSlideshowAdapter = new SplashSlideshowAdapter(getSupportFragmentManager(), NUM_PAGES);
            splashPager.setAdapter(splashSlideshowAdapter);
            splashPager.setCurrentItem(0);

            splashPagerIndicator = (TabLayout) findViewById(R.id.splash_pager_indicator);
            splashPagerIndicator.setupWithViewPager(splashPager, true);

            Button getStartedButton = (Button) findViewById(R.id.get_started_button);

            getStartedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }


    }

    @Override
    public void onBackPressed() {
        if (splashPager == null || splashPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            splashPager.setCurrentItem(splashPager.getCurrentItem() - 1);
        }
    }

}

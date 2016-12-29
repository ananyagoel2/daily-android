package in.dailyatfive.socialify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import in.dailyatfive.socialify.adapters.SplashSlideshowAdapter;
import in.dailyatfive.socialify.fragments.SplashSlideshowFragment;
import in.dailyatfive.socialify.helper.SessionHelper;
import in.dailyatfive.socialify.network.API;
import in.dailyatfive.socialify.network.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager splashPager;

    private static int NUM_PAGES = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(SplashActivity.this);
        AppEventsLogger.activateApp(SplashActivity.this);

        if(SessionHelper.isLoggedIn(sharedPreferences)) {

            User user = SessionHelper.getUser(sharedPreferences);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            API api = retrofit.create(API.class);
            retrofit2.Call<User> getUserCall = api.getUser(
                    user.getId(),"JWT "+SessionHelper.getJwtToken(sharedPreferences));

            getUserCall.enqueue(callback);
            setContentView(R.layout.activity_splash_static);

            FragmentManager fm = getSupportFragmentManager();
            SplashSlideshowFragment fragment = SplashSlideshowFragment.newInstance(R.drawable.splash_slide_1);
            fm.beginTransaction().add(R.id.activity_splash_static, fragment ).commit();

        } else {

            setContentView(R.layout.activity_splash);

            splashPager = (ViewPager) findViewById(R.id.splash_pager);
            SplashSlideshowAdapter splashSlideshowAdapter = new SplashSlideshowAdapter(getSupportFragmentManager(), NUM_PAGES);
            splashPager.setAdapter(splashSlideshowAdapter);
            splashPager.setCurrentItem(0);

            splashPager.addOnPageChangeListener(this);

            findViewById(R.id.fb_login_button).setVisibility(View.GONE);
        }


    }

    private Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            int code = response.code();
            if(code == 200) {
                User user = response.body();
                SessionHelper.saveUser(sharedPreferences,user);
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            } else if (code == 401) {
                SessionHelper.clearUser(sharedPreferences);
                Intent intent = new Intent(SplashActivity.this,SplashActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SplashActivity.this," Error : " + code ,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            Toast.makeText(SplashActivity.this," Error : Something went wrong ..! ",Toast.LENGTH_SHORT).show();
        }
    };

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

package in.chefsway.chefsway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import in.chefsway.chefsway.adapters.SplashSlideshowAdapter;
import in.chefsway.chefsway.fragments.SplashSlideshowFragment;
import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.network.API;
import in.chefsway.chefsway.network.models.Register;
import in.chefsway.chefsway.network.models.User;
import in.chefsway.chefsway.ui.UpdateActivity;
import in.chefsway.chefsway.utils.Constants;
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

            setContentView(R.layout.activity_splash_static);

            FragmentManager fm = getSupportFragmentManager();
            SplashSlideshowFragment fragment = SplashSlideshowFragment.newInstance(R.drawable.splash_slide_1);
            fm.beginTransaction().add(R.id.activity_splash_static, fragment ).commit();

            User user = SessionHelper.getUser(sharedPreferences);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            API api = retrofit.create(API.class);
            retrofit2.Call<Register> getUserCall = api.registerUser(user.getFacebookId(),user.getFacebook().getToken());

            getUserCall.enqueue(callback);

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

    private Callback<Register> callback = new Callback<Register>() {
        @Override
        public void onResponse(Call<Register> call, Response<Register> response) {
            int code = response.code();
            if(code == 200) {
                Register register = response.body();
                findViewById(R.id.fb_login_button).setVisibility(View.GONE);
                // Check version
                if(Integer.parseInt(register.getMinVersionCode()) > Constants.API_VERSION_CODE ) {
                    Intent intent = new Intent(SplashActivity.this, UpdateActivity.class);
                    intent.putExtra("force",true);
                    startActivity(intent);
                    finish();
                } else if (Integer.parseInt(register.getCurrentVersionCode()) > Constants.API_VERSION_CODE ) {
                    Intent intent = new Intent(SplashActivity.this, UpdateActivity.class);
                    startActivity(intent);
                } else {
                    User user = register.getUser();
                    SessionHelper.saveUser(sharedPreferences, user);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
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
        public void onFailure(Call<Register> call, Throwable t) {
            Toast.makeText(SplashActivity.this," Error : Something went wrong ..! ",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        if (splashPager == null || splashPager.getCurrentItem() == 0) {
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

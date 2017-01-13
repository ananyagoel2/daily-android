package in.chefsway.chefsway;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import in.chefsway.chefsway.adapters.SplashSlideshowAdapter;
import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.network.API;
import in.chefsway.chefsway.network.models.User;
import in.chefsway.chefsway.ui.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends BaseActivity {

    private ViewPager splashPager;
    private TabLayout splashPagerIndicator;

    private static int NUM_PAGES = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SessionHelper.isLoggedIn(sharedPreferences)) {

            User user = SessionHelper.getUser(sharedPreferences);
            String token = "JWT "+ SessionHelper.getJwtToken(sharedPreferences);

            setContentView(R.layout.activity_splash_static);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            API api = retrofit.create(API.class);
            retrofit2.Call<User> getUserCall = api.getUser(user.getId(),token);

            getUserCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    int code = response.code();
                    if(code == 200) {

                        SessionHelper.saveUser(sharedPreferences,response.body());

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else if( code == 401 ) {
                        Toast.makeText(SplashActivity.this,"Session Expired. Login again.",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(SplashActivity.this,"Error : Something went wrong..",Toast.LENGTH_LONG).show();
                }
            });

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

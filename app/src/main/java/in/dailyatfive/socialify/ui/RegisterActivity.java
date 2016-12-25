package in.dailyatfive.socialify.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import in.dailyatfive.socialify.BaseActivity;
import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.adapters.RegistrationAdapter;
import in.dailyatfive.socialify.models.UserModel;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SharedPreferences sharedPreferences = getSharedPreferences("PREF_FILE", Context.MODE_PRIVATE);
        UserModel userModel = UserModel.getUser(sharedPreferences);

        ViewPager registerPager = (ViewPager) findViewById(R.id.registration_pager);
        RegistrationAdapter registrationAdapter = new RegistrationAdapter(getSupportFragmentManager(),userModel);

        registerPager.setAdapter(registrationAdapter);
        registerPager.setCurrentItem(0);
    }
}

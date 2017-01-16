package in.chefsway.chefsway.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import in.chefsway.chefsway.BaseActivity;
import in.chefsway.chefsway.MainActivity;
import in.chefsway.chefsway.R;
import in.chefsway.chefsway.adapters.MainAdapter;
import in.chefsway.chefsway.adapters.RegistrationAdapter;
import in.chefsway.chefsway.custom.NonSwipableViewPager;
import in.chefsway.chefsway.fragments.RegistrationFragment;
import in.chefsway.chefsway.helper.SessionHelper;

public class RegisterActivity extends BaseActivity implements RegistrationFragment.RegFragmentCallback {

    private NonSwipableViewPager registerPager;
    private RegistrationAdapter registrationAdapter;
    private int firstItemIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
            actionBar.setTitle("Register");
        }


        registerPager = (NonSwipableViewPager) findViewById(R.id.registration_pager);

        registrationAdapter = new RegistrationAdapter(getSupportFragmentManager());

        registerPager.setAdapter(registrationAdapter);
        if(SessionHelper.isLoggedIn(sharedPreferences)) {
            firstItemIndex = 2;
        } else {
            firstItemIndex = 0;
        }
        registerPager.setCurrentItem(firstItemIndex);

    }

    @Override
    public void goToNextPage() {
        if(registerPager.getCurrentItem() == registrationAdapter.getCount()-1) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            registerPager.setCurrentItem(registerPager.getCurrentItem()+1,true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(registerPager.getCurrentItem() == firstItemIndex) {
            super.onBackPressed();
        } else {
            registerPager.setCurrentItem(registerPager.getCurrentItem()-1,true);
        }
    }
}

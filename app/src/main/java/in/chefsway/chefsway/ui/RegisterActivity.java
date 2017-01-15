package in.chefsway.chefsway.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import in.chefsway.chefsway.BaseActivity;
import in.chefsway.chefsway.R;
import in.chefsway.chefsway.adapters.RegistrationAdapter;
import in.chefsway.chefsway.custom.NonSwipableViewPager;
import in.chefsway.chefsway.fragments.RegistrationFragment;
import in.chefsway.chefsway.helper.SessionHelper;

public class RegisterActivity extends BaseActivity implements RegistrationFragment.RegFragmentCallback {

    private NonSwipableViewPager registerPager;
    private int registerPageCount;

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

        final RegistrationAdapter registrationAdapter = new RegistrationAdapter(getSupportFragmentManager());
        registerPageCount = registrationAdapter.getCount();

        registerPager.setAdapter(registrationAdapter);
        if(SessionHelper.isLoggedIn(sharedPreferences)) {
            registerPager.setCurrentItem(2); // After email and password
        } else {
            registerPager.setCurrentItem(0);
        }

    }

    @Override
    public void goToNextPage() {
        registerPager.setCurrentItem(registerPager.getCurrentItem()+1,true);
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
        if(registerPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            registerPager.setCurrentItem(registerPager.getCurrentItem()-1,true);
        }
    }
}

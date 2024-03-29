package in.dailyatfive.socialify.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import in.dailyatfive.socialify.BaseActivity;
import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.adapters.RegistrationAdapter;
import in.dailyatfive.socialify.custom.NonSwipableViewPager;
import in.dailyatfive.socialify.fragments.RegEmailFragment;
import in.dailyatfive.socialify.fragments.RegMobileFragment;
import in.dailyatfive.socialify.fragments.RegProfileFragment;

public class RegisterActivity extends BaseActivity implements ViewPager.OnPageChangeListener,RegEmailFragment.RegCallback {

    private NonSwipableViewPager registerPager;
    private int registerPageCount;

    private Button prevButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerPager = (NonSwipableViewPager) findViewById(R.id.registration_pager);

        final RegistrationAdapter registrationAdapter = new RegistrationAdapter(getSupportFragmentManager());
        registerPageCount = registrationAdapter.getCount();

        prevButton = (Button) findViewById(R.id.prev_button);
        nextButton = (Button) findViewById(R.id.next_button);

        registerPager.setAdapter(registrationAdapter);
        registerPager.setCurrentItem(0);
        registerPager.addOnPageChangeListener(this);

        prevButton.setVisibility(View.INVISIBLE);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPager.setCurrentItem(registerPager.getCurrentItem()-1,true);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = registerPager.getCurrentItem();
                Object fragment = registrationAdapter.instantiateItem(registerPager,currentPosition);
                if( fragment instanceof RegProfileFragment) {
                    ((RegProfileFragment) fragment).submit();
                } else if( fragment instanceof RegEmailFragment) {
                    ((RegEmailFragment) fragment).submit();
                } else if( fragment instanceof RegMobileFragment) {
                    ((RegMobileFragment) fragment).submit();
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if( registerPager.getCurrentItem()  == registerPageCount ){
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
        if( registerPager.getCurrentItem() == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void goToNextPage() {
        registerPager.setCurrentItem(registerPager.getCurrentItem()+1,true);
    }
}

package in.chefsway.chefsway;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.squareup.picasso.Picasso;

import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.network.models.User;
import in.chefsway.chefsway.ui.ProfileActivity;
import in.chefsway.chefsway.ui.RegisterActivity;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerArrowDrawable drawerArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SessionHelper.isLoggedIn(sharedPreferences)) {
            User user = SessionHelper.getUser(sharedPreferences);
            if( user.getIsNewUser() ) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                //finish();
            }
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (LinearLayout) findViewById(R.id.navigation_drawer);

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,0,0) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(drawerArrow);
        }

        final MaterialTabHost materialTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);

        MaterialTabListener listener = new MaterialTabListener() {
            @Override
            public void onTabSelected(MaterialTab tab) {
                materialTabHost.setSelectedNavigationItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(MaterialTab tab) {
            }

            @Override
            public void onTabUnselected(MaterialTab tab) {

            }
        };

        materialTabHost.addTab(materialTabHost.newTab().setText("Recipe").setTabListener(listener));
        materialTabHost.addTab(materialTabHost.newTab().setText("Shop").setTabListener(listener));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
                return true;
            case R.id.action_settings:
                Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

}

package com.dataart.vyakunin.coubplayer.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dataart.vyakunin.coubplayer.R;
import com.dataart.vyakunin.coubplayer.ui.fragments.HomeFragment;

import org.androidannotations.annotations.EActivity;

@EActivity
public class HomeActivity extends BaseActivity {

    private ActionBarDrawerToggle toggle;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_home);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_widget));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
//        switchTo(HomeFragment.newInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle != null && toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (toggle != null)
            toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (toggle != null)
            toggle.onConfigurationChanged(newConfig);
    }
}

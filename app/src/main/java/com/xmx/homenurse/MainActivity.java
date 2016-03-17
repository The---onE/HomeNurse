package com.xmx.homenurse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.xmx.homenurse.ActivityBase.BaseNavigationActivity;
import com.xmx.homenurse.Data.DataManager;
import com.xmx.homenurse.Fragment.AppointmentFragment;
import com.xmx.homenurse.Fragment.HomeFragment;
import com.xmx.homenurse.Fragment.MeFragment;
import com.xmx.homenurse.Fragment.PagerAdapter;
import com.xmx.homenurse.Fragment.RecordFragment;
import com.xmx.homenurse.User.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseNavigationActivity {
    private long mExitTime = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        if (!DataManager.getInstance().isLoggedIn()) {
            startActivity(LoginActivity.class);
            finish();
        }

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new RecordFragment());
        fragments.add(new AppointmentFragment());
        fragments.add(new MeFragment());

        List<String> titles = new ArrayList<>();
        titles.add("首页");
        titles.add("记录");
        titles.add("预约");
        titles.add("我");

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments, titles);

        ViewPager vp = (ViewPager) findViewById(R.id.pager_main);
        vp.setAdapter(adapter);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
                showToast(R.string.confirm_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

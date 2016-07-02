package com.chantra.lampscrap.balancing.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.chantra.lampscrap.balancing.R;
import com.chantra.lampscrap.balancing.databinding.ActivityMainBinding;
import com.chantra.lampscrap.balancing.respository.RealmHelper;
import com.chantra.lampscrap.balancing.respository.objects.UserRealm;
import com.chantra.lampscrap.balancing.utils.SessionManager;

import io.realm.Case;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavViewLeft;
    private NavigationView mNavViewRight;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);

        mDrawerLayout = mBinding.drawer;
        mNavViewLeft = mBinding.navLeft;
        mNavViewRight = mBinding.navRight;
        mBinding.navGroupLeft.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mBinding.toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerView.getId() == mBinding.navLeft.getId()) {
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                mToggle.syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        mToggle.syncState();

        mBinding.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSigOut();
            }
        });
    }

    public void doSigOut() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing out...");
        progressDialog.show();
        final String accessToken = SessionManager.init(getApplicationContext()).getAccessToken();
        Realm realm = RealmHelper.init(getApplicationContext()).getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserRealm userRealm = RealmHelper.init(getApplicationContext()).doQuery(UserRealm.class).equalTo("accessToken", accessToken, Case.SENSITIVE).findFirst();
                if (null != userRealm)
                    userRealm.setAccessToken("");
                else {
                    SessionManager.init(getApplicationContext()).reset();
                    goLogin();
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (null != progressDialog)
                    progressDialog.dismiss();
                SessionManager.init(getApplicationContext()).reset();
                goLogin();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (null != progressDialog)
                    progressDialog.dismiss();
            }
        });
    }

    private void goLogin() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_option:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

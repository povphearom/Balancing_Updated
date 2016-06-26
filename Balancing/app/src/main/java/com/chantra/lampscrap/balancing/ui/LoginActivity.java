package com.chantra.lampscrap.balancing.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chantra.lampscrap.balancing.R;
import com.chantra.lampscrap.balancing.databinding.ActivityLoginBinding;
import com.chantra.lampscrap.balancing.utils.SessionManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    private void doLogin() {
        SessionManager.init(this).setIsLogin(true);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}


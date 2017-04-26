package com.xmx.androidframeworkbase.module.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.xmx.androidframeworkbase.common.user.IUserManager;
import com.xmx.androidframeworkbase.common.user.UserConstants;
import com.xmx.androidframeworkbase.common.user.UserData;
import com.xmx.androidframeworkbase.common.user.UserManager;
import com.xmx.androidframeworkbase.core.Constants;
import com.xmx.androidframeworkbase.core.activity.MainActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.base.activity.BaseActivity;
import com.xmx.androidframeworkbase.common.user.callback.LoginCallback;
import com.xmx.androidframeworkbase.utils.ExceptionUtil;

public class LoginActivity extends BaseActivity {
    private long mExitTime = 0;
    public boolean mustFlag = false;

    private IUserManager userManager = UserManager.getInstance();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void setListener() {
        final Button login = getViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView usernameView = getViewById(R.id.tv_username);
                String username = usernameView.getText().toString();
                EditText passwordView = getViewById(R.id.tv_password);
                String password = passwordView.getText().toString();
                if (username.equals("")) {
                    showToast(R.string.username_empty);
                } else if (password.equals("")) {
                    showToast(R.string.password_empty);
                } else {
                    login.setEnabled(false);
                    userManager.login(username, password,
                            new LoginCallback() {
                                @Override
                                public void success(UserData user) {
                                    showToast(R.string.login_success);
                                    if (mustFlag) {
                                        startActivity(MainActivity.class);
                                    }
                                    Intent i = new Intent();
                                    setResult(RESULT_OK, i);
                                    finish();
                                }

                                @Override
                                public void error(AVException e) {
                                    showToast(R.string.network_error);
                                    ExceptionUtil.normalException(e, getBaseContext());
                                    login.setEnabled(true);
                                }

                                @Override
                                public void error(int error) {
                                    switch (error) {
                                        case UserConstants.USERNAME_ERROR:
                                            showToast(R.string.username_error);
                                            login.setEnabled(true);
                                            break;
                                        case UserConstants.PASSWORD_ERROR:
                                            showToast(R.string.password_error);
                                            login.setEnabled(true);
                                            break;
                                    }
                                }
                            });
                }
            }
        });

        Button register = getViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class),
                        UserConstants.REGISTER_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        if (mustFlag) {
            if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
                showToast(R.string.confirm_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 若从注册页注册成功返回
        if (requestCode == UserConstants.REGISTER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        }
    }
}

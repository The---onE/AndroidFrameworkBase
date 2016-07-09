package com.xmx.androidframeworkbase.User;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.androidframeworkbase.Constants;
import com.xmx.androidframeworkbase.MainActivity;
import com.xmx.androidframeworkbase.R;
import com.xmx.androidframeworkbase.Tools.ActivityBase.BaseActivity;
import com.xmx.androidframeworkbase.User.Callback.LoginCallback;

public class LoginActivity extends BaseActivity {
    private long mExitTime = 0;

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
                    UserManager.getInstance().login(username, password,
                            new LoginCallback() {
                                @Override
                                public void success(AVObject user) {
                                    showToast(R.string.login_success);
                                    startActivity(MainActivity.class);
                                    finish();
                                }

                                @Override
                                public void error(AVException e) {
                                    showToast(R.string.network_error);
                                    filterException(e);
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
                startActivity(RegisterActivity.class);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
            showToast(R.string.confirm_exit);
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}

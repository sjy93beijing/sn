package com.example.sjy.snote.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjy.snote.R;
import com.example.sjy.snote.db.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.AccountUtils;
import utils.SP;

/**
 * Created by sjy_1993 on 2017/4/7.
 */
public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.btn_register)
    TextView btnRegister;
    @BindView(R.id.login_name)
    EditText loginName;
    @BindView(R.id.login_pwd)
    EditText loginPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_register, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                break;
            case R.id.btn_login:
                login();//调用登录方法，获取登录数据

                break;
        }
    }

    private void login() {
        String mlogin_name = loginName.getText().toString();
        String mlogin_pwd = loginPwd.getText().toString();
        //注册一个progressDialog
        ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        //正在登录
        progress.setMessage("正在登录...");
        progress.show();
        //判断
        if("admin".equals(mlogin_name)|| "123456".equals(mlogin_pwd)) {
            User user = new User();
            user.setUsername(mlogin_name);
            user.setPassword(mlogin_pwd);
            SP.put(LoginActivity.this, "user_name", mlogin_name);
            SP.put(LoginActivity.this, "pwd", mlogin_pwd);
            //保存
            SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = mSharedPreferences.edit();
            editor.putString("user_pwd",mlogin_pwd);
            editor.putString("user_mobile",mlogin_name);
            editor.commit();
            AccountUtils.saveUserInfos(LoginActivity.this, user,mlogin_pwd);
            progress.dismiss();
            goToHomeActivity();
        }
        else {
            progress.dismiss();
            Toast.makeText(LoginActivity.this,"登录失败！",Toast.LENGTH_SHORT).show();
        goToHomeActivity();
        }

    }

    private void goToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    private void goToRegisterActivity() {
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}

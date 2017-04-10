package com.example.sjy.snote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.sjy.snote.R;
import com.example.sjy.snote.db.NoteDatabase;

import utils.Constants;
import utils.SystemUtils;

public class MainActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener {


    private boolean isFirstUse;//默认是null.如果是第一次加载 置为true
    private  NoteBookFragment noteBookFragment;
    protected FloatingActionButton fab;
    protected DrawerLayout drawer;
    private long mBackPressedTime=0;

    private ImageView headIcon;
    private View baseView;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isFirstUse = new SystemUtils(MainActivity.this).isFirstUse();//是true
        if(isFirstUse){
            IntroducePage();
        }
        //第二次加载不执行初始化
      initMainFragment();
        initUI();
//        initHead();
//        initBgPic();
//        //注册EventBus
//        EventBus.getDefault().register(this);
    }

    private void initMainFragment() {
        //初始化主页面背景
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        noteBookFragment = new NoteBookFragment();

        ft.replace(R.id.main_fraglayout,noteBookFragment,null);
        ft.commit();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到编辑界面
                Log.i(TAG, "onClick:   进入了fab点击事件");
                Intent intent = new Intent(MainActivity.this,NoteEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(NoteEditFragment.NOTE_FROMWHERE_KEY,
                        NoteEditFragment.QUICK_DIALOG);
                intent.putExtra(Constants.BUNDLE_KEY_ARGS,bundle);
                startActivity(intent);
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        baseView= navigationView.getHeaderView(0);
        //头像一个触控
//        headIcon= (ImageView)baseView.findViewById(R.id.imageView);
//
//        headIcon.setOnClickListener(headIconOnTouchListener);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    private void IntroducePage() {
        NoteDatabase noteDatabase =new  NoteDatabase(MainActivity.this);
        noteDatabase.insertIntroduce(this);
        //设置是第一次使用
        new SystemUtils(MainActivity.this).set("isFirstUse","false");
    }
}

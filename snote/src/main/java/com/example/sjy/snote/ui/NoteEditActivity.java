package com.example.sjy.snote.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.sjy.snote.R;

/**
 * Created by sjy_1993 on 2017/4/7.
 */
public class NoteEditActivity extends AppCompatActivity {
    private NoteEditFragment noteEditFragment;
    private static final String TAG = "NoteEditActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_note_edit);
        setTitle("添加记录");
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //新建一个
        noteEditFragment = new NoteEditFragment();
        Log.i(TAG, "initFragment: 转换内容编辑页面******");
        ft.replace(R.id.main_fraglayout,noteEditFragment,null);
        ft.commit();

    }

    //退出到编辑界面
    @Override
    public void onBackPressed() {
        if(!noteEditFragment.onBackPressed())
            super.onBackPressed();
    }
}

package com.example.sjy.snote.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.sjy.snote.R;
import com.example.sjy.snote.adapter.NotebookAdapter;
import com.example.sjy.snote.db.NoteDatabase;
import com.example.sjy.snote.entity.NotebookData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.Constants;
import utils.HTQAnimations;
import widgt.SJYDragGridView;

/**
 * Created by sjy_1993 on 2017/4/8.
 */
public class NoteBookFragment extends Fragment
implements AdapterView.OnItemClickListener {
    private Activity aty;
    private NotebookAdapter adapter;
    private NoteDatabase noteDb;
    private List<NotebookData> datas;
    @BindView(R.id.frag_note_list)
    SJYDragGridView mGrid;
    @BindView(R.id.frag_note_trash)
    ImageView mImgTrash;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String TAG = "NoteBookFragment";
    public static final int STATE_NONE = 0;
    public static int mState = STATE_NONE;
    //    private NotebookAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_note, null, false);
        aty = getActivity();
        ButterKnife.bind(this, rootView);
        //初始化数据
        initData();
        initView(rootView);
        return rootView;
    }
    private void initData() {
        noteDb = new NoteDatabase(aty);
        datas = noteDb.query();//查询操作
        if (datas != null) {

            adapter = new NotebookAdapter(aty, datas);
        } else {
        }
    }

    //设置每个布局的监听
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
        Bundle bundle = new Bundle();
        //来自数据 哪一个key
        bundle.putInt(NoteEditFragment.NOTE_FROMWHERE_KEY,
                NoteEditFragment.NOTEBOOK_ITEM);
        bundle.putSerializable(NoteEditFragment.NOTE_KEY, datas.get(i));
        Intent intent = new Intent(getActivity(),NoteEditActivity.class);
        intent.putExtra(Constants.BUNDLE_KEY_ARGS, bundle);
        Log.i(TAG, "onItemClick:获取输入数据 " + datas.get(i));
        Log.i(TAG, "onItemClick: 还未进入子布局");
        startActivity(intent);
        Log.i(TAG, "onItemClick: " + "进入了子布局" + id + i);
    }

    //初始化布局
    private void initView(View rootView) {
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(this);
        //删除  按钮
        mGrid.setTrashView(mImgTrash);
        mSwipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
       mSwipeRefreshLayout.setRefreshing(true);
//        getData();
        mSwipeRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                adapter = new NotebookAdapter(aty, datas);
                return false;

            }
        });


        mGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //设置被删除的动画
        mGrid.setOnDeleteListener(new SJYDragGridView.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                delete(position);
            }
        });
        mGrid.setOnMoveListener(new SJYDragGridView.OnMoveListener(){
         //开始移动
            @Override
            public void startMove() {


                //隐藏这个刷新栏
                mSwipeRefreshLayout.setEnabled(false);
                //设置动画
                mImgTrash.startAnimation(HTQAnimations.getTranslateAnimation(0,
                        0,mImgTrash.getTop(),0,500));
                //删除图片设置可见
                mImgTrash.setVisibility(View.VISIBLE);
            }
            @Override
            public void finishMove() {
                mImgTrash.setVisibility(View.INVISIBLE);
                mImgTrash.startAnimation(HTQAnimations.getTranslateAnimation(0,0,0,mImgTrash.getTop(),500));
            if(adapter.getDataChange()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //数据库重置
                        noteDb.reset(adapter.getDatas());
                    }
                }).start();
            }
            }

            @Override
            public void cancleMove() {

            }
        });
//数据刷新
    }
//
//    private void getData() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    adapter = new NotebookAdapter(aty, datas);
////                    Thread.sleep(1000);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                Snackbar.make(mGrid, "已从服务器端同步数据！", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//        }).start();
//    }


    //删除数据   跟数据库操作在一起
    private void delete(int index){
        final int noteId = datas.get(index).getId();
        noteDb.delete(noteId);
        datas.remove(index);
        if(datas != null && adapter != null){
            adapter.refurbishData(datas);
            mGrid.setAdapter(adapter);
            //重新刷入数据  adapter
        }
    }


    /**
     * 使用自带缓存功能的网络请求，防止多次刷新造成的流量损耗以及服务器压力
     */
    private void refurbish() {
        datas = noteDb.query();
        if (datas != null) {
            if (adapter != null) {
                adapter.refurbishData(datas);
            } else {
                adapter = new NotebookAdapter(aty, datas);
                mGrid.setAdapter(adapter);
            }
        }
    }
}

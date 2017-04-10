package com.example.sjy.snote.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjy.snote.R;
import com.example.sjy.snote.db.NoteDatabase;
import com.example.sjy.snote.entity.NotebookData;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.AccountUtils;
import utils.Constants;
import utils.DialogHelp;
import utils.StringUtils;
import utils.SystemUtils;
/**
 * Created by sjy_1993 on 2017/4/7.
 */
public class NoteEditFragment extends Fragment  implements View.OnClickListener,View.OnTouchListener{
    public static final String NOTE_KEY = "notebook_key";
    public static final String NOTE_FROMWHERE_KEY = "fromwhere_key";
    public static final int QUICK_DIALOG = 0;
    public static final int NOTEBOOK_ITEM = 1;
    protected boolean isNewNote;
    private int whereFrom = QUICK_DIALOG;// 从哪个界面来 0:加号 1：主界面

    @BindView(R.id.note_detail_edit)
    EditText mEtContent;
    @BindView(R.id.note_detail_tv_date)
    TextView mTvDate;
    @BindView(R.id.note_detail_titlebar)
    RelativeLayout mLayoutTitle;
//    @BindView(R.id.note_detail_img_thumbtack)
//    ImageView mImgThumbtack;
    @BindView(R.id.note_detail_img_thumbtack)
    ImageView mImgThumbtack;
    @BindView(R.id.note_detail_img_button)
    ImageView mImgMenu;
    @BindView(R.id.note_detail_menu)
    RelativeLayout mLayoutMenu;

    //笔记数据库操作类
    private NoteDatabase noteDb;
    private NotebookData editData;
    private int mFontSizeId;//字体大小
//    @BindView(R.id.note_detail_img_green)
//    ImageView mImgGreen;
//    @BindView(R.id.note_detail_img_blue)
//    ImageView mImgBlue;
    private static final String USER_NAME = "user_name";
    private static final String USER_PWD = "user_pwd";
    @BindView(R.id.note_detail_img_green)
    ImageView mImgGreen;
    SharedPreferences pref;

    private static final String TAG = "NoteEditFragment";

    public static final int[] sBackGrounds = { 0xffe5fce8,// 绿色
            0xfffffdd7,// 黄色
            0xffffddde,// 红色
            0xffccf2fd,// 蓝色
            0xfff7f5f6,// 紫色
    };

    public static final int[] sTitleBackGrounds = { 0xffcef3d4,// 绿色
            0xffebe5a9,// 黄色
            0xffecc4c3,// 红色
            0xffa9d5e2,// 蓝色
            0xffddd7d9,// 紫色
    };
    public static final int[] sThumbtackImgs = { R.drawable.green,
            R.drawable.yellow, R.drawable.red, R.drawable.blue,
            R.drawable.purple };

    //绘制界面时，回调的方法
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       super.onCreateView(inflater, container, savedInstanceState);
     View rootView = inflater.inflate(R.layout.note_edit_fragment,
          container,false );
        Log.i(TAG, "onCreateView: ++++++++++++");
        ButterKnife.bind(this,rootView);
        initData();
        Log.i(TAG, "onCreateView: 经过初始化数据后"+editData);
        initView(rootView);
        return  rootView;
    }

    private void initData() {
        noteDb = new NoteDatabase(getActivity());
        if(editData == null){

            Log.i(TAG, "initData:此时为空"+editData);
            editData = new NotebookData();
            editData.setContent(new SystemUtils(getActivity()).getNoteDraft());
            isNewNote = true;//确定是 新笔记
        }
        //如果日期不为空
        if(StringUtils.isEmpty(editData.getDate())){
            //设置日期格式化
            editData.setDate(StringUtils.getDataTime("yyyy/MM/dd"));
        }

    }

    private void initView(View rootView) {
        mImgGreen.setOnClickListener(this);
//        mImgBlue.setOnClickListener(this);
//        mImgPurple.setOnClickListener(this);
//        mImgYellow.setOnClickListener(this);
//        mImgRed.setOnClickListener(this);
        //设置内容背景的颜色   从后台获得
        mEtContent.setBackgroundColor(sBackGrounds[editData.getColor()]);
        mLayoutTitle.setBackgroundColor(sTitleBackGrounds[editData.getColor()]);
        mImgThumbtack.setImageResource(sThumbtackImgs[editData.getColor()]);
        mEtContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mEtContent.setSingleLine(false);
        mEtContent.setHorizontallyScrolling(false);
        //内容保存到实体类中
        mEtContent.setText(Html.fromHtml(editData.getContent()).toString());
        mTvDate.setText(editData.getDate());
        Log.i(TAG, "initView: "+mEtContent.getText().toString());
        //设置菜单栏的事件
        mImgMenu.setOnTouchListener(this);
        mLayoutMenu.setOnTouchListener(this);

    }

    @Override
    public void onClick(View view) {

    }

    //实现的触发事件
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(MotionEvent.ACTION_DOWN == motionEvent.getAction()){
            if(mLayoutMenu.getVisibility() == View.GONE){
                openMenu();
            }else {
                closeMenu();
            }
        }

        return false;
    }

    //菜单栏的ActionBar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.notebook_edit_menu,menu);
    }

    //菜单栏ActionBar选项菜单点击事件

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.public_menu_send:
               //点击以后                    即保存
               //如果屏幕的输入不为空的话，则保存
               if(!StringUtils.isEmpty(mEtContent.getText().toString())){
                   /**
                    * 保存数据的动作
                    */
                   Log.i(TAG, "onOptionsItemSelected: 保存的数据"+mEtContent.getText().toString());

                   //两种进入方式
                   /*
                     主界面的加号进来的
                    */
                  if(whereFrom ==0 ){
//                      editData.postNoteToServer(getActivity());
                      save();
                      //这里暂不保存到服务器
                      Snackbar.make(mImgGreen,"保存到本地数据库", Snackbar.LENGTH_LONG).show();
                 getActivity().finish();
                  } else if(whereFrom ==1){
//                      editData.updateNote(getActivity(),editData.getObjectId()){
//
//                      }

                      NotebookData notebook = new NotebookData();
                      notebook.setId(editData.getId());
                      notebook.setContent(mEtContent.getText().toString());
                      notebook.setDate(editData.getUnixTime());
                      NoteDatabase note = new NoteDatabase(getContext());
                      editData.getUserId();
                      editData.setContent(mEtContent.getText().toString());
                      editData.setUnixTime(StringUtils.getDataTime("yyyy-MM-dd HH:mm:ss"));
//                      note.save(editData);

                    update(notebook);
                      Log.i(TAG, "onOptionsItemSelected: "+"editData是："+editData);
                      Snackbar.make(mImgGreen,"保存到本地数据库***了", Snackbar.LENGTH_LONG).show();
                     getActivity().finish();
                  }
               }
           break;
       }
        return true;
    }
    /**
     * 保存已编辑内容到数据库
     */
    private void save() {
        Log.i(TAG, "save: "+editData.toString());
        setNoteProperty();
        Log.i(TAG, "save: "+"数据还未保存完毕  此时editData的值是："+editData.toString());
        noteDb.save(editData);
    }
    //修改数据到数据库
    private void update(NotebookData note){
        Log.i(TAG, "update: "+note.getContent().toString());
        noteDb.update(note);
    }

    private void setNoteProperty() {
        //保存数据
        if(editData.getId() == 0){
            editData.setId( -1* StringUtils.toInt(
                    StringUtils.getDataTime("dddHHmmss"),0));
            //个人的数据名
//           final SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
            //取出userId
            String userId = AccountUtils.getUserId(getActivity());
//            int pwd = pref.getInt("pwd",0);
            //把用户姓名存进去
            editData.setUserId(userId);
            editData.setContent(mEtContent.getText().toString());
            editData.setUnixTime(StringUtils.getDataTime("yyyy-MM-dd HH:mm:ss"));
            //ObjectID这里有一个
        }
    }

    //初始化必要的组件
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //有菜单栏选项
        Log.i(TAG, "onCreate: ******************");
        setHasOptionsMenu(true);
        Bundle  bundle = getActivity().getIntent().getBundleExtra(Constants.BUNDLE_KEY_ARGS);
        if(bundle !=null){
            //取出数据
            //  取出的两个序列化数据包括是  NOte_KEY
            editData = (NotebookData) bundle.getSerializable(NOTE_KEY);

            //来自于娜的数据则包括此方法。
            whereFrom = bundle.getInt(NOTE_FROMWHERE_KEY,QUICK_DIALOG);
//            editData = (NotebookData) bundle.getSerializable(NOTE_FROMWHERE_KEY);
//            whereFrom = bundle.getInt(NOTE_FROMWHERE_KEY,NOTEBOOK_ITEM);
            Log.i(TAG, "onCreate: "+"编辑数据:"+editData+"数据来源："+whereFrom);
        }

        //软输出模式
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    //右下角的按钮
    private void openMenu(){


}
    private void closeMenu() {
    }
    public boolean onBackPressed() {
        if (isNewNote) {
            final String content = mEtContent.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                DialogHelp.getConfirmDialog(getActivity(), "是否保存为草稿?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                        new SystemUtils(getActivity()).setNoteDraft(content+"[草稿]");
                        getActivity().finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new SystemUtils(getActivity()).setNoteDraft("");
                        getActivity().finish();
                    }
                }).show();
                return true;
            }
        }
        return false;
    }
}

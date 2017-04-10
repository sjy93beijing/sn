package com.example.sjy.snote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sjy_1993 on 2017/4/7.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String SNOTE_DATABASE_NAME = "snote";         //数据库名
    public static final String NOTE_TABLE_NAME = " sjy_notebook";     //笔记表名
    public static final String CREATE_NOTE_TABLE = "create table "
            + NOTE_TABLE_NAME
            + " (_id integer primary key autoincrement, objectid text, iid integer,"
            + " time varchar(10), date varchar(10), content text, color integer)";

    //构造函数
    public DatabaseHelper(Context context) {
        super(context, NOTE_TABLE_NAME, null, 1);
    }
    public static final String NEWS_LIST = "osc_news_list";

    public static final String CREATE_NEWS_LIST_TABLE = "create table "
            + NOTE_TABLE_NAME + "(" + "_id integer primary key autoincrement, "
            + "news_id interger, title varchar(10), " + ")";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

package com.example.sjy.snote.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sjy.snote.entity.NotebookData;

import java.util.ArrayList;
import java.util.List;

import utils.AccountUtils;
import utils.StringUtils;

/**
 * Created by sjy_1993 on 2017/4/7.
 */
public class NoteDatabase {
    private final DatabaseHelper dbHelper;
    public NoteDatabase(Context context){
        super();
        dbHelper = new DatabaseHelper(context);//创建构造函数
    }
    //增加数据
    public void insert(NotebookData data) {
        String sql = "insert into " + DatabaseHelper.NOTE_TABLE_NAME;

        sql += "(_id, objectid, iid, time, date, content, color) values(?, ?, ?, ?, ?, ?, ?)";
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        sqlite.execSQL(sql, new String[] { data.getId() + "",data.getIid() + "", data.getObjectId() + "",
                 data.getUnixTime() + "", data.getDate(),
                data.getContent(), data.getColor() + "" });
        sqlite.close();
    }

    public List<NotebookData> query() {
        return query(" ");
    }

    /**
     * 查
     *
     * @param where
     * @return
     */


    //where   为空
    public List<NotebookData> query(String where) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ArrayList<NotebookData> data = null;
        data = new ArrayList<NotebookData>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.NOTE_TABLE_NAME + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            NotebookData notebookData = new NotebookData();
            notebookData.setId(cursor.getInt(0));
            notebookData.setObjectId(cursor.getString(1));
            notebookData.setIid(cursor.getInt(2));
            notebookData.setUnixTime(cursor.getString(3));
            notebookData.setDate(cursor.getString(4));
            notebookData.setContent(cursor.getString(5));
            notebookData.setColor(cursor.getInt(6));
            data.add(notebookData);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }

    /**
     * 删
     */
    public void delete(int id){
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        //delete from snote where id == ?
        String sql = ("delete from " + DatabaseHelper.NOTE_TABLE_NAME + " where _id=?");
//        Log.i(TAG, "delete: ");
        sqlite.execSQL(sql,new Integer[] { id });
        sqlite.close();//关闭数据库

    }

    /**
     * 改
     *
     * @param data
     */
    public void update(NotebookData data) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        //更新用户名   id
        String sql = ("update " + DatabaseHelper.NOTE_TABLE_NAME + " set iid=?,objectid=?,  time=?, date=?, content=?, color=? where _id=?");
        sqlite.execSQL(sql,
                new String[] { data.getIid() + "",data.getObjectId() + "", data.getUnixTime() + "",
                        data.getDate(), data.getContent(),
                        data.getColor() + "", data.getId() + "" });
        sqlite.close();
    }

    /**
     * 重置
     * @param
     */
    public void reset(List<NotebookData> datas){
        if (datas !=null){
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            //删除全部
            sqLiteDatabase.execSQL("delete from "+
            DatabaseHelper.NOTE_TABLE_NAME);
            for (NotebookData data : datas){
                //循环遍历datas里的数据  赋值给data里
                insert(data);
            }
            sqLiteDatabase.close();
        }

    }

    public void insertIntroduce(Context context)
    {
        NotebookData editData=new NotebookData();
        if (editData.getId() == 0) {
            editData.setId(-1
                    * StringUtils.toInt(
                    StringUtils.getDataTime("dddHHmmss"), 0));
        }
        editData.setUnixTime(StringUtils.getDataTime("yyyy-MM-dd HH:mm:ss"));
        editData.setContent("欢迎使用此记事本，赶快记下你此刻的灵感吧！");
        editData.setUserId(AccountUtils.getUserId(context));
        save(editData);
    }
    public void save(NotebookData data){
        List<NotebookData>  datas = query(" where _id=" + data.getId());
        if(datas != null && !datas.isEmpty()){
            update(data);

        }else {
            insert(data);
        }

    }





}

package com.example.sjy.snote.entity;

import android.content.Context;

import com.example.sjy.snote.db.NoteDatabase;

import java.io.Serializable;

/**
 * Created by sjy_1993 on 2017/4/7.
 */
public class NotebookData implements   Comparable<NotebookData>, Serializable {
    @Override
    public int compareTo(NotebookData notebookData) {
        return 0;
    }

    private int id;
    private int iid;    //1.
    private String objectId;
    private String userId;//用于服务器端存储需要

    private String unixTime;  //2.

    private String date;   //3.

    private String content;   //4.

    private String colorText;

    private int color;   //5.
    public static final String NOTE_USER_ID = "userId";

    private NoteDatabase nodb;
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * 删除数据
     * @param
     */
    public void deleteNoteInServe(Context context,int id){
        nodb = new NoteDatabase(context);
        nodb.delete(id);

    }
    public void updateNote(Context context,String objectId){
        nodb = new NoteDatabase(context);
//        nodb.update();


    }

    public void postNoteToServer(Context context)
    {
//       save(context);

    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        } else {
            if (o instanceof NotebookData) {
                NotebookData data = (NotebookData) o;
                try {
                    return (this.id == data.getId())
                            && (this.iid == data.getIid())
//                            && (this.objectId==data.getObjectId())
                            && (this.unixTime == data.getUnixTime())
                            && (this.date.equals(data.getDate()))
                            && (this.content == data.getContent())
                            && (this.color == data.getColor());
                } catch (NullPointerException e) {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static String getNoteUserId() {
        return NOTE_USER_ID;
    }
}

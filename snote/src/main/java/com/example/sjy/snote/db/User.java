package com.example.sjy.snote.db;

/**
 * Created by sjy_1993 on 2017/4/7.
 */
public class User {
    private String nickname;
    private String sex;
    private String notePwd;
    private String headUrl;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNotePwd() {
        return notePwd;
    }

    public void setNotePwd(String notePwd) {
        this.notePwd = notePwd;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}

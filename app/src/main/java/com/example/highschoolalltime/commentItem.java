package com.example.highschoolalltime;

public class commentItem {
    private String useridStr;
    private String commentsStr;
    private String timeStr;
    private String content_useridStr;
    private String content_userSchoolStr;
    private String content_titleStr ;
    private String content_contentStr ;
    private String content_WhatboardStr;
    private String content_timeStr ;

    public commentItem(String userid, String comments, String time){
        this.useridStr = userid;
        this.commentsStr = comments;
        this.timeStr = time;
        //System.out.println(this.useridStr + this.commentsStr + this.timeStr + "// commentItem");
    }
    public commentItem() {};

    public void setUserid(String userid){
        useridStr = userid;
    }
    public void setComments(String comments){
       commentsStr = comments;
    }
    public void setTime(String time){
        timeStr = time;
    }

    public void setContent_userid(String content_userid){
        content_useridStr = content_userid;
    }
    public void setContent_userSchool(String content_userSchool){
        content_userSchoolStr = content_userSchool;
    }
    public void setContent_title(String content_title){
        content_titleStr = content_title;
    }
    public void setContent_content(String content_content){
        content_contentStr = content_content;
    }
    public void setContent_Whatboard(String content_whatboard){
        content_WhatboardStr = content_whatboard;
    }
    public void setContent_time(String content_time){
        content_timeStr = content_time;
    }


    public String getUserID(){ return this.useridStr; }
    public String getComments(){ return this.commentsStr; }
    public String getTime(){ return this.timeStr; }
    public String getContent_userid(){
        return this.content_useridStr;
    }
    public String getContent_userSchool(){
        return this.content_userSchoolStr;
    }
    public String getContent_title(){
        return this.content_titleStr;
    }
    public String getContent_content(){
        return this.content_contentStr;
    }
    public String getContent_Whatboard(){
        return this.content_WhatboardStr;
    }
    public String getContent_time(){
        return this.content_timeStr;
    }
}


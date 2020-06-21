package com.example.highschoolalltime;

public class commentItem {
    private String useridStr; //댓글 쓴 유저 아이디
    private String commentsStr; // 댓글 내용
    private String timeStr; //댓글 쓴 시간
    private String content_useridStr; // 댓글을 적은 게시글 유저 아이디
    private String content_userSchoolStr; // 게시글 유저 학교
    private String content_titleStr ; // 게시글 제목
    private String content_contentStr ; // 게시글 내용
    private String content_WhatboardStr; //게시글 보드
    private String content_timeStr ; //게시글 시간

    public commentItem(String userid, String comments, String time){ // 생성자로 댓글 정보 저장
        this.useridStr = userid;
        this.commentsStr = comments;
        this.timeStr = time;
        //System.out.println(this.useridStr + this.commentsStr + this.timeStr + "// commentItem");
    }
    public commentItem() {};

    //필요한 정보 저장하는 메서드
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

//필요한 정보 가져오는 메서드
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


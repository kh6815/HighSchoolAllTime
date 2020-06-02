package com.example.highschoolalltime;

public class MyItem {
    private String  userIDStr;
    private String titleStr;
    private String contentStr;
    private String commentStr;
    private String whatboardStr;
    private String timeStr;
    private String hotCountStr;
    private String hotclickUserStr;

    public MyItem(String userID,String title, String content, String comment,String time, String hotCount, String hotclickUser){
        this.userIDStr = userID;
        this.titleStr = title;
        this.contentStr = content;
        this.commentStr = comment;
        this.timeStr = time;
        this.hotCountStr = hotCount;
        this.hotclickUserStr = hotclickUser;
    }
    public MyItem() {};

    public void setuserID(String userID){
        userIDStr = userID;
    }
    public void setTitle(String title){
        titleStr = title;
    }
    public void setContent(String content){
        contentStr = content;
    }
    public void setComments(String comments){
       commentStr = comments;
    }
    public void setTime(String time){ timeStr = time; }
    public void setHotCount(String hotCount){
        hotCountStr = hotCount;
    }
    public void setHotClickUser(String hotclickUser){
        hotCountStr = hotclickUser;
    }
    public void setWhatboard(String whatboard) {whatboardStr = whatboard;}

    public String getuserID(){
        return this.userIDStr;
    }
    public String getTitle(){
        return this.titleStr;
    }
    public String getContent(){
        return this.contentStr;
    }
    public String getCommentStr(){ return this.commentStr; }
    public String getTime(){ return this.timeStr; }
    public String getHotCount(){ return this.hotCountStr; }
    public String getHotClickUser(){ return this.hotclickUserStr; }
    public String getWhatboard(){ return this.whatboardStr;}
}

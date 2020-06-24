package com.example.highschoolalltime;

public class MyItem {
    //게시글 아이템 정보 변수
    private String  userIDStr;
    private String titleStr;
    private String contentStr;
    private String commentStr;
    private String whatboardStr;
    private String timeStr;
    private String hotCountStr;
    private String hotclickUserStr;

    //생성자 함수로 게시글 정보 저장
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

    //값을 저장하는 메서드
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

    //값을 불러오는 메서드
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

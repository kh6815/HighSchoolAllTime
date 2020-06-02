package com.example.highschoolalltime;

import android.app.Application;

public class use_user extends Application {

    private  String userID;
    private  String userSchool;
    private  String userName;
    private  String userEmail;
    private  String userGrade;
    private  String userPassword;
    public String getUserID(){
        return userID;
    }
    public String getUserPassword(){return userPassword;}
    public String getUserSchool() { return userSchool;}
    public String getUserName() { return userName;}
    public String getUserEmail() { return userEmail;}
    public String getUserGrade() { return userGrade;}
    public void setUser(String userID, String userPassword, String userSchool, String userName, String userEmail, String userGrade){
        this.userID = userID;
        this.userPassword = userPassword;
        this.userSchool = userSchool;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userGrade = userGrade;
    }
}


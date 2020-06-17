package com.example.highschoolalltime;

public class HomeTimeTableItem {
    private String subjectStr;
    private String positionStr;

    public HomeTimeTableItem(String subject, String position){
        this.subjectStr = subject;
        this.positionStr = position;
    }
    public HomeTimeTableItem() {};

    public void setSubject(String subject){
        subjectStr = subject;
    }
    public void setPosition(String position){
        positionStr = position;
    }

    public String getSubject(){
        return this.subjectStr;
    }
    public String getPosition(){
        return this.positionStr;
    }
}

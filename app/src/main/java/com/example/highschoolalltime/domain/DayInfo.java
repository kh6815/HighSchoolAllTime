package com.example.highschoolalltime.domain;

public class DayInfo {
    private String day;
    private boolean inMonth;
    //날짜반환
    public String getDay()
    {
        return day;
    }
    //날짜저장
    public void setDay(String day)
    {
        this.day = day;
    }
    //이번달 날짜인지 반환(true or false)
    public boolean isInMonth()
    {
        return inMonth;
    }
    //이번달 날짜저장인지 저장(이번달이면 true, 아니면 false)
    public void setInMonth(boolean inMonth)
    {
        this.inMonth = inMonth;
    }

}
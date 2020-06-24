package com.example.highschoolalltime;

public class Scheduleitem {
    private String whatdateStr;
    private String todoStr;
    private String scheduleidStr;
    //ScheduleAdapter에서 사용할 item들을 선언한다.
    public Scheduleitem(String Todo, String WhatDate, String ScheduleID){
        this.whatdateStr = WhatDate;//날짜
        this.todoStr = Todo;//내용
        this.scheduleidStr = ScheduleID;//추가한 시간
    }
    public Scheduleitem() {};
    //밖에서 받아온 값을 저장한다.
    public void setWhatDate(String WhatDate){
        whatdateStr = WhatDate;
    }
    public void setTodo(String Todo){
        todoStr = Todo;
    }
    public void setScheduleID(String ScheduleID) {scheduleidStr = ScheduleID;}
    //저장되어있는 값을 반환한다.
    public String getWhatDate(){
        return this.whatdateStr;
    }
    public String getTodo(){
        return this.todoStr;
    }
    public String getScheduleID(){
        return this.scheduleidStr;
    }
}
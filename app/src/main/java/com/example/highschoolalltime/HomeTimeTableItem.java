package com.example.highschoolalltime;

public class HomeTimeTableItem {
    //홈 화면 시간표리스트뷰 정보 저장할 변수
    private String subjectStr;
    private String positionStr;

    //생성자로 데이터 저장
    public HomeTimeTableItem(String subject, String position){
        this.subjectStr = subject;
        this.positionStr = position;
    }
    public HomeTimeTableItem() {};

    //데이터를 저장할 메서드
    public void setSubject(String subject){
        subjectStr = subject;
    }
    public void setPosition(String position){
        positionStr = position;
    }

    //저장된 데이터 불러올때 사용할 메서드
    public String getSubject(){
        return this.subjectStr;
    }
    public String getPosition(){
        return this.positionStr;
    }
}

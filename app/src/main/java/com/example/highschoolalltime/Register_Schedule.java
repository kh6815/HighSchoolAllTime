//Register_Cafe, 급식DB에 INSERT하여 급식을 추가해주는 php와 연결하는 클래스
package com.example.highschoolalltime;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Register_Schedule extends StringRequest {
    //서버 URL설정 (PHP파일연동)
    final static private  String URL = "http://highschool.dothome.co.kr/Register_Schedule.php";
    //string배열로 저장
    private Map<String, String> map;

    public Register_Schedule(String userID, String WhatDate, String Todo, String  ScheduleID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        //map에 값넣기
        map = new HashMap<>();
        map.put("userID",userID);
        map.put("WhatDate",WhatDate);
        map.put("Todo",Todo);
        map.put("ScheduleID", ScheduleID);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
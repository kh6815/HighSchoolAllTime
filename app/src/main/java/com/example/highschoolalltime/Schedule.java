//Cafeteria_Request, DB안에 급식표를 SELECT * 하는 php와 연결하는 클래스
package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Schedule extends StringRequest {
    //서버 URL설정 (PHP파일연동)
    final static private  String URL = "http://highschool.dothome.co.kr/Schedule.php";
    //string배열로 저장
    private Map<String, String> map;

    public Schedule(String WhatDate, String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        //map에 저장
        map = new HashMap<>();
        map.put("WhatDate",WhatDate);
        map.put("userID",userID);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

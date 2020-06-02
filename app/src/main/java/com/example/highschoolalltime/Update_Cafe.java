//Update_Cafe, 급식표 DB안에 급식을 UPDATE하는 php와 연결하는 클래스
package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Update_Cafe extends StringRequest {
    //서버 URL설정 (PHP파일연동)
    final static private  String URL = "http://highschool.dothome.co.kr/Update_Cafe.php";
    //string배열로 저장
    private Map<String, String> map;

    public Update_Cafe(String Menu, String WhatDate, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        //map에 값넣기
        map = new HashMap<>();
        map.put("Menu",Menu);
        map.put("WhatDate",WhatDate);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

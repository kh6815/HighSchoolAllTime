//시간표 삭제

package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Delete_Schedule extends StringRequest {
    //서버 URL설정 (PHP파일연동)
    final static private  String URL = "http://highschool.dothome.co.kr/Delete_Schedule.php";
    //string배열로 저장
    private Map<String, String> map;

    public Delete_Schedule(String userID, String WhatDate, String  ScheduleID,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        //map에 값넣기
        map = new HashMap<>();
        map.put("userID", userID);
        map.put("WhatDate", WhatDate);
        map.put("ScheduleID", ScheduleID);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
//시간표 추가

package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Register_Time extends StringRequest {
    //서버 URL설정 (PHP파일연동)
    final static private  String URL = "http://highschool.dothome.co.kr/Register_Time.php";

    //string배열로 저장
    private Map<String, String> map;

    public Register_Time(String Subject, String Position, String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        //map에 값넣기
        map = new HashMap<>();
        map.put("Subject", Subject);
        map.put("Position", Position);
        map.put("userID", userID);
    }

    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindPW_Request extends StringRequest {
    //서버 URL 설정( PHP  파일 연동)
    final  static  private  String URL = "http://highschool.dothome.co.kr/FindPW.php";
    private Map<String, String> map;//string배열로 저장

    public FindPW_Request(String userSchool, String userName, String userEmail, String userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
    //map에 값넣기
        map = new HashMap<>();
        map.put("userSchool", userSchool);
        map.put("userName", userName);
        map.put("userEmail", userEmail);
        map.put("userID", userID);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
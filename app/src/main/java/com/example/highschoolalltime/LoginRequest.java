package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    //서버 URL 설정( PHP  파일 연동)
    final  static  private  String URL = "http://highschool.dothome.co.kr/Login.php";
    private Map<String, String> map;//string배열로 저장

    public LoginRequest(String userID, String userPassword, String userSchool, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        //map에 저장
        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userSchool", userSchool);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

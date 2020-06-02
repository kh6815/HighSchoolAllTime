package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Update_User extends StringRequest {
    //서버 URL설정 (PHP파일연동)
    final static private  String URL = "http://highschool.dothome.co.kr/Update_User.php";
    //string배열로 저장
    private Map<String, String> map;

    public Update_User(String userName, String userSchool, String userEmail, String userPassword,
                       String userGrade, String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        //map에 값넣기
        map = new HashMap<>();
        map.put("userName",userName);
        map.put("userSchool",userSchool);
        map.put("userEmail",userEmail);
        map.put("userPassword",userPassword);
        map.put("userGrade",userGrade);
        map.put("userID",userID);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
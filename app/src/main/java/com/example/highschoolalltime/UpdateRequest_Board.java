package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequest_Board extends StringRequest {
    //서버 URL 설정( PHP  파일 연동)
    final  static  private  String URL = "http://wkwjsrjekffk.dothome.co.kr/Update_Board.php";
    private Map<String, String> map;

    public UpdateRequest_Board(String userid, String userSchool , String title, String content , String comments, String Whatboard, String time, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userid", userid);
        map.put("userSchool", userSchool);
        map.put("title", title);
        map.put("content", content);
        map.put("comments", comments);
        map.put("Whatboard", Whatboard);
        map.put("time", time);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
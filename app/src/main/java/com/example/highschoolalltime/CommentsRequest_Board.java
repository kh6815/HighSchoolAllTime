package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentsRequest_Board extends StringRequest {
    //서버 URL 설정( PHP  파일 연동)
    final  static  private  String URL = "http://wkwjsrjekffk.dothome.co.kr/Comments_Register.php";
    private Map<String, String> map;

    public CommentsRequest_Board(String content_userid, String content_userSchool , String content_title, String content_content , String content_Whatboard, String content_time, String userid, String comments, String time,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("content_userid", content_userid);
        map.put("content_userSchool", content_userSchool);
        map.put("content_title", content_title);
        map.put("content_content", content_content);
        map.put("content_Whatboard", content_Whatboard);
        map.put("content_time", content_time);

        map.put("userid", userid);
        map.put("comments", comments);
        map.put("time", time);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

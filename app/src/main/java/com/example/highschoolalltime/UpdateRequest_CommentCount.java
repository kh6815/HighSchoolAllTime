package com.example.highschoolalltime;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequest_CommentCount extends StringRequest {
    //서버 URL 설정( PHP  파일 연동)
    final  static  private  String URL = "http://wkwjsrjekffk.dothome.co.kr/Update_CommentsCount.php";
    private Map<String, String> map;//string배열로 저장
    public UpdateRequest_CommentCount(String userid, String userSchool , String title, String content , String comments, String Whatboard, String time, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        //map에 값넣기
        map = new HashMap<>();
        map.put("userid", userid);
        map.put("userSchool", userSchool);
        map.put("title", title);
        map.put("content", content);
        map.put("comments", comments);
        map.put("Whatboard", Whatboard);
        map.put("time", time);
    }
    //map리턴
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
